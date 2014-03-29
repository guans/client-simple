package com.ubirtls.view.map;

import coordinate.TwoDCoordinate;

import android.graphics.Point;
import android.graphics.Rect;
/**
 * 坐标映射，实现屏幕像素坐标和地图XY坐标以及地图像素坐标之间的转换
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class Projection {
	/**
	 * mapview变量 用于获得mapView的level以及当前scroller的Rect
	 */
	private MapView mapView;

	/**
	 * 构造函数
	 * 
	 * @param view {@link MapView}对象
	 */
	public Projection(MapView view) {
		this.mapView = view;
	}

	/**
	 * 获得屏幕矩形
	 * 
	 * @param reuse 可以重复使用的Rect对象 如果不为空 返回的就是 该reuse 否则 new一个Rect再返回
	 * @return Rect对象 屏幕矩形
	 */
	public Rect getScreenRect(Rect reuse) {
		return mapView.getScreenRect(reuse);
	}

	/**
	 * 将地图XY坐标转换成屏幕像素坐标
	 * 
	 * @param coordinate 地图x y坐标
	 * @param level 地图当前的缩放细节
	 * @return 转换后的屏幕像素做坐
	 */
	public Point toScreenPixels(TwoDCoordinate coordinate, int level) {
		Point screen = TileSystem.MapXYToMapPixels(coordinate, level);
		screen.offset(-mapView.getScrollX(), -mapView.getScrollY());
		return screen;
	}

	/**
	 * 将屏幕像素坐标转换成地图XY坐标
	 * 
	 * @param x
	 * @param y
	 * @param level
	 * @return 转换成后的地图XY坐标
	 */
	public TwoDCoordinate fromScreenPixels(float x, float y, int level) {
		// 获得屏幕矩形
		Rect rect = this.getScreenRect(null);
		return TileSystem.MapPixelsToMapXY(rect.left + (int) x, rect.top
				+ (int) y, level);
	}

	public Point toBigScreenPixels(TwoDCoordinate coordinate, int level) {
		Point screen = TileSystem.MapXYToMapPixels(coordinate, level);
		screen.offset(mapView.getScrollX(), mapView.getScrollY());
		return screen;
	}
}
