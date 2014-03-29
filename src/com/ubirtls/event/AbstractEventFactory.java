package com.ubirtls.event;

import java.util.EventObject;

/**
 * 事件工厂的抽象类，这里用到了工厂方法设计模式
 * 
 * @author 胡旭科
 * @version 1.0
 */
public abstract class AbstractEventFactory {
	/**
	 * 身份认证事件标识
	 */
	public static final int AUTHENTICATION_EVENT = 0x01;

	/**
	 * 修改密码事件标识
	 */
	public static final int CHANGEPASSWORD_EVENT = 0x02;

	/**
	 * 注册事件标识
	 */
	public static final int REGISTER_EVENT = 0x03;

	/**
	 * 定位结果事件标识
	 */
	public static final int LOCATIONRESULT_EVENT = 0x04;
	/**
	 * 火灾点结果事件标识
	 */
	public static final int FILEPOINT_EVENT=0x06;

	/**
	 * 地图信息事件标识
	 */
	public static final int MAPINFO_EVENT = 0x05;
	/**
	 * 医生信息事件
	 */
	public static final int DOCTOR_EVENT=0x07;
	/**
	 * 导航事件
	 */
	public static final int NAVIGATE_EVENT=0x08;
	/**
	 * 点查询事件
	 */
	public static final int QUERY_EVENT=0x09;
	/**
	 * 获取初始位置事件
	 */
	public static final int URADIOLOCATION_EVENT=0x10;
	/**
	 * 抽象方法，创建事件
	 * 
	 * @param event 标识创建哪个事件
	 * @param object Object对象,形成事件需要传递的参数
	 * @return EventObject对象
	 */
	public abstract EventObject createEvent(int event, Object object);
}
