package com.ubirtls.event;

/**
 * ��¼��֤������ ��{@link AuthenticationEvent}ʹ��
 * 
 * @author �����
 * @version 1.0
 */
public class AuthenticationResult {
	/**
	 * ��¼��֤�Ƿ�ɹ�
	 */
	public final boolean isSuccess;

	/**
	 * �û����Ƿ���ȷ
	 */
	public final boolean isUserNameCorrect;

	/**
	 * �����Ƿ���ȷ
	 */
	public final boolean isPasswordCorrect;

	/**
	 * ���캯��
	 * 
	 * @param success ��֤�Ƿ�ɹ�
	 * @param username �û�����ȷ
	 * @param password ������ȷ
	 */
	public AuthenticationResult(boolean success, boolean username,
			boolean password) {
		this.isSuccess = success;
		this.isUserNameCorrect = username;
		this.isPasswordCorrect = password;
	}
}
