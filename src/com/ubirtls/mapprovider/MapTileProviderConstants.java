package com.ubirtls.mapprovider;

import java.io.File;

import android.os.Environment;
/**
 * 获取tile时用到的一些常量
 * @author 胡旭科
 * @version 1.0
 */
public interface MapTileProviderConstants {
	/** 文件扩展名 */
	public static String EXTENTION = ".jpg";
	/** client程序的根目录 */
	public static final File CLIENT_PATH = new File(Environment
			.getExternalStorageDirectory(), "client");
}
