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
 * ʵ��MapTileProvider��ͨ��webService��ʽ���ʷ����������tile����
 * 
 * @author �����
 * @version 1.0
 */
public class MapTileDownloader extends MapTileProvider {

	/** ��װMapService�ӿ� */
	private MapService service;

	/**
	 * ���캯�� ��ʼ��MapService����
	 */
	public MapTileDownloader() {
		// TODO Auto-generated constructor stub
		service = new MapService();
	}

	@Override
	public Drawable getMapTile(MapTile tile) {
		/* ͨ��web service������Ƭ���� */
		String imageData = service.getMapTile(tile);

		if (imageData != null) {
			// ����DataHandler�γ��ļ� ������drawable
			try {
				// �����ļ���
				File dir = new File(CLIENT_PATH, REGION);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// �����ļ�
				final File file = new File(CLIENT_PATH, getTileURLString(tile));
				if (!file.exists()) {
					file.createNewFile();
				}
				/* ��������ת�����ļ� */
				byte[] buffer = Base64.decode(imageData);
				FileOutputStream fos = null;
				fos = new FileOutputStream(file);
				fos.write(buffer);
				fos.flush();
				fos.close();
				/* ���ļ�ת����drawable���󲢷��� */
				final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
				if (bitmap != null) {
					Drawable drawable = new BitmapDrawable(bitmap);
					// ����drawable����
					TILE_CACHE.putTile(tile, drawable);
					return drawable;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/* ����ȡ��Ƭ��ְ�𽻸���һ��provider */
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
