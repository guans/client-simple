package com.ubirtls.event;

import java.util.EventObject;

/**
 * 派生于EventObject。当消息中间件传回注册结果消息时会触发该事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class RegisterEvent extends EventObject {
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 注册的结果，当注册事件发生时，会返回一个注册结果(注册是否成功)
	 */
	public final boolean isSuccess;

	/**
	 * 构造函数， 参数result为注册的结果。
	 * 
	 * @param result 注册的结果
	 */
	public RegisterEvent(boolean result) {
		// TODO Auto-generated constructor stub
		super(result);
		this.isSuccess = result;
	}
}
