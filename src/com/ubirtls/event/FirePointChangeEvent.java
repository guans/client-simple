package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

public class FirePointChangeEvent extends EventObject{
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ��FilePointChangeEvent�¼�����ʱ�ᴫ��һ������ֵ
	 */
	public final TwoDCoordinate result;
	public FirePointChangeEvent(TwoDCoordinate coordinate) {
		super(coordinate);
		// TODO Auto-generated constructor stub
		result=coordinate;
	}

}
