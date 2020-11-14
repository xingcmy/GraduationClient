package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cn.graduationclient.R;


public class HoldTitle extends RelativeLayout {
    ImageView ivback,ivmore;
    TextView tvtitle,tvmore;
    public HoldTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        //视图初始化
        inview(context,attrs);
        //文件初始化
        init(context,attrs);
    }

    //文件初始化

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.HoldTitle);
        String title=typedArray.getString(R.styleable.HoldTitle_title);
        int left=typedArray.getResourceId(R.styleable.HoldTitle_left, R.drawable.ic_back);
        int right=typedArray.getResourceId(R.styleable.HoldTitle_right, R.drawable.ic_more);
        String text=typedArray.getString(R.styleable.HoldTitle_text);
        int type=typedArray.getInt(R.styleable.HoldTitle_type,10);

        //赋值
        tvtitle.setText(title);
        tvmore.setText(text);
        ivback.setImageResource(left);
        ivmore.setImageResource(right);

        //传入值
        if (type==10){
            ivmore.setVisibility(View.GONE);
            tvmore.setVisibility(View.VISIBLE);
        }else if (type==11){
            tvmore.setVisibility(View.GONE);
            ivmore.setVisibility(View.VISIBLE);
        }
    }

    //视图初始化

    private void inview(Context context, AttributeSet attrs) {
        View inflate= LayoutInflater.from(context).inflate(R.layout.holdbar,this);
        ivback=inflate.findViewById(R.id.iv_back);
        ivmore=inflate.findViewById(R.id.iv_more);
        tvmore=inflate.findViewById(R.id.tv_login);
        tvtitle=inflate.findViewById(R.id.tv_title);
    }

    //点击事件

    public void setIvbackOnClickListener(OnClickListener v) {
        ivback.setOnClickListener(v);
    }

    public void setIvmoreOnClickListener(OnClickListener v) {
        ivmore.setOnClickListener(v);
    }

    public void setTvmoreOnClickListener(OnClickListener v) {
        tvmore.setOnClickListener(v);
    }

    public void setTvtitle(String title){
        tvtitle.setText(title);
    }
    public String getTvtitle(){
        return tvtitle.getText().toString();
    }
}
