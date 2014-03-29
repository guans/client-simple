package com.ubirtls.config;

import coordinate.TwoDCoordinate;

public class MapNavigateContants {
	/**
	 * 地图导航中服务器返回的地图点的名称
	 */
	public static String POINT_NAME=""; 
	/**
	 * 从服务器返回的对应名称点的坐标信息
	 */
	public static TwoDCoordinate coordinate;
	/**
	 * 地图导航中默认的导航起点
	 */
	public static String MY_LOCATION="我的位置";
	/**导航中地图选点时记录的起点坐标*/
	public static String START_NAME="";
	/**查询得到的匹配结果的数目*/
	public static String[] QUERY_NAME=null;
	/**查询结果对应的坐标*/
	public static TwoDCoordinate[] QUERY_COORDINATES;
	/**判断查询结果是否为空*/
	public static boolean ISEMPTY=true;
	/**判断当前状态为紧急状态*/
	public static boolean ISEMERGENCY=false;
	/**判断地图导航的类型，当为1时为以自身作为起点，当为为2时查询为起点，当为3时查询终点*/
	public static int NAVIGATE_TYPE=0;
	
	/**
	 * 地图导航中变量的设置
	 */
	/**从地图上选择起点、终点*/
	public static boolean MAP_GETSTART_POINT=false;
	public static boolean MAP_GETEND_POINT=false;
	/**记录起点、终点的名称*/
	public static String STARTPOINT_NAME="";
	public static String ENDPOINT_NAME="";
	/**
	 * 查询结果在地图上做标记
	 */
	public static boolean QUERYRESULT_SHOW=false;
	public static TwoDCoordinate QUERYRESULT;
	
	/**
	 * 优频定位系统提供的起始点
	 */
	public static TwoDCoordinate URADIOLOCATION;
	/**
	 * 
	 */
	public static String QUERY_TYPE="";
}
