package com.ubirtls.view.map;

import java.io.FileOutputStream;
import java.util.Properties;

import com.ubirtls.Controller;
import com.ubirtls.mapprovider.MapTile;
import com.ubirtls.view.Activity.mapnavigate;

import coordinate.TwoDCoordinate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;

import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;

public class TilesOverlay extends Overlay {

	/**
	 * MVC ģʽ�еĿ����� TilesOverlayͨ���������ͣ�MapProvider��ģ�ͽ��� �����Ƭ
	 */
	private Controller controller;

	/**
	 * handler����
	 */
	private final Handler mapTileRequestHandler;

	/**
	 * 
	 */
	private BitmapDrawable defaultTile = null;

	/**
	 * ������ɫ
	 */
	private int loadingBackgroundColor = Color.rgb(216, 208, 208);

	/**
	 * ������ɫ
	 */
	private int loadingLineColor = Color.rgb(200, 192, 192);

	/**
	 * ���ڻ���tile�ľ�������
	 */
	private Rect tileRect = new Rect();
	
	/***/
	private int point=0;
	/**
	 * ���캯��
	 * 
	 * @param tileProviderID ��ʶʹ���ĸ�provider�ṩ��Ƭ����
	 * @param handler ����tile������
	 */
	public TilesOverlay(final Context context, Handler handler) {
		super(context);
		controller = Controller.getInstance();
		this.mapTileRequestHandler = handler;
	}

	public static int mod(int number, final int modulus) {
		if (number > 0)
			return number % modulus;

		while (number < 0)
			number += modulus;

		return number;
	}

	/**
	 * ���maptile�����handler
	 * 
	 * @return maptile�����handler
	 */
	public Handler getHandler() {
		return this.mapTileRequestHandler;
	}

	@Override
	public boolean onLongPress(MotionEvent e, MapView mapView,TwoDCoordinate coor,int flag) {
		// TODO Auto-generated method stub
			/*TwoDCoordinate*/
			coor = mapView.getProjection().fromScreenPixels(
					e.getX(), e.getY(), mapView.getZoomLevel());
			
			Toast toast = Toast.makeText(super.context, "�˴�Ϊ:��"
					+ new Float(coor.getXCoordinate()).toString() + ","
					+ new Float(coor.getYCoordinate()).toString() + ")",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.LEFT | Gravity.TOP, (int) e.getX(), (int) e
					.getY());
			//toast.show();
		
		return true;
	}
	@Override
	public void onDetach(final MapView mapView) {
		super.onDetach(mapView);
	}



	@Override
	protected void draw(Canvas c, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		if (shadow) {
			return;
		}
		int zoomLevel = mapView.zoomLevel;
		// ��ô����Ƶ�����
		Rect screenRect = mapView.getScreenRect(null);
		/* ������Ϣ */
		/*
		 * Log.i("screen", new Integer(screenRect.left).toString() + " " + new
		 * Integer(screenRect.top).toString() + " " + new
		 * Integer(screenRect.right).toString() + " " + new
		 * Integer(screenRect.bottom).toString());
		 */
		Point upperLeft = TileSystem.PixelXYToTileXY(screenRect.left,
				screenRect.top);
		//upperLeft.offset(-1, -1);
		Point lowerRight = TileSystem.PixelXYToTileXY(screenRect.right,
				screenRect.bottom);
		lowerRight.offset(1, 1);
		final int mapTileWBound = TileSystem.MapTileWidth(mapView.zoomLevel);
		final int mapTileHBound = TileSystem.MapTileHeight(mapView.zoomLevel);
		// ��ǰ��Ļ��Ҫ����Ƭ����
		final int numNeeded = (lowerRight.y - upperLeft.y + 1)
				* (lowerRight.x - upperLeft.x + 1);
		// ȷ���������㹻��
		controller.ensureCapacity(numNeeded);
		Log.i("ensureCapacity", String.valueOf(numNeeded));
		// ������Ϣ
/*		int offsetX = (TileSystem.TILE_WIDTH_SIZE - (TileSystem.MapWidthSize(zoomLevel))
				% TileSystem.TILE_WIDTH_SIZE);
*/		
		int offsetX = 0;
		int offsetY = (TileSystem.TILE_HEIGHT_SIZE - (TileSystem.MapHeightSize(zoomLevel))
				% TileSystem.TILE_HEIGHT_SIZE);
		long allStartTime = System.currentTimeMillis();
		// ��ȡ��������Ƭ
		for (int y = upperLeft.y; y <= lowerRight.y; y++) {
			for (int x = upperLeft.x; x <= lowerRight.x; x++) {
				// Construct a MapTile to request from the tile provider.
				final int tileY = mod(mapTileHBound - 1 -y, mapTileHBound);
				final int tileX = mod(x, mapTileWBound);
				long startTime = System.currentTimeMillis();
				MapTile tile = new MapTile(tileX, tileY, zoomLevel);
				Drawable currentMapTile = controller.getMapTileAsync(tile);
				Log.i("bitmap-size", String.valueOf(Controller.getInstance().mapProvider.TILE_CACHE.size()));
				if (currentMapTile == null) {
					currentMapTile = getDefaultTile();
				}

				// ������Ƭ

				if (currentMapTile != null) {
					int rectX = x * TileSystem.TILE_WIDTH_SIZE;
					int rectY = y * TileSystem.TILE_HEIGHT_SIZE ;
					rectY = y * TileSystem.TILE_HEIGHT_SIZE - offsetY;
					rectX = x * TileSystem.TILE_WIDTH_SIZE - offsetX;
				
						tileRect.set(rectX, rectY, rectX
								+ TileSystem.TILE_WIDTH_SIZE,rectY
								+ TileSystem.TILE_HEIGHT_SIZE);
					
					/* ������Ϣ */

					currentMapTile.setBounds(tileRect);
					currentMapTile.draw(c);

				}
				long endTime = System.currentTimeMillis();
				Log.i("singal-time",  String.valueOf(endTime - startTime));
			}
		}
		long allEndTime = System.currentTimeMillis();
		Log.i("all-time", "number " + String.valueOf(numNeeded)+" " + String.valueOf(allEndTime - allStartTime));
	}

	/**
	 * ��ȡ������ɫ
	 * 
	 * @return ������ɫ
	 */
	public int getLoadingBackgroundColor() {
		return loadingBackgroundColor;
	}

	/**
	 * ���ȴ���Ƭ����ʱ �������ڻ��Ʊ�������ɫ
	 * 
	 * @param pLoadingBackgroundColor ʹ�õ���ɫ. ���ֵ�� {@link Color.TRANSPARENT}
	 *            �Ͳ��������Ƭ
	 */
	public void setLoadingBackgroundColor(final int pLoadingBackgroundColor) {
		if (loadingBackgroundColor != pLoadingBackgroundColor) {
			loadingBackgroundColor = pLoadingBackgroundColor;
			clearDefaultTile();
		}
	}

	/**
	 * ��û�����������ɫ
	 * 
	 * @return ��������ɫ
	 */
	public int getLoadingLineColor() {
		return loadingLineColor;
	}

	/**
	 * ���û�������ʱ����ɫ
	 * 
	 * @param pLoadingLineColor ����ɫ
	 */
	public void setLoadingLineColor(final int pLoadingLineColor) {
		if (loadingLineColor != pLoadingLineColor) {
			loadingLineColor = pLoadingLineColor;
			clearDefaultTile();
		}
	}

	/**
	 * ���Ĭ�ϵ�tile
	 * 
	 * @return �Լ����Ƶ�Ĭ�ϵ�drawable����
	 */
	private Drawable getDefaultTile() {
		if (defaultTile == null && loadingBackgroundColor != Color.TRANSPARENT) {
			try {
				// �����Ƭ�Ŀ��ߣ�����һ����ͬ��С��λͼ
				final int tileWSize = TileSystem.TILE_WIDTH_SIZE;
				final int tileHSize = TileSystem.TILE_HEIGHT_SIZE;
				final Bitmap bitmap = Bitmap.createBitmap(tileWSize, tileHSize,
						Bitmap.Config.ARGB_8888);
				final Canvas canvas = new Canvas(bitmap);
				// ���û��ʺͻ���
				final Paint paint = new Paint();
				canvas.drawColor(loadingBackgroundColor);
				paint.setColor(loadingLineColor);
				paint.setStrokeWidth(0);
				// ��ʼ���л���16*16��С����
				int lineSize = tileHSize / 16;
				for (int a = 0; a < tileHSize; a += lineSize) {
					canvas.drawLine(0, a, tileHSize, a, paint);
				}
				lineSize = tileWSize / 16;
				for (int a = 0; a < tileWSize; a += lineSize) {
					canvas.drawLine(a, 0, a, tileHSize, paint);
				}
				defaultTile = new BitmapDrawable(bitmap);
			} catch (final OutOfMemoryError e) {
				System.gc();
			}
		}

		return defaultTile;
	}

	/**
	 * ���Ĭ�ϵ�tile
	 */
	private void clearDefaultTile() {
		final BitmapDrawable bitmapDrawable = defaultTile;
		defaultTile = null;
		if (bitmapDrawable != null) {
			bitmapDrawable.getBitmap().recycle();
		}
	}
}
