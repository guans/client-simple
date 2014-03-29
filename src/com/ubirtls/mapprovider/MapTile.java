package com.ubirtls.mapprovider;

/**
 * ����һ��MapTile,�ݴ�������tile ��Ƭ������һ����ά������X Y �Լ�����ϸ����ȷ��
 * 
 * @author �����
 * @version 0.1
 */
public class MapTile {
	/**
	 * tile����ɹ�ID
	 */
	public static final int MAPTILE_SUCCESS_ID = 0;

	/**
	 * tile����ʧ��ID
	 */
	public static final int MAPTILE_FAIL_ID = MAPTILE_SUCCESS_ID + 1;

	/**
	 * tile xֵ
	 */
	private int x;

	/**
	 * tile yֵ
	 */
	private int y;

	/**
	 * ��ͼ����ϸ��
	 */
	private int zoomLevel;

	/**
	 * ���캯��
	 * 
	 * @param x ��ƬX����
	 * @param y ��ƬY����
	 * @param zoomLevel ��ͼ����ϸ��
	 */
	public MapTile(final int x, final int y, final int zoomLevel) {
		this.x = x;
		this.y = y;
		this.zoomLevel = zoomLevel;
	}

	/**
	 * ����zoomLevel
	 * 
	 * @return ��ͼ����ϸ��
	 */
	public int getZoomLevel() {
		return zoomLevel;
	}

	/**
	 * ������ƬX����
	 * 
	 * @return ��ƬX����
	 */
	public int getX() {
		return x;
	}

	/**
	 * ������ƬY����
	 * 
	 * @return ��ƬY����
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
