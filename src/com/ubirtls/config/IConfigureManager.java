package com.ubirtls.config;

/**
 * �ӿ� ���ù���
 * 
 * @author �����
 * @version 1.0
 * @deprecated Android��Preference���Ժܷ���Ľ����������
 */
public interface IConfigureManager {
	/**
	 * ���������ļ�����������������
	 * 
	 * @return ���سɹ�����true ���򷵻�false
	 */
	public abstract boolean loadConfigurationFile();

	/**
	 * ���������
	 * 
	 * @return ����ɹ�����true ���򷵻�false
	 */
	public abstract boolean saveConfigurationFile();
}
