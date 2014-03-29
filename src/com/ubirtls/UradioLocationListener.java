package com.ubirtls;

import coordinate.TwoDCoordinate;
/**
 * 从优频定位系统中获取定位的初始值
 * @author UbiLoc
 *
 */
public interface UradioLocationListener {

	public abstract void getUradioLocation(TwoDCoordinate coordinate);
}
