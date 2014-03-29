package com.ubirtls.config;

/**
 * 该类保存了程序运行时的全局设置信息，应用程序启动时先从配置文件加载，设置该类的静态变量；
 * 运行时，根据用户的设置，修改类的静态变量；退出时将静态成员变量保存到配置文件中。
 * 
 * @author 胡旭科
 * @version 0.1
 */
public class Setting {
	/**
	 * 中文简体常量
	 */
	public static final int LANGUAGEJIAN = 1;
	/**
	 * 繁字体常量
	 */
	public static final int LANGUAGEFAN = 2;
	/**
	 *
	 */
	public static final int LANGUAGEEN = 3;
	/**
	 * 大字体常量
	 */
	public static final int FONTSIZEBIG = 1;
	/**
	 * 中字体常量
	 */
	public static final int FONTSIZEMID = 2;
	/**
	 * 小字体常量
	 */
	public static final int FONTSIZESMALL = 3;
	/**
	 * 北向上视图模式常量
	 */
	public static final int VIEWMODE1 = 1;
	/**
	 * 车头向上视图模式常量
	 */
	public static final int VIEWMODE2 = 2;
	/**
	 * 三维视图模式常量
	 */
	public static final int VIEWMODE3 = 3;
	/**
	 * 白天模式
	 */
	public static final int SHOWMODEDAY = 1;
	/**
	 * 昼夜显示模式
	 */
	public static final int SHOWMODENIGHT = 2;
	/**
	 * 自动模式
	 */
	public static final int SHOWMODEAUTO = 3;
	/**
	 * 是否记录此项日志1
	 */
	public boolean LOG1 = false;
	/**
	 * 是否记录此项日志2
	 */
	public boolean LOG2 = false;
	/**
	 * 是否记录此项日志3
	 */
	public boolean LOG3 = false;
	/**
	 * 字体大小变量
	 */
	public static int FONTSIZE = 1;
	/**
	 * 语言变量
	 */
	public static int LANGUAGE = 1;
	/**
	 * 显示模式变量
	 */
	public static int SHOWMODE = 1;
	/**
	 * 视图模式变量
	 */
	public static int VIEWMODE = 1;
	/**
	 * 模拟速度变量
	 */
	public static double SIMULATIONSPEED = 50;
	/**
	 * 监控模式 是否被监控客户端监控
	 */
	public static boolean MONITORMODE = true;
	/**
	 * 扫描间隔变量 单位 ms
	 */
	public static int SCANINTERVAL = 1000;
	/**
	 * 订阅服务数组，标识那些服务被订阅
	 */
	public static int[] SUBSCRIBEDSERVICES = null;
	/**
	 * 订阅的服务数量
	 */
	public static int SUBSCRIBEDSERVICESNUMBER = 0;
	/**
	 * 默认的位置X坐标变量
	 */
	public static double DEFAULT_X = 3.8;
	/**
	 * 默认的位置Y坐标变量
	 */
	public static double DEFAULT_Y = 213.5;
	/**
	 * 默认的位置Z坐标变量
	 */
	public static double DEFAULT_Z = 12.2;
	public static String MAC;
	/**
	 * 登录时是否保存用户密码
	 */
	public static boolean SAVEPASSWORD = false;
	/**
	 * 登录时的用户名
	 */
	public static String USERNAME = "huxk";
	/**
	 * 登录时的用户密码。只有savePassword为true，才会有此项
	 */
	public static String PASSWORD = "";
	
	/**消息中间件IP地址*/
	public static String IP_ADDRESS = "192.168.1.103";//"59.71.236.153";
	/**消息中间件端口号*/
	public static int PORT = 3009;
	/**服务引擎标识*/
	public static String SERVER_ENGINE_ID = "server_engine";
	/**定位引擎标识*/
	public static String LOCATION_ENGINE_ID = "location_engine";
	/**记录用户的登录状态*/
	public static String LOGIN_STATUS="fail";
	/**医生的基本信息*/
	public static String DOCTOR_NAME;
	public static String DOCTOR_PHONE;
	/**移动客户端定位的位置*/
	public static double MYLOCATIONX=0.8;
	public static double MYLOCATIONY=8.8;
	/**
	 * 将配置项以字符串形式显示出来
	 * 
	 * @return 可用于测试的字符串
	 */
	public String toString() {
		String configString = "";
		configString += "fontsize " + new Integer(FONTSIZE).toString();
		configString += " language " + new Integer(LANGUAGE).toString();
		configString += " showmode " + new Integer(SHOWMODE).toString();
		configString += " viewmode " + new Integer(VIEWMODE).toString();
		configString += " speed " + new Double(SIMULATIONSPEED).toString();
		configString += " intarval " + new Integer(SCANINTERVAL).toString();
		configString += " monitormode " + new Boolean(MONITORMODE).toString();
		configString += " savepassword " + new Boolean(SAVEPASSWORD).toString();
		configString += " username " + USERNAME;
		configString += " password " + PASSWORD;
		configString += " IP " + IP_ADDRESS;
		configString += " port " + new Integer(PORT).toString();
		configString += " X " + new Double(DEFAULT_X).toString();
		configString += " Y " + new Double(DEFAULT_Y).toString();
		configString += " Z " + new Double(DEFAULT_Z).toString();
		return configString;
	}
}
