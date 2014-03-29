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

	/** 默认的Item drawable */
	protected Bitmap defaultMarker;
	/** Item列表 */
	private final ArrayList<OverlayItem> internalItemList;
	/** item临时屏幕坐标 */
	private Point curScreenCoords = new Point();
	protected final Paint paint = new Paint();
	protected final PointF PERSON_HOTSPOT;

	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param defaultMarker drawable对象
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
		/*对POI Item变化进行监听，如获得新的POI*/
		Controller.getInstance().addItemListener(this);

	}

	/**
	 * 获得item的数量
	 * 
	 * @return item 的size
	 */
	public int getItemSize() {
		return internalItemList.size();
	}

	/**
	 * 获得item
	 * 
	 * @param position item索引
	 * @return position处的item
	 */
	public OverlayItem getItem(int position) {
		return internalItemList.get(position);
	}

	/**
	 * 添加OverlayItem
	 * 
	 * @param item OverlayItem对象
	 */
	public void addItem(OverlayItem item) {
		/*item中不包括该item时，添加进去*/
		if (!internalItemList.contains(item))
			internalItemList.add(item);
	}

	/**
	 * 删除一个item
	 * 
	 * @param item 删除的item
	 */
	public void removeItem(OverlayItem item) {
		internalItemList.remove(item);
	}

	/**
	 * 清除所有Item项
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
			/* 开始绘制icon */
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
