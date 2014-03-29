package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

/**
 * ������EventObject������Ϣ�м�����ض�λ�����Ϣʱ�ᴥ�����¼���
 * 
 * @author �����
 * @version 1.0
 */
public class LocationResultEvent extends EventObject {

	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ������ĳ�Ա��������LocationResultEvent�¼�����ʱ���ᴫ��һ���������
	 */
	public final TwoDCoordinate result;

	/**
	 * ���캯��
	 * 
	 * @param coordinate ����
	 */
	public LocationResultEvent(TwoDCoordinate coordinate) {
		super(coordinate);
		result = coordinate;
	}
}
