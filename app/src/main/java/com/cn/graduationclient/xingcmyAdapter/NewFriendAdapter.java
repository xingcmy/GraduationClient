package com.cn.graduationclient.xingcmyAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.http.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class NewFriendAdapter extends BaseAdapter {

    private List<Newfriend> friendsList;
    private Context context;
    public String UID;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                    break;
                case 0x02:
                    Toast.makeText(context,"已拒绝",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public NewFriendAdapter(){}

    public NewFriendAdapter(Context context,List<Newfriend> friendsList,String UID){
        this.context=context;
        this.friendsList=friendsList;
        this.UID=UID;
    }
    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewFriend viewFriend=null;
        if (convertView==null){
            viewFriend=new ViewFriend();
            convertView=View.inflate(context, R.layout.agree_friend,null);
            viewFriend.name=convertView.findViewById(R.id.text_agree_name);
            viewFriend.msg=convertView.findViewById(R.id.text_agree_message);
            viewFriend.agree=convertView.findViewById(R.id.button_agree);
            viewFriend.refuse=convertView.findViewById(R.id.button_refuse);
            viewFriend.layout=convertView.findViewById(R.id.agree_button_layout);
            convertView.setTag(viewFriend);
        }else {
            viewFriend=(ViewFriend)convertView.getTag();
        }
        viewFriend.name.setText(friendsList.get(position).getId());
        viewFriend.msg.setText(friendsList.get(position).getMsg());
        ViewFriend finalViewFriend = viewFriend;
        viewFriend.agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil httpUtil=new HttpUtil();
                        try {
                            String agree_add=httpUtil.httpSendMsg(UID,friendsList.get(position).getId(),"agree", TypeSystem.ADD_AGREE, TypeSystem.WRITE);
                            JSONObject jsonObject=new JSONObject(agree_add);
                            if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                Message message=new Message();
                                message.what=0x01;
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finalViewFriend.layout.setVisibility(View.GONE);
            }
        });

        viewFriend.refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil httpUtil=new HttpUtil();
                        try {
                            String refuse_add=httpUtil.httpSendMsg(UID,friendsList.get(position).getId(),"refuse", TypeSystem.ADD_REFUSE, TypeSystem.WRITE);
                            JSONObject jsonObject=new JSONObject(refuse_add);
                            if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                Message message=new Message();
                                message.what=0x02;
                                handler.sendMessage(message);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                finalViewFriend.layout.setVisibility(View.GONE);
            }
        });
        return convertView;
    }

    public class ViewFriend{
        TextView name,msg;
        Button agree,refuse;
        LinearLayout layout;
    }
}
