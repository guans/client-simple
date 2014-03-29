package com.ubirtls.event;

import java.util.EventObject;

/**
 * ������EventObject������Ϣ�м�������޸�����Ľ��ʱ�ᴥ�����¼���
 * 
 * @author �����
 * @version 1.0
 */
public class ChangePasswordEvent extends EventObject {
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * �޸�����Ľ�������޸������¼�����ʱ���᷵��һ�������޸ĵĽ����
	 */
	public final boolean isSuccess;

	/**
	 * ���캯���� ����resultΪ�����޸ĵĽ��
	 * 
	 * @param result �Ƿ�ɹ�
	 */
	public ChangePasswordEvent(boolean result) {
		super(result);
		this.isSuccess = result;
	}
}
