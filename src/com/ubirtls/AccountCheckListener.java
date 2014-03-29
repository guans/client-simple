package com.ubirtls;

/**
 * �����û����˻����
 * 
 * @author �����
 * @version 1.0
 */
public interface AccountCheckListener {
	/**
	 * ���������֤�¼�
	 * 
	 * @param success ��¼�Ƿ�ɹ�
	 * @param usernameSuccess �û�������Ƿ���ȷ
	 * @param passwordSuccess �������Ƿ���ȷ
	 */
	public abstract void loginCheck(boolean success, boolean isUsernameCorrect,
			boolean isPasswordCorrect);

	/**
	 * ����ע���¼�
	 * 
	 * @param success ע���Ƿ�ɹ�
	 */
	public abstract void registerCheck(boolean success);

	/**
	 * �����޸������¼�
	 * 
	 * @param success �����޸��Ƿ�ɹ�
	 */
	public abstract void changePasswordCheck(boolean success);

}
