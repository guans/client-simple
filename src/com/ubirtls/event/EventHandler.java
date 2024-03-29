package com.ubirtls.event;

import java.util.EventObject;

/**
 * 事件处理的接口，任何想处理这些事件的类必须实现该接口
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface EventHandler {
	/**
	 * 处理各种事件
	 * 
	 * @param event EventObject对象 需要处理的事件
	 */
	public void handleEvent(EventObject event);
}
