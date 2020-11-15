package com.cn.graduationclient.message;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.cn.graduationclient.R;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.db.NewFriendFbhelper;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;
import com.cn.graduationclient.xingcmyAdapter.NewFriendAdapter;
import com.cn.graduationclient.xingcmyAdapter.Newfriend;
import com.cn.graduationclient.xingcmyAdapter.NewfriendUitl;

import java.util.List;

public class AgreeRefuse extends Activity {

    ListView listView;
    List<Newfriend> newfriendList;
    NewFriendAdapter newFriendAdapter;

    String UID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_friend);

        Intent intent=getIntent();
        UID=intent.getStringExtra(StructureSystem.UID);

        HoldTitle holdTitle=findViewById(R.id.new_hold_friend);
        listView=findViewById(R.id.new_list_friend);
        holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newfriendList=new NewfriendUitl().getNewFriend(this,UID);
        if (newfriendList!=null){
            newFriendAdapter=new NewFriendAdapter(this,newfriendList,UID);
            listView.setAdapter(newFriendAdapter);
        }

    }
}
