package com.ubirtls.event;

import java.util.EventObject;

public class NavigateEvent extends EventObject{
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * �����������
	 */
	public static NavigateResult result;
	/**
	 * ������Ĺ��캯�� 
	 * @param result ����·�߽��
	 */
	public NavigateEvent(NavigateResult result) {
		super(result);
		// TODO Auto-generated constructor stub
		this.result=result;
	}
}
