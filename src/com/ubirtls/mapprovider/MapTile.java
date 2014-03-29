package com.ubirtls.mapprovider;

/**
 * 描述一个MapTile,据此来请求tile 瓦片数据是一个二维数组用X Y 以及缩放细节来确定
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class MapTile {
	/**
	 * tile请求成功ID
	 */
	public static final int MAPTILE_SUCCESS_ID = 0;

	/**
	 * tile请求失败ID
	 */
	public static final int MAPTILE_FAIL_ID = MAPTILE_SUCCESS_ID + 1;

	/**
	 * tile x值
	 */
	private int x;

	/**
	 * tile y值
	 */
	private int y;

	/**
	 * 地图缩放细节
	 */
	private int zoomLevel;

	/**
	 * 构造函数
	 * 
	 * @param x 瓦片X坐标
	 * @param y 瓦片Y坐标
	 * @param zoomLevel 地图缩放细节
	 */
	public MapTile(final int x, final int y, final int zoomLevel) {
		this.x = x;
		this.y = y;
		this.zoomLevel = zoomLevel;
	}

	/**
	 * 返回zoomLevel
	 * 
	 * @return 地图缩放细节
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * 返回瓦片X坐标
	 * 
	 * @return 瓦片X坐标
	 */
	public int getX() {
		return x;
	}

	/**
	 * 返回瓦片Y坐标
	 * 
	 * @return 瓦片Y坐标
	 */
	public int getY() {
		return y;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof MapTile))
			return false;
		final MapTile rhs = (MapTile) obj;
		return (zoomLevel == rhs.zoomLevel && x == rhs.x && y == rhs.y);
	}

	@Override
	public int hashCode() {
		int code = 17;
		code *= 37 + zoomLevel;
		code *= 37 + x;
		code *= 37 + y;
		return code;
	}
}
