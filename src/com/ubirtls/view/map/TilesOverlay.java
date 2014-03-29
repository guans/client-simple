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
	 * MVC 模式中的控制器 TilesOverlay通过控制器和（MapProvider）模型交互 获得瓦片
	 */
	private Controller controller;

	/**
	 * handler对象
	 */
	private final Handler mapTileRequestHandler;

	/**
	 * 
	 */
	private BitmapDrawable defaultTile = null;

	/**
	 * 背景颜色
	 */
	private int loadingBackgroundColor = Color.rgb(216, 208, 208);

	/**
	 * 线条颜色
	 */
	private int loadingLineColor = Color.rgb(200, 192, 192);

	/**
	 * 用于绘制tile的矩形区域
	 */
	private Rect tileRect = new Rect();
	
	/***/
	private int point=0;
	/**
	 * 构造函数
	 * 
	 * @param tileProviderID 标识使用哪个provider提供瓦片数据
	 * @param handler 处理tile的请求
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
	 * 获得maptile请求的handler
	 * 
	 * @return maptile请求的handler
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
			
			Toast toast = Toast.makeText(super.context, "此处为:（"
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
		// 获得待绘制的区域
		Rect screenRect = mapView.getScreenRect(null);
		/* 调试信息 */
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
		// 当前屏幕需要的瓦片数量
		final int numNeeded = (lowerRight.y - upperLeft.y + 1)
				* (lowerRight.x - upperLeft.x + 1);
		// 确保缓存区足够大
		controller.ensureCapacity(numNeeded);
		Log.i("ensureCapacity", String.valueOf(numNeeded));
		// 调试信息
/*		int offsetX = (TileSystem.TILE_WIDTH_SIZE - (TileSystem.MapWidthSize(zoomLevel))
				% TileSystem.TILE_WIDTH_SIZE);
*/		
		int offsetX = 0;
		int offsetY = (TileSystem.TILE_HEIGHT_SIZE - (TileSystem.MapHeightSize(zoomLevel))
				% TileSystem.TILE_HEIGHT_SIZE);
		long allStartTime = System.currentTimeMillis();
		// 提取并绘制瓦片
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

				// 绘制瓦片

				if (currentMapTile != null) {
					int rectX = x * TileSystem.TILE_WIDTH_SIZE;
					int rectY = y * TileSystem.TILE_HEIGHT_SIZE ;
					rectY = y * TileSystem.TILE_HEIGHT_SIZE - offsetY;
					rectX = x * TileSystem.TILE_WIDTH_SIZE - offsetX;
				
						tileRect.set(rectX, rectY, rectX
								+ TileSystem.TILE_WIDTH_SIZE,rectY
								+ TileSystem.TILE_HEIGHT_SIZE);
					
					/* 调试信息 */

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
	 * 获取背景颜色
	 * 
	 * @return 背景颜色
	 */
	public int getLoadingBackgroundColor() {
		return loadingBackgroundColor;
	}

	/**
	 * 当等待瓦片加载时 设置用于绘制背景的颜色
	 * 
	 * @param pLoadingBackgroundColor 使用的颜色. 如果值是 {@link Color.TRANSPARENT}
	 *            就不会加载瓦片
	 */
	public void setLoadingBackgroundColor(final int pLoadingBackgroundColor) {
		if (loadingBackgroundColor != pLoadingBackgroundColor) {
			loadingBackgroundColor = pLoadingBackgroundColor;
			clearDefaultTile();
		}
	}

	/**
	 * 获得绘制线条的颜色
	 * 
	 * @return 线条的颜色
	 */
	public int getLoadingLineColor() {
		return loadingLineColor;
	}

	/**
	 * 设置绘制线条时的颜色
	 * 
	 * @param pLoadingLineColor 线颜色
	 */
	public void setLoadingLineColor(final int pLoadingLineColor) {
		if (loadingLineColor != pLoadingLineColor) {
			loadingLineColor = pLoadingLineColor;
			clearDefaultTile();
		}
	}

	/**
	 * 获得默认的tile
	 * 
	 * @return 自己绘制的默认的drawable对象
	 */
	private Drawable getDefaultTile() {
		if (defaultTile == null && loadingBackgroundColor != Color.TRANSPARENT) {
			try {
				// 获得瓦片的宽、高，并建一个相同大小的位图
				final int tileWSize = TileSystem.TILE_WIDTH_SIZE;
				final int tileHSize = TileSystem.TILE_HEIGHT_SIZE;
				final Bitmap bitmap = Bitmap.createBitmap(tileWSize, tileHSize,
						Bitmap.Config.ARGB_8888);
				final Canvas canvas = new Canvas(bitmap);
				// 设置画笔和画布
				final Paint paint = new Paint();
				canvas.drawColor(loadingBackgroundColor);
				paint.setColor(loadingLineColor);
				paint.setStrokeWidth(0);
				// 开始进行绘制16*16的小方格
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
	 * 清空默认的tile
	 */
	private void clearDefaultTile() {
		final BitmapDrawable bitmapDrawable = defaultTile;
		defaultTile = null;
		if (bitmapDrawable != null) {
			bitmapDrawable.getBitmap().recycle();
		}
	}
}
