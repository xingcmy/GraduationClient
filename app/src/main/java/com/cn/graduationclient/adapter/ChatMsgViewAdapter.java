package com.cn.graduationclient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.MessageQueue.IdleHandler;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.graduationclient.bean.Message;
import com.cn.graduationclient.bean.User;
import com.cn.graduationclient.R;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.SystemConstant;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatMsgViewAdapter {//extends BaseAdapter {
//	private Context context;
//    private LayoutInflater mInflater;
//    private List<Message> msgList;
//    private User curUser;
//    private Timer timer = new Timer();
//    public ChatMsgViewAdapter(Context context, User curUser, List<Message> msgList) {
//        this.context = context;
//        this.msgList = msgList;
//        this.curUser = curUser;
//        mInflater = LayoutInflater.from(this.context);
//    }
//
//    public int getCount() {
//        return msgList.size();
//    }
//
//    public Object getItem(int position) {
//        return msgList.get(position);
//    }
//
//    public long getItemId(int position) {
//        return position;
//    }
//
//	public int getItemViewType(int position) {
//
//		Message msg = msgList.get(position);
//		if (msg.getId() == curUser.getId()) {
//				return 0;
//		} else {
//				return 1;
//		}
//	}
//
//	public int getViewTypeCount() {
//
//		return 2;
//	}
//
//    public View getView(final int position, View convertView, ViewGroup parent) {
//    	final Message msg = msgList.get(position);
//    	ViewHolder viewHolder = null;
//	    if (convertView == null)
//	    {
//	    	  if (msg.getId() == curUser.getId())
//			  {
//				  convertView = mInflater.inflate(R.layout.chat_item_right, null);
//			  }else{
//				  convertView = mInflater.inflate(R.layout.chat_item_left, null);
//			  }
//	    	  viewHolder = new ViewHolder();
//			  viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
//			  viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
//			  viewHolder.msgBgView = (View) convertView.findViewById(R.id.chat_msg_bg);
//			  viewHolder.ivUserImage = (ImageView) convertView.findViewById(R.id.iv_userhead);
//			  convertView.setTag(viewHolder);
//	    }else{
//	        viewHolder = (ViewHolder) convertView.getTag();
//	    }
//	    viewHolder.tvSendTime.setText(msg.getSend_date());
//	    viewHolder.tvUserName.setText(msg.getSend_person());
//	    SpannableString spannableString = ExpressionUtil.getExpressionString(context, msg.getSend_ctn());
//	    TextView tvContent = (TextView) viewHolder.msgBgView.findViewById(R.id.tv_chatcontent);
//	    final ImageView ivPlay = (ImageView) viewHolder.msgBgView.findViewById(R.id.iv_play_voice);
//	    tvContent.setText(spannableString);
//	    if(msg.isIfyuyin()){
//	    	ivPlay.setVisibility(View.VISIBLE);
//	    }else{
//	    	ivPlay.setVisibility(View.GONE);
//	    }
//
//	    viewHolder.msgBgView.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				final String path = msg.getRecord_path();
//				if(null!= path && !"".equals(path)){
//					try {
//
//						final long recordTime = msg.getRecordTime();
//
//						final int type = getItemViewType(position);
//						if(type == 0){
//							ivPlay.setBackgroundResource(R.drawable.chatto_voice_play_frame);
//						}else{
//							ivPlay.setBackgroundResource(R.drawable.chatfrom_voice_play_frame);
//						}
//						final AnimationDrawable animation = (AnimationDrawable) ivPlay.getBackground();
//						context.getMainLooper().myQueue().addIdleHandler(new IdleHandler() {
//							public boolean queueIdle() {
//								animation.start();
//								timer.schedule(new RecordTimeTask(animation, ivPlay, type), recordTime);
//								return false;
//							}
//						});
//
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								try {
//									BufferedInputStream dis = new BufferedInputStream(
//											new FileInputStream(path));
//									int bufferSize = AudioRecord
//											.getMinBufferSize(
//													SystemConstant.SAMPLE_RATE_IN_HZ,
//													SystemConstant.CHANNEL_CONFIG,
//													SystemConstant.AUDIO_FORMAT);
//									byte[] buffer = new byte[bufferSize];
//
//									AudioTrack track = new AudioTrack(
//											AudioManager.STREAM_MUSIC,
//											SystemConstant.SAMPLE_RATE_IN_HZ,
//											SystemConstant.CHANNEL_CONFIG,
//											SystemConstant.AUDIO_FORMAT,
//											bufferSize, AudioTrack.MODE_STREAM);
//
//									track.play();
//
//									int length = 0;
//									while ((length = dis.read(buffer)) != -1) {
//
//										track.write(buffer, 0, length);
//									}
//									track.stop();
//									track.release();
//									dis.close();
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//							}
//						}).start();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		});
//	    if(null!= msg.getBitmap()){
//	    	viewHolder.ivUserImage.setImageBitmap(msg.getBitmap());
//	    }
//	    return convertView;
//    }
//
//    private final class RecordTimeTask extends TimerTask {
//		private AnimationDrawable animation;
//		private ImageView ivPlay;
//		private int type;
//		public RecordTimeTask(AnimationDrawable animation, ImageView ivPlay, int type) {
//			this.animation = animation;
//			this.ivPlay = ivPlay;
//			this.type = type;
//		}
//		public void run() {
//			animation.stop();
//			android.os.Message msg = handle.obtainMessage();
//			msg.obj = ivPlay;
//			msg.arg1 = this.type;
//			handle.sendMessage(msg);
//		}
//    }
//
//    @SuppressLint("HandlerLeak")
//	private Handler handle = new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			super.handleMessage(msg);
//			ImageView ivPlay = (ImageView) msg.obj;
//			int type = msg.arg1;
//			if(type == 0){
//				ivPlay.setBackgroundResource(R.drawable.chatto_voice_playing);
//			}else{
//				ivPlay.setBackgroundResource(R.drawable.chatfrom_voice_playing);
//			}
//		}
//    };
//
//    private class ViewHolder {
//        public TextView tvSendTime;
//        public TextView tvUserName;
//        public View msgBgView;
//        public ImageView ivUserImage;
//    }
}
