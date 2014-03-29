package com.ubirtls;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import com.ubirtls.mapprovider.MapTile;

/**
 * 封装service Engine的地图服务 提取瓦片数据
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapService {
	/** 命名空间 */
	private static final String NAMESPACE = "http://service.serviceEngine.ubieyes.com/";

	/** 投递SOAP数据的目标地址 */
	private static String URL = "http://192.168.1.123:8080/ServiceEngine/mapService?wsdl";

	/** 需要调用的方法名(获得本天气预报Web Services支持的洲、国内外省份和城市信息) */
	private static final String GET_MAPTILE = "getMapTile";

	private static final String ACTION = "http://service.serviceEngine.ubieyes.com/getMapTile";

	/**
	 * 通过webservice请求地图瓦片
	 * 
	 * @param tile 瓦片标示
	 * @return TileInfo 存放瓦片数据流
	 */
	public String getMapTile(MapTile tile) {
		/* 新建一个SoapObject对象 */
		SoapObject sobject = new SoapObject(NAMESPACE, GET_MAPTILE);
		// 为方法添加参数
		sobject.addProperty("mapID", 1);
		sobject.addProperty("x", tile.getY());
		sobject.addProperty("y", tile.getX());
		sobject.addProperty("layer", tile.getZoomLevel());

		HttpTransportSE ht = new HttpTransportSE(URL);
		ht.debug = true;
		// 以下是封装
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.bodyOut = sobject;
		envelope.dotNet = false;
		envelope.setOutputSoapObject(sobject);
		try {
			// 调用等待返回
			ht.call(ACTION, envelope);
			// 下面是获取复杂的节点对象
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject soap = (SoapObject) result.getProperty("return");
			String fileString = soap.getProperty("tileImageData").toString();
			return fileString;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return null;
		}
	}
}
