package com.ubirtls;

import coordinate.TwoDCoordinate;

/**
 * λ�ñ仯�ļ����ߡ�controller��ģ�͵õ��û��µ�λ�ú���֪ͨ���е���Щ�����ߡ�
 * 
 * @author �����
 * @version 1.0
 */
public interface LocationChangeListener {
	/**
	 * λ�ñ仯�������µ�λ��
	 * 
	 * @param coor ���ص�����
	 */
	public abstract void locationChanged(TwoDCoordinate coor);
	/**
	 * ���ֵ�λ�ñ仯
	 */
	public abstract void firepointchanged(TwoDCoordinate coordinate);
}
