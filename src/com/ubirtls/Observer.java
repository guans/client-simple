package com.ubirtls;

import coordinate.TwoDCoordinate;

/**
 * �۲��߽ӿڣ��۲�Overlay�������ͼ�仯��
 * 
 * @author �����
 * @version 1.0
 */
public interface Observer {
	/**
	 * ֪ͨ��ͼ��Ч����Ҫ���»���
	 */
	public void letInvalidate();

	/**
	 * ֪ͨ��ͼ����ĳ������
	 * 
	 * @param coordinate ��ͼ���ٸ�����
	 */
	public void notifyFollow(TwoDCoordinate coordinate);

}
