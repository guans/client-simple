package com.ubirtls.event;

import java.util.EventObject;

public class QueryEvent extends EventObject{
	/**
	 * �̳�EventObject����Զ���Ӹñ���
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ��ѯ����¼�
	 */
	public static Query_Result result;
	/**
	 * 
	 * @param source
	 */
	public QueryEvent(Query_Result result) {
		super(result);
		// TODO Auto-generated constructor stub
		this.result=result;
	}
}
