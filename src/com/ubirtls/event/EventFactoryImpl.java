package com.ubirtls.event;

import java.util.EventObject;

import coordinate.TwoDCoordinate;

/**
 *扩展了 {@link AbstractEventFactory}
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class EventFactoryImpl extends AbstractEventFactory {

	@Override
	public EventObject createEvent(int event, Object object) {
		// TODO Auto-generated method stub
		EventObject eventObject = null;
		try {
			/* 定位结果事件 */
			if (event == LOCATIONRESULT_EVENT) {
				eventObject = new LocationResultEvent((TwoDCoordinate) object);
			}
			/* 登录身份认证事件 */
			else if (event == AUTHENTICATION_EVENT) {
				eventObject = new AuthenticationEvent(
						(AuthenticationResult) object);

			}
			/*火灾点事件*/
			else if(event==FILEPOINT_EVENT)
			{
				eventObject=new FirePointChangeEvent(
						(TwoDCoordinate) object);
			}
			/*医生信息事件*/
			else if(event==DOCTOR_EVENT)
			{
				eventObject=new DoctorInformationEvent(
						(DoctorResult)object);
			}
			/*导航事件*/
			else if(event==NAVIGATE_EVENT)
			{
				eventObject=new NavigateEvent((NavigateResult)object);
			}
			/*查询事件*/
			else if(event==QUERY_EVENT)
			{
				eventObject=new QueryEvent((Query_Result)object);
			}
			/* 注册事件 */
			else if (event == REGISTER_EVENT) {
				eventObject = new RegisterEvent((Boolean) object);
			}
			/* 修改密码事件 */
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
			 * 异常处理
			 */
		}
		return eventObject;
	}
}
