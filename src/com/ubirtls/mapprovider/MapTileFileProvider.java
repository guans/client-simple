package com.ubirtls.mapprovider;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 继承 MapTileProvider，通过访问本地文件方式获得tile数据
 * 
 * @author 胡旭科
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
		
		// 構造的文件存在
		if (file.exists()) {
			try {
				// 默认的实现是将文件作为一个bitmap进行加载，并创建一个BitmapDrawable对象
				String path = file.getPath();
				final Bitmap bitmap = BitmapFactory.decodeFile(path);
				if (bitmap != null) {
					Drawable drawable = new BitmapDrawable(bitmap);
					long end = System.currentTimeMillis();
				   // Log.i("file-inteval", String.valueOf(end - start));
					// 缓存drawable对象
					TILE_CACHE.putTile(tile, drawable);
					return drawable;
				} else {
					// 文件无法加载，说明文件无效，删除文件
					try {
						new File(path).delete();
					} catch (final Throwable e) {
					}
				}
			}
			// 超出内存大小错误，进行垃圾回收
			catch (final OutOfMemoryError e) {
				System.gc();
			}
			return null;
		}
		// 不存在，需要調用下一個Provider进行tile的请求
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
