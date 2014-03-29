package com.ubirtls.config;

/**
 * 该类包含XML文件中元素的名称的常量，通过这些名称，可以获得XML文件中元素的值 后面根据需要再行添加
 * 
 * @author 胡旭科
 * @version 0.1
 * @deprecated Android的Preference可以很方便的解决设置问题
 */
public class XMLElement {
	/**
	 * 语言设置项标识，据此可以从配置文件中获得语言设置项
	 */
	public static final String LANGUAGE = "language";
	/**
	 * 字体大小设置项标识，据此可以从配置文件中获得字体大小设置项
	 */
	public static final String FONTSIZE = "fontsize";
	/**
	 * 视图模式设置项标识，据此可以从配置文件中获得视图模式设置项
	 */
	public static final String VIEWMODE = "viewmode";
	/**
	 * 显示模式设置项标识，据此可以从配置文件中获得显示模式设置项
	 */
	public static final String SHOWMODE = "showmode";
	/**
	 * 模拟速度设置项标识，据此可以从配置文件中获得模拟速度设置项
	 */
	public static final String SIMULATIONSPEED = "simulationspeed";
	/**
	 * 日志项设置项标识，据此可以从配置文件中获得日志项设置项
	 */
	public static final String LOGITEMS = "logitems";
	/**
	 * 监控模式设置项标识，据此可以从配置文件中获得监控模式设置项
	 */
	public static final String MONITORMODE = "monitormode";
	/**
	 * 扫描间隔设置项标识。据此可以从配置文件中获得扫描间隔设置项
	 */
	public static final String SCANINTERVAL = "scaninterval";
	/**
	 * 订阅的服务设置项标识，据此可以从配置文件中获得订阅的服务设置项
	 */
	public static final String SUBSCRIBEDSERVICES = "subscribedservices";
	/**
	 * 默认位置设置项标识，据此可以从配置文件中获得默认位置设置项
	 */
	public static final String DEFAULTPOSITION = "defaultposition";
	/**
	 * X Y Z 坐标
	 */
	public static final String X = "x";
	public static final String Y = "y";
	public static final String Z = "z";
	/**
	 * 是否保存用户密码标识，据此可以从配置文件中获得密码是否保存设置项
	 */
	public static final String SAVEPASSWORD = "savepassword";
	/**
	 * 用户名标识。据此可以从配置文件中获得上次登录时的用户名
	 */
	public static final String USERNAME = "username";
	/**
	 * 用户密码标识。据此可以从配置文件中获得上次登录时的密码
	 */
	public static final String PASSWORD = "password";
	/**
	 * 标识一个套接字
	 */
	public static final String SOCKET = "socket";
	/**
	 * 标识IP地址
	 */
	public static final String IP = "ip";
	/**
	 * 标识端口号
	 */
	public static final String PORT = "port";
}
