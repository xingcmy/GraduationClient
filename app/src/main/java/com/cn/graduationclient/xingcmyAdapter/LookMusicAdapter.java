package com.cn.graduationclient.xingcmyAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.graduationclient.R;
import com.cn.graduationclient.music.FriendMusic;

import java.util.List;

public class LookMusicAdapter extends BaseAdapter {

    private Context context;
    private List<FriendMusic> list;

    public LookMusicAdapter(Context context,List<FriendMusic> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewUser viewUser=null;
        if (view == null){
            viewUser= new ViewUser();
            view=View.inflate(context, R.layout.setlookmusic,null);
            viewUser.text_look_name=view.findViewById(R.id.text_look_name_1);

            view.setTag(viewUser);
        }else {
            viewUser=(ViewUser)view.getTag();
        }
        viewUser.text_look_name.setText(list.get(position).getName());
        return view;
    }
    private static class ViewUser{
        private TextView text_look_name;
    }
}
