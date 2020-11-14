package com.cn.graduationclient.tool;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface Tool {
    String ByteToString(byte[] msg);
    byte[] StringToByte(String msg);
    byte[] getBytesByFile(String path);
    String getFileByBytes(byte[] bytes, String filePath, String fileName);
    Bitmap decodeSampleBitmap(ImageView refundPhoto, String path);
}
