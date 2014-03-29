package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

/**
 * 派生于EventObject。当消息中间件传回定位结果消息时会触发该事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class LocationResultEvent extends EventObject {

	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 坐标类的成员变量，当LocationResultEvent事件发生时，会传入一个坐标变量
	 */
	public final TwoDCoordinate result;

	/**
	 * 构造函数
	 * 
	 * @param coordinate 坐标
	 */
	public LocationResultEvent(TwoDCoordinate coordinate) {
		super(coordinate);
		result = coordinate;
	}
}
