package com.ubirtls.view.map;

import coordinate.TwoDCoordinate;

import android.graphics.Point;
import android.graphics.Rect;
/**
 * ����ӳ�䣬ʵ����Ļ��������͵�ͼXY�����Լ���ͼ��������֮���ת��
 * 
 * @author �����
 * @version 0.1
 */
public class Projection {
	/**
	 * mapview���� ���ڻ��mapView��level�Լ���ǰscroller��Rect
	 */
	private MapView mapView;

	/**
	 * ���캯��
	 * 
	 * @param view {@link MapView}����
	 */
	public Projection(MapView view) {
		this.mapView = view;
	}

	/**
	 * �����Ļ����
	 * 
	 * @param reuse �����ظ�ʹ�õ�Rect���� �����Ϊ�� ���صľ��� ��reuse ���� newһ��Rect�ٷ���
	 * @return Rect���� ��Ļ����
	 */
	public Rect getScreenRect(Rect reuse) {
		return mapView.getScreenRect(reuse);
	}

	/**
	 * ����ͼXY����ת������Ļ��������
	 * 
	 * @param coordinate ��ͼx y����
	 * @param level ��ͼ��ǰ������ϸ��
	 * @return ת�������Ļ��������
	 */
	public Point toScreenPixels(TwoDCoordinate coordinate, int level) {
		Point screen = TileSystem.MapXYToMapPixels(coordinate, level);
		screen.offset(-mapView.getScrollX(), -mapView.getScrollY());
		return screen;
	}

	/**
	 * ����Ļ��������ת���ɵ�ͼXY����
	 * 
	 * @param x
	 * @param y
	 * @param level
	 * @return ת���ɺ�ĵ�ͼXY����
	 */
	public TwoDCoordinate fromScreenPixels(float x, float y, int level) {
		// �����Ļ����
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
