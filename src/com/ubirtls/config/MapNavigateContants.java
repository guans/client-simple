package com.ubirtls.config;

import coordinate.TwoDCoordinate;

public class MapNavigateContants {
	/**
	 * ��ͼ�����з��������صĵ�ͼ�������
	 */
	public static String POINT_NAME=""; 
	/**
	 * �ӷ��������صĶ�Ӧ���Ƶ��������Ϣ
	 */
	public static TwoDCoordinate coordinate;
	/**
	 * ��ͼ������Ĭ�ϵĵ������
	 */
	public static String MY_LOCATION="�ҵ�λ��";
	/**�����е�ͼѡ��ʱ��¼���������*/
	public static String START_NAME="";
	/**��ѯ�õ���ƥ��������Ŀ*/
	public static String[] QUERY_NAME=null;
	/**��ѯ�����Ӧ������*/
	public static TwoDCoordinate[] QUERY_COORDINATES;
	/**�жϲ�ѯ����Ƿ�Ϊ��*/
	public static boolean ISEMPTY=true;
	/**�жϵ�ǰ״̬Ϊ����״̬*/
	public static boolean ISEMERGENCY=false;
	/**�жϵ�ͼ���������ͣ���Ϊ1ʱΪ��������Ϊ��㣬��ΪΪ2ʱ��ѯΪ��㣬��Ϊ3ʱ��ѯ�յ�*/
	public static int NAVIGATE_TYPE=0;
	
	/**
	 * ��ͼ�����б���������
	 */
	/**�ӵ�ͼ��ѡ����㡢�յ�*/
	public static boolean MAP_GETSTART_POINT=false;
	public static boolean MAP_GETEND_POINT=false;
	/**��¼��㡢�յ������*/
	public static String STARTPOINT_NAME="";
	public static String ENDPOINT_NAME="";
	/**
	 * ��ѯ����ڵ�ͼ�������
	 */
	public static boolean QUERYRESULT_SHOW=false;
	public static TwoDCoordinate QUERYRESULT;
	
	/**
	 * ��Ƶ��λϵͳ�ṩ����ʼ��
	 */
	public static TwoDCoordinate URADIOLOCATION;
	/**
	 * 
	 */
	public static String QUERY_TYPE="";
}
