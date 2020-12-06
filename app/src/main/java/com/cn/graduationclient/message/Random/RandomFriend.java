package com.cn.graduationclient.message.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cn.graduationclient.R;
import com.cn.graduationclient.adapter.ExpressionGvAdapter;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.constant.ContentFlag;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.tool.SystemConstant;
import com.cn.graduationclient.xingcmyAdapter.Chat;
import com.cn.graduationclient.xingcmyAdapter.ChatAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomFriend extends Activity {

    ImageButton back,more,btn_express;
    TextView friend_name;
    RecyclerView lv_message;
    EditText content;
    Button sendMsg;
    FrameLayout viewpager_layout;
    ViewPager tabpager;
    RelativeLayout express_spot_layout;

    String UID,ID,name;

    HttpUtil httpUtil=new HttpUtil();
    int imageIds[] = ExpressionUtil.getExpressRcIds();
    ChatAdapter chatAdapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    String setMsg=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(setMsg);
                        String error=jsonObject.getString(StructureSystem.ERROR);
                        if (error.equals(StructureSystem.SUCCESS)){
                            Toast.makeText(RandomFriend.this,"发送成功",Toast.LENGTH_SHORT).show();
                            chatArrayList.add(new Chat(content.getText().toString(), ChatAdapter.TYPE_SEND,UID, TypeSystem.MSG_TEXT,new MsgTool().setTime(0)));
                            chatAdapter = new ChatAdapter(RandomFriend.this, chatArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RandomFriend.this);
                            lv_message.setLayoutManager(linearLayoutManager);
                            lv_message.setAdapter(chatAdapter);
                            int item=chatArrayList.size();
                            lv_message.scrollToPosition(item-1);
                            content.setText("");
                            viewpager_layout.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x02:
                    String getMsg=(String)msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(getMsg);
                        String error=jsonObject.getString(StructureSystem.ERROR);
                        if (error.equals(StructureSystem.SUCCESS)){
                            String setId=jsonObject.getString(StructureSystem.ID);
                            String GMsg=jsonObject.getString(StructureSystem.MSG);
                            chatArrayList.add(new Chat(GMsg,ChatAdapter.TYPE_RECEIVE,setId,TypeSystem.MSG_TEXT,new MsgTool().setTime(0)));
                            chatAdapter = new ChatAdapter(RandomFriend.this, chatArrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RandomFriend.this);
                            lv_message.setLayoutManager(linearLayoutManager);
                            lv_message.setAdapter(chatAdapter);
                            int item=chatArrayList.size();
                            lv_message.scrollToPosition(item-1);
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
        setContentView(R.layout.random);

        Intent intent=getIntent();
        UID=intent.getStringExtra("UID");
        ID=intent.getStringExtra("id");

        getId();


        chatAdapter = new ChatAdapter(this, chatArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lv_message.setLayoutManager(linearLayoutManager);
        lv_message.setAdapter(chatAdapter);
        int item=chatArrayList.size();
        lv_message.scrollToPosition(item-1);

        friend_name.setText(ID);


        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetMsg().start();
            }
        }).start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            httpUtil.httpRandom(UID,null,null,3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finish();
            }
        });

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=content.getText().toString();
                if (msg==null||msg==""){
                    Toast.makeText(RandomFriend.this,R.string.tip_input,Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String error=httpUtil.httpRandomSetMsg(UID,ID,content.getText().toString(),TypeSystem.WRITE);
                                Message message=new Message();
                                message.obj=error;
                                message.what=0x01;
                                handler.sendMessage(message);
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

    public void getId(){
        back=findViewById(R.id.random_imbtn_message_back);
        more=findViewById(R.id.random_imbtn_message_more);
        btn_express=findViewById(R.id.random_btn_express);
        friend_name=findViewById(R.id.random_text_message_friend_name);
        lv_message=findViewById(R.id.random_lv_message);
        content=findViewById(R.id.random_content);
        sendMsg=findViewById(R.id.random_sendMsg);
        viewpager_layout=findViewById(R.id.random_viewpager_layout);
        tabpager=findViewById(R.id.random_tabpager);
        express_spot_layout=findViewById(R.id.random_express_spot_layout);
    }

    class GetMsg extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                while (true){
                    Thread.sleep(500);
                    String error=httpUtil.httpRandomSetMsg(UID,null,null,TypeSystem.READ);
                    Message message=new Message();
                    message.obj=error;
                    message.what=0x02;
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

    public void hideSoftinput(View view){
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if(manager.isActive()){
            manager.hideSoftInputFromWindow(content.getWindowToken(), 0);
        }
    }

    public void showExpressionWindow(View view) {
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

        tabpager.setAdapter(new PagerAdapter() {
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

        tabpager.setOnPageChangeListener(new RandomFriend.MyPageChangeListener());
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
                content.append(spannableString);

                //viewpager_layout.setVisibility(View.GONE);
            }
        });

        return express_view;
    }
}
