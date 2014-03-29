package com.ubirtls.event;

public class MapInfo {
	/**
	 * ����ID ������ʾһ�������һ�ŵ�ͼ
	 */
	public String regionID;
	
	/**
	 * ��Ƭ�Ŀ�
	 */
	public int tileWidthSize;
	
	/**
	 * ��Ƭ�ĸ�
	 */
	public int tileHeightSize;
	
	/**
	 * ��ͼ���صĿ�
	 */
	public int mapWidthSize;
	
	/**
	 * ��ͼ���صĸ�
	 */
	public int mapHeightSize;
	
	/**
	 * ��ͼ��С��Xֵ
	 */
	public double minX;
	
	/**
	 * ��ͼ��С��Yֵ
	 */
	public double minY;
	
	/**
	 * ��ͼ����Xֵ
	 */
	public double maxX;
	
	/**
	 * ��ͼ����Yֵ
	 */
	public double maxY;
	
	/**
	 * ��ͼ��С���ż���
	 */
	public int minLevel;
	
	/**
	 * ��ͼ������ż���
	 */
	public int maxLevel;

	/**
	 * ���캯��
	 * 
	 * @param tileWidthSize ��Ƭ�Ŀ�
	 * @param tileHeightSize ��Ƭ�ĸ�
	 * @param minWTileNum ��С���ż���ʱ��tile�ĸ���
	 * @param minHTileNum ��С���ż���ʱ��tile�ĸ���
	 * @param minX ��ͼ��С��Xֵ
	 * @param minY ��ͼ��С��Xֵ
	 * @param maxX ��ͼ����Xֵ
	 * @param maxY ��ͼ����Yֵ
	 * @param minLevel ��ͼ��С���ż���
	 * @param maxLevel ��ͼ������ż���
	 */
	public MapInfo(String regionID, int tileWidthSize, int tileHeightSize,
			int mapWidthSize, int mapHeightSize, double minX, double minY,
			double maxX, double maxY, int minLevel, int maxLevel) {
		this.regionID = regionID;
		this.tileWidthSize = tileWidthSize;
		this.tileHeightSize = tileHeightSize;
		this.mapWidthSize = mapWidthSize;
		this.mapHeightSize = mapHeightSize;
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
	}

	public MapInfo() {
		// TODO Auto-generated constructor stub
	}
}
