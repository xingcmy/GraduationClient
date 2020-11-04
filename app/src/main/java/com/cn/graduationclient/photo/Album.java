package com.cn.graduationclient.photo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class Album {
    public Album(Cursor cur) {
//图片路径
        this(cur.getString(3),
//所在文件夹的名称
                cur.getString(1),
//所在的父文件
                cur.getLong(0),
//文件夹的文件数目
                cur.getInt(2),
//修改时间
                cur.getLong(4)
        );
    }

    public Album(String string, String string1, long aLong, int anInt, long aLong1) {
    }


    public String getId() {
        return null;
    }

    public static Cursor getAlbumCursor(ContentResolver cr){
//打开files这个表
        Uri uri= MediaStore.Files.getContentUri("external");
        String [] project =new String[]{
                MediaStore.Files.FileColumns.PARENT,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                "count(*)",
                MediaStore.Images.Media.DATA,
                "max(" + MediaStore.Images.Media.DATE_MODIFIED + ")"
        };
//用group by进行整合
        String selection = String.format("%s=? or %s=?) group by (%s",
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.PARENT
        ) ;
        String[] selectionArgs = new String[]{
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE+"",//获取图片文件
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO+"",//获取视频文件
        };
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
//        String sortOrder = "max(date_modified) DESC";
// 转换成sql语句: SELECT parent, bucket_display_name, count(*), _data, max(date_modified) FROM files WHERE (media_type=? or media_type=?)
// group by (parent) HAVING (_data NOT LIKE ? ) ORDER BY max(date_modified) DESC
        return cr.query(uri,project,selection,selectionArgs,sortOrder);
    }

    public static Cursor getMediaCursor(ContentResolver cr,Album album){
        Uri uri = MediaStore.Files.getContentUri("external");
        String[] sProjection = new String[] {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.ORIENTATION
        };
//获取parent一样的文件
        String selections = (String.format("(%s=? or %s=?) and %s=?",
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.PARENT));
        String[] selectionArgs = new String[]{
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE+"",
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO+"",
                album.getId()+""
        };
        String sortOrder = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        return  cr.query(uri,sProjection,selections,selectionArgs,sortOrder);
    }
}
