package com.cn.graduationclient.chatroom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.cn.graduationclient.R;
import com.cn.graduationclient.login.LoginActivity;

public class Appstart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xingcmy);
		
	new Handler().postDelayed(new Runnable(){
		@Override
		public void run(){
			Intent intent = new Intent(Appstart.this, LoginActivity.class);
			startActivity(intent);			
			Appstart.this.finish();
		}
	}, 2000);
   }
}