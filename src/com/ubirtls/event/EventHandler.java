package com.ubirtls.event;

import java.util.EventObject;

/**
 * �¼�����Ľӿڣ��κ��봦����Щ�¼��������ʵ�ָýӿ�
 * 
 * @author �����
 * @version 1.0
 */
public interface EventHandler {
	/**
	 * ��������¼�
	 * 
	 * @param event EventObject���� ��Ҫ������¼�
	 */
	public void handleEvent(EventObject event);
}
