package com.cn.graduationclient.tool;

public interface Tool {
    String ByteToString(byte[] msg);
    byte[] StringToByte(String msg);
    byte[] getBytesByFile(String path);
    void getFileByBytes(byte[] bytes, String filePath, String fileName);
}
