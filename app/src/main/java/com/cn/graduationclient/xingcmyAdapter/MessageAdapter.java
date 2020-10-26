package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.graduationclient.R;

import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private List<Message> messageList;
    private Context context;
    //private MessageAdapter adapter;
   // private ImageView image_message_more;

    public MessageAdapter(List<Message> messages, Context context) {
        this.messageList = messages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Object getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewMessage viewMessage=null;
        if (view==null){
            viewMessage =new viewMessage();
            view = View.inflate(context, R.layout.item_message_layout,null);
            viewMessage.text_message_name=view.findViewById(R.id.text_message_name);
            viewMessage.text_message_nowmessage=view.findViewById(R.id.text_message_nowmessage);
            viewMessage.text_message_time=view.findViewById(R.id.text_message_time);
            viewMessage.image_message_more=view.findViewById(R.id.image_message_more);
        }else {
            viewMessage=(viewMessage) view.getTag();
        }
        viewMessage.text_message_name.setText(messageList.get(i).getName());
        viewMessage.text_message_nowmessage.setText(messageList.get(i).getNowmessage());
        viewMessage.text_message_time.setText(messageList.get(i).getTime());
        viewMessage.image_message_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    private class viewMessage{
        private  TextView text_message_name,text_message_nowmessage,text_message_time;
        private ImageView image_message_more;
    }


}
