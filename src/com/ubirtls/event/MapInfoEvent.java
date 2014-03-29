/**
 * 
 */
package com.ubirtls.event;

import java.util.EventObject;

/**
 * 派生于EventObject。当消息中间件回地图描述信息消息时会触发该事件。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapInfoEvent extends EventObject {

	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 地图描述信息请求结果
	 */
	public MapInfo result;

	/**
	 * 构造函数， 参数result为地图描述信息请求结果。
	 * 
	 * @param result 地图描述信息请求结果
	 */
	public MapInfoEvent(MapInfo result) {
		super(result);
		this.result = result;
	}
}
