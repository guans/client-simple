package com.ubirtls.event;

/**
 * ��Ϣ�����ӿ� ����������Ϣʱ��Ҫ�õ���һЩ��������������ID ��Ϣ��ӦID��
 * 
 * @author �����
 * @version 1.0
 */
public interface MessageConstants {
	/** ��¼�ɹ���Ӧ */
	public static final String LOGIN_SUCCESS = "0000";

	/** ��¼���������Ӧ */
	public static final String PASSWORD_ERROR = "0001";

	/** ��¼����֪��������Ӧ */
	public static final String LOGIN_USERNAME_ERROR = "0002";

	/** ע��ɹ���Ӧ */
	public static final String REGISTER_SUCCESS = "0003";

	/** ע��ʧ����Ӧ */
	public static final String REGISTER_FAIL = "0004";

	/** �޸��û���Ϣ�ɹ���Ӧ */
	public static final String MODIFY_INFO_SUCCESS = "0009";

	/** �޸��û���Ϣʧ����Ӧ */
	public static final String MODIFY_INFO_FAIL = "0010";

	/** receiver ID */

/*	*//** ��������ID *//*
	public static final String SERVER_ENGINE_ID = "server_engine";

	*//** ��λ����ID *//*
	public static final String LOCATION_ENGINE_ID = "location_engine";*/

	/** ��Ϣ�����ʶ */

	/** ��¼���� */
	public static final String LOGIN_CMD = "0101";

	/** ע������ */
	public static final String REGISTER_CMD = "0102";
	/**ע������*/
	public static final String LOGOUT_CMD = "0104";
	/** ��λ���� */
	public static final String LOCATION_CMD = "0201";

	/** ��ͼ������Ϣ�������� */
	public static final String MAPINFO_CMD = "0310";

	/** �˲�ѡ������ */
	public static final String FILTER_CMD = "0109";

	/** ��λ������� */
	public static final String LOCATION_RESULT_CMD = "0202";

	/** ��ͼ������Ϣ������� */
	public static final String MAPINFO_RESULT_CMD = "0314";

	/** �ָ���SP */
	public static final String SP = "|";

	/** ������CLRF */
	public static final String CLRF = "\r\n";
	
	/**����ָ���*/
	public static final String SP1 = ":";

}
