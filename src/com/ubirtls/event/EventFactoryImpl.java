package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

/**
 *��չ�� {@link AbstractEventFactory}
 * 
 * @author �����
 * @version 1.0
 */
public class EventFactoryImpl extends AbstractEventFactory {

	@Override
	public EventObject createEvent(int event, Object object) {
		// TODO Auto-generated method stub
		EventObject eventObject = null;
		try {
			/* ��λ����¼� */
			if (event == LOCATIONRESULT_EVENT) {
				eventObject = new LocationResultEvent((TwoDCoordinate) object);
			}
			/* ��¼�����֤�¼� */
			else if (event == AUTHENTICATION_EVENT) {
				eventObject = new AuthenticationEvent(
						(AuthenticationResult) object);

			}
			/*���ֵ��¼�*/
			else if(event==FILEPOINT_EVENT)
			{
				eventObject=new FirePointChangeEvent(
						(TwoDCoordinate) object);
			}
			/*ҽ����Ϣ�¼�*/
			else if(event==DOCTOR_EVENT)
			{
				eventObject=new DoctorInformationEvent(
						(DoctorResult)object);
			}
			/*�����¼�*/
			else if(event==NAVIGATE_EVENT)
			{
				eventObject=new NavigateEvent((NavigateResult)object);
			}
			/*��ѯ�¼�*/
			else if(event==QUERY_EVENT)
			{
				eventObject=new QueryEvent((Query_Result)object);
			}
			/* ע���¼� */
			else if (event == REGISTER_EVENT) {
				eventObject = new RegisterEvent((Boolean) object);
			}
			/* �޸������¼� */
			else if (event == CHANGEPASSWORD_EVENT) {
				eventObject = new ChangePasswordEvent((Boolean) object);

			} else if (event == MAPINFO_EVENT) {
				eventObject = new MapInfoEvent((MapInfo) object);
			}
			else if(event==URADIOLOCATION_EVENT)
			{
				eventObject=new UradioLocationEvent((TwoDCoordinate) object);
			}
		} catch (Exception e) {
			/*
			 * �쳣����
			 */
		}
		return eventObject;
	}
}
