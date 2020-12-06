package com.cn.graduationclient.xingcmyAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.cn.graduationclient.R;
import com.cn.graduationclient.audio.MainContract;
import com.cn.graduationclient.audio.VoiceImageView;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.db.HeadDbHelper;
import com.cn.graduationclient.message.FriendMessage;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.MsgTool;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int TYPE_SEND=0;
    public static final int TYPE_RECEIVE=1;

    private boolean mNextLoadEnable;
    private boolean mLoadMoreEnable;
    private boolean mLoading;
    private int mLastPosition;
    private int mCurrentPlayAnimPosition = -1;//当前播放动画的位置

    protected List<File> mData;
    private RequestLoadMoreListener mRequestLoadMoreListener;
    private Context context;
    int imageIds[] = ExpressionUtil.getExpressRcIds();
    private ArrayList<Chat> chatArrayList=new ArrayList<>();
    private MainContract.Presenter mPresenter;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    break;
            }
        }
    };

    public ChatAdapter(Context context, ArrayList<Chat> chatArrayList, MainContract.Presenter mPresenter) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        this.mPresenter=mPresenter;
    }

    public ChatAdapter(Context context,ArrayList<Chat> chatArrayList){
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
            view= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            return new SendViewHolder(view);
        }else{
            view= LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
            return new ReceiveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        HeadDbHelper headDbHelper=new HeadDbHelper(context);
        SQLiteDatabase sqLiteDatabase=headDbHelper.getReadableDatabase();
        if(holder instanceof SendViewHolder){
            SendViewHolder viewHolder= (SendViewHolder) holder;


            Cursor right_cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+chatArrayList.get(position).getName()+"'",null);
            if (right_cursor.getCount()>=0){
                while (right_cursor.moveToNext()){
                    Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.right_head,right_cursor.getString(1));
                    viewHolder.right_head.setImageBitmap(bitmap);
                }

            }




            viewHolder.right_time.setText(chatArrayList.get(position).getTime());
            if (chatArrayList.get(position).getMsg_type()== TypeSystem.MSG_TEXT){
                viewHolder.textView_send.setVisibility(View.VISIBLE);
                viewHolder.right_img.setVisibility(View.GONE);
                viewHolder.right_voice.setVisibility(View.GONE);

//
//                int lg=chatArrayList.get(position).getMessage().length();
//                String[] msg=new String[lg];
//                String[] split=chatArrayList.get(position).getMessage().split("");
//                int i=0;
//                for ( String str:split){
//                    msg[i]=str;
//                    i++;
//                    if (i==lg){
//                        break;
//                    }
//                }
//
//                int right=0;
//                int count=0;
//                for (int j=0;j<lg;j++){
//                    if (msg[j].equals("[")){
//                        int smile_left=j;
//                        int f=j+1;
//                        int smile_right=j+5;
//                        if (smile_right<lg){
//                            if (msg[smile_left].equals("[")&&msg[f].equals("f")&&msg[smile_right].equals("]")){
//                                String smile=msg[j+2]+msg[j+3]+msg[j+4];
//                                for (int left=right;left<j;left++){
//                                    viewHolder.textView_send.append(msg[j]);
//                                }
//                                int pos=Integer.parseInt(smile);
//                                Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),imageIds[pos]);
//                                ImageSpan imageSpan = new ImageSpan(bitmap);
//                                String str="[f"+smile+"]";
//                                SpannableString spannableString = new SpannableString(str);
//                                spannableString.setSpan(imageSpan, 0, str.length(),
//                                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                                viewHolder.textView_send.append(spannableString);
//                                right=j+6;
//                                count++;
//
//                            }
//                        }
//                    }
//                }
//                if (count!=0){
//                    if (right<lg){
//                        for (;right<lg;right++){
//                            viewHolder.textView_send.append(msg[right]);
//                        }
//                    }
//                }else {
//                    viewHolder.textView_send.setText(chatArrayList.get(position).getMessage());
//                }


                SmileyParser.init(context);
                SmileyParser smileyParser=SmileyParser.getInstance();
                CharSequence charSequence=smileyParser.strToSmiley(chatArrayList.get(position).getMessage());
                viewHolder.textView_send.setText(charSequence);
               // viewHolder.textView_send.setText(chatArrayList.get(position).getMessage());
                String end=chatArrayList.get(position).getTime();
                viewHolder.my.setText(chatArrayList.get(position).getName());
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_IMAGE){
                viewHolder.right_img.setVisibility(View.VISIBLE);
                viewHolder.textView_send.setVisibility(View.GONE);
                viewHolder.right_voice.setVisibility(View.GONE);
                viewHolder.my.setText(chatArrayList.get(position).getName());
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.right_img,chatArrayList.get(position).getMessage());
                viewHolder.right_img.setImageBitmap(bitmap);
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_VOICE){
                viewHolder.right_voice.setVisibility(View.VISIBLE);
                viewHolder.textView_send.setVisibility(View.GONE);
                viewHolder.right_img.setVisibility(View.GONE);
                viewHolder.right_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("voice",chatArrayList.get(position).getMessage());
                        int lg=Integer.parseInt(chatArrayList.get(position).getMessage());
                        mPresenter.startPlayRecord(lg);
                    }
                });
            }

            if(position!=0){

            }
        }else if(holder instanceof ReceiveViewHolder){
            ReceiveViewHolder viewHolder= (ReceiveViewHolder) holder;

            Cursor left_cursor=sqLiteDatabase.rawQuery("select * from head where uid='"+chatArrayList.get(position).getName()+"'",null);
            if (left_cursor.getCount()>=0){
                while (left_cursor.moveToNext()){
                    Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.left_head,left_cursor.getString(1));
                    viewHolder.left_head.setImageBitmap(bitmap);
                }
            }
            viewHolder.left_time.setText(chatArrayList.get(position).getTime());
            if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_TEXT){
                viewHolder.left_img.setVisibility(View.GONE);
                viewHolder.left_voice.setVisibility(View.GONE);
                viewHolder.textView_receive.setVisibility(View.VISIBLE);
//
//                int lg=chatArrayList.get(position).getMessage().length();
//                String[] msg=new String[lg];
//                String[] split=chatArrayList.get(position).getMessage().split("");
//                int i=0;
//                for ( String str:split){
//                    msg[i]=str;
//                    i++;
//                    if (i==lg){
//                        break;
//                    }
//                }
//
//                int right=0;
//                int count=0;
//                for (int j=0;j<lg;j++){
//                    if (msg[j].equals("[")){
//                        int smile_left=j;
//                        int f=j+1;
//                        int smile_right=j+5;
//                        if (smile_right<lg){
//                            if (msg[smile_left].equals("[")&&msg[f].equals("f")&&msg[smile_right].equals("]")){
//                                String smile=msg[j+2]+msg[j+3]+msg[j+4];
//                                for (int left=right;left<j;left++){
//                                    viewHolder.textView_receive.append(msg[j]);
//                                }
//                                int pos=Integer.parseInt(smile);
//                                CharSequence charSequence=new EmoticonsTextView(context).replace(smile);
//                                viewHolder.textView_receive.append(charSequence);
//                                right=j+6;
//                                count++;
//
//                            }
//                        }
//                    }
//                }
//                if (count!=0){
//                    if (right<lg){
//                        for (;right<lg;right++){
//                            viewHolder.textView_receive.append(msg[right]);
//                        }
//                    }
//                }else {
//                    viewHolder.textView_receive.setText(chatArrayList.get(position).getMessage());
//                }

                SmileyParser.init(context);
                SmileyParser smileyParser=SmileyParser.getInstance();
                CharSequence charSequence=smileyParser.strToSmiley(chatArrayList.get(position).getMessage());
                viewHolder.textView_receive.setText(charSequence);

                String end=chatArrayList.get(position).getTime();
                viewHolder.your.setText(chatArrayList.get(position).getName());
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_IMAGE){
                viewHolder.textView_receive.setVisibility(View.GONE);
                viewHolder.left_voice.setVisibility(View.GONE);
                viewHolder.left_img.setVisibility(View.VISIBLE);
                viewHolder.your.setText(chatArrayList.get(position).getName());
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(viewHolder.left_img,chatArrayList.get(position).getMessage());
                viewHolder.left_img.setImageBitmap(bitmap);
            }else if (chatArrayList.get(position).getMsg_type()==TypeSystem.MSG_VOICE){
                viewHolder.left_voice.setVisibility(View.VISIBLE);
                viewHolder.textView_receive.setVisibility(View.GONE);
                viewHolder.left_img.setVisibility(View.GONE);
                viewHolder.left_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("voice",chatArrayList.get(position).getMessage());
                        int lg=Integer.parseInt(chatArrayList.get(position).getMessage());
                        mPresenter.startPlayRecord(lg);
                    }
                });
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
        private TextView my,right_time,textView_send;
        private ImageView right_img,right_head;
        private VoiceImageView right_voice;

        public SendViewHolder(View itemView) {
            super(itemView);
            textView_send = itemView.findViewById(R.id.right_msg);
            my=itemView.findViewById(R.id.is_my);
            right_img=itemView.findViewById(R.id.right_msg_img);
            right_time=itemView.findViewById(R.id.tv_right_time);
            right_head=itemView.findViewById(R.id.is_my_head);
            right_voice=itemView.findViewById(R.id.right_voice);
        }
    }

    private class ReceiveViewHolder extends RecyclerView.ViewHolder {

        private TextView your,left_time,textView_receive;
        private ImageView left_img,left_head;
        private VoiceImageView left_voice;

        public ReceiveViewHolder(View itemView) {
            super(itemView);
            textView_receive = itemView.findViewById(R.id.left_msg);
            your=itemView.findViewById(R.id.is_your);
            left_img=itemView.findViewById(R.id.left_msg_img);
            left_time=itemView.findViewById(R.id.tv_left_time);
            left_head=itemView.findViewById(R.id.is_your_head);
            left_voice=itemView.findViewById(R.id.left_voice);
        }
    }


    public interface RequestLoadMoreListener {
        void onLoadMoreRequested();
    }

    public void startPlayAnim(int position) {
        mCurrentPlayAnimPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 停止播放动画
     */
    public void stopPlayAnim() {
        mCurrentPlayAnimPosition = -1;
        notifyDataSetChanged();
    }


}
