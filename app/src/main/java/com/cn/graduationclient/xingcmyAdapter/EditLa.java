package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.graduationclient.R;

public class EditLa extends RelativeLayout {
    TextView tv_lable,tv_labledata;
    EditText tv_labletitle;
    ImageView iv_lablephoto;

    public EditLa(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        inview(context, attrs);

        init(context, attrs);
    }

    //文件初始化

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.EditLa);
        String lable=typedArray.getString(R.styleable.EditLa_el);
        String title=typedArray.getString(R.styleable.EditLa_elname);
        int photo=typedArray.getResourceId(R.styleable.EditLa_elphoto, R.drawable.ic_set);
        String data=typedArray.getString(R.styleable.EditLa_eldata);
        int number=typedArray.getInt(R.styleable.EditLa_elnumber,10);

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
        }
    }

    //视图初始化

    private void inview(Context context, AttributeSet attrs) {
        View inflate= LayoutInflater.from(context).inflate(R.layout.edit_lable,this);
        iv_lablephoto=inflate.findViewById(R.id.iv_elphoto);
        tv_labledata=inflate.findViewById(R.id.tv_eldata);
        tv_lable=inflate.findViewById(R.id.tv_el);
        tv_labletitle=inflate.findViewById(R.id.et_eltitle);
    }

    public void setIv_lablephotoOnclickListener(OnClickListener v){iv_lablephoto.setOnClickListener(v);}

    public void setTv_labletitle(String name){
        tv_labletitle.setText(name);
    }
}
