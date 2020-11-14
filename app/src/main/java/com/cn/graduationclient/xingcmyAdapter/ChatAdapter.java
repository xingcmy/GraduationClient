package com.cn.graduationclient.xingcmyAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.MsgTool;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_SEND=0;
    public static final int TYPE_RECEIVE=1;

    private Context context;
    int imageIds[] = ExpressionUtil.getExpressRcIds();
    private ArrayList<Chat> chatArrayList=new ArrayList<>();

    public ChatAdapter(Context context, ArrayList<Chat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        if(chatArrayList.get(position).getType()==TYPE_SEND){
            return TYPE_SEND;
        }else if(chatArrayList.get(position).getType()==TYPE_RECEIVE){
            return TYPE_RECEIVE;
        }else{
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==TYPE_SEND){
            view= LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
            return new SendViewHolder(view);
        }else{
            view= LayoutInflater.from(context).inflate(R.layout.item_receive,parent,false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SendViewHolder){
            SendViewHolder viewHolder= (SendViewHolder) holder;

//            byte[] bytes=chatArrayList.get(position).getMessage().getBytes();
//            String out="";
//            for (int i=0;i<bytes.length;i++){
//                if (bytes[i]==91){
//                    int j=i+1;
//                    if (bytes[j]==102&&bytes[j+4]==93){
//                        for (int o=i+2;o<i+5;o++){
//                            char c=(char)bytes[o];
//                            out+=c;
//                        }
//                        int num=Integer.parseInt(out);
//                        Bitmap bitmap =new FriendMessage().smileB(num);
//                        ImageSpan imageSpan = new ImageSpan(bitmap);
//                        String str = "";
//                        if(num < 10){
//                            str = "[f00"+num+"]";
//                        }else if(num < 100){
//                            str = "[f0"+num+"]";
//                        }else{
//                            str = "[f"+num+"]";;
//                        }
//                        SpannableString spannableString = new SpannableString(str);
//                        spannableString.setSpan(imageSpan, 0, str.length(),
//                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                        viewHolder.textView_send.append(spannableString);
//                    }
//                }
//            }
            if (chatArrayList.get(position).getMsg_type()== TypeSystem.MSG_TEXT){
                viewHolder.textView_send.setVisibility(View.VISIBLE);
                viewHolder.right_img.setVisibility(View.GONE);
                viewHolder.textView_send.setText(chatArrayList.get(position).getMessage());
                String end=chatArrayList.get(position).getTime();
                viewHolder.my.setText(chatArrayList.get(position).getName());
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_IMAGE){
                viewHolder.right_img.setVisibility(View.VISIBLE);
                viewHolder.textView_send.setVisibility(View.GONE);
                viewHolder.my.setText(chatArrayList.get(position).getName());
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.right_img,chatArrayList.get(position).getMessage());
                viewHolder.right_img.setImageBitmap(bitmap);
            }

            if(position!=0){

            }
        }else if(holder instanceof ReceiveViewHolder){
            ReceiveViewHolder viewHolder= (ReceiveViewHolder) holder;
//            byte[] bytes=chatArrayList.get(position).getMessage().getBytes();
//            String out="";
//            for (int i=0;i<bytes.length;i++){
//                if (bytes[i]==91){
//                    int j=i+1;
//                    if (bytes[j]==102&&bytes[j+4]==93){
//                        for (int o=i+2;o<i+5;o++){
//                            char c=(char)bytes[o];
//                            out+=c;
//                        }
//                        int num=Integer.parseInt(out);
//                        Bitmap bitmap =new FriendMessage().smileB(num);
//                        ImageSpan imageSpan = new ImageSpan(bitmap);
//                        String str = "";
//                        if(num < 10){
//                            str = "[f00"+num+"]";
//                        }else if(num < 100){
//                            str = "[f0"+num+"]";
//                        }else{
//                            str = "[f"+num+"]";;
//                        }
//                        SpannableString spannableString = new SpannableString(str);
//                        spannableString.setSpan(imageSpan, 0, str.length(),
//                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                        viewHolder.textView_receive.append(spannableString);
//                    }
//                }
//            }
            if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_TEXT){
                viewHolder.left_img.setVisibility(View.GONE);
                viewHolder.textView_receive.setVisibility(View.VISIBLE);
                viewHolder.textView_receive.setText(chatArrayList.get(position).getMessage());
                String end=chatArrayList.get(position).getTime();
                viewHolder.your.setText(chatArrayList.get(position).getName());
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_IMAGE){
                viewHolder.textView_receive.setVisibility(View.GONE);
                viewHolder.left_img.setVisibility(View.VISIBLE);
                viewHolder.your.setText(chatArrayList.get(position).getName());
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.left_img,chatArrayList.get(position).getMessage());
                viewHolder.left_img.setImageBitmap(bitmap);
            }

            if(position!=0){

            }
        }

    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    private class SendViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_send,my;
        private ImageView right_img;

        public SendViewHolder(View itemView) {
            super(itemView);
            textView_send = itemView.findViewById(R.id.right_msg);
            my=itemView.findViewById(R.id.text_id_my);
            right_img=itemView.findViewById(R.id.right_msg_img);
        }
    }

    private class ReceiveViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_receive,your;
        private ImageView left_img;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            textView_receive = itemView.findViewById(R.id.left_msg);
            your=itemView.findViewById(R.id.text_is_your);
            left_img=itemView.findViewById(R.id.left_msg_img);
        }
    }
}
