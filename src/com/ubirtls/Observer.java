package com.ubirtls;

import coordinate.TwoDCoordinate;

/**
 * 观察者接口，观察Overlay引起的视图变化。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface Observer {
	/**
	 * 通知视图无效，需要重新绘制
	 */
	public void letInvalidate();

	/**
	 * 通知视图跟踪某个坐标
	 * 
	 * @param coordinate 地图跟踪该坐标
	 */
	public void notifyFollow(TwoDCoordinate coordinate);

}
