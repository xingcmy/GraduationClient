package com.cn.graduationclient.homepage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.graduationclient.R;
import com.cn.graduationclient.audio.MainContract;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.db.FriendDbHelper;
import com.cn.graduationclient.db.MessageDbHelper;
import com.cn.graduationclient.db.NewFriendFbhelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.AgreeRefuse;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.message.Random.random;
import com.cn.graduationclient.music.GetMusic;
import com.cn.graduationclient.music.Music;
import com.cn.graduationclient.music.MusicUtil;
import com.cn.graduationclient.music.Random;
import com.cn.graduationclient.my.Information;
import com.cn.graduationclient.my.friend;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.xingcmyAdapter.Chat;
import com.cn.graduationclient.xingcmyAdapter.ChatAdapter;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.Lable;
import com.cn.graduationclient.xingcmyAdapter.MessageAdapter;
import com.cn.graduationclient.xingcmyAdapter.MusicAdapter;
import com.cn.graduationclient.xingcmyAdapter.friendUItl;
import com.cn.graduationclient.xingcmyAdapter.friends;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kotlin.Suppress;

public class HomePageActivity extends Activity implements View.OnClickListener {

    private static final String TAG ="XINGCMY";
    private static final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS =1 ;

    LinearLayout radio_message,radio_music,radio_my;
    TextView lable,data,title;
    ImageView photo;
    ListView listView,listView_msg;
    MediaPlayer mediaPlayer;
    List<Music> lists;
    List<friends> friendsList;
    MusicAdapter adapter;
    MessageAdapter adapter_friend;

    Timer timer = new Timer();

    int index = 0;
    int legtht,back=0;
    Cursor cursor;
    int flog=0,flag=0,success=0,fsongrdm=0,songfoud=0;

    ImageButton more;

    Random random=new Random();

    final int [] image=new int[]{R.drawable.ic_remote_view_play,R.drawable.ic_remote_view_pause};
    final int [] music_image=new int[]{R.drawable.ic_play,R.drawable.ic_pause};
    final int [] music_mode_image=new int[]{R.drawable.ic_play_mode_list,R.drawable.ic_play_mode_loop,R.drawable.ic_play_mode_shuffle,R.drawable.ic_play_mode_single};//顺序 列表循环 随机 单曲循环

    ImageView image_view_play_last,image_view_play_next,image_view_play_toggle;
    LinearLayout liner_music_list,liner_music;
    AppCompatSeekBar seekBar;
    AppCompatImageView button_view_music_toggle,button_view_music_last,button_view_music_next,button_play_mode_music_toggle;
    TextView text_view_name,text_view_music_name;
    ImageView image_view_album,image_view_album_music;
    RadioGroup radioGroup;
    RadioButton radioButton_list, radioButton_music, radioButton_files, radioButton_settings;
    TextView text_view_duration,text_view_progress;
    Handler  handler;

    HttpUtil httpUtil=new HttpUtil();

    String my_id,my_phone,my_email;
    String UID,PHONE,EMAIL,NAME;

    MessageDbHelper messageDbHelper;
    FriendDbHelper friendDbHelper;
    NewFriendFbhelper newFriendFbhelper;

    SQLiteDatabase sqLiteDatabase,sqLiteDatabase_friend,sqLiteDatabase_new;

    @SuppressLint("HandlerLeak")
    Handler handler_longMusic=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler_newMsg=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            friendsList=new friendUItl().getFriend(HomePageActivity.this,UID);
            if (friendsList!=null){
                adapter_friend=new MessageAdapter(friendsList,HomePageActivity.this);
                listView_msg.setAdapter(adapter_friend);
            }
        }
    };
    @SuppressLint("HandlerLeak")
    Handler handler_friendName=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    String friendName=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(friendName);
                        String Id=jsonObject.getString("Id");
                        String name=jsonObject.getString("name");
                        Intent intent=new Intent(HomePageActivity.this, FriendMessage.class);
                        intent.putExtra(StructureSystem.UID,UID);
                        intent.putExtra(StructureSystem.ID,Id);
                        intent.putExtra(StructureSystem.NAME,name);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_msg=new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String message= (String) msg.obj;
            try {
                JSONObject jsonObject=new JSONObject(message);
                String error=jsonObject.getString(StructureSystem.ERROR);
                if (error.equals(StructureSystem.SUCCESS)){
                    String id=jsonObject.getString(StructureSystem.ID);
                    String now=jsonObject.getString(StructureSystem.MSG);
                    int type=jsonObject.getInt(StructureSystem.TYPE);
                    Log.d("cs",type+"");
                    String time=jsonObject.getString("time");
                    cursor=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+id+"'",null);
                    if (cursor.getCount()<=0){
                        if (type==TypeSystem.MSG_IMAGE){
                            String img="图片";
                            sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+id+"','"+img+"','"+time+"',"+type+")");
                        }else if (type==TypeSystem.ADD_FRIEND){
                            String newName="新朋友";
                            Cursor c=cursor=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+newName+"'",null);
                            if (c.getCount()>0){
                                sqLiteDatabase_friend.execSQL("update friend set msg='"+now+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+newName+"'");
                            }else if (c.getCount()<=0){
                                sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+newName+"','"+now+"','"+time+"',"+type+")");
                            }
                        }else if (type==TypeSystem.ADD_AGREE){
                            String friend="快来一起聊天吧！";
                            sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+id+"','"+friend+"','"+time+"',"+type+")");
                        }else if (type==TypeSystem.ADD_REFUSE){
                            String Name="通知";
                            String MsgRefuse="你的好友请求被"+id+"拒绝了"+now;
                            sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+Name+"','"+MsgRefuse+"','"+time+"',"+type+")");
                        }else {
                            sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+id+"','"+now+"','"+time+"',"+type+")");
                        }
                    }else if (cursor.getCount()>0){
                        if (type==TypeSystem.MSG_IMAGE){
                            String img="图片";
                            sqLiteDatabase_friend.execSQL("update friend set msg='"+img+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+id+"'");
                        }else if (type==TypeSystem.ADD_FRIEND){
                            String newName="新朋友";
                            Cursor c=cursor=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+newName+"'",null);
                            if (c.getCount()>0){
                                sqLiteDatabase_friend.execSQL("update friend set msg='"+now+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+newName+"'");
                            }else if (c.getCount()<=0){
                                sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+newName+"','"+now+"','"+time+"',"+type+")");
                            }
                        }else {
                            sqLiteDatabase_friend.execSQL("update friend set msg='"+now+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+id+"'");
                        }
                    }
                    friendsList=new friendUItl().getFriend(HomePageActivity.this,UID);
                    adapter_friend=new MessageAdapter(friendsList,HomePageActivity.this);
                    listView_msg.setAdapter(adapter_friend);

                    String filePath="";
                    if (type==TypeSystem.MSG_IMAGE){
                        byte[] bytes=new MsgTool().StringToByte(now);

                        filePath=new MsgTool().getFileByBytes(bytes,HomePageActivity.this.getExternalFilesDir(null).getPath(),id+"to"+UID+setTime(1)+".jpg");
                        Toast.makeText(HomePageActivity.this,filePath,Toast.LENGTH_SHORT).show();
                        Log.d("cs",filePath);
                        if (filePath!=null||filePath!="") {

                            sqLiteDatabase.execSQL("insert into message values('" + UID + "','" + id + "','" + id + "','" + filePath + "','" + time + "'," + type + ")");

                        }
                    }else if (type==TypeSystem.MSG_TEXT){
                        sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+id+"','"+id+"','"+now+"','"+time+"',"+type+")");
                    }

                    //sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+id+"','"+id+"','"+now+"','"+time+"',"+type+")");
                   else if (type==TypeSystem.ADD_FRIEND){
                        photo.setVisibility(View.VISIBLE);
                        Cursor c=sqLiteDatabase_new.rawQuery("select * from newfriend where uid='"+UID+"' and friend='"+id+"'",null);
                        if (c.getCount()>0){
                            sqLiteDatabase_new.execSQL("update newfriend set msg='"+now+"',type='null' where uid='"+UID+"' and friend='"+id+"'");
                        }else if(c.getCount()<=0){
                            sqLiteDatabase_new.execSQL("insert into newfriend values('"+UID+"','"+id+"','"+now+"','null')");
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            List<String> permissions = new ArrayList<String>();
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            } else {
                messageDbHelper=new MessageDbHelper(this);
                friendDbHelper=new FriendDbHelper(this);
                newFriendFbhelper=new NewFriendFbhelper(this);

                sqLiteDatabase=messageDbHelper.getReadableDatabase();
                sqLiteDatabase_friend=friendDbHelper.getReadableDatabase();
                sqLiteDatabase_new=newFriendFbhelper.getReadableDatabase();

                Intent intent=getIntent();

                UID=intent.getStringExtra(StructureSystem.UID);
                my_phone=intent.getStringExtra("phone");
                my_email=intent.getStringExtra("email");

                back=1;
                getViewId();
                getId();
                onclickMessage();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new GetMsg().start();
                       // new NewMsg().start();
                    }
                }).start();
                friendsList=new friendUItl().getFriend(HomePageActivity.this,UID);
                if (friendsList!=null){
                    adapter_friend=new MessageAdapter(friendsList,HomePageActivity.this);
                    listView_msg.setAdapter(adapter_friend);
                }
            }




            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);

            } else {

            }

            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_SOME_FEATURES_PERMISSIONS: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void getViewId(){
        radio_music=findViewById(R.id.layout_music_body);
        radio_message=findViewById(R.id.layout_message_body);
        radio_my=findViewById(R.id.layout_my_body);
    }

    public void onclickMessage(View view) {
        back=1;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new GetMsg().start();
//            }
//        }).start();
        friendsList=new friendUItl().getFriend(HomePageActivity.this,UID);
        if (friendsList!=null){
            adapter_friend=new MessageAdapter(friendsList,HomePageActivity.this);
            listView_msg.setAdapter(adapter_friend);
        }



        onclickMessage();
    }

    @SuppressLint("HandlerLeak")
    public void onclickMusic(View view) {
        back=1;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new GetMsg().start();
//            }
//        }).start();
        radio_music.setVisibility(View.VISIBLE);
        radio_message.setVisibility(View.GONE);
        radio_my.setVisibility(View.GONE);


        liner_music_list.setVisibility(View.VISIBLE);
        liner_music.setVisibility(View.GONE);
        Listening();
        success=0;




        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    mediaPlayer.seekTo(i);
                    //text_view_progress.setText(" : "+String.valueOf(seekBar.getProgress()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //Toast.makeText(GetMusic.this,""+legtht,Toast.LENGTH_LONG).show();

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String path=lists.get(position).getData();
                String music_name=lists.get(position).getTitle();
                String music_artist=lists.get(position).getArtist();
                String music_duration=lists.get(position).getDuration();

                Message message=new Message();
                String[] msg=new String[]{path,music_name,music_artist,music_duration};
                message.obj=msg;
                handler_longMusic.sendMessage(message);

                View view1=LinearLayout.inflate(HomePageActivity.this,R.layout.long_list_music,null);
                final AlertDialog.Builder ab=new AlertDialog.Builder(HomePageActivity.this);
                AlertDialog out_long=ab.create();
                final Window window=out_long.getWindow();
               // window.setBackgroundDrawable(new ColorDrawable(0));
                out_long.setView(view1);
                out_long.show();

                int tim=Integer.parseInt(lists.get(position).getDuration());

                String tm=new Music().formatTime(tim);

                HoldTitle holdTitle=view1.findViewById(R.id.long_hold);
                TextView name_long=view1.findViewById(R.id.text_title_long);
                TextView artist_long=view1.findViewById(R.id.text_artist_long);
                TextView duration_long=view1.findViewById(R.id.text_duration_long);
                Button no=view1.findViewById(R.id.long_no);
                Button yes=view1.findViewById(R.id.long_yes);

                name_long.setText(music_name);
                artist_long.setText(music_artist);
                duration_long.setText(tm);

                holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        out_long.dismiss();
                    }
                });

                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        out_long.dismiss();
                    }
                });
                yes.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        File file=new File(path);
                        String ends = null;
                        if (file.getAbsolutePath().endsWith("mp3")){
                            ends="mp3";
                        }else if (file.getAbsolutePath().endsWith("wav")){
                            ends="wav";
                        }else if (file.getAbsolutePath().endsWith("flac")){
                            ends="flac";
                        }else if (file.getAbsolutePath().endsWith("ape")){
                            ends="ape";
                        }
                        Toast.makeText(HomePageActivity.this,ends,Toast.LENGTH_SHORT).show();

                        byte[] BFile =new MsgTool().getBytesByFile(path);
                        String bytes=new MsgTool().ByteToString(BFile);
                    }
                });






                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;
                seekBar.setProgress(0);
                mediaPlayer.reset();
                try {
                    seekBar.setMax(Integer.parseInt(lists.get(i).getDuration()));
                    mediaPlayer.setDataSource(lists.get(i).getData());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    flog=1;
                    success=1;
                    songfoud=1;
                    Update();
                    image_view_play_toggle.setImageResource(image[1]);
                    text_view_name.setText(lists.get(i).getTitle());
                    button_view_music_toggle.setImageResource(music_image[1]);
                    text_view_music_name.setText(lists.get(i).getTitle());
                    int tim=Integer.parseInt(lists.get(i).getDuration());

                    String tm=Music.formatTime(tim);
                    text_view_duration.setText(tm);
                    //Toast.makeText(GetMusic.this,""+i,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,1000);

        handler= new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//更新Seekbar
                seekBar.setProgress(msg.what);

                Update();

                //text_view_progress.setText(musicTime(msg.what));
            }
        };

        //if (succes==1)
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

                if (success==1)
                    switch (flag){
                        case 0:
                            index++;
                            if (index>=legtht){
                                index=0;
                                mediaPlayer.pause();
                            }else {
                                mediaPlayer.reset();
                                try {
                                    seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                                    mediaPlayer.setDataSource(lists.get(index).getData());
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();
                                    flog=1;
                                    Update();
                                    text_view_music_name.setText(lists.get(index).getTitle());
                                    text_view_name.setText(lists.get(index).getTitle());
                                    //button_view_music_toggle.setImageResource(music_image[1]);
                                    // image_view_play_toggle.setImageResource(image[1]);
                                    int tim=Integer.parseInt(lists.get(index).getDuration());

                                    String tm=Music.formatTime(tim);
                                    text_view_duration.setText(tm);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case 1:
                            index++;
                            if (index>=legtht)
                                index=0;
                            mediaPlayer.reset();
                            try {
                                seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                                mediaPlayer.setDataSource(lists.get(index).getData());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                flog=1;
                                Update();
                                text_view_music_name.setText(lists.get(index).getTitle());
                                text_view_name.setText(lists.get(index).getTitle());
                                // button_view_music_toggle.setImageResource(music_image[1]);
                                //image_view_play_toggle.setImageResource(image[1]);
                                int tim=Integer.parseInt(lists.get(index).getDuration());

                                String tm=Music.formatTime(tim);
                                text_view_duration.setText(tm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            index=random.Random(index,legtht,fsongrdm);
                            mediaPlayer.reset();
                            try {
                                seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                                mediaPlayer.setDataSource(lists.get(index).getData());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                flog=1;
                                fsongrdm++;
                                if (fsongrdm > 10)
                                    fsongrdm=0;
                                Update();
                                text_view_music_name.setText(lists.get(index).getTitle());
                                text_view_name.setText(lists.get(index).getTitle());
                                //button_view_music_toggle.setImageResource(music_image[1]);
                                // image_view_play_toggle.setImageResource(image[1]);
                                int tim=Integer.parseInt(lists.get(index).getDuration());

                                String tm=Music.formatTime(tim);
                                text_view_duration.setText(tm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 3:
                            mediaPlayer.reset();
                            try {
                                seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                                mediaPlayer.setDataSource(lists.get(index).getData());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                flog=1;
                                Update();
                                text_view_music_name.setText(lists.get(index).getTitle());
                                text_view_name.setText(lists.get(index).getTitle());
                                //button_view_music_toggle.setImageResource(music_image[1]);
                                //image_view_play_toggle.setImageResource(image[1]);
                                int tim=Integer.parseInt(lists.get(index).getDuration());

                                String tm=Music.formatTime(tim);
                                text_view_duration.setText(tm);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                    }

            }
        });


    }

    public void onclickMy(View view) {
        back=1;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new GetMsg().start();
//            }
//        }).start();
        radio_my.setVisibility(View.VISIBLE);
        radio_message.setVisibility(View.GONE);
        radio_music.setVisibility(View.GONE);

        Lable my_information=findViewById(R.id.personal_information);
        Lable my_friend=findViewById(R.id.my_friend);

        ConstraintLayout xin_friend=findViewById(R.id.xin_friend);

        cursor=sqLiteDatabase.rawQuery("select * from message where uid='"+UID+"'and type="+TypeSystem.ADD_FRIEND+"",null);
        int lenght=cursor.getCount();
        if (lenght<=0){
            photo.setVisibility(View.GONE);
        }else if (lenght>0){
            photo.setVisibility(View.VISIBLE);
        }

        //my_information.setNumber(13);
        my_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageActivity.this, Information.class);

                intent.putExtra("UID",UID);
                intent.putExtra("phone",my_phone);
                intent.putExtra("email",my_email);

                startActivity(intent);
            }
        });


        my_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageActivity.this, friend.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
            }
        });
        xin_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageActivity.this, AgreeRefuse.class);
                intent.putExtra(StructureSystem.UID,UID);
                startActivity(intent);
            }
        });
    }

    public void onclickMessage(){
        radio_message.setVisibility(View.VISIBLE);
        radio_music.setVisibility(View.GONE);
        radio_my.setVisibility(View.GONE);

        friendsList=new friendUItl().getFriend(this,UID);
        adapter_friend=new MessageAdapter(friendsList,this);
        listView_msg.setAdapter(adapter_friend);

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePageActivity.this, com.cn.graduationclient.message.Random.random.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
            }
        });

        listView_msg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(HomePageActivity.this,friendsList.get(position).getType(),Toast.LENGTH_SHORT).show();
                Log.d("cs",friendsList.get(position).getType()+"");
                if (friendsList.get(position).getType()==TypeSystem.ADD_FRIEND){
                    Intent intent=new Intent(HomePageActivity.this, AgreeRefuse.class);
                    intent.putExtra(StructureSystem.UID,UID);
                    startActivity(intent);
                }else //if (friendsList.get(position).getType()==TypeSystem.MSG_IMAGE||friendsList.get(position).getType()==TypeSystem.MSG_TEXT)
                    {
                    String friendID=friendsList.get(position).getId();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Message message=new Message();
                                String friendName=httpUtil.httpInformation(friendID);
                                message.what=0x01;
                                message.obj=friendName;
                                handler_friendName.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }


            }
        });


    }

    public String getFriendName(String id){

        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.radio_button_list:
                liner_music_list.setVisibility(View.VISIBLE);
                liner_music.setVisibility(View.GONE);
                break;
            case R.id.radio_button_music:
                //case R.id.image_view_album:
                //radioButton_music.setChecked(true);
                liner_music_list.setVisibility(View.GONE);
                liner_music.setVisibility(View.VISIBLE);
                break;
            case R.id.radio_button_local_files:
            case R.id.radio_button_settings:
                liner_music_list.setVisibility(View.GONE);
                liner_music.setVisibility(View.GONE);
                break;
            case R.id.image_view_play_last:
            case R.id.button_play_music_last:
                index--;
                if (index<0)
                    index=legtht-1;
                mediaPlayer.reset();
                try {
                    seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                    mediaPlayer.setDataSource(lists.get(index).getData());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    flog=1;
                    success=1;
                    songfoud=1;
                    Update();
                    text_view_music_name.setText(lists.get(index).getTitle());
                    text_view_name.setText(lists.get(index).getTitle());
                    button_view_music_toggle.setImageResource(music_image[1]);
                    image_view_play_toggle.setImageResource(image[1]);
                    int tim=Integer.parseInt(lists.get(index).getDuration());

                    String tm=Music.formatTime(tim);
                    text_view_duration.setText(tm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_view_play_next:
            case R.id.button_play_music_next:
                index++;
                if (index>=legtht)
                    index=0;
                mediaPlayer.reset();
                try {
                    seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                    mediaPlayer.setDataSource(lists.get(index).getData());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    flog=1;
                    success=1;
                    songfoud=1;
                    Update();
                    text_view_music_name.setText(lists.get(index).getTitle());
                    text_view_name.setText(lists.get(index).getTitle());
                    button_view_music_toggle.setImageResource(music_image[1]);
                    image_view_play_toggle.setImageResource(image[1]);
                    int tim=Integer.parseInt(lists.get(index).getDuration());

                    String tm=Music.formatTime(tim);
                    text_view_duration.setText(tm);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.image_view_play_toggle:
            case R.id.button_play_music_toggle:
                if (flog==0){
                    mediaPlayer.start();
                    flog=1;
                    success=1;
                    button_view_music_toggle.setImageResource(music_image[1]);
                    image_view_play_toggle.setImageResource(image[1]);
                    if (songfoud==0){
                        mediaPlayer.reset();
                        try {
                            seekBar.setMax(Integer.parseInt(lists.get(index).getDuration()));
                            mediaPlayer.setDataSource(lists.get(index).getData());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                            flog=1;
                            success=1;
                            Update();
                            text_view_music_name.setText(lists.get(index).getTitle());
                            text_view_name.setText(lists.get(index).getTitle());
                            //button_view_music_toggle.setImageResource(music_image[1]);
                            // image_view_play_toggle.setImageResource(image[1]);
                            int tim=Integer.parseInt(lists.get(index).getDuration());

                            String tm=Music.formatTime(tim);
                            text_view_duration.setText(tm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else if (flog==1){
                    mediaPlayer.pause();
                    flog=0;
                    success=0;
                    button_view_music_toggle.setImageResource(music_image[0]);
                    image_view_play_toggle.setImageResource(image[0]);
                }
                break;
            case R.id.button_play_mode_music_toggle:
                flag++;
                if (flag>3)
                    flag=0;
                button_play_mode_music_toggle.setImageResource(music_mode_image[flag]);
                break;
        }
    }

    public void Update(){
        int currentTime=mediaPlayer.getCurrentPosition();//获取当前进度
        seekBar.setProgress(currentTime);//设置进度条
        text_view_progress.setText(musicTime(currentTime));//设置显示时间
        handler.sendEmptyMessageDelayed(0,1000);//每隔1000ms更新一次
    }

    public void getId() {

        radioButton_list = findViewById(R.id.radio_button_list);
        radioButton_music = findViewById(R.id.radio_button_music);
        radioButton_files = findViewById(R.id.radio_button_local_files);
        radioButton_settings = findViewById(R.id.radio_button_settings);

        image_view_play_last=findViewById(R.id.image_view_play_last);
        image_view_play_next=findViewById(R.id.image_view_play_next);
        image_view_play_toggle=findViewById(R.id.image_view_play_toggle);

        listView = findViewById(R.id.listView);
        listView_msg=findViewById(R.id.home_list_msg);

        image_view_play_last.setImageResource(R.drawable.ic_remote_view_play_last);
        image_view_play_next.setImageResource(R.drawable.ic_remote_view_play_next);

        liner_music_list=findViewById(R.id.liner_music_list);
        liner_music=findViewById(R.id.liner_music);

        radioGroup=findViewById(R.id.radio_group_controls);

        seekBar=findViewById(R.id.seek_bar);

        more=findViewById(R.id.more);

        text_view_name=findViewById(R.id.text_view_name);
        text_view_music_name=findViewById(R.id.text_view_music_name);

        button_view_music_toggle=findViewById(R.id.button_play_music_toggle);
        button_view_music_last=findViewById(R.id.button_play_music_last);
        button_view_music_next=findViewById(R.id.button_play_music_next);

        text_view_duration=findViewById(R.id.text_view_duration);
        text_view_progress=findViewById(R.id.text_view_progress);

        image_view_album_music=findViewById(R.id.image_view_album_music);
        image_view_album=findViewById(R.id.image_view_album);

        image_view_album.setImageResource(R.drawable.ic_baseline_audiotrack);

        image_view_album_music.setImageResource(R.drawable.ic_baseline_album);

        button_play_mode_music_toggle=findViewById(R.id.button_play_mode_music_toggle);

        lable=findViewById(R.id.new_friend_lable);
        data=findViewById(R.id.new_friend_data);
        title=findViewById(R.id.new_friend_title);
        photo=findViewById(R.id.new_friend_photo);

        button_play_mode_music_toggle.setImageResource(music_mode_image[0]);
        button_view_music_toggle.setImageResource(music_image[0]);
        image_view_play_toggle.setImageResource(image[0]);
        radioButton_list.setChecked(true);

        NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            nMgr.cancelAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        friendsList=new friendUItl().getFriend(this,UID);
        if (friendsList!=null){
            adapter_friend=new MessageAdapter(friendsList,this);
            listView_msg.setAdapter(adapter_friend);
        }


        lists = new MusicUtil().getMusic(this);
        Log.i(TAG, "" + lists);
        adapter = new MusicAdapter(lists, this);
        listView.setAdapter(adapter);

        legtht=listView.getCount();

        seekBar.setProgress(0);
        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }



    }

    public void Listening(){
        image_view_play_last.setOnClickListener(HomePageActivity.this);
        image_view_play_next.setOnClickListener(HomePageActivity.this);
        image_view_play_toggle.setOnClickListener(HomePageActivity.this);
        radioButton_list.setOnClickListener(HomePageActivity.this);
        radioButton_music.setOnClickListener(HomePageActivity.this);
        radioButton_files.setOnClickListener(HomePageActivity.this);
        radioButton_settings.setOnClickListener(HomePageActivity.this);
        button_view_music_toggle.setOnClickListener(HomePageActivity.this);
        button_view_music_last.setOnClickListener(HomePageActivity.this);
        button_view_music_next.setOnClickListener(HomePageActivity.this);
        image_view_album.setOnClickListener(HomePageActivity.this);
        button_play_mode_music_toggle.setOnClickListener(HomePageActivity.this);

    }

    private String musicTime(int length){
        Date date = new Date(length);
//时间格式化工具
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == event.KEYCODE_BACK) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            if (back==1){
                back=0;
            }else {
                return super.onKeyDown(keyCode,event);
            }
        }
        return false;
    }

    class GetMsg extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                try {
//                    friendsList=new friendUItl().getFriend(HomePageActivity.this,UID);
//                    adapter_friend=new MessageAdapter(friendsList,HomePageActivity.this);
//                    listView_msg.setAdapter(adapter_friend);
                    Thread.sleep(500);
                    Object msg=httpUtil.httpInMsg(UID, TypeSystem.READ);
                    Message message=new Message();
                    message.obj=msg;
                    handler_msg.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    class NewMsg extends Thread{
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    Thread.sleep(2000);
                    Message message=new Message();
                    handler_newMsg.sendMessage(message);
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public String setTime(int i){
        String time="";
        switch (i){
            case 0:
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
                Date date = new Date(System.currentTimeMillis());
                time=formatter.format(date);

                break;
            case 1:
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter1= new SimpleDateFormat("yyyyMMddHHmmss");
                Date date1 = new Date(System.currentTimeMillis());
                time=formatter1.format(date1);
                break;

        }
        return time;
    }

    public void SetList(){

    }
}
