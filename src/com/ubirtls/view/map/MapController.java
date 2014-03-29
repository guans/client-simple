package com.ubirtls.view.map;

import android.graphics.Point;
import android.util.Log;
import coordinate.TwoDCoordinate;

/**
 * 控制地图的缩放、地图的移动
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class MapController implements MapViewConstants {
	/**
	 * MapView 对象
	 */
	private MapView mapView;

	/**
	 * 构造函数
	 * 
	 * @param mapView MapView对象
	 */
	public MapController(final MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * 启动地图向coordinate点移动
	 * 
	 * @param coordinate TwoDCoordinate对象 地图将移动到该点
	 */
	public void animateTo(final TwoDCoordinate coordinate) {
		final int x = mapView.getScrollX();
		final int y = mapView.getScrollY();
		final Point p = TileSystem.MapXYToMapPixels(coordinate, this.mapView
				.getZoomLevel());
		if (mapView.getAnimation() == null || mapView.getAnimation().hasEnded()) {
			mapView.getScroller().startScroll(x, y,
					p.x - x - mapView.getWidth() / 2,
					p.y - y - mapView.getHeight() / 2,
					ANIMATION_DURATION_DEFAULT);
			// 地图重绘
			mapView.postInvalidate();
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void scrollBy(final int x, final int y) {
		this.mapView.scrollBy(x, y);
	}

	/**
	 * 设置地图到指定的中心点 不会有动画启动
	 */
	public void setMapCenter(final TwoDCoordinate coordinate) {
		final Point p = TileSystem.MapXYToMapPixels(coordinate, this.mapView
				.getZoomLevel());
		this.mapView.scrollTo(p.x, p.y);
	}

	/**
	 * 设置zoom的缩放级别 直接调用mapView的函数实现
	 * 
	 * @param zoomlevel 缩放级别
	 * @return 缩放后的缩放级别
	 */
	public int setZoom(final int zoomlevel) {
		return mapView.setZoomLevel(zoomlevel);
	}

	/**
	 * 放大一级
	 * 
	 * @return 放大成功返回true 否则返回false
	 */
	public boolean zoomIn() {
		return mapView.zoomIn();
	}

	/**
	 * 先将地图中心点设为地图XY坐标coordinate 再放大一级
	 * 
	 * @param coordinate 设置的地图中心点
	 * @return 放大成功 返回true 否则返回false
	 */
	public boolean zoomInFixing(final TwoDCoordinate coordinate) {
		return mapView.zoomInFixing(coordinate);
	}

	/**
	 * 先将地图中心点设为像素坐标(pixelX,pixelY) 再放大一级
	 * 
	 * @param pixelX 地图像素X坐标
	 * @param pixelY 地图像素Y坐标
	 * @return 放大成功 返回true 否则返回false
	 */
	public boolean zoomInFixing(final int pixelX, final int pixelY) {
		return mapView.zoomInFixing(pixelX, pixelX);
	}

	/**
	 * 缩小一级
	 * 
	 * @return 缩小成功返回true 否则返回false
	 */
	public boolean zoomOut() {
		return mapView.zoomOut();
	}

	/**
	 * 先将地图中心点设为地图XY坐标coordinate 再缩小一级 实际并未使用
	 * 
	 * @param coordinate 设置的地图中心点
	 * @return 缩小成功 返回true 否则返回false
	 */
	public boolean zoomOutFixing(final TwoDCoordinate point) {
		return mapView.zoomOutFixing(point);
	}
}
