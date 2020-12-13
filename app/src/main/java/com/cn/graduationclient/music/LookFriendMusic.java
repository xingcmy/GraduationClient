package com.cn.graduationclient.music;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.homepage.HomePageActivity;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.LookMusicAdapter;
import com.mob.tools.RxMob;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LookFriendMusic extends Activity {

    List<FriendMusic> musicList=new ArrayList<>();

    HttpUtil httpUtil=new HttpUtil();
    String UID,id,friendName,music;
    String[] FriendMusic,name;
    ListView listView;
    TextView textView;


    MediaPlayer mediaPlayer;

    String MusicType;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    String del=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(del);
                        String error=jsonObject.getString(StructureSystem.ERROR);
                        if (error.equals(StructureSystem.SUCCESS)){
                            Toast.makeText(LookFriendMusic.this,"删除成功",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x02:
                    int count=msg.arg1;
                    textView.setText(count+"");
                    if (count!=0){
                        FriendMusic=new String[count];
                        name=new String[count];
                    }
                    String[] lookMusic=(String[]) msg.obj;
                    for (int i=0;i<count;i++){
                        try {
                            JSONObject jsonObject=new JSONObject(lookMusic[i]);
                            FriendMusic[i]=jsonObject.getString("msg");
                            name[i]=jsonObject.getString("data");
                            Log.d("music",name[i]);
                            FriendMusic friendMusic=new FriendMusic(jsonObject.getString("msg"),jsonObject.getString("data"));
                            // musicList.add(friendMusic);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (count!=0){
                        musicList=new SetFriendMusic().getMusic(LookFriendMusic.this,FriendMusic,name);
                        LookMusicAdapter adapter=new LookMusicAdapter(LookFriendMusic.this,musicList);
                        listView.setAdapter(adapter);
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookfriendmusic);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");
        MusicType=intent.getStringExtra("type");
        friendName=intent.getStringExtra("name");
        id=intent.getStringExtra("id");
        music=id;

        Log.d("id",music);

        HoldTitle holdTitle=findViewById(R.id.look_hold);
        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        holdTitle.setTvtitle("来自"+friendName);

        getId();
        textView.setText("正在获取歌曲......");

        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String count=httpUtil.httpGetMusic(music,0);
                    JSONObject jsonObject=new JSONObject(count);
                    int num=jsonObject.getInt("fileCount");
                    String[] friend=new String[num];
                    Message message=new Message();
                    message.arg1=num;
                    Log.d("cs",num+"");
                    for (int i=0;i<num;i++){
                        String musicF=httpUtil.httpGetMusic(music,1);
                        friend[i]=musicF;

                    }
                    message.obj=friend;
                    message.what=0x02;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
//        musicList=new SetFriendMusic().getMusic(LookFriendMusic.this,FriendMusic,name);
//        LookMusicAdapter adapter=new LookMusicAdapter(this,musicList);
//        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View view1= LinearLayout.inflate(LookFriendMusic.this,R.layout.playlookmusic,null);
                final AlertDialog.Builder ab=new AlertDialog.Builder(LookFriendMusic.this);
                AlertDialog out_long=ab.create();
                final Window window=out_long.getWindow();
                // window.setBackgroundDrawable(new ColorDrawable(0));
                out_long.setView(view1);
                out_long.show();

                String MusicName=musicList.get(position).getName();
                String Music= (String) musicList.get(position).getMusic();
                TextView textView=view1.findViewById(R.id.play_music);
                textView.setText(MusicName);
                Button button=view1.findViewById(R.id.play_button);
                HoldTitle holdTitle1=view1.findViewById(R.id.play_hold);
//                if (MusicType.equals("my2048")){
//                    button.setText("删除");
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        String del=httpUtil.httpGetMusic(UID,3,MusicName);
//                                        Message message=new Message();
//                                        message.what=0x01;
//                                        message.obj=del;
//                                        handler.sendMessage(message);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }).start();
//                        }
//                    });
//                }else if (MusicType.equals("you2048")){
//                    byte[] bytes=new MsgTool().StringToByte(Music);
//                    String  filePath=new MsgTool().getFileByBytes(bytes,LookFriendMusic.this.getExternalFilesDir(null).getPath(),MusicName);
//                    button.setText("开始播放");
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (button.getText().toString().equals("开始播放")){
//                                button.setText("结束播放");
//                                mediaPlayer.reset();
//                                try {
//                                    mediaPlayer.setDataSource(filePath);
//                                    mediaPlayer.prepare();
//                                    mediaPlayer.start();
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }else if (button.getText().toString().equals("结束播放")){
//                                button.setText("开始播放");
//                                mediaPlayer.pause();
//                            }
//                        }
//                    });
//                }

                byte[] bytes=new MsgTool().StringToByte(Music);
                String  filePath=new MsgTool().getFileByBytes(bytes,LookFriendMusic.this.getExternalFilesDir(null).getPath(),MusicName);
                button.setText("开始播放");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (button.getText().toString().equals("开始播放")){
                            button.setText("结束播放");
                            mediaPlayer.reset();
                            try {
                                mediaPlayer.setDataSource(filePath);
                                mediaPlayer.prepare();
                                mediaPlayer.start();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if (button.getText().toString().equals("结束播放")){
                            button.setText("开始播放");
                            mediaPlayer.pause();
                        }
                    }
                });

                holdTitle1.setIvbackOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File file=new File(filePath);
                        file.delete();
                        mediaPlayer.pause();
                        out_long.dismiss();
                    }
                });




                return false;
            }
        });



    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.d("XINGCMY","页面刷新");
//        onCreate(null);
//
//    }

    private void getId() {
        listView=findViewById(R.id.look_list);
        textView=findViewById(R.id.look_text);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK) {
            //Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            finish();
            return super.onKeyDown(keyCode,event);
        }
        return false;
    }
}
