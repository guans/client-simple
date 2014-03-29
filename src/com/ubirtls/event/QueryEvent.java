package com.ubirtls.event;

import java.util.EventObject;

public class QueryEvent extends EventObject{
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 查询结果事件
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
