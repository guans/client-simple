package com.ubirtls.event;

import java.util.EventObject;

/**
 * 派生于EventObject。当消息中间件回身份认证结果消息时会触发该事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class AuthenticationEvent extends EventObject {
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 登录认证的结果，当登录事件发生时，会返回一个登录结果。
	 */
	public final AuthenticationResult result;

	/**
	 * 构造函数
	 * 
	 * @param result {@link AuthenticationResult} 对象
	 */
	public AuthenticationEvent(AuthenticationResult result) {
		super(result);
		this.result = result;
	}
}
