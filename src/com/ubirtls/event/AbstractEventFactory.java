package com.ubirtls.event;

import java.util.EventObject;

/**
 * �¼������ĳ����࣬�����õ��˹����������ģʽ
 * 
 * @author �����
 * @version 1.0
 */
public abstract class AbstractEventFactory {
	/**
	 * �����֤�¼���ʶ
	 */
	public static final int AUTHENTICATION_EVENT = 0x01;

	/**
	 * �޸������¼���ʶ
	 */
	public static final int CHANGEPASSWORD_EVENT = 0x02;

	/**
	 * ע���¼���ʶ
	 */
	public static final int REGISTER_EVENT = 0x03;

	/**
	 * ��λ����¼���ʶ
	 */
	public static final int LOCATIONRESULT_EVENT = 0x04;
	/**
	 * ���ֵ����¼���ʶ
	 */
	public static final int FILEPOINT_EVENT=0x06;

	/**
	 * ��ͼ��Ϣ�¼���ʶ
	 */
	public static final int MAPINFO_EVENT = 0x05;
	/**
	 * ҽ����Ϣ�¼�
	 */
	public static final int DOCTOR_EVENT=0x07;
	/**
	 * �����¼�
	 */
	public static final int NAVIGATE_EVENT=0x08;
	/**
	 * ���ѯ�¼�
	 */
	public static final int QUERY_EVENT=0x09;
	/**
	 * ��ȡ��ʼλ���¼�
	 */
	public static final int URADIOLOCATION_EVENT=0x10;
	/**
	 * ���󷽷��������¼�
	 * 
	 * @param event ��ʶ�����ĸ��¼�
	 * @param object Object����,�γ��¼���Ҫ���ݵĲ���
	 * @return EventObject����
	 */
	public abstract EventObject createEvent(int event, Object object);
}
