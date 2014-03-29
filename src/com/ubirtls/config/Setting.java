package com.ubirtls.config;

/**
 * ���ౣ���˳�������ʱ��ȫ��������Ϣ��Ӧ�ó�������ʱ�ȴ������ļ����أ����ø���ľ�̬������
 * ����ʱ�������û������ã��޸���ľ�̬�������˳�ʱ����̬��Ա�������浽�����ļ��С�
 * 
 * @author �����
 * @version 0.1
 */
public class Setting {
	/**
	 * ���ļ��峣��
	 */
	public static final int LANGUAGEJIAN = 1;
	/**
	 * �����峣��
	 */
	public static final int LANGUAGEFAN = 2;
	/**
	 *
	 */
	public static final int LANGUAGEEN = 3;
	/**
	 * �����峣��
	 */
	public static final int FONTSIZEBIG = 1;
	/**
	 * �����峣��
	 */
	public static final int FONTSIZEMID = 2;
	/**
	 * С���峣��
	 */
	public static final int FONTSIZESMALL = 3;
	/**
	 * ��������ͼģʽ����
	 */
	public static final int VIEWMODE1 = 1;
	/**
	 * ��ͷ������ͼģʽ����
	 */
	public static final int VIEWMODE2 = 2;
	/**
	 * ��ά��ͼģʽ����
	 */
	public static final int VIEWMODE3 = 3;
	/**
	 * ����ģʽ
	 */
	public static final int SHOWMODEDAY = 1;
	/**
	 * ��ҹ��ʾģʽ
	 */
	public static final int SHOWMODENIGHT = 2;
	/**
	 * �Զ�ģʽ
	 */
	public static final int SHOWMODEAUTO = 3;
	/**
	 * �Ƿ��¼������־1
	 */
	public boolean LOG1 = false;
	/**
	 * �Ƿ��¼������־2
	 */
	public boolean LOG2 = false;
	/**
	 * �Ƿ��¼������־3
	 */
	public boolean LOG3 = false;
	/**
	 * �����С����
	 */
	public static int FONTSIZE = 1;
	/**
	 * ���Ա���
	 */
	public static int LANGUAGE = 1;
	/**
	 * ��ʾģʽ����
	 */
	public static int SHOWMODE = 1;
	/**
	 * ��ͼģʽ����
	 */
	public static int VIEWMODE = 1;
	/**
	 * ģ���ٶȱ���
	 */
	public static double SIMULATIONSPEED = 50;
	/**
	 * ���ģʽ �Ƿ񱻼�ؿͻ��˼��
	 */
	public static boolean MONITORMODE = true;
	/**
	 * ɨ�������� ��λ ms
	 */
	public static int SCANINTERVAL = 1000;
	/**
	 * ���ķ������飬��ʶ��Щ���񱻶���
	 */
	public static int[] SUBSCRIBEDSERVICES = null;
	/**
	 * ���ĵķ�������
	 */
	public static int SUBSCRIBEDSERVICESNUMBER = 0;
	/**
	 * Ĭ�ϵ�λ��X�������
	 */
	public static double DEFAULT_X = 3.8;
	/**
	 * Ĭ�ϵ�λ��Y�������
	 */
	public static double DEFAULT_Y = 213.5;
	/**
	 * Ĭ�ϵ�λ��Z�������
	 */
	public static double DEFAULT_Z = 12.2;
	public static String MAC;
	/**
	 * ��¼ʱ�Ƿ񱣴��û�����
	 */
	public static boolean SAVEPASSWORD = false;
	/**
	 * ��¼ʱ���û���
	 */
	public static String USERNAME = "huxk";
	/**
	 * ��¼ʱ���û����롣ֻ��savePasswordΪtrue���Ż��д���
	 */
	public static String PASSWORD = "";
	
	/**��Ϣ�м��IP��ַ*/
	public static String IP_ADDRESS = "192.168.1.103";//"59.71.236.153";
	/**��Ϣ�м���˿ں�*/
	public static int PORT = 3009;
	/**���������ʶ*/
	public static String SERVER_ENGINE_ID = "server_engine";
	/**��λ�����ʶ*/
	public static String LOCATION_ENGINE_ID = "location_engine";
	/**��¼�û��ĵ�¼״̬*/
	public static String LOGIN_STATUS="fail";
	/**ҽ���Ļ�����Ϣ*/
	public static String DOCTOR_NAME;
	public static String DOCTOR_PHONE;
	/**�ƶ��ͻ��˶�λ��λ��*/
	public static double MYLOCATIONX=0.8;
	public static double MYLOCATIONY=8.8;
	/**
	 * �����������ַ�����ʽ��ʾ����
	 * 
	 * @return �����ڲ��Ե��ַ���
	 */
	public String toString() {
		String configString = "";
		configString += "fontsize " + new Integer(FONTSIZE).toString();
		configString += " language " + new Integer(LANGUAGE).toString();
		configString += " showmode " + new Integer(SHOWMODE).toString();
		configString += " viewmode " + new Integer(VIEWMODE).toString();
		configString += " speed " + new Double(SIMULATIONSPEED).toString();
		configString += " intarval " + new Integer(SCANINTERVAL).toString();
		configString += " monitormode " + new Boolean(MONITORMODE).toString();
		configString += " savepassword " + new Boolean(SAVEPASSWORD).toString();
		configString += " username " + USERNAME;
		configString += " password " + PASSWORD;
		configString += " IP " + IP_ADDRESS;
		configString += " port " + new Integer(PORT).toString();
		configString += " X " + new Double(DEFAULT_X).toString();
		configString += " Y " + new Double(DEFAULT_Y).toString();
		configString += " Z " + new Double(DEFAULT_Z).toString();
		return configString;
	}
}
