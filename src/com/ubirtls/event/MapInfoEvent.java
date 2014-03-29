/**
 * 
 */
package com.ubirtls.event;

import java.util.EventObject;

/**
 * ������EventObject������Ϣ�м���ص�ͼ������Ϣ��Ϣʱ�ᴥ�����¼���
 * 
 * @author �����
 * @version 1.0
 */
public class MapInfoEvent extends EventObject {

	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��ͼ������Ϣ������
	 */
	public MapInfo result;

	/**
	 * ���캯���� ����resultΪ��ͼ������Ϣ��������
	 * 
	 * @param result ��ͼ������Ϣ������
	 */
	public MapInfoEvent(MapInfo result) {
		super(result);
		this.result = result;
	}
}
