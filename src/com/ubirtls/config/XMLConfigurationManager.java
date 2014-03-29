package com.ubirtls.config;

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

import android.util.Log;
import android.util.Xml;

/**
 * ���ฺ�����ù����ڳ�������ʱ���������������{@link Setting}�У������˳�ʱ����{@link Setting}�� ��ֵ��������������ļ�
 * ������Ԫ�����Ʋο�{@link XMLElement}
 * 
 * @author �����
 * @version 0.1
 * @deprecated Android��Preference���Ժܷ���Ľ����������
 * */
public class XMLConfigurationManager implements IConfigureManager {
	/**
	 * �����ļ���·��
	 */
	private String filePath = null;

	/**
	 * ���캯�� ��ʼ�������ļ��Ĵ洢·��
	 * 
	 * @param filePath �ļ��Ĵ洢·�� ���Ϊnull ����ΪĬ��ֵ/Sdcard/config.xml
	 */
	public XMLConfigurationManager(String filePath) {
		if (filePath == null)
			this.filePath = "/sdcard/config.xml";
		else
			this.filePath = filePath;
	}

	/**
	 * ���������ļ�����������������
	 * 
	 * @return �������������ܼ��سɹ�����true ���򷵻�false
	 */
	public boolean loadConfigurationFile() {
		File file = new File(filePath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		}
		Document doc = null;
		try {
			doc = db.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		// ��������
		if (!loadLanguage(doc))
			return false;
		// ������ͼģʽ
		if (!loadViewMode(doc))
			return false;
		// ������ʾģʽ
		if (!loadShowMode(doc))
			return false;
		// ����ģ���ٶ�
		if (!loadSimulationSpeed(doc))
			return false;
		// ������־��
		if (!loadLog(doc))
			return false;
		// ���ؼ��ģʽ
		if (!loadMonitorMode(doc))
			return false;
		// ���ض��ķ���
		if (!loadSubscribedService(doc))
			return false;
		// ����Ĭ��λ��
		if (!loadDefaultPosition(doc))
			return false;
		// ����ɨ����������
		if (!loadScanInterval(doc))
			return false;
		// ���������С
		if (!loadFontSize(doc))
			return false;
		// �����˻�
		if (!loadAccount(doc))
			return false;
		// ���ط�������IP��ַ
		if (!loadSockets(doc))
			return false;
		return true;
	}

	/**
	 * ����������
	 */
	public boolean saveConfigurationFile() {
		File linceseFile = new File(filePath);
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
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "setting");
			/* �������������� */
			serializer.startTag(null, XMLElement.LANGUAGE);
			serializer.text(new Integer(Setting.LANGUAGE).toString());
			serializer.endTag(null, XMLElement.LANGUAGE);
			/* �������������� */
			serializer.startTag(null, XMLElement.FONTSIZE);
			serializer.text(new Integer(Setting.FONTSIZE).toString());
			serializer.endTag(null, XMLElement.FONTSIZE);
			/* ������ʾģʽ������ */
			serializer.startTag(null, XMLElement.SHOWMODE);
			serializer.text(new Integer(Setting.SHOWMODE).toString());
			serializer.endTag(null, XMLElement.SHOWMODE);
			/* ������ͼģʽ������ */
			serializer.startTag(null, XMLElement.VIEWMODE);
			serializer.text(new Integer(Setting.VIEWMODE).toString());
			serializer.endTag(null, XMLElement.VIEWMODE);
			/* ����ģ���ٶ������� */
			serializer.startTag(null, XMLElement.SIMULATIONSPEED);
			serializer.text(new Double(Setting.SIMULATIONSPEED).toString());
			serializer.endTag(null, XMLElement.SIMULATIONSPEED);
			/* ����ɨ���������� */
			serializer.startTag(null, XMLElement.SCANINTERVAL);
			serializer.text(new Integer(Setting.SCANINTERVAL).toString());
			serializer.endTag(null, XMLElement.SCANINTERVAL);
			/* ������ģʽ������ */
			serializer.startTag(null, XMLElement.MONITORMODE);
			serializer.text(new Boolean(Setting.MONITORMODE).toString());
			serializer.endTag(null, XMLElement.MONITORMODE);
			/* �Ƿ񱣴����� */
			serializer.startTag(null, XMLElement.SAVEPASSWORD);
			serializer.text(new Boolean(Setting.SAVEPASSWORD).toString());
			serializer.endTag(null, XMLElement.SAVEPASSWORD);
			/* �û��� */
			serializer.startTag(null, XMLElement.USERNAME);
			serializer.text(Setting.USERNAME);
			serializer.endTag(null, XMLElement.USERNAME);
			/* ���� */
			serializer.startTag(null, XMLElement.PASSWORD);
			serializer.text(Setting.PASSWORD);
			serializer.endTag(null, XMLElement.PASSWORD);
			/* �û�Ĭ�ϵ�λ�� */
			serializer.startTag(null, XMLElement.DEFAULTPOSITION);
			/* X */
			serializer.startTag(null, XMLElement.X);
			serializer.text(new Double(Setting.DEFAULT_X).toString());
			serializer.endTag(null, XMLElement.X);
			/* Y */
			serializer.startTag(null, XMLElement.Y);
			serializer.text(new Double(Setting.DEFAULT_Y).toString());
			serializer.endTag(null, XMLElement.Y);
			/* Z */
			serializer.startTag(null, XMLElement.Z);
			serializer.text(new Double(Setting.DEFAULT_Z).toString());
			serializer.endTag(null, XMLElement.Z);

			serializer.endTag(null, XMLElement.DEFAULTPOSITION);
			/* ��Ϣ�м���׽��� */
			serializer.startTag(null, XMLElement.SOCKET);
			/* IP */
			serializer.startTag(null, XMLElement.IP);
			serializer.text(Setting.IP_ADDRESS);
			serializer.endTag(null, XMLElement.IP);
			/* Port */
			serializer.startTag(null, XMLElement.PORT);
			serializer.text(new Integer(Setting.PORT).toString());
			serializer.endTag(null, XMLElement.PORT);

			serializer.endTag(null, XMLElement.SOCKET);

			serializer.endTag(null, "setting");
			serializer.endDocument();
			serializer.flush();
			fileos.close();

		} catch (Exception e) {
			Log.e("Exception", "error occurred while creating xml file");
			return false;
		}
		return true;
	}

	/**
	 * �������ļ��н�������������
	 * 
	 * @param doc xml�ĵ�
	 * @return ��������������ɹ�������true ����false
	 */
	private boolean loadLanguage(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList language = root.getElementsByTagName(XMLElement.LANGUAGE);
		if (language.getLength() == 0)
			return false;
		Element result = (Element) language.item(0);
		Setting.LANGUAGE = new Integer(result.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н�����ͼģʽ������
	 * 
	 * @param doc xml�ĵ�
	 * @return ������ͼģʽ������ɹ�������true ����false
	 */
	private boolean loadViewMode(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList viewmode = root.getElementsByTagName(XMLElement.VIEWMODE);
		if (viewmode.getLength() == 0)
			return false;
		Element result = (Element) (viewmode.item(0));
		Setting.VIEWMODE = new Integer(result.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н�����ʾģʽ������
	 * 
	 * @param doc xml�ĵ�
	 * @return ������ʾģʽ������ɹ� ����true ����false
	 */
	private boolean loadShowMode(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList showmode = root.getElementsByTagName(XMLElement.SHOWMODE);
		if (showmode.getLength() == 0)
			return false;
		Element result = (Element) (showmode.item(0));
		Setting.SHOWMODE = new Integer(result.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н���ģ���ٶ�������
	 * 
	 * @param doc xml�ĵ�
	 * @return ����ģ���ٶ���ɹ� ����true ���򷵻�false
	 */
	private boolean loadSimulationSpeed(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList simulationspeed = root
				.getElementsByTagName(XMLElement.SIMULATIONSPEED);
		if (simulationspeed.getLength() == 0)
			return false;
		Element result = (Element) (simulationspeed.item(0));
		Setting.SIMULATIONSPEED = new Double(result.getFirstChild()
				.getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н�����־������
	 * 
	 * @param doc
	 * @return ��0.1�汾��ʵ��
	 */
	private boolean loadLog(Document doc) {
		return true;
	}

	/**
	 * �������ļ��н������ģʽ������
	 * 
	 * @param doc xml�ļ�
	 * @return ���ؼ��ģʽ�ɹ� ����true ���򷵻�false
	 */
	private boolean loadMonitorMode(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList monitormode = root
				.getElementsByTagName(XMLElement.MONITORMODE);
		if (monitormode.getLength() == 0)
			return false;
		Element result = (Element) (monitormode.item(0));
		Setting.MONITORMODE = new Boolean(result.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н���ɨ����������
	 * 
	 * @param doc xml�ĵ�
	 * @return ����ɨ�����ɹ� ����true ���򷵻�false
	 */
	private boolean loadScanInterval(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList scaninterval = root
				.getElementsByTagName(XMLElement.SCANINTERVAL);
		if (scaninterval.getLength() == 0)
			return false;
		Element result = (Element) (scaninterval.item(0));
		Setting.SCANINTERVAL = new Integer(result.getFirstChild()
				.getNodeValue());
		return true;
	}

	private boolean loadSubscribedService(Document doc) {
		return true;
	} // �������ļ��н������ķ���������

	/**
	 * �������ļ��н���Ĭ��λ��
	 * 
	 * @param doc XML�ĵ�
	 * @return
	 */
	private boolean loadDefaultPosition(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList socket = root.getElementsByTagName(XMLElement.DEFAULTPOSITION);
		// Ĭ��λ��
		Element position = (Element) socket.item(0);
		Element X = (Element) position.getElementsByTagName(XMLElement.X).item(
				0);
		Element Y = (Element) position.getElementsByTagName(XMLElement.Y).item(
				0);
		Element Z = (Element) position.getElementsByTagName(XMLElement.Z).item(
				0);
		// ����ȫ�ֱ��� X Y Z
		Setting.DEFAULT_X = new Double(X.getFirstChild().getNodeValue());
		Setting.DEFAULT_Y = new Double(Y.getFirstChild().getNodeValue());
		Setting.DEFAULT_Z = new Double(Z.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н��������С
	 * 
	 * @param doc
	 * @return
	 */
	private boolean loadFontSize(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList fontsize = root.getElementsByTagName(XMLElement.FONTSIZE);
		if (fontsize.getLength() == 0)
			return false;
		Element result = (Element) (fontsize.item(0));
		Setting.FONTSIZE = new Integer(result.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * �������ļ��н��������Ƿ񱣴棬�����淵��true�����򷵻�false��username�Լ�password��Ϊ
	 * ��������������û����Լ����루�������û����룩���ڵ�¼ʱ����
	 * 
	 * @param doc XML�ĵ�
	 * @return
	 * 
	 */
	private boolean loadAccount(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		// ����Ƿ񱣴��˻�����
		NodeList savepassword = root
				.getElementsByTagName(XMLElement.SAVEPASSWORD);
		if (savepassword.getLength() == 0)
			return false;
		Element result = (Element) (savepassword.item(0));
		Setting.SAVEPASSWORD = new Boolean(result.getFirstChild()
				.getNodeValue());
		if (Setting.SAVEPASSWORD) {
			// ����û��˻�
			NodeList username = root.getElementsByTagName(XMLElement.USERNAME);
			if (username == null || username.getLength() == 0) {
				Setting.USERNAME = "";
				return false;
			}
			Element r = (Element) (username.item(0));
			Setting.USERNAME = r.getFirstChild().getNodeValue();
			// ����û�����
			NodeList password = root.getElementsByTagName(XMLElement.PASSWORD);
			if (password == null || password.getLength() == 0) {
				Setting.PASSWORD = "";
				return false;
			}
			Element p = (Element) (password.item(0));
			Setting.PASSWORD = p.getFirstChild().getNodeValue();
			return true;
		}
		// �˻�������� δ����
		else {
			Setting.USERNAME = "";
			Setting.PASSWORD = "";
			return true;
		}
	}

	/**
	 * �����׽��� ����Ϣ�м��ͨ�ŵ��׽���IP:Port
	 * 
	 * @param doc xml�ĵ�
	 * @return ����socket�ɹ�����true ���򷵻�false
	 */
	private boolean loadSockets(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList socket = root.getElementsByTagName(XMLElement.SOCKET);
		// ��������socket
		Element MOMsocket = (Element) socket.item(0);
		Element ip = (Element) MOMsocket.getElementsByTagName(XMLElement.IP)
				.item(0);
		Element port = (Element) MOMsocket
				.getElementsByTagName(XMLElement.PORT).item(0);
		// ����ȫ�ֱ���
		Setting.IP_ADDRESS = ip.getFirstChild().getNodeValue();
		Setting.PORT = new Integer(port.getFirstChild().getNodeValue());
		return true;
	}
}
