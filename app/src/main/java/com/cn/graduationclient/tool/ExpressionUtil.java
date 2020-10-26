package com.cn.graduationclient.tool;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.cn.graduationclient.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionUtil {
	public static String pattern = "\\[f\\d{3}\\]";
	public static void dealExpression(Context context, SpannableString spannableString, Pattern patten)
				throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			//[f000]
			String filedname = key.substring(1, key.length()-1);
			Field field = R.drawable.class.getDeclaredField(filedname);
			int resId = Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
				ImageSpan imageSpan = new ImageSpan(bitmap);
				int end = matcher.start() + key.length(); 
				spannableString.setSpan(imageSpan, matcher.start(), end, 
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
		}
	}

	
	public static SpannableString getExpressionString(Context context, String str) {
		SpannableString spannableString = new SpannableString(str);
		Pattern sinaPatten = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}
	
	
	public static int[] getExpressRcIds(){
		int imageIds[] = new int[SystemConstant.express_counts];
		int resourceId = 0;
		String fieldName;
		for (int i = 0; i < SystemConstant.express_counts; i++) {
			try {
				if(i < 10){
					fieldName = "f00"+i;
				}else if(i < 100){
					fieldName = "f0"+i;
				}else{
					fieldName = "f"+i;
				}
				Field field = R.drawable.class.getDeclaredField(fieldName);
				resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return imageIds;
	}
}
