package com.ubirtls.mapprovider;

import android.graphics.drawable.Drawable;

public interface IMapTileProviderCallback {
	/**
	 * tile请求完成 成功获得瓦片数据
	 * 
	 * @param tile MapTile对象
	 * @param drawable 请求到的瓦片数据
	 */
	void mapTileRequestCompleted(MapTile tile, Drawable drawable);

	/**
	 * tile请求失败
	 * 
	 * @param tile MapTile对象 该瓦片数据请求失败
	 */
	void mapTileRequestFailed(MapTile tile);
}
