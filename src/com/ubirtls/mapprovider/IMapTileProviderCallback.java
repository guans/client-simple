package com.ubirtls.mapprovider;

import android.graphics.drawable.Drawable;

public interface IMapTileProviderCallback {
	/**
	 * tile������� �ɹ������Ƭ����
	 * 
	 * @param tile MapTile����
	 * @param drawable ���󵽵���Ƭ����
	 */
	void mapTileRequestCompleted(MapTile tile, Drawable drawable);

	/**
	 * tile����ʧ��
	 * 
	 * @param tile MapTile���� ����Ƭ��������ʧ��
	 */
	void mapTileRequestFailed(MapTile tile);
}
