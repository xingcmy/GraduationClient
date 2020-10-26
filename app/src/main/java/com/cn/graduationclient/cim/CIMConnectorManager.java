package com.cn.graduationclient.cim;



import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.cn.graduationclient.cim.coder.ClientMessageDecoder;
import com.cn.graduationclient.cim.coder.ClientMessageEncoder;
import com.cn.graduationclient.cim.logger.CIMLogger;
import com.cn.graduationclient.cim.model.HeartbeatRequest;
import com.cn.graduationclient.cim.model.HeartbeatResponse;
import com.cn.graduationclient.cim.model.Protobufable;
import com.cn.graduationclient.cim.model.ReplyBody;
import com.cn.graduationclient.cim.model.SentBody;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CIMConnectorManager {
    private static CIMConnectorManager manager;
    private static final int READ_BUFFER_SIZE = 2048;
    private static final int WRITE_BUFFER_SIZE = 1024;
    private static final int CONNECT_TIME_OUT = 10000;
    private static final int CONNECT_ALIVE_TIME_OUT = 150000;
    private static final CIMLogger LOGGER = CIMLogger.getLogger();
    private static final HandlerThread IDLE_HANDLER_THREAD = new HandlerThread("READ-IDLE", 10);
    private volatile SocketChannel socketChannel;
    private final Context context;
    private final ByteBuffer headerBuffer = ByteBuffer.allocate(3);
    private final ExecutorService workerExecutor = Executors.newFixedThreadPool(1, (r) -> {
        return new Thread(r, "worker-");
    });
    private final ExecutorService bossExecutor = Executors.newFixedThreadPool(1, (r) -> {
        return new Thread(r, "boss-");
    });
    private final ClientMessageEncoder messageEncoder = new ClientMessageEncoder();
    private final ClientMessageDecoder messageDecoder = new ClientMessageDecoder();
    private final Handler idleHandler;

    private CIMConnectorManager(Context context) {
        this.idleHandler = new Handler(IDLE_HANDLER_THREAD.getLooper()) {
            public void handleMessage(Message m) {
                CIMConnectorManager.this.onSessionIdle();
            }
        };
        this.context = context;
    }

    public static synchronized CIMConnectorManager getManager(Context context) {
        if (manager == null) {
            manager = new CIMConnectorManager(context);
        }

        return manager;
    }

    public void connect(String host, int port) {
        if (!CIMPushManager.isNetworkConnected(this.context)) {
            Intent intent = new Intent();
            intent.setPackage(this.context.getPackageName());
            intent.setAction("com.farsunset.cim.CONNECT_FAILED");
            this.context.sendBroadcast(intent);
        } else if (!this.isConnected()) {
            this.bossExecutor.execute(() -> {
                if (!this.isConnected()) {
                    LOGGER.startConnect(host, port);
                    CIMCacheManager.putBoolean(this.context, "KEY_CIM_CONNECTION_STATE", false);

                    try {
                        this.socketChannel = SocketChannel.open();
                        this.socketChannel.configureBlocking(true);
                        this.socketChannel.socket().setTcpNoDelay(true);
                        this.socketChannel.socket().setKeepAlive(true);
                        this.socketChannel.socket().setReceiveBufferSize(2048);
                        this.socketChannel.socket().setSendBufferSize(1024);
                        this.socketChannel.socket().connect(new InetSocketAddress(host, port), 10000);
                        this.handleConnectedEvent();

                        while(this.socketChannel.read(this.headerBuffer) > 0) {
                            this.handleSocketReadEvent();
                        }

                        this.close();
                    } catch (SocketTimeoutException | ConnectException var4) {
                        this.handleConnectAbortedEvent();
                    } catch (IOException var5) {
                        this.handleDisconnectedEvent();
                    }

                }
            });
        }
    }

    public void close() {
        if (this.isConnected()) {
            try {
                this.socketChannel.close();
            } catch (IOException var5) {
            } finally {
                this.onSessionClosed();
            }

        }
    }

    public boolean isConnected() {
        return this.socketChannel != null && this.socketChannel.isConnected();
    }

    public void sendHeartbeat() {
        this.send(HeartbeatResponse.getInstance());
    }

    public void send(Protobufable body) {
        if (this.isConnected()) {
            this.workerExecutor.execute(() -> {
                int result = 0;

                try {
                    for(ByteBuffer buffer = this.messageEncoder.encode(body); buffer.hasRemaining(); result += this.socketChannel.write(buffer)) {
                    }
                } catch (Exception var7) {
                    result = -1;
                } finally {
                    if (result <= 0) {
                        this.close();
                    } else {
                        this.onMessageSent(body);
                    }

                }

            });
        }
    }

    private void onSessionCreated() {
        LOGGER.sessionCreated(this.socketChannel);
        Intent intent = new Intent();
        intent.setPackage(this.context.getPackageName());
        intent.setAction("com.farsunset.cim.CONNECT_FINISHED");
        this.context.sendBroadcast(intent);
    }

    private void onSessionClosed() {
        this.idleHandler.removeMessages(0);
        LOGGER.sessionClosed(this.socketChannel);
        Intent intent = new Intent();
        intent.setPackage(this.context.getPackageName());
        intent.setAction("com.farsunset.cim.CONNECTION_CLOSED");
        this.context.sendBroadcast(intent);
    }

    private void onSessionIdle() {
        LOGGER.sessionIdle(this.socketChannel);
        this.close();
    }

    private void onMessageReceived(Object obj) {
        Intent intent;
        if (obj instanceof com.cn.graduationclient.cim.model.Message) {
            intent = new Intent();
            intent.setPackage(this.context.getPackageName());
            intent.setAction("com.farsunset.cim.MESSAGE_RECEIVED");
            intent.putExtra(com.cn.graduationclient.cim.model.Message.class.getName(), (com.cn.graduationclient.cim.model.Message)obj);
            this.context.sendBroadcast(intent);
        }

        if (obj instanceof ReplyBody) {
            intent = new Intent();
            intent.setPackage(this.context.getPackageName());
            intent.setAction("com.farsunset.cim.REPLY_RECEIVED");
            intent.putExtra(ReplyBody.class.getName(), (ReplyBody)obj);
            this.context.sendBroadcast(intent);
        }

    }

    private void onMessageSent(Object message) {
        LOGGER.messageSent(this.socketChannel, message);
        if (message instanceof SentBody) {
            Intent intent = new Intent();
            intent.setPackage(this.context.getPackageName());
            intent.setAction("com.farsunset.cim.SEND_FINISHED");
            intent.putExtra(SentBody.class.getName(), (SentBody)message);
            this.context.sendBroadcast(intent);
        }

    }

    private void handleDisconnectedEvent() {
        this.close();
    }

    private void handleConnectAbortedEvent() {
        long interval = 30000L - (long)(5000 - (new Random()).nextInt(15000));
        LOGGER.connectFailure(interval);
        Intent intent = new Intent();
        intent.setPackage(this.context.getPackageName());
        intent.setAction("com.farsunset.cim.CONNECT_FAILED");
        intent.putExtra("interval", interval);
        this.context.sendBroadcast(intent);
    }

    private void handleConnectedEvent() {
        this.closeCountDown();
        this.onSessionCreated();
    }

    private void handleSocketReadEvent() throws IOException {
        this.closeCountDown();
        Object message = this.messageDecoder.doDecode(this.headerBuffer, this.socketChannel);
        LOGGER.messageReceived(this.socketChannel, message);
        if (message instanceof HeartbeatRequest) {
            this.send(HeartbeatResponse.getInstance());
        } else {
            this.onMessageReceived(message);
        }
    }

    private void closeCountDown() {
        this.idleHandler.removeMessages(0);
        this.idleHandler.sendEmptyMessageDelayed(0, 150000L);
    }

    static {
        IDLE_HANDLER_THREAD.start();
    }
}
