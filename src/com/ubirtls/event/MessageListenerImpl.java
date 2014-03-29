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
 * ʵ��MessageListener ���ڼ�����Ϣ�м������������Ϣ
 * 
 * @author �����
 * @version 1.0
 * 
 */
public class MessageListenerImpl implements MessageListener, MessageConstants {

	/** �¼����� ���ڽ����յ�����Ϣת���ɿ��Ա�������¼� */
	AbstractEventFactory factory = new EventFactoryImpl();
	/**��ȡ�ӷ������˷��ص��û���*/
	public static String[] doctorInfo=new String[2];
	/**��ȡ�ӷ��������ص�ҽ���ĵ绰�������Ϣ*/
	/**Ҫ������Ϣ�¼��ļ�����*/
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
			//�ж��Ƿ�Ϊ�������
			/*if(message.getHeader("emergencystatus").equals("true"))MapNavigateContants.ISEMERGENCY=true;
			else MapNavigateContants.ISEMERGENCY=false;*/
			MapNavigateContants.ISEMERGENCY=true;
			
			// ��ȡ��Ϣͷ
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
			/**���û�������ж�λ*/
			else if (message.getHeader("type").equals("locateself")) {
				TwoDCoordinate coordinate=TestUtilityGet.DoLocateSelf(message);
				EventObject event = factory.createEvent(
						AbstractEventFactory.URADIOLOCATION_EVENT, coordinate);
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
			/**��ȡҽ���Ļ�����Ϣ*/
			else if (message.getHeader("type").equals("docterinfo")) {
				doctorInfo=TestUtilityGet.DoDocterInfo(message);
				DoctorResult result=new DoctorResult(doctorInfo[0], doctorInfo[1]);
				EventObject event=factory.createEvent(AbstractEventFactory.DOCTOR_EVENT, result);
				for (EventHandler handler : eventHandlers) {
					handler.handleEvent(event);
				}
			}
			/**��ȡ��ѯ���������Ϣ*/
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
			/**�����¼��ͽ����������·��*/
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
			//���ֵ��¼�
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
	 * ����¼�������
	 * 
	 * @param envenHandler �¼�������
	 */
	public void addEventHandler(EventHandler envenHandler) {
		eventHandlers.add(envenHandler);
	}

	/**
	 * ɾ���¼�������
	 * 
	 * @param eventHandler �¼�������
	 */
	public void removeEventHandler(int location) {
		eventHandlers.remove(location);
	}
}
