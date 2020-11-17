package com.cn.graduationclient.tool;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class MsgTool implements Tool {

    int imageIds[] = ExpressionUtil.getExpressRcIds();

    /**
     *
     * @param msg
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public String ByteToString(byte[] msg){

        return Base64.getEncoder().encodeToString(msg);

    }

    /**
     *
     * @param msg
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public byte[] StringToByte(String msg){
        return Base64.getDecoder().decode(msg);
    }


    /**
     *
     * @param path
     * @return
     */
    @Override
    public byte[] getBytesByFile(String path) {
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param bytes
     * @param filePath
     * @param fileName
     */

    @Override
    public String getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String s="";
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            s=filePath+"/"+fileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    /**
     * 压缩图片大小
     * @param path 图片的路径
     * @param refundPhoto
     * @return
     */
    @Override
    public Bitmap decodeSampleBitmap(ImageView refundPhoto,String path) {
        //refundPhoto是将要呈现图片的ImageView控件
        int targetW = refundPhoto.getWidth();
        int targetH = refundPhoto.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        //获取图片的最大压缩比
       // int scaleFactor = Math.max(photoW/targetW,photoH/targetH);
        options.inJustDecodeBounds = false;
      //  options.inSampleSize = scaleFactor;
        options.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);
        return  bitmap;
    }


}
