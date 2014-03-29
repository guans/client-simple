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
	 * ��ͼ���ǲ� ������TilesOverlay���� VectorOverlay����
	 */
	private Overlay backgroundOverlay;
	/**
	 * �������Overlay���ڻ���ʱ���Ȼ��Ƶ�Overlayλ���²㡣
	 */
	private CopyOnWriteArrayList<Overlay> overlayList;

	/**
	 * ���캯��
	 * 
	 * @param backgroundOverlay
	 */
	public OverlayManager(Overlay backgroundOverlay) {
		overlayList = new CopyOnWriteArrayList<Overlay>();
		this.backgroundOverlay = backgroundOverlay;
	}

	/**
	 * �����������Overlay
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
	 * ���overlay�ĸ���
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
	 *ɾ��һ��overlay
	 */
	public Overlay remove(final int index) {
		return overlayList.remove(index);

	}

	/**
	 * ����һ���µ�overlay
	 */
	public Overlay set(final int index, final Overlay element) {
		return overlayList.set(index, element);
	}

	/**
	 * ���ñ�����ͼOverlay
	 * 
	 * @param tilesOverlay
	 */
	public void setBackgroundOverlay(final Overlay tilesOverlay) {
		this.backgroundOverlay = tilesOverlay;
	}

	/**
	 * ��ñ�����ͼoverlay
	 * 
	 * @return
	 */
	public Overlay getBackGroundOverlay() {
		return backgroundOverlay;
	}

	/**
	 * ���λ��Ƹ���overlay���Ȼ��Ƶ������²�
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
	 * ��overlay������ӦDetach�¼�
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
	 * ��overlay������Ӧ�������¼�
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
	 * ��overlay������Ӧ�������¼�
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
	 * ��overlay������Ӧ�����¼�
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
	 * ��overlay������Ӧ˫���¼�
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
	 * �����¼�
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
