package com.ubirtls.mapprovider;

import java.io.File;

import android.os.Environment;
/**
 * ��ȡtileʱ�õ���һЩ����
 * @author �����
 * @version 1.0
 */
public interface MapTileProviderConstants {
	/** �ļ���չ�� */
	public static String EXTENTION = ".jpg";
	/** client����ĸ�Ŀ¼ */
	public static final File CLIENT_PATH = new File(Environment
			.getExternalStorageDirectory(), "client");
}
