package com.cn.graduationclient.audio;

import android.content.Context;
import android.net.Uri;
import android.provider.Contacts;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.xingcmyAdapter.Chat;
import com.cn.graduationclient.xingcmyAdapter.ChatAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页P层
 */
public class MainPresenter<T extends MainContract.View> implements MainContract.Presenter {

    private static final int MAX_VOICE_TIME = 20;//声音最大时间
    private static final String AUDIO_DIR_NAME = "audio_cache";

    private T mView;
    private Context mContext;
    private String UID;
    private String id;
    private String time;
    private File mAudioDir;
    private List<File> mListData = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Chat> chatList;

    public MainPresenter(T view, Context context, String UID, String id, String time, ChatAdapter chatAdapter, RecyclerView recyclerView,ArrayList<Chat> chatList) {
        this.mView = view;
        this.mContext = context;
        this.UID=UID;
        this.id=id;
        this.time=time;
        this.chatAdapter=chatAdapter;
        this.recyclerView=recyclerView;
        this.chatList=chatList;
        mAudioDir = new File((mContext.getExternalFilesDir(null).getPath()));
        if (!mAudioDir.exists()) {//判断文件夹是否存在，不存在则创建
            mAudioDir.mkdirs();
        }
    }

    @Override
    public void init() {
        initAudioManager();
        loadAudioCacheData();
    }

    @Override
    public void startRecord() {
        AudioRecordManager.getInstance(mContext,UID,id,time).startRecord();
    }

    @Override
    public void stopRecord() {
        AudioRecordManager.getInstance(mContext,UID,id,time).stopRecord();
    }

    @Override
    public void willCancelRecord() {
        AudioRecordManager.getInstance(mContext,UID,id,time).willCancelRecord();
    }

    @Override
    public void continueRecord() {
        AudioRecordManager.getInstance(mContext,UID,id,time).continueRecord();
    }

    @Override
    public void startPlayRecord(final int position) {
        File item = mListData.get(position);
        Uri audioUri = Uri.fromFile(item);
        Log.d("P_startPlayRecord", audioUri.toString());
        AudioPlayManager.getInstance().startPlay(mContext, audioUri, new IAudioPlayListener() {
            @Override
            public void onStart(Uri var1) {
                mView.startPlayAnim(position);
            }

            @Override
            public void onStop(Uri var1) {
                mView.stopPlayAnim();
            }

            @Override
            public void onComplete(Uri var1) {
                mView.stopPlayAnim();
            }
        });
    }

    /**
     * 初始化音频播放管理对象
     */
    private void initAudioManager() {
        AudioRecordManager.getInstance(mContext,UID,id,time).setAudioSavePath(mAudioDir.getAbsolutePath());
        AudioRecordManager.getInstance(mContext,UID,id,time).setMaxVoiceDuration(MAX_VOICE_TIME);
        AudioRecordManager.getInstance(mContext,UID,id,time).setAudioRecordListener(new IAudioRecordListener() {
            @Override
            public void initTipView() {
                mView.showNormalTipView();
            }

            @Override
            public void setTimeoutTipView(int counter) {
                mView.showTimeOutTipView(counter);
            }

            @Override
            public void setRecordingTipView() {
                mView.showRecordingTipView();
            }

            @Override
            public void setAudioShortTipView() {
                mView.showRecordTooShortTipView();
            }

            @Override
            public void setCancelTipView() {
                mView.showCancelTipView();
            }

            @Override
            public void destroyTipView() {
                mView.hideTipView();
            }

            @Override
            public void onStartRecord() {

            }

            @Override
            public void onFinish(Uri audioPath, int duration) {
                File file = new File(audioPath.getPath());
                if (file.exists()) {
                    Toast.makeText(mContext.getApplicationContext(), "录制成功", Toast.LENGTH_SHORT).show();
                    loadAudioCacheData();
                }
            }

            @Override
            public void onAudioDBChanged(int db) {
                mView.updateCurrentVolume(db);
            }
        });
    }

    private void loadAudioCacheData() {
        if (mAudioDir.exists()) {
            mListData.clear();
            File[] files = mAudioDir.listFiles();
            for (File file : files) {
                if (file.getAbsolutePath().endsWith("voice")) {
                    mListData.add(file);
                    int lg=mListData.size();
                    chatList.add(new Chat(lg+"",ChatAdapter.TYPE_SEND,UID, TypeSystem.MSG_VOICE,new MsgTool().setTime(0)));
                    chatAdapter =new ChatAdapter(mContext,chatList,MainPresenter.this);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(chatAdapter);
                }
            }
            mView.showList(mListData);
        }
    }
}
