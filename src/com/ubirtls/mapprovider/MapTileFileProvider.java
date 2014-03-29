package com.ubirtls.mapprovider;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * �̳� MapTileProvider��ͨ�����ʱ����ļ���ʽ���tile����
 * 
 * @author �����
 * @version 1.0
 */
public class MapTileFileProvider extends MapTileProvider {

	public MapTileFileProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Drawable getMapTile(MapTile tile) {
		// TODO Auto-generated method stub
		long start = System.currentTimeMillis();

		final File file = new File(CLIENT_PATH, getTileURLString(tile));
		
		// ������ļ�����
		if (file.exists()) {
			try {
				// Ĭ�ϵ�ʵ���ǽ��ļ���Ϊһ��bitmap���м��أ�������һ��BitmapDrawable����
				String path = file.getPath();
				final Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (bitmap != null) {
					Drawable drawable = new BitmapDrawable(bitmap);
					long end = System.currentTimeMillis();
				   // Log.i("file-inteval", String.valueOf(end - start));
					// ����drawable����
					TILE_CACHE.putTile(tile, drawable);
					return drawable;
				} else {
					// �ļ��޷����أ�˵���ļ���Ч��ɾ���ļ�
					try {
						new File(path).delete();
					} catch (final Throwable e) {
					}
				}
			}
			// �����ڴ��С���󣬽�����������
			catch (final OutOfMemoryError e) {
				System.gc();
			}
			return null;
		}
		// �����ڣ���Ҫ�{����һ��Provider����tile������
		else if (nextProvider != null) {
			Log.i("file", file.getPath());
			return nextProvider.getMapTile(tile);
		}
		return null;
	}

	@Override
	protected String getThreadGroupName() {
		// TODO Auto-generated method stub
		return "file";
	}

}
