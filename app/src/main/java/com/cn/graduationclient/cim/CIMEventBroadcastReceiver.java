package com.cn.graduationclient.cim;




import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import com.cn.graduationclient.cim.model.Message;
import com.cn.graduationclient.cim.model.ReplyBody;
import com.cn.graduationclient.cim.model.SentBody;

public abstract class CIMEventBroadcastReceiver extends BroadcastReceiver {
    protected Context context;

    public CIMEventBroadcastReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        if ("android.intent.action.USER_PRESENT".equals(action) || "android.intent.action.ACTION_POWER_CONNECTED".equals(action) || "android.intent.action.ACTION_POWER_DISCONNECTED".equals(action)) {
            this.startPushService();
        }

        if ("com.farsunset.cim.NETWORK_CHANGED".equals(action) || "android.net.conn.CONNECTIVITY_CHANGE".equals(action)) {
            this.onDevicesNetworkChanged();
        }

        if ("com.farsunset.cim.CONNECTION_CLOSED".equals(action)) {
            this.onInnerConnectionClosed();
        }

        if ("com.farsunset.cim.CONNECT_FAILED".equals(action)) {
            long interval = intent.getLongExtra("interval", 30000L);
            this.onInnerConnectFailed(interval);
        }

        if ("com.farsunset.cim.CONNECT_FINISHED".equals(action)) {
            this.onInnerConnectFinished();
        }

        if ("com.farsunset.cim.MESSAGE_RECEIVED".equals(action)) {
            this.onInnerMessageReceived((Message)intent.getSerializableExtra(Message.class.getName()), intent);
        }

        if ("com.farsunset.cim.REPLY_RECEIVED".equals(action)) {
            this.onReplyReceived((ReplyBody)intent.getSerializableExtra(ReplyBody.class.getName()));
        }

        if ("com.farsunset.cim.SEND_FINISHED".equals(action)) {
            this.onSentSucceed((SentBody)intent.getSerializableExtra(SentBody.class.getName()));
        }

        if ("com.farsunset.cim.CONNECTION_RECOVERY".equals(action)) {
            this.connect(0L);
        }

    }

    private void startPushService() {
        Intent intent = new Intent(this.context, CIMPushService.class);
        intent.setAction("ACTION_ACTIVATE_PUSH_SERVICE");
        if (VERSION.SDK_INT >= 26) {
            this.context.startForegroundService(intent);
        } else {
            this.context.startService(intent);
        }

    }

    private void onInnerConnectionClosed() {
        CIMCacheManager.putBoolean(this.context, "KEY_CIM_CONNECTION_STATE", false);
        if (CIMPushManager.isNetworkConnected(this.context)) {
            this.connect(0L);
        }

        this.onConnectionClosed();
    }

    private void onInnerConnectFailed(long interval) {
        if (CIMPushManager.isNetworkConnected(this.context)) {
            this.onConnectFailed();
            this.connect(interval);
        }

    }

    private void onInnerConnectFinished() {
        CIMCacheManager.putBoolean(this.context, "KEY_CIM_CONNECTION_STATE", true);
        boolean autoBind = CIMPushManager.autoBindAccount(this.context);
        this.onConnectFinished(autoBind);
    }

    private void onDevicesNetworkChanged() {
        if (CIMPushManager.isNetworkConnected(this.context)) {
            this.connect(0L);
        }

        this.onNetworkChanged();
    }

    private void connect(long delay) {
        Intent serviceIntent = new Intent(this.context, CIMPushService.class);
        serviceIntent.putExtra("KEY_DELAYED_TIME", delay);
        serviceIntent.setAction("ACTION_CREATE_CIM_CONNECTION");
        CIMPushManager.startService(this.context, serviceIntent);
    }

    private void onInnerMessageReceived(Message message, Intent intent) {
        if (this.isForceOfflineMessage(message.getAction())) {
            CIMPushManager.stop(this.context);
        }

        this.onMessageReceived(message, intent);
    }

    private boolean isForceOfflineMessage(String action) {
        return "999".equals(action);
    }

    public abstract void onMessageReceived(Message var1, Intent var2);

    public void onNetworkChanged() {
        CIMListenerManager.notifyOnNetworkChanged(CIMPushManager.getNetworkInfo(this.context));
    }

    public void onConnectFinished(boolean hasAutoBind) {
        CIMListenerManager.notifyOnConnectFinished(hasAutoBind);
    }

    public void onConnectFailed() {
        CIMListenerManager.notifyOnConnectFailed();
    }

    public void onConnectionClosed() {
        CIMListenerManager.notifyOnConnectionClosed();
    }

    public void onReplyReceived(ReplyBody body) {
        CIMListenerManager.notifyOnReplyReceived(body);
    }

    public void onSentSucceed(SentBody body) {
        CIMListenerManager.notifyOnSendFinished(body);
    }
}
