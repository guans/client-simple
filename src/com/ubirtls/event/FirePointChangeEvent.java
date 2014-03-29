package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

public class FirePointChangeEvent extends EventObject{
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 当FilePointChangeEvent事件发生时会传入一个坐标值
	 */
	public final TwoDCoordinate result;
	public FirePointChangeEvent(TwoDCoordinate coordinate) {
		super(coordinate);
		// TODO Auto-generated constructor stub
		result=coordinate;
	}

}
