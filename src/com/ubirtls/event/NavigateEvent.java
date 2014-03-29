package com.ubirtls.event;

import java.util.EventObject;

public class NavigateEvent extends EventObject{
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 导航结果对象
	 */
	public static NavigateResult result;
	/**
	 * 导航类的构造函数 
	 * @param result 导航路线结果
	 */
	public NavigateEvent(NavigateResult result) {
		super(result);
		// TODO Auto-generated constructor stub
		this.result=result;
	}
}
