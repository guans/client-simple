package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

public class UradioLocationEvent extends EventObject{
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;
	
	public static TwoDCoordinate coordinate;
	
	public UradioLocationEvent(TwoDCoordinate coordinate) {
		super(coordinate);
		// TODO Auto-generated constructor stub
		this.coordinate=coordinate;
	}
}
