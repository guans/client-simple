package com.ubirtls.event;

import coordinate.TwoDCoordinate;

public class NavigateResult {
	/**
	 * 存储导航路线的节点数目
	 */
	public static int Node_num;
	/**
	 * 存储导航路线中所有的导航节点
	 */
	public static TwoDCoordinate[] coordinate;
	/**
	 * 存储导航结果信息
	 * @param num 导航路线中的节点数目
	 * @param coordinate 导航路线中对应的节点
	 */
	public NavigateResult(int num,TwoDCoordinate[] coordinate)
	{
		this.Node_num=num;
		this.coordinate=new TwoDCoordinate[this.Node_num];
		this.coordinate=coordinate;
	}
}
