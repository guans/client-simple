package com.ubirtls.event;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

import com.ubirtls.config.Setting;
import com.ubirtls.view.Activity.LoginActivity;

import test.TestUtilitySend;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

/**
 * 封装类MOMClient 提供通信的接口
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class Communication implements MessageConstants {

	/** Client对象，负责与消息中间件进行消息的收发 */
	private MOMClient client;

	
	Message testmessage = null;
	/**
	 * 构造函数
	 * 
	 * @param client MOMClient对象
	 */
	public Communication(MOMClient client) {
		this.client = client;
	}

	/**
	 * 设置消息监听者
	 * 
	 * @param listener
	 */
	public void setMessageListener(MessageListener listener) {
		client.setMessageListener(listener);
	}

	/**
	 * 设置是否被监控，向消息中间件发送消息。
	 * 
	 * @param isMonitered true表示监控 false表示不被监控
	 * @deprecated 现在已经不再使用，不需用户设置监控模式，默认需要被监控
	 */
	public void setMonitorMode(boolean isMonitered) {
	}

	/**
	 * 计算位置函数，通过中间件向定位引擎发送测量值进行位置的解算。
	 * 
	 * @param measurement 提取的测量值
	 */

	/**
	 * 登录，向消息中间件发送登录消息。
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception {
		/*if (client != null) {
			Message message = new Message();
			String content = LOGIN_CMD + SP + username + SP + password + CLRF;
			message.setSender(Setting.MAC);
			message.setRecipient(Setting.SERVER_ENGINE_ID);
			//message.setHeader("123", "deu");
			// 添加异常处理 
			try {
				message.setContent(content);
				client.sendMessage(message);
			} catch (IOException e) {
				// 捕获异常 
			}
		}*/
		
		Message message=new Message();
		message=TestUtilitySend.LogIn(username,password);
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		// 添加异常处理 
		try {
			//message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			// 捕获异常 
		}
	}
	
	public void logout(String username) {
		if (client != null) {
			Message message = new Message();
			String content = LOGOUT_CMD + SP + username + CLRF;
			message.setSender(Setting.MAC);
			message.setRecipient(Setting.SERVER_ENGINE_ID);
			/* 添加异常处理 */
			try {
				message.setContent(content);
				client.sendMessage(message);
			} catch (IOException e) {
				/* 捕获异常 */
			}
		}
	}
	
	/**
	 * 注册，向消息中间件发送注册消息。
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @param email email地址
	 * @throws Exception 
	 */
	public void register(String username, String password, String email) throws Exception {
		Message message = new Message();
		/*String content = REGISTER_CMD + SP + username + SP + password + SP
				+ email + SP +Setting.MAC + CLRF;*/
		message=TestUtilitySend.Enroll(username, password, email);
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		/* 添加异常处理 */
		try {
			//message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			/* 捕获异常 */
		}
	}

	/**
	 * 修改账户密码，向消息中间件发送修改密码消息。
	 * 
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePassword(String username, String oldPassword,
			String newPassword) {

	}

	/**
	 * 向服务引擎请求地图描述信息
	 * 
	 * @param regionId 区域ID，标识一个区域和场景地图
	 */
	public void requestMapInfo(int regionId) {
		Message message = new Message();
		String content = MAPINFO_CMD + SP + regionId + SP + CLRF;
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		/* 添加异常处理 */
		try {
			message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			/* 捕获异常 */
		}
	}
	/**
	 * 导航，向消息中间件发送导航的起始点坐标信息
	 * @param start_name 导航起始点的名称
	 * @param end_name 导航终止点的名称
	 * @throws Exception 
	 */
   public void navigate(String start_name,String end_name) throws Exception
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   if(Setting.LOGIN_STATUS=="success")
			   message=TestUtilitySend.Navigation(start_name, true, end_name,Setting.USERNAME);
		   else
			   message=TestUtilitySend.Navigation(start_name, false, end_name,Setting.USERNAME);
		   if(start_name.equals("我的位置")||end_name.equals("我的位置"))
		   {
			   //将客户端自身的位置作为起点发送到服务器端
			   message.setHeader("myLocationX", new Double(Setting.MYLOCATIONX).toString());
			   message.setHeader("myLocationY", new Double(Setting.MYLOCATIONY).toString());
		   }
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   /*
		    * 添加异常处理
		    */
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(IOException e)
		   {
			   /*捕获异常*/
		   }
	   }
   }
   /**
    * 查询。根据用户输入的部分关键字进行查询
    * @param point_name 用户输入的部分关键字名称
 * @throws Exception 
    */
   public void name_Query(String point_name) throws Exception
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.SearchPoint(point_name);
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   
		   /**
		    * 对发送消息添加异常处理
		    */
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch (IOException e) 
		   {
			// TODO: handle exception/*获取异常*/
		   }
	   }
   }
   /**
    * 定位请求。请求获取用户自身的位置
 * @throws Exception 
    */
   public void getMyLocation() throws Exception
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.LocateSelf();
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.LOCATION_ENGINE_ID);
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(IOException e)
		   {
			   /*获取异常*/
		   }
	   }
   }
   /**
    * 获取医生的信息
 * @throws Exception 
 * @throws Exception 
    */
   public void getDoctorInformation(String DoctorInformation) throws Exception
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.DocterInfo(DoctorInformation);
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(IOException e)
		   {
			   /*获取异常*/
		   }
		   
	   }
   }
   /**
    * 紧急情况下的导航路径请求
    */
   public void Emergency_Request()
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.EmergencyRequest(Setting.USERNAME);
		   //将客户端自身的位置作为起点发送到服务器端
		   message.setHeader("myLocationX", new Double(Setting.MYLOCATIONX).toString());
		   message.setHeader("myLocationY", new Double(Setting.MYLOCATIONY).toString());
		   
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(IOException e)
		   {
			/**捕获异常*/   
		   }
	   }
   }
   /**
    * 根据地图上的坐标得到匹配的节点的名称
    * @param x 地图的x坐标
    * @param y 地图的y坐标
    * @throws Exception
    */
   public void Query_By_Coordinate(double x,double y) throws Exception
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.SearchCoordinate(x, y ,0);
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(Exception e)
		   {
			   /*捕获异常*/
		   }
	   }
   }
   /**
    * 将移动客户端获取到的信息发送到定位引擎
    * @param x
    * @param y
    */
   public void sendLocationDataToServer(double x,double y)
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.LOCATION_ENGINE_ID);
		   message.setHeader("type", "mobilelocation");
		   message.setHeader("from", "utility");
		   message.setHeader("to", "server");
		   message.setHeader("locationX", new Double(x).toString());
		   message.setHeader("locationY", new Double(y).toString());
		   
		   try{
			   client.sendMessage(message);
		   }
		   catch(Exception e)
		   {
			   /**/
		   }
	   }
   }
   
   /**
	 * 设置定位时的滤波类型，以此改进定位精度
	 * 
	 * @param filterType 滤波类型
	 */
	/*
	 * void setFilter(String filterType) { Message message = new Message();
	 * String content = FILTER_CMD + SP + filterType + CLRF;
	 * message.setSender(Setting.USERNAME);
	 * message.setRecipient(LOCATION_ENGINE_ID); 添加异常处理 try {
	 * message.setContent(content); client.sendMessage(message); } catch
	 * (IOException e) { 捕获异常 } }
	 */
}
