package com.ubirtls.event;

import coordinate.TwoDCoordinate;

public class Query_Result {
	/**
	 * ��ѯʱ��Ӧ�Ľڵ������
	 */
	public static String[] Point_Name;
	/**
	 * ��ѯ�õ�����ƥ��Ľڵ���Ŀ
	 */
	public static int point_Num;
	/**
	 * ƥ��Ľڵ������
	 */
	public static TwoDCoordinate[] coordinate; 
	/**
	 * ��ѯ�����ͣ���Ϊ������õ���ѯ�����Ƶõ���ѯ
	 */
	public static String query_type;
	/**
	 * ��ѯ������캯��
	 * @param num ��ѯ�õ��Ľ����Ŀ
	 * @param point_name ��ѯ���Ӧ�Ľ��������
	 */
	public Query_Result(int num,String []point_name,TwoDCoordinate[] coordinates)
	{
		this.point_Num=num;
		this.Point_Name=new String[num];
		this.coordinate=new TwoDCoordinate[num];
		this.coordinate=coordinates;
		this.Point_Name=point_name;
	}
}
