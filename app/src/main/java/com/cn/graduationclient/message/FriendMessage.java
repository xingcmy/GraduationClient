package com.cn.graduationclient.message;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cn.graduationclient.R;
import com.cn.graduationclient.adapter.ExpressionGvAdapter;
import com.cn.graduationclient.audio.MainContract;
import com.cn.graduationclient.audio.MainPresenter;
import com.cn.graduationclient.audio.RecordAudioButton;
import com.cn.graduationclient.audio.RecordVoicePopWindow;
import com.cn.graduationclient.cmd.StructureSystem;
import com.cn.graduationclient.cmd.TypeSystem;
import com.cn.graduationclient.constant.ContentFlag;
import com.cn.graduationclient.db.FriendDbHelper;
import com.cn.graduationclient.db.MessageDbHelper;
import com.cn.graduationclient.http.HttpUtil;
import com.cn.graduationclient.tool.ExpressionUtil;
import com.cn.graduationclient.tool.MsgTool;
import com.cn.graduationclient.tool.SystemConstant;
import com.cn.graduationclient.xingcmyAdapter.Chat;
import com.cn.graduationclient.xingcmyAdapter.ChatAdapter;
import com.cn.graduationclient.xingcmyAdapter.HoldTitle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.namee.permissiongen.PermissionGen;

public class FriendMessage<T extends MainContract.View> extends AppCompatActivity implements MainContract.View {

    private static final int REQUEST_CODE = 1;
    EditText etCtn;
    View viewpager_layout;
    RelativeLayout express_spot_layout;
    ViewPager viewPager;
    Button sendMsg;
    RecordAudioButton recordAudioButton;
    LinearLayout sendLayout;
    ImageButton speak;
    LinearLayout rootMain;

    MainContract.Presenter mPresenter;
    RecordVoicePopWindow recordVoicePopWindow;

    String UID,id,name;

    HttpUtil httpUtil=new HttpUtil();
    int imageIds[] = ExpressionUtil.getExpressRcIds();

    MessageDbHelper messageDbHelper;
    FriendDbHelper friendDbHelper;
    SQLiteDatabase sqLiteDatabase,sqLiteDatabase_friend;
    RecyclerView lv_message;
    Cursor cursor,cursor_friend;

    ChatAdapter chatAdapter;
   ArrayList<Chat> chatArrayList = new ArrayList<>();
    String path;
    int type;
    AlertDialog out;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x00:
                    chatArrayList.add(new Chat(etCtn.getText().toString(),ChatAdapter.TYPE_SEND,UID,TypeSystem.MSG_TEXT,setTime(0)));
                    chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList,mPresenter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendMessage.this);
                    lv_message.setLayoutManager(linearLayoutManager);
                    lv_message.setAdapter(chatAdapter);
                    int item=chatArrayList.size();
                    lv_message.scrollToPosition(item-1);
                    etCtn.setText("");
                    viewpager_layout.setVisibility(View.GONE);
                    break;
                case 0x01:
                    String InMsg= (String) msg.obj;
                    try {
                        JSONObject jsonObject=new JSONObject(InMsg);
                        Log.d("cs",jsonObject+"");
                        if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                            String send_id=jsonObject.getString(StructureSystem.ID);
                            String message=jsonObject.getString(StructureSystem.MSG);
                            //Toast.makeText(FriendMessage.this,message,Toast.LENGTH_SHORT).show();
                            String time=jsonObject.getString(StructureSystem.TIME);
                            int type=jsonObject.getInt(StructureSystem.TYPE);

                            cursor_friend=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+send_id+"'",null);
                            if (cursor_friend.getCount()<=0){
                                if (type==TypeSystem.MSG_IMAGE){
                                    String img="图片";
                                    sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+send_id+"','"+img+"','"+time+"',"+type+")");
                                }else {
                                    sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+send_id+"','"+message+"','"+time+"',"+type+")");
                                }
                            }else if (cursor_friend.getCount()>0){
                                if (type==TypeSystem.MSG_IMAGE){
                                    String img="图片";
                                    sqLiteDatabase_friend.execSQL("update friend set msg='"+img+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+send_id+"'");
                                }else {
                                    sqLiteDatabase_friend.execSQL("update friend set msg='"+message+"',time='"+time+"',type="+type+" where uid='"+UID+"' and friend='"+send_id+"'");
                                }
                            }

                            String filePath="";
                            if (type==TypeSystem.MSG_IMAGE){
                                byte[] bytes=new MsgTool().StringToByte(message);

                                filePath=new MsgTool().getFileByBytes(bytes,FriendMessage.this.getExternalFilesDir(null).getPath(),send_id+"to"+UID+setTime(1)+".jpg");
                                Toast.makeText(FriendMessage.this,filePath,Toast.LENGTH_SHORT).show();
                                Log.d("cs",filePath);
                                if (filePath!=null||filePath!="") {

                                    sqLiteDatabase.execSQL("insert into message values('" + UID + "','" + send_id + "','" + send_id + "','" + filePath + "','" + time + "'," + type + ")");
                                    chatArrayList.add(new Chat(filePath, ChatAdapter.TYPE_RECEIVE, id, TypeSystem.MSG_IMAGE,time));

                                }
                            }else if (type==TypeSystem.MSG_TEXT){
                                    sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+send_id+"','"+send_id+"','"+message+"','"+time+"',"+type+")");
                                    chatArrayList.add(new Chat(message,ChatAdapter.TYPE_RECEIVE,id,TypeSystem.MSG_TEXT,time));
                                }

                            chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList,mPresenter);
                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FriendMessage.this);
                            lv_message.setLayoutManager(linearLayoutManager1);
                            lv_message.setAdapter(chatAdapter);
                            int item1=chatArrayList.size();
                            lv_message.scrollToPosition(item1-1);
                        }
                        else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)){

                        }

//                        chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList);
//                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FriendMessage.this);
//                        lv_message.setLayoutManager(linearLayoutManager1);
//                        lv_message.setAdapter(chatAdapter);
//
//                        int item2=chatArrayList.size();
//                        lv_message.scrollToPosition(item2-1);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x02:
                    Toast.makeText(FriendMessage.this,"发送成功！",Toast.LENGTH_SHORT).show();
                    cursor_friend=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+id+"'",null);
                    if (cursor_friend.getCount()<=0){
                            String img="图片";
                            sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+id+"','"+img+"','"+setTime(0)+"',"+TypeSystem.MSG_IMAGE+")");
                    }else if (cursor_friend.getCount()>0){
                            String img="图片";
                            sqLiteDatabase_friend.execSQL("update friend set msg='"+img+"',time='"+setTime(0)+"',type="+TypeSystem.MSG_IMAGE+" where uid='"+UID+"' and friend='"+id+"'");
                    }
                    chatArrayList.add(new Chat((String) msg.obj,ChatAdapter.TYPE_SEND,UID,TypeSystem.MSG_IMAGE,setTime(0)));
                    chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList,mPresenter);
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(FriendMessage.this);
                    lv_message.setLayoutManager(linearLayoutManager1);
                    lv_message.setAdapter(chatAdapter);

                    int item3=chatArrayList.size();
                    lv_message.scrollToPosition(item3-1);
                    out.dismiss();
                    break;
                case 0x03:
                    Toast.makeText(FriendMessage.this,"发送失败！",Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    @SuppressLint("HandlerLeak")
    Handler pathHandler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.obj!=null||msg.obj!=""){
                View view=getLayoutInflater().inflate(R.layout.file_path,null);
                final AlertDialog.Builder ab=new AlertDialog.Builder(FriendMessage.this);
                out=ab.create();
                final Window window=out.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(0));
                out.setView(view);
                out.show();
                HoldTitle holdTitle=view.findViewById(R.id.file_path_hold);
                TextView textView=view.findViewById(R.id.text_file_path);
                ImageView img=view.findViewById(R.id.file_path_img);
                String filePath=(String)msg.obj;
                Bitmap bitmap=new MsgTool().decodeSampleBitmap(img,filePath);
                img.setImageBitmap(bitmap);
                textView.setText(filePath);
                holdTitle.setTvmoreOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        if (filePath!=null||filePath!=""){
                            byte[] BFile =new MsgTool().getBytesByFile(filePath);
                            String bytes=new MsgTool().ByteToString(BFile);
                            FileMsg(bytes,filePath);
                        }else {
                            Toast.makeText(FriendMessage.this,"没有选择文件",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                holdTitle.setIvbackOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        out.dismiss();
                    }
                });
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_message);

        messageDbHelper=new MessageDbHelper(this);
        friendDbHelper=new FriendDbHelper(this);

        sqLiteDatabase_friend=friendDbHelper.getReadableDatabase();

        sqLiteDatabase=messageDbHelper.getReadableDatabase();
        Intent intent=getIntent();

        UID=intent.getStringExtra(StructureSystem.UID);
        id=intent.getStringExtra(StructureSystem.ID);
        name=intent.getStringExtra(StructureSystem.NAME);

        sendMsg=findViewById(R.id.sendMsg);
        lv_message=findViewById(R.id.lv_message);
        recordAudioButton=findViewById(R.id.friend_voice);
        sendLayout=findViewById(R.id.send_layout_msg);
        speak=findViewById(R.id.btn_speak);
        rootMain=findViewById(R.id.friend_main);

       // mPresenter=new MainPresenter<MainContract.View>((MainContract.View) this,this);

        if(lv_message.getRecycledViewPool()!=null){
            lv_message.getRecycledViewPool().setMaxRecycledViews(0, 10);
        }

        ImageButton back=findViewById(R.id.imbtn_message_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       setList();

        new Thread(new Runnable() {
            @Override
            public void run() {
                new InMsg().start();
            }
        }).start();

        etCtn = (EditText) findViewById(R.id.content);
        viewpager_layout = findViewById(R.id.viewpager_layout);
        express_spot_layout = (RelativeLayout) findViewById(R.id.express_spot_layout);
        viewPager = (ViewPager) findViewById(R.id.tabpager);

        TextView set_friend_name=findViewById(R.id.text_message_friend_name);
        set_friend_name.setText(name);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ctn = etCtn.getText().toString();
                if(ctn.equals("") || ctn == null){
                    Toast.makeText(FriendMessage.this, R.string.tip_input, Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String cs=httpUtil.httpSendMsg(UID,id,ctn, TypeSystem.MSG_TEXT, TypeSystem.WRITE);
                                Log.d("cs",cs);
                                JSONObject jsonObject=new JSONObject(cs);
                                Message message=new Message();
                                if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                                    sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+id+"','"+UID+"','"+ctn+"','"+setTime(0)+"',"+TypeSystem.MSG_TEXT+")");


                                    cursor_friend=sqLiteDatabase_friend.rawQuery("select * from friend where uid='"+UID+"' and friend='"+id+"'",null);
                                    if (cursor_friend.getCount()<=0){
                                        sqLiteDatabase_friend.execSQL("insert into friend values('"+UID+"','"+id+"','"+ctn+"','"+setTime(0)+"',"+TypeSystem.MSG_TEXT+")");
                                    }else if (cursor_friend.getCount()>0){
                                        sqLiteDatabase_friend.execSQL("update friend set msg='"+ctn+"',time='"+setTime(0)+"',type="+TypeSystem.MSG_TEXT+" where uid='"+UID+"' and friend='"+id+"'");
                                    }

                                    message.what=0x00;
                                    //handler.sendMessage(message);
                                }else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)||cs==null||cs==""){
                                    message.what=0x03;
                                }
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //setList();
                }
            }
        });
    }

    public void FileMsg(String fileString,String path){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String fileReturn=httpUtil.httpSendMsg(UID,id,fileString,TypeSystem.MSG_IMAGE,TypeSystem.WRITE);
                    JSONObject jsonObject=new JSONObject(fileReturn);
                    Message message=new Message();
                    if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.SUCCESS)){
                        sqLiteDatabase.execSQL("insert into message values('"+UID+"','"+id+"','"+UID+"','"+path+"','"+setTime(0)+"',"+TypeSystem.MSG_IMAGE+")");
                        message.what=0x02;
                        message.obj=path;

                    }else if (jsonObject.getString(StructureSystem.ERROR).equals(StructureSystem.FAILED)){
                        message.what=0x03;
                    }
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void hideSoftinput(View view){
        InputMethodManager manager = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if(manager.isActive()){
            manager.hideSoftInputFromWindow(etCtn.getWindowToken(), 0);
        }
    }

    public void showExpressionWindow(View view){

        this.hideSoftinput(view);
        if (viewpager_layout.getVisibility()==View.VISIBLE){
            viewpager_layout.setVisibility(View.GONE);
        }else {
            viewpager_layout.setVisibility(View.VISIBLE);
        }

        Display currDisplay = getWindowManager().getDefaultDisplay();
        int displayWidth = currDisplay.getWidth();
        int displayHeight=currDisplay.getHeight();

        Bitmap express = BitmapFactory.decodeResource(getResources(), R.drawable.f000);
        int headWidth = express.getWidth();
        int headHeight = express.getHeight();
        Log.d(ContentFlag.TAG, displayWidth+":" + headWidth);
        final int colmns = Math.min(displayWidth / headWidth, 7);
        final int rows = Math.min(displayHeight / headHeight, SystemConstant.express_counts/colmns);
        Log.d("cs",rows+"");
        final int pageItemCount = colmns * rows;

        int totalPage = SystemConstant.express_counts % pageItemCount == 0 ?
                SystemConstant.express_counts / pageItemCount : SystemConstant.express_counts / pageItemCount + 1;
        final List<View> listView = new ArrayList<View>();
        for (int index = 0; index < totalPage; index++) {
            listView.add(getViewPagerItem(index, colmns, pageItemCount));
        }
        express_spot_layout.removeAllViews();
        for (int i = 0; i < totalPage; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i+1);
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.d2);
            }else{
                imageView.setBackgroundResource(R.drawable.d1);
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            layoutParams.bottomMargin = 20;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            if(i!= 0){
                layoutParams.addRule(RelativeLayout.ALIGN_TOP, i);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i);
            }
            express_spot_layout.addView(imageView, layoutParams);
        }
        Log.d(ContentFlag.TAG, express_spot_layout.getChildCount() + "");

        viewPager.setAdapter(new PagerAdapter() {
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            public int getCount() {
                return listView.size();
            }

            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(listView.get(position));
            }

            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(listView.get(position));
                return listView.get(position);
            }
        });

        viewPager.setOnPageChangeListener(new FriendMessage.MyPageChangeListener());
    }

    public void openPhoto(View view) {
        // 打开系统的文件选择器
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/jpg");//选择图片
        // intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
           // intent.setType("*/*");//无限制
            this.startActivityForResult(intent, REQUEST_CODE);

    }

    // 获取文件的真实路径
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Message message=new Message();
        if (resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){
                path=uri.getPath();
                message.obj=path;
                pathHandler.sendMessage(message);
                return;
            }
            if (Build.VERSION.SDK_INT>Build.VERSION_CODES.KITKAT){
                path=getPath(this,uri);
            }else {
                path=getRealPathFromURI(uri);
            }
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        if (data == null) {
            // 用户未选择任何文件，直接返回
            path="";
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        // 通过ContentProvider查询文件路径
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        if (cursor == null) {
            // 未查询到，说明为普通文件，可直接通过URI获取文件路径
            path = uri.getPath();
            message.obj=path;
            pathHandler.sendMessage(message);
            return;
        }
        if (cursor.moveToFirst()) {
            // 多媒体文件，从数据库中获取文件的真实路径
            path = cursor.getString(cursor.getColumnIndex("_data"));
            message.obj=path;
            pathHandler.sendMessage(message);
        }
        cursor.close();
    }


    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};

            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }





    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.external.storage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void requestPermission() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK)
                .request();
    }

    public void onclickVoice(View view) {
        Toast.makeText(FriendMessage.this,"该功能尚未开发完成",Toast.LENGTH_SHORT).show();
        viewpager_layout.setVisibility(View.GONE);
        if (recordAudioButton.getVisibility()==View.VISIBLE){
            sendLayout.setVisibility(View.VISIBLE);
            recordAudioButton.setVisibility(View.GONE);
            speak.setImageResource(R.drawable.btn_intercon);
        }else if (recordAudioButton.getVisibility()==View.GONE){
            recordAudioButton.setVisibility(View.VISIBLE);
            sendLayout.setVisibility(View.GONE);
            speak.setImageResource(R.drawable.btn_keyboard);

            String time=setTime(1);
            mPresenter=new MainPresenter<MainContract.View>(FriendMessage.this,this,UID,id,time,chatAdapter,lv_message,chatArrayList);

            requestPermission();
            mPresenter.init();

            recordAudioButton.setOnVoiceButtonCallBack(new RecordAudioButton.OnVoiceButtonCallBack() {
                @Override
                public void onStartRecord() {
                    mPresenter.startRecord();
                }

                @Override
                public void onStopRecord() {
                    mPresenter.stopRecord();
                }

                @Override
                public void onWillCancelRecord() {
                    mPresenter.willCancelRecord();
                }

                @Override
                public void onContinueRecord() {
                    mPresenter.continueRecord();
                }
            });
//            chatArrayList.add(new Chat(this.getExternalFilesDir(null).getPath()+UID+"to"+id+time+".voice",
//                    ChatAdapter.TYPE_SEND,UID,TypeSystem.MSG_VOICE,time));
//            chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList,mPresenter);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendMessage.this);
//            lv_message.setLayoutManager(linearLayoutManager);
//            lv_message.setAdapter(chatAdapter);
//            int item=chatArrayList.size();
//            lv_message.scrollToPosition(item-1);
        }

    }

    public void openMore(View view) {
        Toast.makeText(FriendMessage.this,"该功能尚未开发",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showList(List<File> list) {

    }

    @Override
    public void showNormalTipView() {
        if (recordVoicePopWindow==null){
            recordVoicePopWindow=new RecordVoicePopWindow(this);
        }
        recordVoicePopWindow.showAsDropDown(rootMain);
    }

    @Override
    public void showTimeOutTipView(int remainder) {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.showTimeOutTipView(remainder);
        }
    }

    @Override
    public void showRecordingTipView() {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.showRecordingTipView();
        }
    }

    @Override
    public void showRecordTooShortTipView() {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.showRecordTooShortTipView();
        }
    }

    @Override
    public void showCancelTipView() {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.showCancelTipView();
        }
    }

    @Override
    public void hideTipView() {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.dismiss();
        }
    }

    @Override
    public void updateCurrentVolume(int db) {
        if (recordVoicePopWindow!=null){
            recordVoicePopWindow.updateCurrentVolume(db);
        }
    }

    @Override
    public void startPlayAnim(int position) {
        chatAdapter.startPlayAnim(position);
    }

    @Override
    public void stopPlayAnim() {
        chatAdapter.stopPlayAnim();
    }

    private final class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int curIndex = 0;
        public void onPageSelected(int index) {
            express_spot_layout.getChildAt(curIndex).setBackgroundResource(R.drawable.d1);
            express_spot_layout.getChildAt(index).setBackgroundResource(R.drawable.d2);
            curIndex = index;
        }
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    private View getViewPagerItem(final int index, int colums, final int pageItemCount) {
        LayoutInflater inflater = this.getLayoutInflater();
        View express_view = inflater.inflate(R.layout.express_gv, null);
        GridView gridView = (GridView) express_view.findViewById(R.id.gv_express);
        gridView.setNumColumns(colums);
        gridView.setAdapter(new ExpressionGvAdapter(index, pageItemCount, imageIds, inflater));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positon,
                                    long id) {
                Bitmap bitmap = null;
                int start = index * pageItemCount;
                positon = positon + start;
                bitmap = BitmapFactory.decodeResource(getResources(), imageIds[positon]);
                ImageSpan imageSpan = new ImageSpan(bitmap);
                String str = "";
                if(positon < 10){
                    str = "[f00"+positon+"]";
                }else if(positon < 100){
                    str = "[f0"+positon+"]";
                }else{
                    str = "[f"+positon+"]";;
                }
                SpannableString spannableString = new SpannableString(str);
                spannableString.setSpan(imageSpan, 0, str.length(),
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                etCtn.append(spannableString);

                //viewpager_layout.setVisibility(View.GONE);
            }
        });

        return express_view;
    }

    class InMsg extends Thread{
        @Override
        public void run() {
            super.run();
                while (true){
                    Object msg= null;
                    try {
                        Thread.sleep(500);
                        msg = httpUtil.httpInMsg(UID, TypeSystem.READ);
                        if (msg!=null){
                            Message message=new Message();
                            message.obj=msg;
                            message.what=0x01;
                            handler.sendMessage(message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


        }
    }

    public String setTime(int i){
        String time="";
        switch (i){
            case 0:
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
                Date date = new Date(System.currentTimeMillis());
                time=formatter.format(date);

            break;
            case 1:
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter1= new SimpleDateFormat("yyyyMMddHHmmss");
                Date date1 = new Date(System.currentTimeMillis());
                time=formatter1.format(date1);
                break;

        }
        return time;
    }

    public void setList(){

        cursor=sqLiteDatabase.rawQuery("select * from message where uid='"+UID+"' and id='"+id+"'",null);
        ArrayList<HashMap<String, String>> photolist;
        photolist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> al;

        int length=cursor.getCount();
        String[] id=new String[length];
        String[] name=new String[length];
        String[] message=new String[length];
        String[] time=new String[length];
        int [] type=new int[length];
        if (length>0){
            int j=0;
            while (cursor.moveToNext()){
                id[j]=cursor.getString(2);
                name[j]=cursor.getString(2);
                message[j]=cursor.getString(3);
                time[j]=cursor.getString(4);
                type[j]=cursor.getInt(5);
                j++;
            }
            for (int i=0;i<length;i++){

                al = new HashMap<String, String>();
                if (id[i].equals(UID)){
                    chatArrayList.add(new Chat(message[i],ChatAdapter.TYPE_SEND,id[i],type[i],time[i]));
                }else {
                    chatArrayList.add(new Chat(message[i],ChatAdapter.TYPE_RECEIVE,id[i],type[i],time[i]));
                }

            }

        }
        chatAdapter = new ChatAdapter(FriendMessage.this, chatArrayList,mPresenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FriendMessage.this);
        lv_message.setLayoutManager(linearLayoutManager);
        lv_message.setAdapter(chatAdapter);
        int item=chatArrayList.size();
        lv_message.scrollToPosition(item-1);

//        }else {
//            cursor.moveToNext();
//
//            for (int j=0;j<length;j++){
//                id[j]=cursor.getString(2);
//                name[j]=cursor.getString(2);
//                message[j]=cursor.getString(3);
//                time[j]=cursor.getString(4);
//                cursor.moveToNext();
//            }
//
//            for (int i=0;i<length;i++){
//
//                al = new HashMap<String, String>();
//                if (id[i].equals(UID)){
//                    al.put("id","");
//                    al.put("name",id[i]);
//                    al.put("message",message[i]);
//                    al.put("time",time[i]);
//                }else {
//                    al.put("id",id[i]);
//                    al.put("name","");
//                    al.put("message",message[i]);
//                    al.put("time",time[i]);
//                }
//
//                photolist.add(al);
//            }
//        }


        /*list=new SendMessageUtil().getsendmessage(this,id);
        Log.i(TAG, "" + list);*/
//        SimpleAdapter simpleAdapter=new SimpleAdapter(FriendMessage.this, photolist, R.layout.message,new String[]{"id","name","message","time"},new int[]{R.id.text_view_message_id,R.id.text_view_message_name, R.id.text_view_message_msg, R.id.text_view_message_time});
//        //adapter=new SendMessageAdapter(list,this);
//        lv_message.setAdapter(simpleAdapter);


    }

    public Bitmap smileB(int positon){
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(getResources(),imageIds[positon]);
        return bitmap;
    }


}
