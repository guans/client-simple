package com.ubirtls.event;

import java.util.EventObject;

/**
 * ������EventObject������Ϣ�м���������֤�����Ϣʱ�ᴥ�����¼���
 * 
 * @author �����
 * @version 1.0
 */
public class AuthenticationEvent extends EventObject {
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ��¼��֤�Ľ��������¼�¼�����ʱ���᷵��һ����¼�����
	 */
	public final AuthenticationResult result;

	/**
	 * ���캯��
	 * 
	 * @param result {@link AuthenticationResult} ����
	 */
	public AuthenticationEvent(AuthenticationResult result) {
		super(result);
		this.result = result;
	}
}
