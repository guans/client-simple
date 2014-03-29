package com.ubirtls.view.map;

import coordinate.TwoDCoordinate;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class Overlay {
	/**
	 * 留作后面绘图时使用
	 */
	private final static Rect rect = new Rect();
	/**
	 * 是否使能该overlay，为true表示使能，否则禁止使能
	 */
	private boolean enabled = true;
	/**
	 * 当前显示器的密度
	 */
	protected final float scale;
	protected final Context context;

	/**
	 * 构造函数
	 * 
	 * @param ctx Context对象
	 */
	public Overlay(final Context context) {
		this.context = context;
		scale = context.getResources().getDisplayMetrics().density;
	}

	/**
	 * 设置该overlay是否使能
	 * 
	 * @param pEnabled
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 判断该overlay是否使能
	 * 
	 * @return
	 * 
	 */
	public boolean isEnabled() {
		return this.enabled;

	}

	/**
	 * 绘制自身overlay
	 * 
	 * @param c
	 * @param mapView
	 * @param shadow
	 */
	protected abstract void draw(final Canvas c, final MapView mapView,
			final boolean shadow);
	
	/**
	 * 该overlay响应Detach事件
	 * 
	 * @param pMapView
	 */
	public void onDetach(final MapView pMapView) {

	}

	/**
	 * 该overlay响应键按下事件
	 * 
	 * @param keyCode
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onKeyDown(final int keyCode, final KeyEvent event,
			final MapView pMapView) {
		return false;
	}

	/**
	 * 该overlay响应键向上事件
	 * 
	 * @param keyCode
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onKeyUp(final int keyCode, final KeyEvent event,
			final MapView pMapView) {
		return false;
	}

	/**
	 * 该overlay响应触摸事件
	 * 
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onTouchEvent(final MotionEvent event, final MapView pMapView) {
		return false;
	}

	/**
	 * 该overlay响应双击事件
	 * 
	 * @param e
	 * @param pMapView
	 * @return
	 */
	public boolean onDoubleTap(final MotionEvent e, final MapView pMapView) {
		return false;

	}

	/**
	 * 响应down事件
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onDown(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * 响应滑动事件
	 * 
	 * @param pEvent1
	 * @param pEvent2
	 * @param pVelocityX
	 * @param pVelocityY
	 * @param pMapView
	 * @return
	 */
	public boolean onFling(final MotionEvent pEvent1,
			final MotionEvent pEvent2, final float pVelocityX,
			final float pVelocityY, final MapView pMapView) {
		return false;
	}

	/**
	 * 响应长按事件
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onLongPress(final MotionEvent e, final MapView mapView,TwoDCoordinate coor,int flag) {
		return false;
	}

	/**
	 * 响应滚动事件
	 * 
	 * @param pEvent1
	 * @param pEvent2
	 * @param pDistanceX
	 * @param pDistanceY
	 * @param pMapView
	 * @return
	 */
	public boolean onScroll(final MotionEvent pEvent1,
			final MotionEvent pEvent2, final float pDistanceX,
			final float pDistanceY, final MapView pMapView) {
		return false;
	}

	/**
	 * 在x，y像素点处绘制一个drawable对象的简单方法
	 * 
	 * @param canvas
	 * @param drawable
	 * @param x
	 * @param y
	 * @param shadow
	 */
	protected synchronized static void drawAt(android.graphics.Canvas canvas,
			android.graphics.drawable.Drawable drawable, int x, int y,
			boolean shadow) {
		if (!shadow) {
			drawable.copyBounds(rect);
			drawable.setBounds(rect.left + x, rect.top + y, rect.right + x,
					rect.bottom + y);
			drawable.draw(canvas);
			drawable.setBounds(rect);
		}
	}

}
