package com.cn.graduationclient.music;


import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSeekBar;


import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.R;
import com.cn.graduationclient.xingcmyAdapter.MusicAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GetMusic extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "GetMusic";
    HoldTitle music_holld;
    ListView listView;
    MediaPlayer mediaPlayer;
    List<Music> lists;
    MusicAdapter adapter;
    Timer timer = new Timer();
    int index = 0;
    int legtht;
    int flog=0,flag=0,succes=0,fsongrdm=0,songfoud=0;
    Random random=new Random();
    final int [] image=new int[]{R.drawable.ic_remote_view_play,R.drawable.ic_remote_view_pause};
    final int [] music_image=new int[]{R.drawable.ic_play,R.drawable.ic_pause};
    final int [] music_mode_image=new int[]{R.drawable.ic_play_mode_list,R.drawable.ic_play_mode_loop,R.drawable.ic_play_mode_shuffle,R.drawable.ic_play_mode_single};//顺序 列表循环 随机 单曲循环
    ImageView image_view_play_last,image_view_play_next,image_view_play_toggle;
    LinearLayout liner_music_list,liner_music;
    AppCompatSeekBar seekBar;
    AppCompatImageView button_view_music_toggle,button_view_music_last,button_view_music_next,button_play_mode_music_toggle;

    TextView text_view_name,text_view_music_name;

    ImageView image_view_album;


    RadioGroup radioGroup;
    RadioButton radioButton_list, radioButton_music, radioButton_files, radioButton_settings;

    TextView text_view_duration,text_view_progress;

    Handler  handler;



    boolean isStop=false;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_tool);


        listView = findViewById(R.id.listView);
        getId();
        button_play_mode_music_toggle.setImageResource(music_mode_image[0]);
        button_view_music_toggle.setImageResource(music_image[0]);
        image_view_play_toggle.setImageResource(image[0]);
        radioButton_list.setChecked(true);

        liner_music_list.setVisibility(View.VISIBLE);
        liner_music.setVisibility(View.GONE);
        Listening();

        seekBar.setProgress(0);
        mediaPlayer = new MediaPlayer();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b == true){
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

        lists = new MusicUtil().getMusic(this);
        Log.i(TAG, "" + lists);
        adapter = new MusicAdapter(lists, this);
        listView.setAdapter(adapter);

        legtht=listView.getCount();

        //Toast.makeText(GetMusic.this,""+legtht,Toast.LENGTH_LONG).show();

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
                    succes=1;
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
        music_holld.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
                finish();
            }
        });

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

                if (succes==1)
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

    public void Update(){
        int currentTime=mediaPlayer.getCurrentPosition();//获取当前进度
        seekBar.setProgress(currentTime);//设置进度条
        text_view_progress.setText(musicTime(currentTime));//设置显示时间
        handler.sendEmptyMessageDelayed(0,1000);//每隔1000ms更新一次
    };

    public void getId() {

        radioButton_list = findViewById(R.id.radio_button_list);
        radioButton_music = findViewById(R.id.radio_button_music);
        radioButton_files = findViewById(R.id.radio_button_local_files);
        radioButton_settings = findViewById(R.id.radio_button_settings);

        image_view_play_last=findViewById(R.id.image_view_play_last);
        image_view_play_next=findViewById(R.id.image_view_play_next);
        image_view_play_toggle=findViewById(R.id.image_view_play_toggle);

        liner_music_list=findViewById(R.id.liner_music_list);
        liner_music=findViewById(R.id.liner_music);

        radioGroup=findViewById(R.id.radio_group_controls);

        seekBar=findViewById(R.id.seek_bar);

        text_view_name=findViewById(R.id.text_view_name);
        text_view_music_name=findViewById(R.id.text_view_music_name);

        button_view_music_toggle=findViewById(R.id.button_play_music_toggle);
        button_view_music_last=findViewById(R.id.button_play_music_last);
        button_view_music_next=findViewById(R.id.button_play_music_next);

        text_view_duration=findViewById(R.id.text_view_duration);
        text_view_progress=findViewById(R.id.text_view_progress);

        image_view_album=findViewById(R.id.image_view_album);
        button_play_mode_music_toggle=findViewById(R.id.button_play_mode_music_toggle);



    }

    public void Listening(){
        image_view_play_last.setOnClickListener(GetMusic.this);
        image_view_play_next.setOnClickListener(GetMusic.this);
        image_view_play_toggle.setOnClickListener(GetMusic.this);
        radioButton_list.setOnClickListener(GetMusic.this);
        radioButton_music.setOnClickListener(GetMusic.this);
        radioButton_files.setOnClickListener(GetMusic.this);
        radioButton_settings.setOnClickListener(GetMusic.this);
        button_view_music_toggle.setOnClickListener(GetMusic.this);
        button_view_music_last.setOnClickListener(GetMusic.this);
        button_view_music_next.setOnClickListener(GetMusic.this);
        image_view_album.setOnClickListener(GetMusic.this);
        button_play_mode_music_toggle.setOnClickListener(GetMusic.this);

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
                    succes=1;
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
                    succes=1;
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
                    succes=1;
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
                            succes=1;
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
                    succes=0;
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

    private String musicTime(int length){
        Date date = new Date(length);
//时间格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }



}








