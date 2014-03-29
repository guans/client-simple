package com.ubirtls;

/**
 * 监听用户的账户检查
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface AccountCheckListener {
	/**
	 * 监听身份认证事件
	 * 
	 * @param success 登录是否成功
	 * @param usernameSuccess 用户名检查是否正确
	 * @param passwordSuccess 密码检查是否正确
	 */
	public abstract void loginCheck(boolean success, boolean isUsernameCorrect,
			boolean isPasswordCorrect);

	/**
	 * 监听注册事件
	 * 
	 * @param success 注册是否成功
	 */
	public abstract void registerCheck(boolean success);

	/**
	 * 监听修改密码事件
	 * 
	 * @param success 密码修改是否成功
	 */
	public abstract void changePasswordCheck(boolean success);

}
