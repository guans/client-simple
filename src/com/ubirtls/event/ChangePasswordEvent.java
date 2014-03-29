package com.ubirtls.event;

import java.util.EventObject;

/**
 * 派生于EventObject。当消息中间件传回修改密码的结果时会触发该事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class ChangePasswordEvent extends EventObject {
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 修改密码的结果，当修改密码事件发生时，会返回一个密码修改的结果。
	 */
	public final boolean isSuccess;

	/**
	 * 构造函数， 参数result为密码修改的结果
	 * 
	 * @param result 是否成功
	 */
	public ChangePasswordEvent(boolean result) {
		super(result);
		this.isSuccess = result;
	}
}
