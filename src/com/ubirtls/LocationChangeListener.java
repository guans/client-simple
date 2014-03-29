package com.ubirtls;

import coordinate.TwoDCoordinate;

/**
 * 位置变化的监听者。controller从模型得到用户新的位置后，再通知所有的这些监听者。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface LocationChangeListener {
	/**
	 * 位置变化，返回新的位置
	 * 
	 * @param coor 返回的坐标
	 */
	public abstract void locationChanged(TwoDCoordinate coor);
	/**
	 * 火灾点位置变化
	 */
	public abstract void firepointchanged(TwoDCoordinate coordinate);
}
