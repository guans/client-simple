package com.ubirtls;

import com.ubirtls.util.OverlayItem;

/**
 * 监听标识Item，包括Item的添加，删除......
 * @author 胡旭科
 * @version 1.0
 *
 */
public interface ItemListener {
	/**
	 * 添加新的标签Item
	 * @param item OverlayItem对象，添加的Item对象
	 */
	public abstract void addMarkerItem(OverlayItem item);
}
