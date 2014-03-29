package com.ubirtls.event;

import java.util.EventObject;

/**
 * ������EventObject������Ϣ�м������ע������Ϣʱ�ᴥ�����¼���
 * 
 * @author �����
 * @version 1.0
 */
public class RegisterEvent extends EventObject {
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ע��Ľ������ע���¼�����ʱ���᷵��һ��ע����(ע���Ƿ�ɹ�)
	 */
	public final boolean isSuccess;

	/**
	 * ���캯���� ����resultΪע��Ľ����
	 * 
	 * @param result ע��Ľ��
	 */
	public RegisterEvent(boolean result) {
		// TODO Auto-generated constructor stub
		super(result);
		this.isSuccess = result;
	}
}
