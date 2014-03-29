package com.ubirtls.mapprovider;

import java.io.File;
import java.io.FileOutputStream;

import org.kobjects.base64.Base64;

import com.ubirtls.MapService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * 实现MapTileProvider，通过webService方式访问服务器，获得tile数据
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapTileDownloader extends MapTileProvider {

	/** 封装MapService接口 */
	private MapService service;

	/**
	 * 构造函数 初始化MapService对象
	 */
	public MapTileDownloader() {
		// TODO Auto-generated constructor stub
		service = new MapService();
	}

	@Override
	public Drawable getMapTile(MapTile tile) {
		/* 通过web service请求瓦片数据 */
		String imageData = service.getMapTile(tile);

		if (imageData != null) {
			// 根据DataHandler形成文件 并返回drawable
			try {
				// 创建文件夹
				File dir = new File(CLIENT_PATH, REGION);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 创建文件
				final File file = new File(CLIENT_PATH, getTileURLString(tile));
				if (!file.exists()) {
					file.createNewFile();
				}
				/* 将输入流转换成文件 */
				byte[] buffer = Base64.decode(imageData);
				FileOutputStream fos = null;
				fos = new FileOutputStream(file);
				fos.write(buffer);
				fos.flush();
				fos.close();
				/* 将文件转换成drawable对象并返回 */
				final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
				if (bitmap != null) {
					Drawable drawable = new BitmapDrawable(bitmap);
					// 缓存drawable对象
					TILE_CACHE.putTile(tile, drawable);
					return drawable;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/* 将提取瓦片的职责交给下一个provider */
		if (nextProvider != null)
			return nextProvider.getMapTile(tile);
		return null;
	}

	@Override
	protected String getThreadGroupName() {
		// TODO Auto-generated method stub
		return "downloder";
	}

}
