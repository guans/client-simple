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
	 * ���������ͼʱʹ��
	 */
	private final static Rect rect = new Rect();
	/**
	 * �Ƿ�ʹ�ܸ�overlay��Ϊtrue��ʾʹ�ܣ������ֹʹ��
	 */
	private boolean enabled = true;
	/**
	 * ��ǰ��ʾ�����ܶ�
	 */
	protected final float scale;
	protected final Context context;

	/**
	 * ���캯��
	 * 
	 * @param ctx Context����
	 */
	public Overlay(final Context context) {
		this.context = context;
		scale = context.getResources().getDisplayMetrics().density;
	}

	/**
	 * ���ø�overlay�Ƿ�ʹ��
	 * 
	 * @param pEnabled
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * �жϸ�overlay�Ƿ�ʹ��
	 * 
	 * @return
	 * 
	 */
	public boolean isEnabled() {
		return this.enabled;

	}

	/**
	 * ��������overlay
	 * 
	 * @param c
	 * @param mapView
	 * @param shadow
	 */
	protected abstract void draw(final Canvas c, final MapView mapView,
			final boolean shadow);
	
	/**
	 * ��overlay��ӦDetach�¼�
	 * 
	 * @param pMapView
	 */
	public void onDetach(final MapView pMapView) {

	}

	/**
	 * ��overlay��Ӧ�������¼�
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
	 * ��overlay��Ӧ�������¼�
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
	 * ��overlay��Ӧ�����¼�
	 * 
	 * @param event
	 * @param pMapView
	 * @return
	 */
	public boolean onTouchEvent(final MotionEvent event, final MapView pMapView) {
		return false;
	}

	/**
	 * ��overlay��Ӧ˫���¼�
	 * 
	 * @param e
	 * @param pMapView
	 * @return
	 */
	public boolean onDoubleTap(final MotionEvent e, final MapView pMapView) {
		return false;

	}

	/**
	 * ��Ӧdown�¼�
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onDown(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * ��Ӧ�����¼�
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
	 * ��Ӧ�����¼�
	 * 
	 * @param e
	 * @param mapView
	 * @return
	 */
	public boolean onLongPress(final MotionEvent e, final MapView mapView,TwoDCoordinate coor,int flag) {
		return false;
	}

	/**
	 * ��Ӧ�����¼�
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
	 * ��x��y���ص㴦����һ��drawable����ļ򵥷���
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
