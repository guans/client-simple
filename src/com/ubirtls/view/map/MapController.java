package com.ubirtls.view.map;

import android.graphics.Point;
import android.util.Log;
import coordinate.TwoDCoordinate;

/**
 * ���Ƶ�ͼ�����š���ͼ���ƶ�
 * 
 * @author �����
 * @version 0.1
 */
public class MapController implements MapViewConstants {
	/**
	 * MapView ����
	 */
	private MapView mapView;

	/**
	 * ���캯��
	 * 
	 * @param mapView MapView����
	 */
	public MapController(final MapView mapView) {
		this.mapView = mapView;
	}

	/**
	 * ������ͼ��coordinate���ƶ�
	 * 
	 * @param coordinate TwoDCoordinate���� ��ͼ���ƶ����õ�
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
			// ��ͼ�ػ�
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
	 * ���õ�ͼ��ָ�������ĵ� �����ж�������
	 */
	public void setMapCenter(final TwoDCoordinate coordinate) {
		final Point p = TileSystem.MapXYToMapPixels(coordinate, this.mapView
				.getZoomLevel());
		this.mapView.scrollTo(p.x, p.y);
	}

	/**
	 * ����zoom�����ż��� ֱ�ӵ���mapView�ĺ���ʵ��
	 * 
	 * @param zoomlevel ���ż���
	 * @return ���ź�����ż���
	 */
	public int setZoom(final int zoomlevel) {
		return mapView.setZoomLevel(zoomlevel);
	}

	/**
	 * �Ŵ�һ��
	 * 
	 * @return �Ŵ�ɹ�����true ���򷵻�false
	 */
	public boolean zoomIn() {
		return mapView.zoomIn();
	}

	/**
	 * �Ƚ���ͼ���ĵ���Ϊ��ͼXY����coordinate �ٷŴ�һ��
	 * 
	 * @param coordinate ���õĵ�ͼ���ĵ�
	 * @return �Ŵ�ɹ� ����true ���򷵻�false
	 */
	public boolean zoomInFixing(final TwoDCoordinate coordinate) {
		return mapView.zoomInFixing(coordinate);
	}

	/**
	 * �Ƚ���ͼ���ĵ���Ϊ��������(pixelX,pixelY) �ٷŴ�һ��
	 * 
	 * @param pixelX ��ͼ����X����
	 * @param pixelY ��ͼ����Y����
	 * @return �Ŵ�ɹ� ����true ���򷵻�false
	 */
	public boolean zoomInFixing(final int pixelX, final int pixelY) {
		return mapView.zoomInFixing(pixelX, pixelX);
	}

	/**
	 * ��Сһ��
	 * 
	 * @return ��С�ɹ�����true ���򷵻�false
	 */
	public boolean zoomOut() {
		return mapView.zoomOut();
	}

	/**
	 * �Ƚ���ͼ���ĵ���Ϊ��ͼXY����coordinate ����Сһ�� ʵ�ʲ�δʹ��
	 * 
	 * @param coordinate ���õĵ�ͼ���ĵ�
	 * @return ��С�ɹ� ����true ���򷵻�false
	 */
	public boolean zoomOutFixing(final TwoDCoordinate point) {
		return mapView.zoomOutFixing(point);
	}
}
