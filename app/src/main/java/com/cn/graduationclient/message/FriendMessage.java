package com.cn.graduationclient.message;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cn.graduationclient.R;
import com.cn.graduationclient.adapter.ExpressionGvAdapter;
import com.cn.graduationclient.chatroom.Appstart;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.constant.ContentFlag;
import com.cn.graduationclient.db.MessageDbHelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.login.LoginActivity;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.SystemConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FriendMessage extends AppCompatActivity {

    EditText etCtn;
    View viewpager_layout;
    RelativeLayout express_spot_layout;
    ViewPager viewPager;
    Button sendMsg;

    String UID,id,name;

    HttpUtil httpUtil=new HttpUtil();
    int imageIds[] = ExpressionUtil.getExpressRcIds();

    MessageDbHelper messageDbHelper;

    SQLiteDatabase sqLiteDatabase;
    ListView lv_message;
    Cursor cursor;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x00:
                    setList();
                    break;
                case 0x01:
                    String InMsg= (String) msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(InMsg);
                        String error=jsonObject.getString(StructureSystem.ERROR);
                        if (error.equals(StructureSystem.SUCCESS)){
                            String send_id=jsonObject.getString(StructureSystem.ID);
                            String message=jsonObject.getString(StructureSystem.MSG);
                            String time=jsonObject.getString("time");

                            sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+send_id+"','"+send_id+"','"+message+"','"+time+"')");
                            setList();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }


        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_message);

        messageDbHelper=new MessageDbHelper(this);

        sqLiteDatabase=messageDbHelper.getReadableDatabase();
        Intent intent=getIntent();
        UID=intent.getStringExtra(StructureSystem.UID);
        id=intent.getStringExtra(StructureSystem.ID);
        name=intent.getStringExtra(StructureSystem.NAME);

        sendMsg=findViewById(R.id.sendMsg);
        lv_message=findViewById(R.id.lv_message);

        ImageButton back=findViewById(R.id.imbtn_message_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new InMsg().start();
            }
        }).start();

        etCtn = (EditText) findViewById(R.id.content);
        viewpager_layout = findViewById(R.id.viewpager_layout);
        express_spot_layout = (RelativeLayout) findViewById(R.id.express_spot_layout);
        viewPager = (ViewPager) findViewById(R.id.tabpager);

        TextView set_friend_name=findViewById(R.id.text_message_friend_name);
        set_friend_name.setText(name);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ctn = etCtn.getText().toString();
                if(ctn.equals("") || ctn == null){
                    Toast.makeText(FriendMessage.this, R.string.tip_input, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String cs=httpUtil.httpSendMsg(UID,id,ctn, StructureSystem.MSG_TEXT, TypeSystem.WRITE);
                                Log.d("cs",cs);
                                JSONObject jsonObject=new JSONObject(cs);
                                if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                    sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+id+"','"+UID+"','"+ctn+"','"+setTime()+"')");

                                    Message message=new Message();
                                    message.what=0x00;
                                    handler.sendMessage(message);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //setList();
                }
            }
        });
    }

    public void hideSoftinput(View view){
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if(manager.isActive()){
            manager.hideSoftInputFromWindow(etCtn.getWindowToken(), 0);
        }
    }

    public void showExpressionWindow(View view){

        this.hideSoftinput(view);
        if (viewpager_layout.getVisibility()==View.VISIBLE){
            viewpager_layout.setVisibility(View.GONE);
        }else {
            viewpager_layout.setVisibility(View.VISIBLE);
        }

        Display currDisplay = getWindowManager().getDefaultDisplay();
        int displayWidth = currDisplay.getWidth();
        int displayHeight=currDisplay.getHeight();

        Bitmap express = BitmapFactory.decodeResource(getResources(), R.drawable.f000);
        int headWidth = express.getWidth();
        int headHeight = express.getHeight();
        Log.d(ContentFlag.TAG, displayWidth+":" + headWidth);
        final int colmns = Math.min(displayWidth / headWidth, 7);
        final int rows = Math.min(displayHeight / headHeight, SystemConstant.express_counts/colmns);
        Log.d("cs",rows+"");
        final int pageItemCount = colmns * rows;

        int totalPage = SystemConstant.express_counts % pageItemCount == 0 ?
                SystemConstant.express_counts / pageItemCount : SystemConstant.express_counts / pageItemCount + 1;
        final List<View> listView = new ArrayList<View>();
        for (int index = 0; index < totalPage; index++) {
            listView.add(getViewPagerItem(index, colmns, pageItemCount));
        }
        express_spot_layout.removeAllViews();
        for (int i = 0; i < totalPage; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i+1);
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.d2);
            }else{
                imageView.setBackgroundResource(R.drawable.d1);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.bottomMargin = 20;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            if(i!= 0){
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, i);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);
            }
            express_spot_layout.addView(imageView, layoutParams);
        }
        Log.d(ContentFlag.TAG, express_spot_layout.getChildCount() + "");

        viewPager.setAdapter(new PagerAdapter() {
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            public int getCount() {
                return listView.size();
            }

            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(listView.get(position));
            }

            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(listView.get(position));
                return listView.get(position);
            }
        });

        viewPager.setOnPageChangeListener(new FriendMessage.MyPageChangeListener());
    }

    private final class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int curIndex = 0;
        public void onPageSelected(int index) {
            express_spot_layout.getChildAt(curIndex).setBackgroundResource(R.drawable.d1);
            express_spot_layout.getChildAt(index).setBackgroundResource(R.drawable.d2);
            curIndex = index;
        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private View getViewPagerItem(final int index, int colums, final int pageItemCount) {
        LayoutInflater inflater = this.getLayoutInflater();
        View express_view = inflater.inflate(R.layout.express_gv, null);
        GridView gridView = (GridView) express_view.findViewById(R.id.gv_express);
        gridView.setNumColumns(colums);
        gridView.setAdapter(new ExpressionGvAdapter(index, pageItemCount, imageIds, inflater));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positon,
                                    long id) {
                Bitmap bitmap = null;
                int start = index * pageItemCount;
                positon = positon + start;
                bitmap = BitmapFactory.decodeResource(getResources(), imageIds[positon]);
                ImageSpan imageSpan = new ImageSpan(bitmap);
                String str = "";
                if(positon < 10){
                    str = "[f00"+positon+"]";
                }else if(positon < 100){
                    str = "[f0"+positon+"]";
                }else{
                    str = "[f"+positon+"]";;
                }
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(imageSpan, 0, str.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                etCtn.append(spannableString);
                //viewpager_layout.setVisibility(View.GONE);
            }
        });

        return express_view;
    }

    class InMsg extends Thread{
        @Override
        public void run() {
            super.run();
                while (true){
                    Object msg= null;
                    try {
                        Thread.sleep(500);
                        msg = httpUtil.httpInMsg(UID, TypeSystem.READ);
                        if (msg!=null){
                            Message message=new Message();
                            message.obj=msg;
                            message.what=0x01;
                            handler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


        }
    }

    public String setTime(){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        Date date = new Date(System.currentTimeMillis());
        String time=formatter.format(date);
        return time;
    }

    public void setList(){

        cursor=sqLiteDatabase.rawQuery("select * from message where uid='"+UID+"' and id='"+id+"'",null);
        ArrayList<HashMap<String, String>> photolist;
        photolist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> al;

        int length=cursor.getCount();
        String[] id=new String[length];
        String[] name=new String[length];
        String[] message=new String[length];
        String[] time=new String[length];
        if (length==0){

        }else {
            cursor.moveToNext();

            for (int j=0;j<length;j++){
                id[j]=cursor.getString(2);
                name[j]=cursor.getString(2);
                message[j]=cursor.getString(3);
                time[j]=cursor.getString(4);
                cursor.moveToNext();
            }

            for (int i=0;i<length;i++){

                al = new HashMap<String, String>();
                if (id[i].equals(UID)){
                    al.put("id","");
                    al.put("name",id[i]);
                    al.put("message",message[i]);
                    al.put("time",time[i]);
                }else {
                    al.put("id",id[i]);
                    al.put("name","");
                    al.put("message",message[i]);
                    al.put("time",time[i]);
                }

                photolist.add(al);
            }
        }


        /*list=new SendMessageUtil().getsendmessage(this,id);
        Log.i(TAG, "" + list);*/
        SimpleAdapter simpleAdapter=new SimpleAdapter(FriendMessage.this, photolist, R.layout.message,new String[]{"id","name","message","time"},new int[]{R.id.text_view_message_id,R.id.text_view_message_name, R.id.text_view_message_msg, R.id.text_view_message_time});
        //adapter=new SendMessageAdapter(list,this);
        lv_message.setAdapter(simpleAdapter);

    }

}
