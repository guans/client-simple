package com.ubirtls;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


import com.ubirtls.mapprovider.MapTile;

/**
 * ��װservice Engine�ĵ�ͼ���� ��ȡ��Ƭ����
 * 
 * @author �����
 * @version 1.0
 */
public class MapService {
	/** �����ռ� */
	private static final String NAMESPACE = "http://service.serviceEngine.ubieyes.com/";

	/** Ͷ��SOAP���ݵ�Ŀ���ַ */
	private static String URL = "http://192.168.1.123:8080/ServiceEngine/mapService?wsdl";

	/** ��Ҫ���õķ�����(��ñ�����Ԥ��Web Services֧�ֵ��ޡ�������ʡ�ݺͳ�����Ϣ) */
	private static final String GET_MAPTILE = "getMapTile";

	private static final String ACTION = "http://service.serviceEngine.ubieyes.com/getMapTile";

	/**
	 * ͨ��webservice�����ͼ��Ƭ
	 * 
	 * @param tile ��Ƭ��ʾ
	 * @return TileInfo �����Ƭ������
	 */
	public String getMapTile(MapTile tile) {
		/* �½�һ��SoapObject���� */
		SoapObject sobject = new SoapObject(NAMESPACE, GET_MAPTILE);
		// Ϊ������Ӳ���
		sobject.addProperty("mapID", 1);
		sobject.addProperty("x", tile.getY());
		sobject.addProperty("y", tile.getX());
		sobject.addProperty("layer", tile.getZoomLevel());

		HttpTransportSE ht = new HttpTransportSE(URL);
		ht.debug = true;
		// �����Ƿ�װ
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		envelope.bodyOut = sobject;
		envelope.dotNet = false;
		envelope.setOutputSoapObject(sobject);
		try {
			// ���õȴ�����
			ht.call(ACTION, envelope);
			// �����ǻ�ȡ���ӵĽڵ����
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
