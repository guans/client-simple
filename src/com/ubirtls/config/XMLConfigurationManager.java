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
 * 该类负责配置管理，在程序启动时加载设置项，保存在{@link Setting}中；程序退出时根据{@link Setting}中 的值保存设置项到配置文件
 * 配置项元素名称参考{@link XMLElement}
 * 
 * @author 胡旭科
 * @version 0.1
 * @deprecated Android的Preference可以很方便的解决设置问题
 * */
public class XMLConfigurationManager implements IConfigureManager {
	/**
	 * 配置文件的路径
	 */
	private String filePath = null;

	/**
	 * 构造函数 初始化配置文件的存储路径
	 * 
	 * @param filePath 文件的存储路径 如果为null 设置为默认值/Sdcard/config.xml
	 */
	public XMLConfigurationManager(String filePath) {
		if (filePath == null)
			this.filePath = "/sdcard/config.xml";
		else
			this.filePath = filePath;
	}

	/**
	 * 加载配置文件，解析给个配置项
	 * 
	 * @return 如果所有配置项都能加载成功返回true 否则返回false
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
		// 加载语言
		if (!loadLanguage(doc))
			return false;
		// 加载视图模式
		if (!loadViewMode(doc))
			return false;
		// 加载显示模式
		if (!loadShowMode(doc))
			return false;
		// 加载模拟速度
		if (!loadSimulationSpeed(doc))
			return false;
		// 加载日志项
		if (!loadLog(doc))
			return false;
		// 加载监控模式
		if (!loadMonitorMode(doc))
			return false;
		// 加载订阅服务
		if (!loadSubscribedService(doc))
			return false;
		// 加载默认位置
		if (!loadDefaultPosition(doc))
			return false;
		// 加载扫描间隔配置项
		if (!loadScanInterval(doc))
			return false;
		// 加载字体大小
		if (!loadFontSize(doc))
			return false;
		// 加载账户
		if (!loadAccount(doc))
			return false;
		// 加载服务引擎IP地址
		if (!loadSockets(doc))
			return false;
		return true;
	}

	/**
	 * 保存配置项
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
			/* 保存语言设置项 */
			serializer.startTag(null, XMLElement.LANGUAGE);
			serializer.text(new Integer(Setting.LANGUAGE).toString());
			serializer.endTag(null, XMLElement.LANGUAGE);
			/* 保存字体设置项 */
			serializer.startTag(null, XMLElement.FONTSIZE);
			serializer.text(new Integer(Setting.FONTSIZE).toString());
			serializer.endTag(null, XMLElement.FONTSIZE);
			/* 保存显示模式设置项 */
			serializer.startTag(null, XMLElement.SHOWMODE);
			serializer.text(new Integer(Setting.SHOWMODE).toString());
			serializer.endTag(null, XMLElement.SHOWMODE);
			/* 保存视图模式设置项 */
			serializer.startTag(null, XMLElement.VIEWMODE);
			serializer.text(new Integer(Setting.VIEWMODE).toString());
			serializer.endTag(null, XMLElement.VIEWMODE);
			/* 保存模拟速度设置项 */
			serializer.startTag(null, XMLElement.SIMULATIONSPEED);
			serializer.text(new Double(Setting.SIMULATIONSPEED).toString());
			serializer.endTag(null, XMLElement.SIMULATIONSPEED);
			/* 保存扫描间隔设置项 */
			serializer.startTag(null, XMLElement.SCANINTERVAL);
			serializer.text(new Integer(Setting.SCANINTERVAL).toString());
			serializer.endTag(null, XMLElement.SCANINTERVAL);
			/* 保存监控模式设置项 */
			serializer.startTag(null, XMLElement.MONITORMODE);
			serializer.text(new Boolean(Setting.MONITORMODE).toString());
			serializer.endTag(null, XMLElement.MONITORMODE);
			/* 是否保存密码 */
			serializer.startTag(null, XMLElement.SAVEPASSWORD);
			serializer.text(new Boolean(Setting.SAVEPASSWORD).toString());
			serializer.endTag(null, XMLElement.SAVEPASSWORD);
			/* 用户名 */
			serializer.startTag(null, XMLElement.USERNAME);
			serializer.text(Setting.USERNAME);
			serializer.endTag(null, XMLElement.USERNAME);
			/* 密码 */
			serializer.startTag(null, XMLElement.PASSWORD);
			serializer.text(Setting.PASSWORD);
			serializer.endTag(null, XMLElement.PASSWORD);
			/* 用户默认的位置 */
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
			/* 消息中间件套接字 */
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
	 * 从配置文件中解析语言项设置
	 * 
	 * @param doc xml文档
	 * @return 加载语言设置项成功，返回true 否则false
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
	 * 从配置文件中解析视图模式项设置
	 * 
	 * @param doc xml文档
	 * @return 加载视图模式设置项成功，返回true 否则false
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
	 * 从配置文件中解析显示模式项设置
	 * 
	 * @param doc xml文档
	 * @return 加载显示模式设置项成功 返回true 否则false
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
	 * 从配置文件中解析模拟速度项设置
	 * 
	 * @param doc xml文档
	 * @return 加载模拟速度项成功 返回true 否则返回false
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
	 * 从配置文件中解析日志设置项
	 * 
	 * @param doc
	 * @return 在0.1版本不实现
	 */
	private boolean loadLog(Document doc) {
		return true;
	}

	/**
	 * 从配置文件中解析监控模式设置项
	 * 
	 * @param doc xml文件
	 * @return 加载监控模式成功 返回true 否则返回false
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
	 * 从配置文件中解析扫描间隔设置项
	 * 
	 * @param doc xml文档
	 * @return 加载扫描间隔成功 返回true 否则返回false
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
	} // 从配置文件中解析订阅服务设置项

	/**
	 * 从配置文件中解析默认位置
	 * 
	 * @param doc XML文档
	 * @return
	 */
	private boolean loadDefaultPosition(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList socket = root.getElementsByTagName(XMLElement.DEFAULTPOSITION);
		// 默认位置
		Element position = (Element) socket.item(0);
		Element X = (Element) position.getElementsByTagName(XMLElement.X).item(
				0);
		Element Y = (Element) position.getElementsByTagName(XMLElement.Y).item(
				0);
		Element Z = (Element) position.getElementsByTagName(XMLElement.Z).item(
				0);
		// 设置全局变量 X Y Z
		Setting.DEFAULT_X = new Double(X.getFirstChild().getNodeValue());
		Setting.DEFAULT_Y = new Double(Y.getFirstChild().getNodeValue());
		Setting.DEFAULT_Z = new Double(Z.getFirstChild().getNodeValue());
		return true;
	}

	/**
	 * 从配置文件中解析字体大小
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
	 * 从配置文件中解析密码是否保存，若保存返回true，否则返回false。username以及password作为
	 * 输出参数，返回用户名以及密码（若保存用户密码），在登录时调用
	 * 
	 * @param doc XML文档
	 * @return
	 * 
	 */
	private boolean loadAccount(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		// 获得是否保存账户密码
		NodeList savepassword = root
				.getElementsByTagName(XMLElement.SAVEPASSWORD);
		if (savepassword.getLength() == 0)
			return false;
		Element result = (Element) (savepassword.item(0));
		Setting.SAVEPASSWORD = new Boolean(result.getFirstChild()
				.getNodeValue());
		if (Setting.SAVEPASSWORD) {
			// 获得用户账户
			NodeList username = root.getElementsByTagName(XMLElement.USERNAME);
			if (username == null || username.getLength() == 0) {
				Setting.USERNAME = "";
				return false;
			}
			Element r = (Element) (username.item(0));
			Setting.USERNAME = r.getFirstChild().getNodeValue();
			// 获得用户密码
			NodeList password = root.getElementsByTagName(XMLElement.PASSWORD);
			if (password == null || password.getLength() == 0) {
				Setting.PASSWORD = "";
				return false;
			}
			Element p = (Element) (password.item(0));
			Setting.PASSWORD = p.getFirstChild().getNodeValue();
			return true;
		}
		// 账户密码清空 未保存
		else {
			Setting.USERNAME = "";
			Setting.PASSWORD = "";
			return true;
		}
	}

	/**
	 * 加载套接字 和消息中间件通信的套接字IP:Port
	 * 
	 * @param doc xml文档
	 * @return 加载socket成功返回true 否则返回false
	 */
	private boolean loadSockets(Document doc) {
		if (doc == null)
			return false;
		Element root = doc.getDocumentElement();
		NodeList socket = root.getElementsByTagName(XMLElement.SOCKET);
		// 服务引擎socket
		Element MOMsocket = (Element) socket.item(0);
		Element ip = (Element) MOMsocket.getElementsByTagName(XMLElement.IP)
				.item(0);
		Element port = (Element) MOMsocket
				.getElementsByTagName(XMLElement.PORT).item(0);
		// 设置全局变量
		Setting.IP_ADDRESS = ip.getFirstChild().getNodeValue();
		Setting.PORT = new Integer(port.getFirstChild().getNodeValue());
		return true;
	}
}
