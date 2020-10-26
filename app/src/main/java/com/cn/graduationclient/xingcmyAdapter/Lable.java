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

public class Lable extends RelativeLayout {
    TextView tv_lable,tv_labledata,tv_labletitle;
    ImageView iv_lablephoto;

    public Lable(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        inview(context, attrs);

        init(context, attrs);
    }

    //文件初始化

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.Lable);
        String lable=typedArray.getString(R.styleable.Lable_lable);
        String title=typedArray.getString(R.styleable.Lable_name);
        int photo=typedArray.getResourceId(R.styleable.Lable_photo, R.drawable.ic_set);
        String data=typedArray.getString(R.styleable.Lable_data);
        int number=typedArray.getInt(R.styleable.Lable_number,10);

        //赋值
        tv_lable.setText(lable);
        tv_labletitle.setText(title);
        tv_labledata.setText(data);
        iv_lablephoto.setImageResource(photo);

        //传入值
        if (number==10){
            iv_lablephoto.setVisibility(View.GONE);
            tv_labledata.setVisibility(View.VISIBLE);
        }else if (number==11){
            tv_labledata.setVisibility(View.GONE);
            iv_lablephoto.setVisibility(View.VISIBLE);
        }else if (number==12){
            tv_labledata.setVisibility(View.GONE);
            iv_lablephoto.setVisibility(View.VISIBLE);
            tv_labletitle.setVisibility(View.GONE);
        }
    }

    //视图初始化

    private void inview(Context context, AttributeSet attrs) {
        View inflate= LayoutInflater.from(context).inflate(R.layout.lable,this);
        iv_lablephoto=inflate.findViewById(R.id.iv_lablephoto);
        tv_labledata=inflate.findViewById(R.id.tv_labledata);
        tv_lable=inflate.findViewById(R.id.tv_lable);
        tv_labletitle=inflate.findViewById(R.id.tv_labletitle);
    }

    public void setIv_lablephotoOnclickListener(OnClickListener v){iv_lablephoto.setOnClickListener(v);}

    public void setTv_labletitle(String name){
        tv_labletitle.setText(name);
    }
}
