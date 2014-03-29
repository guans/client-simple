/**
 * 
 */
package com.ubirtls.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import com.ubirtls.config.MapNavigateContants;
import com.ubirtls.config.Setting;

import android.util.Log;


import coordinate.TwoDCoordinate;

import test.Place;
import test.TestUtilityGet;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * 实现MessageListener 用于监听消息中间件发过来的消息
 * 
 * @author 胡旭科
 * @version 1.0
 * 
 */
public class MessageListenerImpl implements MessageListener, MessageConstants {

	/** 事件工厂 用于将接收到的消息转换成可以被捕获的事件 */
	AbstractEventFactory factory = new EventFactoryImpl();
	/**获取从服务器端返回的用户名*/
	public static String[] doctorInfo=new String[2];
	/**获取从服务器返回的医生的电话号码等信息*/
	/**要处理消息事件的监听者*/
	private List<EventHandler> eventHandlers = new ArrayList<EventHandler>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ubimessage.message.MessageListener#exceptionRaised(java.lang.Exception)
	 */
	@Override
	public void exceptionRaised(Exception arg0) {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ubimessage.message.MessageListener#messageReceived(ubimessage.message
	 * .Message)
	 */
	@Override
	public void messageReceived(Message message) {
		// TODO Auto-generated method stub
		try {
			//判断是否为紧急情况
			/*if(message.getHeader("emergencystatus").equals("true"))MapNavigateContants.ISEMERGENCY=true;
			else MapNavigateContants.ISEMERGENCY=false;*/
			MapNavigateContants.ISEMERGENCY=true;
			
			// 读取信息头
			if (message.getHeader("type").equals("login")) {
				if(TestUtilityGet.DoLogin(message))
				{	
					AuthenticationResult result = new AuthenticationResult(
							true, true, true);
					EventObject event = factory.createEvent(
							AbstractEventFactory.AUTHENTICATION_EVENT, result);
					for (EventHandler handler : eventHandlers) {
						handler.handleEvent(event);
					}
				}
				else
				{
					AuthenticationResult result = new AuthenticationResult(
							false, false, false);
					EventObject event = factory.createEvent(
							AbstractEventFactory.AUTHENTICATION_EVENT, result);
					for (EventHandler handler : eventHandlers) {
						handler.handleEvent(event);
					}
				}
			}
			else if (message.getHeader("type").equals("enroll")) {
				TestUtilityGet.DoEnroll(message);
			} 
			/**对用户自身进行定位*/
			else if (message.getHeader("type").equals("locateself")) {
				TwoDCoordinate coordinate=TestUtilityGet.DoLocateSelf(message);
				EventObject event = factory.createEvent(
						AbstractEventFactory.URADIOLOCATION_EVENT, coordinate);
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
			/**获取医生的基本信息*/
			else if (message.getHeader("type").equals("docterinfo")) {
				doctorInfo=TestUtilityGet.DoDocterInfo(message);
				DoctorResult result=new DoctorResult(doctorInfo[0], doctorInfo[1]);
				EventObject event=factory.createEvent(AbstractEventFactory.DOCTOR_EVENT, result);
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
			/**获取查询点的名称信息*/
			else if (message.getHeader("type").equals("searchpoint")) {
				int num=TestUtilityGet.Do_GetQuery_Num(message);
				Place[] place=new Place[num];
				String[] point_name=new String[num];
				TwoDCoordinate[] coordinates=new TwoDCoordinate[num];
				place=TestUtilityGet.DoSearchPoint(message);
				for(int i=0;i<num;i++)
				{
					point_name[i]=place[i].name;
					coordinates[i]=new TwoDCoordinate(place[i].x,place[i].y);
				}
				Query_Result result=new Query_Result(num, point_name,coordinates);
				result.query_type=message.getHeader("query_type");
				EventObject event=factory.createEvent(AbstractEventFactory.QUERY_EVENT, result);
				for(EventHandler handler:eventHandlers)
				{
					handler.handleEvent(event);
				}
				
			}
			/**导航事件和紧急情况导航路径*/
			else if (message.getHeader("type").equals("navigate")||
					message.getHeader("type").equals("emergency")) {
				int Node_num=TestUtilityGet.DoGetNavigateNode_Num(message);
				TwoDCoordinate[] coordinates=new TwoDCoordinate[Node_num];
				coordinates=TestUtilityGet.DoNavigation(message);
				NavigateResult result=new NavigateResult(Node_num, coordinates);
				EventObject event=factory.createEvent(AbstractEventFactory.NAVIGATE_EVENT,result);
				
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
			//火灾点事件
			else if (message.getHeader("type").equals("firepoint")) {
				TwoDCoordinate coordinate=TestUtilityGet.DoFirePoint(message);
				EventObject event=factory.createEvent(AbstractEventFactory.FILEPOINT_EVENT, coordinate);
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
		}
	     catch (Exception ex) {
	    	 
		}
	}

	/**
	 * 添加事件处理者
	 * 
	 * @param envenHandler 事件处理者
	 */
	public void addEventHandler(EventHandler envenHandler) {
		eventHandlers.add(envenHandler);
	}

	/**
	 * 删除事件处理者
	 * 
	 * @param eventHandler 事件处理者
	 */
	public void removeEventHandler(int location) {
		eventHandlers.remove(location);
	}
}
