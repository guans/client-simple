package com.ubirtls.event;

import coordinate.TwoDCoordinate;

public class Query_Result {
	/**
	 * 查询时对应的节点的名称
	 */
	public static String[] Point_Name;
	/**
	 * 查询得到的相匹配的节点数目
	 */
	public static int point_Num;
	/**
	 * 匹配的节点的坐标
	 */
	public static TwoDCoordinate[] coordinate; 
	/**
	 * 查询的类型，分为由坐标得到查询、名称得到查询
	 */
	public static String query_type;
	/**
	 * 查询结果构造函数
	 * @param num 查询得到的结果数目
	 * @param point_name 查询相对应的结果的名称
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
