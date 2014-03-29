package com.ubirtls.event;

/**
 * 登录认证请求结果 被{@link AuthenticationEvent}使用
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class AuthenticationResult {
	/**
	 * 登录认证是否成功
	 */
	public final boolean isSuccess;

	/**
	 * 用户名是否正确
	 */
	public final boolean isUserNameCorrect;

	/**
	 * 密码是否正确
	 */
	public final boolean isPasswordCorrect;

	/**
	 * 构造函数
	 * 
	 * @param success 认证是否成功
	 * @param username 用户名正确
	 * @param password 密码正确
	 */
	public AuthenticationResult(boolean success, boolean username,
			boolean password) {
		this.isSuccess = success;
		this.isUserNameCorrect = username;
		this.isPasswordCorrect = password;
	}
}
