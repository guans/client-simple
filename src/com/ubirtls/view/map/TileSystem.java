package com.ubirtls.view.map;

import android.graphics.Point;

import com.ubirtls.MapInfoRequestListener;

import coordinate.TwoDCoordinate;

import java.lang.Math;

public class TileSystem implements MapInfoRequestListener {
	public static String REGION_ID = "gym";
	/** 瓦片的宽默认为256 */
	public static int TILE_WIDTH_SIZE = 256;
	/** 瓦片的高默认为256 */
	public static int TILE_HEIGHT_SIZE = 256;
	/**原地图像素宽*/
	public static int WHOLE_MAP_WIDTH_SIZE = 4349;
	/**原地图像素高*/
	public static int WHOLE_MAP_HEIGHT_SIZE = 2875;
	/** 地图最小的X值 */
	public static double MINX = 0;
	/** 地图最小的Y值 */
	public static double MINY = 0;// 最小Y值
	/** 地图最大的X值 */
	public static double MAXX = 6433.52;// 最大X值
	/** 地图最大的Y值 */
	public static double MAXY = 7456.15;
	/** 地图最小缩放级别 */
	public static int MIN_LEVEL = 0;
	/** 地图最大缩放级别 */
	public static int MAX_LEVEL = 5;
	private static Integer layer = null;
	/**原图缩放到0级时，原像素宽缩放的比例*/
	private static Integer level0Width;
	/**原图缩放到0级时，原像素高缩放的比例*/
	private static Integer level0Height;
	/**
	 *将地图XY坐标转换成地图像素坐标
	 * 
	 * @param coordinate 地图XY坐标
	 * @param level 地图当前的缩放级
	 * @return 地图像素坐标
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
     * 指数函数（指数和底数都为整数）
     * @param base 底数
     * @param n 指数
     * @return base的n次方
     */
	private static int pow(int base, int n){
		int result = 1;
		for(int i = 1; i<=n; i++){
			result *= base;
		}
		return result;
	}
	/**
	 * 获得当前地图瓦片宽的个数
	 * @param level 缩放级别
	 * @return 瓦片宽
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
	 * 获得当前地图瓦片高的个数
	 * @param level 缩放级别
	 * @return 瓦片高
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
	 * 将地图像素坐标转换成地图XY坐标
	 * 
	 * @param x 地图像素X坐标
	 * @param y 地图像素Y坐标
	 * @param level 地图缩放级别
	 * @return 地图XY坐标
	 */
	public static TwoDCoordinate MapPixelsToMapXY(int x, int y, int level) {
		int mapWidthSize = 0;
		int mapHeightSize = 0;
		mapWidthSize = MapWidthSize(level);
		mapHeightSize = MapHeightSize(level);
		/* 修正x y的值 */
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
	 * 返回地图像素的宽
	 * 
	 * @param level 地图缩放细节
	 * @return 地图像素宽
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
	 * 返回地图像素的高
	 * 
	 * @param level
	 * @return 地图像素高
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
	 * 将地图像素坐标转换成tile XY坐标
	 * 
	 * @param pixelX 地图像素X坐标
	 * @param pixelY 地图像素Y坐标
	 * @return 瓦片坐标
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
	 * 将tile XY坐标转换成地图像素坐标
	 * 
	 * @param tileX 瓦片X坐标
	 * @param tileY 瓦片Y坐标
	 * @return 地图像素点
	 * @deprecated 未有使用意义
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
