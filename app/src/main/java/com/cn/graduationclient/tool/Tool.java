package com.cn.graduationclient.tool;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface Tool {

    /**
     * 将byte数组转为String字节
     * @param msg
     * @return
     */
    String ByteToString(byte[] msg);

    /**
     * 将String字节转为byte数组
     * @param msg
     * @return
     */
    byte[] StringToByte(String msg);

    /**
     * 将文件转为byte数组
     * @param path
     * @return
     */
    byte[] getBytesByFile(String path);

    /**
     * 将byte数组转为文件
     * @param bytes
     * @param filePath
     * @param fileName
     * @return
     */
    String getFileByBytes(byte[] bytes, String filePath, String fileName);

    /***
     * 设置view图片
     * @param refundPhoto
     * @param path
     * @return
     */
    Bitmap decodeSampleBitmap(ImageView refundPhoto, String path);
}
