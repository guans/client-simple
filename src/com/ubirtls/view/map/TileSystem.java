package com.ubirtls.view.map;

import android.graphics.Point;

import com.ubirtls.MapInfoRequestListener;

import coordinate.TwoDCoordinate;

import java.lang.Math;

public class TileSystem implements MapInfoRequestListener {
	public static String REGION_ID = "gym";
	/** ��Ƭ�Ŀ�Ĭ��Ϊ256 */
	public static int TILE_WIDTH_SIZE = 256;
	/** ��Ƭ�ĸ�Ĭ��Ϊ256 */
	public static int TILE_HEIGHT_SIZE = 256;
	/**ԭ��ͼ���ؿ�*/
	public static int WHOLE_MAP_WIDTH_SIZE = 4349;
	/**ԭ��ͼ���ظ�*/
	public static int WHOLE_MAP_HEIGHT_SIZE = 2875;
	/** ��ͼ��С��Xֵ */
	public static double MINX = 0;
	/** ��ͼ��С��Yֵ */
	public static double MINY = 0;// ��СYֵ
	/** ��ͼ����Xֵ */
	public static double MAXX = 6433.52;// ���Xֵ
	/** ��ͼ����Yֵ */
	public static double MAXY = 7456.15;
	/** ��ͼ��С���ż��� */
	public static int MIN_LEVEL = 0;
	/** ��ͼ������ż��� */
	public static int MAX_LEVEL = 5;
	private static Integer layer = null;
	/**ԭͼ���ŵ�0��ʱ��ԭ���ؿ����ŵı���*/
	private static Integer level0Width;
	/**ԭͼ���ŵ�0��ʱ��ԭ���ظ����ŵı���*/
	private static Integer level0Height;
	/**
	 *����ͼXY����ת���ɵ�ͼ��������
	 * 
	 * @param coordinate ��ͼXY����
	 * @param level ��ͼ��ǰ�����ż�
	 * @return ��ͼ��������
	 */
	public static Point MapXYToMapPixels(TwoDCoordinate coordinate, int level) {
		int mapWidthSize = 0;
		int mapHeightSize = 0;
		mapWidthSize = MapWidthSize(level);
		mapHeightSize = MapHeightSize(level);
		double deX = Math.abs(MAXX - MINX);
		double deY = Math.abs(MAXY - MINY);
		double xOffset = Math.abs(coordinate.getXCoordinate() - MINX);
		double yOffset = Math.abs(coordinate.getYCoordinate() - MAXY);
		int pixelX = (int) (xOffset * mapWidthSize / deX);
		int pixelY = (int) (yOffset * mapHeightSize / deY);
		return new Point(pixelX, pixelY);
	}
    /**
     * ָ��������ָ���͵�����Ϊ������
     * @param base ����
     * @param n ָ��
     * @return base��n�η�
     */
	private static int pow(int base, int n){
		int result = 1;
		for(int i = 1; i<=n; i++){
			result *= base;
		}
		return result;
	}
	/**
	 * ��õ�ǰ��ͼ��Ƭ��ĸ���
	 * @param level ���ż���
	 * @return ��Ƭ��
	 */
	public static int MapTileWidth(int level){
		int size = MapWidthSize(level);
		if(size % TILE_WIDTH_SIZE != 0){
			return (size/TILE_WIDTH_SIZE + 1);
		}
		else
			return size/TILE_WIDTH_SIZE ;
	}
	/**
	 * ��õ�ǰ��ͼ��Ƭ�ߵĸ���
	 * @param level ���ż���
	 * @return ��Ƭ��
	 */
	public static int MapTileHeight(int level){
		int size = MapHeightSize(level);
		if(size % TILE_HEIGHT_SIZE != 0){
			return (size/TILE_HEIGHT_SIZE + 1);
		}
		else
			return size/TILE_HEIGHT_SIZE ;
	}
	/**
	 * ����ͼ��������ת���ɵ�ͼXY����
	 * 
	 * @param x ��ͼ����X����
	 * @param y ��ͼ����Y����
	 * @param level ��ͼ���ż���
	 * @return ��ͼXY����
	 */
	public static TwoDCoordinate MapPixelsToMapXY(int x, int y, int level) {
		int mapWidthSize = 0;
		int mapHeightSize = 0;
		mapWidthSize = MapWidthSize(level);
		mapHeightSize = MapHeightSize(level);
		/* ����x y��ֵ */
		if (x < 0)
			x = mapWidthSize - (-x) % mapWidthSize;
		else
			x = x % mapWidthSize;

		if (y < 0)
			y = mapHeightSize - (-y) % mapHeightSize;
		else
			y = y % mapHeightSize;

		double deX = Math.abs(MAXX - MINX);
		double deY = Math.abs(MAXY - MINY);
		TwoDCoordinate coordinate = new TwoDCoordinate(MINX + x * deX
				/ mapWidthSize, MAXY - y * deY / mapHeightSize);
		return coordinate;
	}

	/**
	 * ���ص�ͼ���صĿ�
	 * 
	 * @param level ��ͼ����ϸ��
	 * @return ��ͼ���ؿ�
	 */
	private static void checkLayer(){
		if(layer == null){
			int w = 1;			
			int h = 1;
			int i = 0;		
			int j = 0;
			while(WHOLE_MAP_WIDTH_SIZE / w > TILE_WIDTH_SIZE){
				i++;
				w = pow(2,i);
			}
			while(WHOLE_MAP_HEIGHT_SIZE / h  >TILE_HEIGHT_SIZE){
				j++;
				h = pow(2,j);
			}
			
			if(w > h)
				layer = w;
			else
				layer = h;
			level0Width = WHOLE_MAP_WIDTH_SIZE / layer;
			level0Height = WHOLE_MAP_HEIGHT_SIZE / layer;
		}
	}
	public static int MapWidthSize(final int level) {
/*		int remainder = WHOLE_MAP_WIDTH_SIZE % TILE_WIDTH_SIZE;
		int remainder1 = WHOLE_MAP__HEIGHT_SIZE % TILE_HEIGHT_SIZE;
		if(remainder1 >= remainder)
			remainder = remainder1;
		int result = WHOLE_MAP_WIDTH_SIZE / TILE_WIDTH_SIZE;
		int level0Width;
		if(remainder!= 0)
			level0Width = WHOLE_MAP_WIDTH_SIZE /(result + 1);
		else*/	
		checkLayer();
		int width = level0Width * pow(2, level);
		return width;
	}

	/**
	 * ���ص�ͼ���صĸ�
	 * 
	 * @param level
	 * @return ��ͼ���ظ�
	 */
	public static int MapHeightSize(final int level) {

/*		int remainder = WHOLE_MAP__HEIGHT_SIZE % TILE_HEIGHT_SIZE;
		int remainder1 = WHOLE_MAP_WIDTH_SIZE % TILE_WIDTH_SIZE;
		if(remainder1 > remainder)
			remainder = remainder1;
		
		int result = WHOLE_MAP__HEIGHT_SIZE / TILE_HEIGHT_SIZE;
		int level0Height;
		if(remainder!= 0)
			level0Height = WHOLE_MAP__HEIGHT_SIZE /(result + 1);
		else*/
		checkLayer();
		int height = level0Height * pow(2, level);
		return height;
	}

	/**
	 * ����ͼ��������ת����tile XY����
	 * 
	 * @param pixelX ��ͼ����X����
	 * @param pixelY ��ͼ����Y����
	 * @return ��Ƭ����
	 */
	public static Point PixelXYToTileXY(final int pixelX, final int pixelY) {
		Point tileXY = new Point();
		if (pixelX >= 0)
			tileXY.x = pixelX / TILE_WIDTH_SIZE;
		else
			tileXY.x = pixelX / TILE_WIDTH_SIZE - 1;
		if (pixelY >= 0)
			tileXY.y = pixelY / TILE_HEIGHT_SIZE;
		else
			tileXY.y = pixelY / TILE_HEIGHT_SIZE - 1;
		/*
		 * tileXY.x = pixelX / TILE_WIDTH_SIZE; tileXY.y = pixelY /
		 * TILE_HEIGHT_SIZE;
		 */
		return tileXY;
	}

	/**
	 * ��tile XY����ת���ɵ�ͼ��������
	 * 
	 * @param tileX ��ƬX����
	 * @param tileY ��ƬY����
	 * @return ��ͼ���ص�
	 * @deprecated δ��ʹ������
	 */
	public static Point TileXYToPixelXY(final int tileX, final int tileY) {
		return null;
	}

	@Override
	public void mapInfoRequested(String regionID, int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight, double minX,
			double minY, double maxX, double maxY, int minLevel, int maxLevel) {
		// TODO Auto-generated method stub
		REGION_ID = regionID;
		TILE_WIDTH_SIZE = tileWidthSize;
		TILE_HEIGHT_SIZE = tileHeightSize;
		WHOLE_MAP_WIDTH_SIZE = wholeMapWidth;
		WHOLE_MAP_HEIGHT_SIZE = wholeMapHeight;
		MINX = minX;
		MINY = minY;
		MAXX = maxX;
		MAXY = maxY;
		MIN_LEVEL = minLevel;
		MAX_LEVEL = maxLevel;
		layer = null;
	}

}
