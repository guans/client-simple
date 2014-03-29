package com.ubirtls.view.map;

import java.util.AbstractList;
import java.util.concurrent.CopyOnWriteArrayList;

import coordinate.TwoDCoordinate;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class OverlayManager extends AbstractList<Overlay> {
	/**
	 * 地图覆盖层 可以是TilesOverlay或者 VectorOverlay类型
	 */
	private Overlay backgroundOverlay;
	/**
	 * 包含多个Overlay，在绘制时，先绘制的Overlay位于下层。
	 */
	private CopyOnWriteArrayList<Overlay> overlayList;

	/**
	 * 构造函数
	 * 
	 * @param backgroundOverlay
	 */
	public OverlayManager(Overlay backgroundOverlay) {
		overlayList = new CopyOnWriteArrayList<Overlay>();
		this.backgroundOverlay = backgroundOverlay;
	}

	/**
	 * 根据索引获得Overlay
	 * 
	 * @param location
	 * @return
	 */
	@Override
	public Overlay get(int location) {
		// TODO Auto-generated method stub
		return overlayList.get(location);
	}

	/**
	 * 获得overlay的个数
	 */
	public int size() {
		return overlayList.size();
	}

	/**
	 * 
	 */
	public void add(final int index, final Overlay element) {
		overlayList.add(index, element);
	}

	/**
	 *删除一个overlay
	 */
	public Overlay remove(final int index) {
		return overlayList.remove(index);

	}

	/**
	 * 设置一个新的overlay
	 */
	public Overlay set(final int index, final Overlay element) {
		return overlayList.set(index, element);
	}

	/**
	 * 设置背景地图Overlay
	 * 
	 * @param tilesOverlay
	 */
	public void setBackgroundOverlay(final Overlay tilesOverlay) {
		this.backgroundOverlay = tilesOverlay;
	}

	/**
	 * 获得背景地图overlay
	 * 
	 * @return
	 */
	public Overlay getBackGroundOverlay() {
		return backgroundOverlay;
	}

	/**
	 * 依次绘制各个overlay，先绘制的在最下层
	 * 
	 * @param c
	 * @param pMapView
	 */
	public void onDraw(final Canvas c, final MapView mapView) {
		if (backgroundOverlay != null && backgroundOverlay.isEnabled())
			backgroundOverlay.draw(c, mapView, false);

		for (Overlay overlay : overlayList) {
			if (overlay != null && overlay.isEnabled())
				overlay.draw(c, mapView, false);
		}
	}

	/**
	 * 各overlay依次响应Detach事件
	 * 
	 * @param pMapView
	 */
	public void onDetach(final MapView mapView) {
		if (backgroundOverlay != null)
			backgroundOverlay.onDetach(mapView);

		for (Overlay overlay : overlayList) {
			overlay.onDetach(mapView);
		}
	}

	/**
	 * 各overlay依次响应键按下事件
	 * 
	 * @param keyCode
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onKeyDown(final int keyCode, final KeyEvent event,
			final MapView pMapView) {
		if (backgroundOverlay != null)
			if (backgroundOverlay.onKeyDown(keyCode, event, pMapView))
				return true;

		for (Overlay overlay : overlayList) {
			if (overlay != null)
				if (overlay.onKeyDown(keyCode, event, pMapView))
					return true;
		}

		return false;
	}

	/**
	 * 各overlay依次响应键向上事件
	 * 
	 * @param keyCode
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onKeyUp(final int keyCode, final KeyEvent event,
			final MapView mapView) {
		if (backgroundOverlay != null)
			if (backgroundOverlay.onKeyUp(keyCode, event, mapView))
				return true;

		for (Overlay overlay : overlayList) {
			if (overlay != null)
				if (overlay.onKeyUp(keyCode, event, mapView))
					return true;
		}

		return false;
	}

	/**
	 * 各overlay依次响应触摸事件
	 * 
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {
		if (backgroundOverlay != null)
			if (backgroundOverlay.onTouchEvent(event, mapView))
				return true;

		for (Overlay overlay : overlayList) {
			if (overlay != null && overlay.onTouchEvent(event, mapView))
				return true;
		}

		return false;
	}

	/**
	 * 各overlay依次响应双击事件
	 * 
	 * @param e
	 * @param pMapView
	 * @return
	 */
	public boolean onDoubleTap(final MotionEvent e, final MapView pMapView) {
		if (backgroundOverlay != null)
			if (backgroundOverlay.onDoubleTap(e, pMapView))
				return true;

		for (Overlay overlay : overlayList) {
			if (overlay != null && overlay.onDoubleTap(e, pMapView))
				return true;
		}

		return false;
	}

	/**
	 * 长按事件
	 * 
	 * @param pEvent
	 * @param pMapView
	 * @return
	 */
	public boolean onLongPress(final MotionEvent event, final MapView mapView,TwoDCoordinate coor,int flag) 
	{
		
		if (backgroundOverlay != null)
			if (backgroundOverlay.onLongPress(event, mapView,coor,flag))
				return true;

		for (final Overlay overlay : overlayList) {
			if (overlay != null && overlay.onLongPress(event, mapView,coor,flag)) {
				return true;
			}
		}
		
		return false;
	}

}
