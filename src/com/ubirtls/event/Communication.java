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
 * ��װ��MOMClient �ṩͨ�ŵĽӿ�
 * 
 * @author �����
 * @version 1.0
 */
public class Communication implements MessageConstants {

	/** Client���󣬸�������Ϣ�м��������Ϣ���շ� */
	private MOMClient client;

	
	Message testmessage = null;
	/**
	 * ���캯��
	 * 
	 * @param client MOMClient����
	 */
	public Communication(MOMClient client) {
		this.client = client;
	}

	/**
	 * ������Ϣ������
	 * 
	 * @param listener
	 */
	public void setMessageListener(MessageListener listener) {
		client.setMessageListener(listener);
	}

	/**
	 * �����Ƿ񱻼�أ�����Ϣ�м��������Ϣ��
	 * 
	 * @param isMonitered true��ʾ��� false��ʾ�������
	 * @deprecated �����Ѿ�����ʹ�ã������û����ü��ģʽ��Ĭ����Ҫ�����
	 */
	public void setMonitorMode(boolean isMonitered) {
	}

	/**
	 * ����λ�ú�����ͨ���м����λ���淢�Ͳ���ֵ����λ�õĽ��㡣
	 * 
	 * @param measurement ��ȡ�Ĳ���ֵ
	 */

	/**
	 * ��¼������Ϣ�м�����͵�¼��Ϣ��
	 * 
	 * @param username �û���
	 * @param password ����
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception {
		/*if (client != null) {
			Message message = new Message();
			String content = LOGIN_CMD + SP + username + SP + password + CLRF;
			message.setSender(Setting.MAC);
			message.setRecipient(Setting.SERVER_ENGINE_ID);
			//message.setHeader("123", "deu");
			// ����쳣���� 
			try {
				message.setContent(content);
				client.sendMessage(message);
			} catch (IOException e) {
				// �����쳣 
			}
		}*/
		
		Message message=new Message();
		message=TestUtilitySend.LogIn(username,password);
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		// ����쳣���� 
		try {
			//message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			// �����쳣 
		}
	}
	
	public void logout(String username) {
		if (client != null) {
			Message message = new Message();
			String content = LOGOUT_CMD + SP + username + CLRF;
			message.setSender(Setting.MAC);
			message.setRecipient(Setting.SERVER_ENGINE_ID);
			/* ����쳣���� */
			try {
				message.setContent(content);
				client.sendMessage(message);
			} catch (IOException e) {
				/* �����쳣 */
			}
		}
	}
	
	/**
	 * ע�ᣬ����Ϣ�м������ע����Ϣ��
	 * 
	 * @param username �û���
	 * @param password ����
	 * @param email email��ַ
	 * @throws Exception 
	 */
	public void register(String username, String password, String email) throws Exception {
		Message message = new Message();
		/*String content = REGISTER_CMD + SP + username + SP + password + SP
				+ email + SP +Setting.MAC + CLRF;*/
		message=TestUtilitySend.Enroll(username, password, email);
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		/* ����쳣���� */
		try {
			//message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			/* �����쳣 */
		}
	}

	/**
	 * �޸��˻����룬����Ϣ�м�������޸�������Ϣ��
	 * 
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 */
	public void changePassword(String username, String oldPassword,
			String newPassword) {

	}

	/**
	 * ��������������ͼ������Ϣ
	 * 
	 * @param regionId ����ID����ʶһ������ͳ�����ͼ
	 */
	public void requestMapInfo(int regionId) {
		Message message = new Message();
		String content = MAPINFO_CMD + SP + regionId + SP + CLRF;
		message.setSender(Setting.MAC);
		message.setRecipient(Setting.SERVER_ENGINE_ID);
		/* ����쳣���� */
		try {
			message.setContent(content);
			client.sendMessage(message);
		} catch (IOException e) {
			/* �����쳣 */
		}
	}
	/**
	 * ����������Ϣ�м�����͵�������ʼ��������Ϣ
	 * @param start_name ������ʼ�������
	 * @param end_name ������ֹ�������
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
		   if(start_name.equals("�ҵ�λ��")||end_name.equals("�ҵ�λ��"))
		   {
			   //���ͻ��������λ����Ϊ��㷢�͵���������
			   message.setHeader("myLocationX", new Double(Setting.MYLOCATIONX).toString());
			   message.setHeader("myLocationY", new Double(Setting.MYLOCATIONY).toString());
		   }
		   message.setSender(Setting.MAC);
		   message.setRecipient(Setting.SERVER_ENGINE_ID);
		   /*
		    * ����쳣����
		    */
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch(IOException e)
		   {
			   /*�����쳣*/
		   }
	   }
   }
   /**
    * ��ѯ�������û�����Ĳ��ֹؼ��ֽ��в�ѯ
    * @param point_name �û�����Ĳ��ֹؼ�������
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
		    * �Է�����Ϣ����쳣����
		    */
		   try
		   {
			   client.sendMessage(message);
		   }
		   catch (IOException e) 
		   {
			// TODO: handle exception/*��ȡ�쳣*/
		   }
	   }
   }
   /**
    * ��λ���������ȡ�û������λ��
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
			   /*��ȡ�쳣*/
		   }
	   }
   }
   /**
    * ��ȡҽ������Ϣ
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
			   /*��ȡ�쳣*/
		   }
		   
	   }
   }
   /**
    * ��������µĵ���·������
    */
   public void Emergency_Request()
   {
	   if(client!=null)
	   {
		   Message message=new Message();
		   message=TestUtilitySend.EmergencyRequest(Setting.USERNAME);
		   //���ͻ��������λ����Ϊ��㷢�͵���������
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
			/**�����쳣*/   
		   }
	   }
   }
   /**
    * ���ݵ�ͼ�ϵ�����õ�ƥ��Ľڵ������
    * @param x ��ͼ��x����
    * @param y ��ͼ��y����
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
			   /*�����쳣*/
		   }
	   }
   }
   /**
    * ���ƶ��ͻ��˻�ȡ������Ϣ���͵���λ����
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
	 * ���ö�λʱ���˲����ͣ��Դ˸Ľ���λ����
	 * 
	 * @param filterType �˲�����
	 */
	/*
	 * void setFilter(String filterType) { Message message = new Message();
	 * String content = FILTER_CMD + SP + filterType + CLRF;
	 * message.setSender(Setting.USERNAME);
	 * message.setRecipient(LOCATION_ENGINE_ID); ����쳣���� try {
	 * message.setContent(content); client.sendMessage(message); } catch
	 * (IOException e) { �����쳣 } }
	 */
}
