package com.ubirtls;

public interface MapInfoRequestListener {
	/**
	 * 获得地图的描述信息
	 * 
	 * @param tileWidthSize 瓦片宽(像素)
	 * @param tileHeightSize 瓦片高(像素)
	 * @param wholeMapWidth 原图宽(像素)
	 * @param wholeMapHeight 原图高(像素)
	 * @param minX 地图最小的X值
	 * @param minY 地图最小的Y值
	 * @param maxX 地图最大的X值
	 * @param maxY 地图最大的Y值
	 * @param minLevel 地图最小缩放级别
	 * @param maxLevel 地图最大缩放级别
	 */
	public abstract void mapInfoRequested(String regionID, int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight, double minX,
			double minY, double maxX, double maxY, int minLevel, int maxLevel);
}
