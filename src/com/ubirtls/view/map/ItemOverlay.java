package com.ubirtls.view.map;

import java.util.ArrayList;

import com.ubirtls.Controller;
import com.ubirtls.ItemListener;
import com.ubirtls.R;
import com.ubirtls.util.OverlayItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

public class ItemOverlay extends Overlay implements ItemListener {

	/** Ĭ�ϵ�Item drawable */
	protected Bitmap defaultMarker;
	/** Item�б� */
	private final ArrayList<OverlayItem> internalItemList;
	/** item��ʱ��Ļ���� */
	private Point curScreenCoords = new Point();
	protected final Paint paint = new Paint();
	protected final PointF PERSON_HOTSPOT;

	/**
	 * ���캯��
	 * 
	 * @param context ������
	 * @param defaultMarker drawable����
	 */
	public ItemOverlay(Context context, final Bitmap defaultMarker) {
		super(context);
		if (defaultMarker == null) {
			this.defaultMarker = BitmapFactory.decodeResource(context
					.getResources(), R.drawable.balloon);
		} else
			this.defaultMarker = defaultMarker;
		internalItemList = new ArrayList<OverlayItem>();
		PERSON_HOTSPOT = new PointF(24.0f * scale + 0.5f, 39.0f * scale + 0.5f);
		/*��POI Item�仯���м����������µ�POI*/
		Controller.getInstance().addItemListener(this);

	}

	/**
	 * ���item������
	 * 
	 * @return item ��size
	 */
	public int getItemSize() {
		return internalItemList.size();
	}

	/**
	 * ���item
	 * 
	 * @param position item����
	 * @return position����item
	 */
	public OverlayItem getItem(int position) {
		return internalItemList.get(position);
	}

	/**
	 * ���OverlayItem
	 * 
	 * @param item OverlayItem����
	 */
	public void addItem(OverlayItem item) {
		/*item�в�������itemʱ����ӽ�ȥ*/
		if (!internalItemList.contains(item))
			internalItemList.add(item);
	}

	/**
	 * ɾ��һ��item
	 * 
	 * @param item ɾ����item
	 */
	public void removeItem(OverlayItem item) {
		internalItemList.remove(item);
	}

	/**
	 * �������Item��
	 */
	public void clearItem() {
		internalItemList.clear();
	}

	@Override
	protected void draw(Canvas c, MapView mapView, boolean shadow) {

		if (shadow) {
			return;
		}

		final int size = this.internalItemList.size() - 1;

		/*
		 * Draw in backward cycle, so the items with the least index are on the
		 * front.
		 */
		for (int i = size; i >= 0; i--) {
			final OverlayItem item = getItem(i);
			curScreenCoords = TileSystem.MapXYToMapPixels(item.getCoordinate(),
					mapView.getZoomLevel());
			/* ��ʼ����icon */
			if (item.getMarker() == null)
				item.setMarker(defaultMarker);

			c.drawBitmap(item.getMarker(), curScreenCoords.x - 25,
					curScreenCoords.y - 60, paint);
		}
	}

	@Override
	public void addMarkerItem(OverlayItem item) {
		// TODO Auto-generated method stub

		this.addItem(item);
	}
}
