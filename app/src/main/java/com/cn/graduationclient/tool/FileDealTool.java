package com.cn.graduationclient.tool;

import android.os.Environment;

import java.io.File;

public class FileDealTool {
	public static void delRecordFile() {
		File dir = new File(Environment.getExternalStorageDirectory()
				+ "/recordMsg/");
		if (dir.exists()) {
			File[] fileList = dir.listFiles();
			if (fileList != null)
				for (int i = 0; i < fileList.length; i++) {
					if (fileList[i].isFile())
						fileList[i].delete();
				}
			dir.delete();
		}
	}
}