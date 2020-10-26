package com.cn.graduationclient.xingcmyAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.graduationclient.R;
import com.cn.graduationclient.my.other.User;

import java.util.List;

public class FriendAdapter extends BaseAdapter {

    private List<User> userList;
    private Context context;

    public FriendAdapter(List<User> userList,Context context){
        this.userList=userList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewUser viewUser=null;
        if (view == null){
            viewUser= new ViewUser();
            view=View.inflate(context, R.layout.friend,null);
            viewUser.text_friend_name=view.findViewById(R.id.text_friends_name_1);
            viewUser.text_friend_signature=view.findViewById(R.id.text_friends_signature);
            view.setTag(viewUser);
        }else {
            viewUser=(ViewUser)view.getTag();
        }
        viewUser.text_friend_name.setText(userList.get(position).getName());
        viewUser.text_friend_signature.setText(userList.get(position).getSignature());
        return view;
    }
    private static class ViewUser{
        private TextView text_friend_name,text_friend_signature;
    }
}
