package com.ubirtls;

public interface MapInfoRequestListener {
	/**
	 * ��õ�ͼ��������Ϣ
	 * 
	 * @param tileWidthSize ��Ƭ��(����)
	 * @param tileHeightSize ��Ƭ��(����)
	 * @param wholeMapWidth ԭͼ��(����)
	 * @param wholeMapHeight ԭͼ��(����)
	 * @param minX ��ͼ��С��Xֵ
	 * @param minY ��ͼ��С��Yֵ
	 * @param maxX ��ͼ����Xֵ
	 * @param maxY ��ͼ����Yֵ
	 * @param minLevel ��ͼ��С���ż���
	 * @param maxLevel ��ͼ������ż���
	 */
	public abstract void mapInfoRequested(String regionID, int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight, double minX,
			double minY, double maxX, double maxY, int minLevel, int maxLevel);
}
