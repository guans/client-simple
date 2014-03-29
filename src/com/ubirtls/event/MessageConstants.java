package com.ubirtls.event;

/**
 * 消息常量接口 声明接收消息时需要用到的一些常量，包括命令ID 消息响应ID等
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface MessageConstants {
	/** 登录成功响应 */
	public static final String LOGIN_SUCCESS = "0000";

	/** 登录密码错误响应 */
	public static final String PASSWORD_ERROR = "0001";

	/** 登录不可知错误发生响应 */
	public static final String LOGIN_USERNAME_ERROR = "0002";

	/** 注册成功响应 */
	public static final String REGISTER_SUCCESS = "0003";

	/** 注册失败响应 */
	public static final String REGISTER_FAIL = "0004";

	/** 修改用户信息成功响应 */
	public static final String MODIFY_INFO_SUCCESS = "0009";

	/** 修改用户信息失败响应 */
	public static final String MODIFY_INFO_FAIL = "0010";

	/** receiver ID */

/*	*//** 服务引擎ID *//*
	public static final String SERVER_ENGINE_ID = "server_engine";

	*//** 定位引擎ID *//*
	public static final String LOCATION_ENGINE_ID = "location_engine";*/

	/** 消息命令标识 */

	/** 登录命令 */
	public static final String LOGIN_CMD = "0101";

	/** 注册命令 */
	public static final String REGISTER_CMD = "0102";
	/**注销命令*/
	public static final String LOGOUT_CMD = "0104";
	/** 定位命令 */
	public static final String LOCATION_CMD = "0201";

	/** 地图描述信息请求命令 */
	public static final String MAPINFO_CMD = "0310";

	/** 滤波选择命令 */
	public static final String FILTER_CMD = "0109";

	/** 定位结果命令 */
	public static final String LOCATION_RESULT_CMD = "0202";

	/** 地图描述信息结果命令 */
	public static final String MAPINFO_RESULT_CMD = "0314";

	/** 分隔符SP */
	public static final String SP = "|";

	/** 结束符CLRF */
	public static final String CLRF = "\r\n";
	
	/**分组分隔符*/
	public static final String SP1 = ":";

}
