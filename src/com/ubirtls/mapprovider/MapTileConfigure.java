package com.ubirtls.mapprovider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import com.ubirtls.event.MapInfo;


import android.util.Log;
import android.util.Xml;

/**
 * 负责维护本地瓦片数据的配置 见{@link com.ubirtls.view.map.TileSystrm }中的属性信息
 * @author 胡旭科
 * @version 1.0
 */
public class MapTileConfigure implements MapTileProviderConstants{

	/**xml元素描述*/
	private static String TILE_INFO = "tileinfo";
	private static String REGION = "region"; 
	private static String TILE_WIDTH_SIZE = "tilewsize"; 
	private static String TILE_HEIGHT_SIZE = "tilehsize"; 
	private static String WHOLE_MAP_WIDTH_SIZE = "mapwsize"; 
	private static String WHOLE_MAP_HEIGHT_SIZE = "maphsize"; 
	private static String MINX = "minx"; 
	private static String MINY = "miny"; 
	private static String MAXX = "maxx"; 
	private static String MAXY = "maxy"; 
	private static String MIN_LEVEL = "minlevel"; 
	private static String MAX_LEVEL = "maxlevel"; 
	/**
	 * 在线获得当前所在区域地图的描述信息后，将该信息保存到本地缓存路径对应的区域描述文件中
	 * @param region 地图所在的区域标识
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
	public void saveMapParams(String region,  int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight, double minX,
			double minY, double maxX, double maxY, int minLevel, int maxLevel){
		//创建xml文件
		File linceseFile = new File(new File(CLIENT_PATH,region),region+".xml");
		try {
			linceseFile.createNewFile();
		} catch (IOException e) {
			Log.e("IOException", "exception in createNewFile() method");
		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(linceseFile);
		} catch (FileNotFoundException e) {
			Log.e("FileNotFoundException", "can't create FileOutputStream");
		}
		//写xml文件
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, TILE_INFO);
			/* 区域信息 */
			serializer.startTag(null, REGION);
			serializer.text(region);
			serializer.endTag(null, REGION);
			/* 瓦片宽像素*/
			serializer.startTag(null,TILE_WIDTH_SIZE);
			serializer.text(new Integer(tileWidthSize).toString());
			serializer.endTag(null, TILE_WIDTH_SIZE);
			/* 瓦片高像素 */
			serializer.startTag(null, TILE_HEIGHT_SIZE);
			serializer.text(new Integer(tileHeightSize).toString());
			serializer.endTag(null, TILE_HEIGHT_SIZE);
			/* 原地图像素宽 */
			serializer.startTag(null, WHOLE_MAP_WIDTH_SIZE);
			serializer.text(new Integer(wholeMapWidth).toString());
			serializer.endTag(null, WHOLE_MAP_WIDTH_SIZE);
			/* 原地图像素高 */
			serializer.startTag(null, WHOLE_MAP_HEIGHT_SIZE);
			serializer.text(new Integer(wholeMapHeight).toString());
			serializer.endTag(null, WHOLE_MAP_HEIGHT_SIZE);
			/* 地图对应的最小x坐标 */
			serializer.startTag(null, MINX);
			serializer.text(new Double(minX).toString());
			serializer.endTag(null, MINX);
			/* 地图对应的最小y坐标 */
			serializer.startTag(null, MINY);
			serializer.text(new Double(minY).toString());
			serializer.endTag(null, MINY);
			/* 地图对应的最大x坐标 */
			serializer.startTag(null, MAXX);
			serializer.text(new Double(maxX).toString());
			serializer.endTag(null, MAXX);
			/* 地图对应的最大y坐标 */
			serializer.startTag(null, MAXY);
			serializer.text(new Double(maxY).toString());
			serializer.endTag(null, MAXY);
			/* 地图最小缩放级别 */
			serializer.startTag(null, MIN_LEVEL);
			serializer.text(new Integer(minLevel).toString());
			serializer.endTag(null, MIN_LEVEL);
			/* 地图最大缩放级别 */
			serializer.startTag(null, MAX_LEVEL);
			serializer.text(new Integer(maxLevel).toString());
			serializer.endTag(null, MAX_LEVEL);

			serializer.endTag(null, TILE_INFO);
			serializer.endDocument();
			serializer.flush();
			fileos.close();

		} catch (Exception e) {
			Log.e("Exception", "error occurred while creating xml file");
		}
	}
	/**
	 * 获取区域对应的瓦片地图信息
	 * @param region 区域ID，用于标示一幅地图
	 * @return MapInfo 瓦片地图信息类
	 */
	public MapInfo getTileInfo(String region){
		//保存瓦片地图描述信息的xml文件
		File file = new File(new File(CLIENT_PATH,region),region+".xml");;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		//从xml文件中解析瓦片地图的描述信息 ，并保存到map对象中返回
		MapInfo map = new MapInfo();
		if((map.regionID = loadRegion(doc)) == null)
			return null;
		if((map.tileWidthSize = loadTileWSize(doc))== 0)
			return null;
		if((map.tileHeightSize = loadTileHSize(doc))== 0)
			return null;
		if((map.mapWidthSize = loadMapWSize(doc))== 0)
			return null;
		if((map.mapHeightSize = loadMapHSize(doc))== 0)
			return null;
		if((map.minX = loadMinX(doc))== -1.0)
			return null;
		if((map.minY = loadMinY(doc))== -1.0)
			return null;
		if((map.maxX = loadMaxX(doc))== -1.0)
			return null;
		if((map.maxY = loadMaxY(doc))== -1.0)
			return null;
		if((map.minLevel = loadMinLevel(doc))== -1)
			return null;
		if((map.maxLevel = loadMaxLevel(doc))== -1)
			return null;
		return map;
	}
	/**
	 * 获得区域信息
	 * @param doc Document对象
	 * @return 区域信息
	 */
	private String loadRegion(Document doc) {
		if (doc == null)
			return null;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(REGION);
		if (language.getLength() == 0)
			return null;
		Element result = (Element) language.item(0);
		return result.getFirstChild().getNodeValue();
	}
	/**
	 * 获得瓦片的宽
	 * @param doc Document对象
	 * @return 瓦片地图的宽
	 */
	private int loadTileWSize(Document doc) {
		if (doc == null)
			return 0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(TILE_WIDTH_SIZE);
		if (language.getLength() == 0)
			return 0;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}
	/**
	 * 获得瓦片的高
	 * @param doc Document对象
	 * @return 瓦片地图的高
	 */
	private int loadTileHSize(Document doc) {
		if (doc == null)
			return 0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(TILE_HEIGHT_SIZE);
		if (language.getLength() == 0)
			return 0;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}
	/**
	 * 获得瓦片的宽
	 * @param doc Document对象
	 * @return 地图的宽
	 */
	private int loadMapWSize(Document doc) {
		if (doc == null)
			return 0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(WHOLE_MAP_WIDTH_SIZE);
		if (language.getLength() == 0)
			return 0;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}

	/**
	 * 获得瓦片的宽
	 * @param doc Document对象
	 * @return 地图的宽
	 */
	private int loadMapHSize(Document doc) {
		if (doc == null)
			return 0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(WHOLE_MAP_HEIGHT_SIZE);
		if (language.getLength() == 0)
			return 0;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}
	/**
	 * 地图最小x坐标
	 * @param doc Document对象
	 * @return 最小x坐标
	 */
	private Double loadMinX(Document doc) {
		if (doc == null)
			return -1.0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MINX);
		if (language.getLength() == 0)
			return -1.0;
		Element result = (Element) language.item(0);
		return new Double(result.getFirstChild().getNodeValue());
	}
	/**
	 * 地图最小Y坐标
	 * @param doc Document对象
	 * @return 最小Y坐标
	 */
	private Double loadMinY(Document doc) {
		if (doc == null)
			return -1.0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MINY);
		if (language.getLength() == 0)
			return -1.0;
		Element result = (Element) language.item(0);
		return new Double(result.getFirstChild().getNodeValue());
	}
	/**
	 * 地图最大X坐标
	 * @param doc Document对象
	 * @return 最大x坐标
	 */
	private Double loadMaxX(Document doc) {
		if (doc == null)
			return -1.0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MAXX);
		if (language.getLength() == 0)
			return -1.0;
		Element result = (Element) language.item(0);
		return new Double(result.getFirstChild().getNodeValue());
	}
	/**
	 * 地图最大Y坐标
	 * @param doc Document对象
	 * @return 最大Y坐标
	 */
	private Double loadMaxY(Document doc) {
		if (doc == null)
			return -1.0;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MAXY);
		if (language.getLength() == 0)
			return -1.0;
		Element result = (Element) language.item(0);
		return new Double(result.getFirstChild().getNodeValue());
	}
	/**
	 * 地图做小的缩放级别
	 * @param doc
	 * @return 做小缩放级别
	 */
	private int loadMinLevel(Document doc) {
		if (doc == null)
			return -1;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MIN_LEVEL);
		if (language.getLength() == 0)
			return -1;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}
	/**
	 * 获得地图最大缩放级别
	 * @param doc
	 * @return 最大缩放级别
	 */
	private int loadMaxLevel(Document doc) {
		if (doc == null)
			return -1;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(MAX_LEVEL);
		if (language.getLength() == 0)
			return -1;
		Element result = (Element) language.item(0);
		return new Integer(result.getFirstChild().getNodeValue());
	}
}
