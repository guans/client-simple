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
 * ����ά��������Ƭ���ݵ����� ��{@link com.ubirtls.view.map.TileSystrm }�е�������Ϣ
 * @author �����
 * @version 1.0
 */
public class MapTileConfigure implements MapTileProviderConstants{

	/**xmlԪ������*/
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
	 * ���߻�õ�ǰ���������ͼ��������Ϣ�󣬽�����Ϣ���浽���ػ���·����Ӧ�����������ļ���
	 * @param region ��ͼ���ڵ������ʶ
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
	public void saveMapParams(String region,  int tileWidthSize,
			int tileHeightSize, int wholeMapWidth, int wholeMapHeight, double minX,
			double minY, double maxX, double maxY, int minLevel, int maxLevel){
		//����xml�ļ�
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
		//дxml�ļ�
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, TILE_INFO);
			/* ������Ϣ */
			serializer.startTag(null, REGION);
			serializer.text(region);
			serializer.endTag(null, REGION);
			/* ��Ƭ������*/
			serializer.startTag(null,TILE_WIDTH_SIZE);
			serializer.text(new Integer(tileWidthSize).toString());
			serializer.endTag(null, TILE_WIDTH_SIZE);
			/* ��Ƭ������ */
			serializer.startTag(null, TILE_HEIGHT_SIZE);
			serializer.text(new Integer(tileHeightSize).toString());
			serializer.endTag(null, TILE_HEIGHT_SIZE);
			/* ԭ��ͼ���ؿ� */
			serializer.startTag(null, WHOLE_MAP_WIDTH_SIZE);
			serializer.text(new Integer(wholeMapWidth).toString());
			serializer.endTag(null, WHOLE_MAP_WIDTH_SIZE);
			/* ԭ��ͼ���ظ� */
			serializer.startTag(null, WHOLE_MAP_HEIGHT_SIZE);
			serializer.text(new Integer(wholeMapHeight).toString());
			serializer.endTag(null, WHOLE_MAP_HEIGHT_SIZE);
			/* ��ͼ��Ӧ����Сx���� */
			serializer.startTag(null, MINX);
			serializer.text(new Double(minX).toString());
			serializer.endTag(null, MINX);
			/* ��ͼ��Ӧ����Сy���� */
			serializer.startTag(null, MINY);
			serializer.text(new Double(minY).toString());
			serializer.endTag(null, MINY);
			/* ��ͼ��Ӧ�����x���� */
			serializer.startTag(null, MAXX);
			serializer.text(new Double(maxX).toString());
			serializer.endTag(null, MAXX);
			/* ��ͼ��Ӧ�����y���� */
			serializer.startTag(null, MAXY);
			serializer.text(new Double(maxY).toString());
			serializer.endTag(null, MAXY);
			/* ��ͼ��С���ż��� */
			serializer.startTag(null, MIN_LEVEL);
			serializer.text(new Integer(minLevel).toString());
			serializer.endTag(null, MIN_LEVEL);
			/* ��ͼ������ż��� */
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
	 * ��ȡ�����Ӧ����Ƭ��ͼ��Ϣ
	 * @param region ����ID�����ڱ�ʾһ����ͼ
	 * @return MapInfo ��Ƭ��ͼ��Ϣ��
	 */
	public MapInfo getTileInfo(String region){
		//������Ƭ��ͼ������Ϣ��xml�ļ�
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
		//��xml�ļ��н�����Ƭ��ͼ��������Ϣ �������浽map�����з���
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
	 * ���������Ϣ
	 * @param doc Document����
	 * @return ������Ϣ
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
	 * �����Ƭ�Ŀ�
	 * @param doc Document����
	 * @return ��Ƭ��ͼ�Ŀ�
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
	 * �����Ƭ�ĸ�
	 * @param doc Document����
	 * @return ��Ƭ��ͼ�ĸ�
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
	 * �����Ƭ�Ŀ�
	 * @param doc Document����
	 * @return ��ͼ�Ŀ�
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
	 * �����Ƭ�Ŀ�
	 * @param doc Document����
	 * @return ��ͼ�Ŀ�
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
	 * ��ͼ��Сx����
	 * @param doc Document����
	 * @return ��Сx����
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
	 * ��ͼ��СY����
	 * @param doc Document����
	 * @return ��СY����
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
	 * ��ͼ���X����
	 * @param doc Document����
	 * @return ���x����
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
	 * ��ͼ���Y����
	 * @param doc Document����
	 * @return ���Y����
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
	 * ��ͼ��С�����ż���
	 * @param doc
	 * @return ��С���ż���
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
	 * ��õ�ͼ������ż���
	 * @param doc
	 * @return ������ż���
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
