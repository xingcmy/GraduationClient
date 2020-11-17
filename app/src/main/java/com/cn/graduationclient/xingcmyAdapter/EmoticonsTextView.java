package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cn.graduationclient.tool.ExpressionUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmoticonsTextView extends androidx.appcompat.widget.AppCompatTextView {

    int imageIds[] = ExpressionUtil.getExpressRcIds();

    public EmoticonsTextView(Context context) {
        super(context);
    }


    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text.toString()), type);
        } else {
            super.setText(text, type);
        }
    }

    public CharSequence replace(String text) {
        try {
            SpannableString spannableString = new SpannableString(text);
            //这里的正则表达式匹配格式是 [发呆]   []里面可以是任意字符
            Pattern pattern = Pattern.compile("[01][0123456789][0123456789]", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            int post=Integer.parseInt(text);
            while (matcher.find()){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),imageIds[post]);
                ImageSpan imageSpan = new ImageSpan(getContext(), bitmap);
                spannableString.setSpan(imageSpan,matcher.start(),matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            return spannableString;
        } catch (Exception e) {
            return text;
        }
    }
}
