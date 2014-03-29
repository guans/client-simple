package com.ubirtls.event;

public class MapInfo {
	/**
	 * 区域ID 用来标示一个区域或一张地图
	 */
	public String regionID;
	
	/**
	 * 瓦片的宽
	 */
	public int tileWidthSize;
	
	/**
	 * 瓦片的高
	 */
	public int tileHeightSize;
	
	/**
	 * 地图像素的宽
	 */
	public int mapWidthSize;
	
	/**
	 * 地图像素的高
	 */
	public int mapHeightSize;
	
	/**
	 * 地图最小的X值
	 */
	public double minX;
	
	/**
	 * 地图最小的Y值
	 */
	public double minY;
	
	/**
	 * 地图最大的X值
	 */
	public double maxX;
	
	/**
	 * 地图最大的Y值
	 */
	public double maxY;
	
	/**
	 * 地图最小缩放级别
	 */
	public int minLevel;
	
	/**
	 * 地图最大缩放级别
	 */
	public int maxLevel;

	/**
	 * 构造函数
	 * 
	 * @param tileWidthSize 瓦片的宽
	 * @param tileHeightSize 瓦片的高
	 * @param minWTileNum 最小缩放级别时宽tile的个数
	 * @param minHTileNum 最小缩放级别时高tile的个数
	 * @param minX 地图最小的X值
	 * @param minY 地图最小的X值
	 * @param maxX 地图最大的X值
	 * @param maxY 地图最大的Y值
	 * @param minLevel 地图最小缩放级别
	 * @param maxLevel 地图最大缩放级别
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
