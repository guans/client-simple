package com.ubirtls.event;

import coordinate.TwoDCoordinate;

public class NavigateResult {
	/**
	 * �洢����·�ߵĽڵ���Ŀ
	 */
	public static int Node_num;
	/**
	 * �洢����·�������еĵ����ڵ�
	 */
	public static TwoDCoordinate[] coordinate;
	/**
	 * �洢���������Ϣ
	 * @param num ����·���еĽڵ���Ŀ
	 * @param coordinate ����·���ж�Ӧ�Ľڵ�
	 */
	public NavigateResult(int num,TwoDCoordinate[] coordinate)
	{
		this.Node_num=num;
		this.coordinate=new TwoDCoordinate[this.Node_num];
		this.coordinate=coordinate;
	}
}
