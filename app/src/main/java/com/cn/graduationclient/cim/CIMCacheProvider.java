package com.cn.graduationclient.cim;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class CIMCacheProvider extends ContentProvider {
    static final String MODEL_KEY = "PRIVATE_CIM_CONFIG";

    public CIMCacheProvider() {
    }

    public int delete(Uri arg0, String key, String[] arg2) {
        this.getContext().getSharedPreferences("PRIVATE_CIM_CONFIG", 0).edit().remove(key).apply();
        return 0;
    }

    public String getType(Uri arg0) {
        return null;
    }

    public Uri insert(Uri arg0, ContentValues values) {
        String key = values.getAsString("key");
        String value = values.getAsString("value");
        this.getContext().getSharedPreferences("PRIVATE_CIM_CONFIG", 0).edit().putString(key, value).apply();
        return null;
    }

    public boolean onCreate() {
        return true;
    }

    public Cursor query(Uri arg0, String[] arg1, String key, String[] arg3, String arg4) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"value"});
        String value = this.getContext().getSharedPreferences("PRIVATE_CIM_CONFIG", 0).getString(arg1[0], (String)null);
        cursor.addRow(new Object[]{value});
        return cursor;
    }

    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        return 0;
    }
}
