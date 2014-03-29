package com.ubirtls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import test.TestUtilitySend;
import ubimessage.MessageITException;
import ubimessage.client.MOMClient;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ubirtls.config.IConfigureManager;
import com.ubirtls.config.Setting;
import com.ubirtls.config.XMLConfigurationManager;
import com.ubirtls.event.AuthenticationEvent;
import com.ubirtls.event.ChangePasswordEvent;
import com.ubirtls.event.Communication;
import com.ubirtls.event.DoctorInformationEvent;
import com.ubirtls.event.DoctorResult;
import com.ubirtls.event.EventHandler;
import com.ubirtls.event.FirePointChangeEvent;
import com.ubirtls.event.LocationResultEvent;
import com.ubirtls.event.MapInfo;
import com.ubirtls.event.MapInfoEvent;
import com.ubirtls.event.MessageListenerImpl;
import com.ubirtls.event.NavigateEvent;
import com.ubirtls.event.QueryEvent;
import com.ubirtls.event.RegisterEvent;
import com.ubirtls.event.UradioLocationEvent;
import com.ubirtls.mapprovider.IMapTileProviderCallback;
import com.ubirtls.mapprovider.MapTile;
import com.ubirtls.mapprovider.MapTileConfigure;
import com.ubirtls.mapprovider.MapTileDownloader;
import com.ubirtls.mapprovider.MapTileFileProvider;
import com.ubirtls.mapprovider.MapTileProvider;
import com.ubirtls.util.OverlayItem;
import com.ubirtls.view.map.MyLocationOverlay;
import com.ubirtls.view.map.PathOverlay;
import com.ubirtls.view.map.TileSystem;
import com.ubirtls.view.map.TrackManangerListener;

import coordinate.TwoDCoordinate;

public class Controller implements EventHandler,
		PreferenceConstants, IMapTileProviderCallback {

	/**
	 * controller�ĵ�һʵ��,��ʼΪnull��
	 */
	private static Controller singleInstance = null;

	/**
	 * �˻����ļ�����
	 */
	private List<AccountCheckListener> accountListeners;

	/**
	 * λ�ñ仯�ļ�����
	 */
	private List<LocationChangeListener> locationListeners;

	/**
	 * web service ����ļ�����
	 */
	private List<WSRequestListener> wsListeners;

	/**
	 * ��ͼ������Ϣ����ļ�����
	 */
	private List<MapInfoRequestListener> mapInfoListeners;

	/** maker item���� */
	private List<ItemListener> itemListeners;
	/**ҽ����Ϣ�ļ���*/
	private List<GetDoctorListener> getDoctorListeners;
	/**��������*/
	private List<NavigateRequestListener> navigateListeners;
	/**��ѯ����*/
	private List<QueryListener> queryListeners;
	/**��ȡ��ʼλ�ü�����*/
	private List<UradioLocationListener> uradioLocationListeners;
	/** �۲��� */
	private Observer observer = null;

	/** �켣��������� */
	private TrackManangerListener trackListener = null;

	/**
	 * ��ͼ�ṩ���࣬������ȡ��ʾ�ĵ�ͼ�������Ǳ���Ҳ�����Ǵ������ȡ��
	 */
	public MapTileProvider mapProvider;

	/**
	 * ͨ���࣬��������Ϣ�м��������Ϣ
	 */
	private Communication communication;

	/**
	 * 
	 */
	private static MOMClient client = null;

	/**
	 * ���ù���������ȡ������Ϣ
	 */
	private IConfigureManager configureManager;
	/**
	 * wifi��������mac��ַ
	 */
	private String mac = null;
	// private SpatialQueryService spatialQueryService;//�ռ��ѯ����
	// private RangingService rangingService;//��������Ҫ���ڲ������
	// private RouteFindService routeFindService;//·�����ҷ�����Ҫ���ڵ���·��������
	// private IFileManager fileManager;//�ļ�������Ҫ���ڹ�����ʷ��ѯ��¼�Լ��ղص���ļ���ȡ��
	/**
	 * ���캯������ʼ����Ա������˽�г�Ա������
	 */
	private Controller() {
		/* ��ʼ�������� */
		accountListeners = new ArrayList<AccountCheckListener>();
		locationListeners = new ArrayList<LocationChangeListener>();
		wsListeners = new ArrayList<WSRequestListener>();
		mapInfoListeners = new ArrayList<MapInfoRequestListener>();
		itemListeners = new ArrayList<ItemListener>();
		getDoctorListeners=new ArrayList<GetDoctorListener>();
		navigateListeners=new ArrayList<NavigateRequestListener>();
		queryListeners=new ArrayList<QueryListener>();
		uradioLocationListeners=new ArrayList<UradioLocationListener>();
		// ����map provider���������ȴӱ��ػ�ȡMapTile,�ٴ�webservice��ʽ��ȡ
		mapProvider = new MapTileFileProvider();
		mapProvider.setNext(new MapTileDownloader());
		mapProvider.setCallback(this);
		/* mapProvider = new MapTileDownloader(); */
		mapInfoListeners.add(mapProvider);
		mapInfoListeners.add(new TileSystem());
	}

	/**
	 * ���Controller�ĵ�һʵ������
	 * 
	 * @return Controller���Ψһʵ��
	 */
	public static synchronized Controller getInstance() {
		if (singleInstance == null)
			singleInstance = new Controller();
		return singleInstance;
	}

	/**
	 * ����˻���������
	 * 
	 * @param listener �˻���������
	 */
	public void addAccountCheckListerner(AccountCheckListener listener) {
		if (listener != null)
			accountListeners.add(listener);
	}

	/**
	 * ���λ�ñ仯������
	 * 
	 * @param listener λ�ñ仯������
	 */
	public void addLocationChangeListerner(LocationChangeListener listener) {
		if (listener != null)
			locationListeners.add(listener);
	}

	/**
	 * ���web service���������
	 * 
	 * @param listener web service���������
	 */
	public void addWSRequestListerner(WSRequestListener listener) {
		if (listener != null)
			wsListeners.add(listener);
	}

	/**
	 * ���MapInfo���������
	 * 
	 * @param listener MapInfoRequestListener����
	 */
	public void addMapInfoRequestListener(MapInfoRequestListener listener) {
		if (listener != null)
			mapInfoListeners.add(listener);
	}

	public void addItemListener(ItemListener listener) {
		if (listener != null)
			itemListeners.add(listener);
	}
	/**
	 * ��ӻ�ȡҽ����Ϣ�ļ�����
	 * @param listener GetDoctorListener����
	 */
	public void addGetDoctorInfoListener(GetDoctorListener listener)
	{
		if(listener!=null)
			getDoctorListeners.add(listener);
	}
	/**
	 * ��ӵ�������ļ�����
	 * @param listener NavigateRequestListener�����������
	 */
	public void addNavigateRequestListener(NavigateRequestListener listener)
	{
		if(listener!=null)
			navigateListeners.add(listener);
	}
	/**
	 * ��Ӳ�ѯ���������
	 * @param listener QueryListener��ѯ�������
	 */
	public void addQueryListener(QueryListener listener)
	{
		if(listener!=null)
			queryListeners.add(listener);
	}
	
	public void addUradioLocationListener(UradioLocationListener listener)
	{
		if(listener!=null)
			uradioLocationListeners.add(listener);
	}
	/**
	 * ���ù۲��� ������Ƭ���ݵ���Ҫ����ʱ��֪ͨ�ù۲���
	 * 
	 * @param observer Observer����
	 */
	public void setInvalidateObserver(Observer observer) {
		this.observer = observer;
	}

	/**
	 * ���ù켣������
	 * 
	 * @param trackListener {@link TrackManangerListener}���� ����֪ͨ�Թ켣���д���
	 */
	public void setTrackListener(TrackManangerListener trackListener) {
		this.trackListener = trackListener;
	}

	/**
	 * ��ӱ�ǩItem
	 * 
	 * @param item OverlayItem����
	 */
	public void addMarkerItem(OverlayItem item) {
		for (ItemListener listener : itemListeners)
			listener.addMarkerItem(item);
	}

	public void followPosition(TwoDCoordinate coor) {
		if (observer != null)
			observer.notifyFollow(coor);
	}

	/**
	 * ʹ��֮ǰ���communication�Ƿ��ѳ�ʼ������δ��ʼ�������Ƚ��г�ʼ��
	 * 
	 * @throws MessageITException
	 * @throws IOException
	 */
	private void checkCommunication() throws IOException, MessageITException {
		// ��Ҫcommunication��ʱ���ٳ�ʼ��
		if (communication == null) {
			client = new MOMClient(Setting.IP_ADDRESS, Setting.PORT, Setting.MAC); 
			MessageListenerImpl messageListener = new MessageListenerImpl();
			messageListener.addEventHandler(this);
			client.setMessageListener(messageListener);
			/* ��ȡ�û�����Ϊ��Ϣ�м���еĿͻ��˱�ʶ */
			client.connect();
			communication = new Communication(client);		
		}
	}
	/**
	 * ����Ƿ��Ѿ�����Ϣ�м����������
	 * 
	 * @return �Ѿ��������Ӿͷ���true ���򷵻�false
	 */
	private boolean isConnected() {
		if ((communication != null) && (client != null))
			return true;
		return false;
	}

	/**
	 * �رպ���Ϣ�м����ͨ�� �ر�����
	 */
	public void closeCommunication() {
		if (communication != null) {
			if (client != null) {
				try {
					client.disconnect();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client = null;
			}
			communication = null;
		}

	}

	/**
	 * ��¼�����������Communication��Ӧ�ĺ���ʵ�֡�
	 * 
	 * @param username �û���
	 * @param password ����
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception {
		
		checkCommunication();
		communication.login(username, password);
		
		//checkClient();
		//sender.login(username, password);
	}
   
	/**
	 * ע�ắ���������ServiceEngine��Ӧ�ĺ���ʵ�֡�
	 * 
	 * @param username �û���
	 * @param password ����
	 * @throws Exception 
	 */
	public void register(String username, String password, String email)
			throws Exception {
		checkCommunication();
		if (communication != null)
			communication.register(username, password, email);
	}
	/**
	 * ע������
	 * @param username �û���
	 * @throws MessageITException 
	 * @throws IOException 
	 */
	public void logout(String username) throws IOException, MessageITException{
		checkCommunication();
		if (communication != null)
			communication.logout(username);
	}
	/**
	 * ��������.�������Ӧ��Communicate�еĺ�������ͨ��
	 * @param start
	 * @param end
	 * @throws Exception 
	 */
	public void navigate(String start,String end) throws Exception
	{
		checkCommunication();
		if(communication!=null)
			communication.navigate(start, end);
	}
	/**
	 * ��ѯ���ؼ��ֲ�ѯ��ƥ���ѯ��
	 * @param point_name �û�����Ĳ�ѯ����
	 * @throws Exception 
	 */
	public void name_Query(String point_name) throws Exception
	{
		checkCommunication();
		if(communication!=null)
			communication.name_Query(point_name);
	}
	/**
	 * �����ȡ�����λ����Ϣ
	 * @throws Exception 
	 */
	public void getMyLocation() throws Exception
	{
		checkCommunication();
		if(communication!=null)
		{
			communication.getMyLocation();
		}
	}
	/**
	 * ��ȡҽ��������Ϣ������
	 * @throws Exception
	 */
	public void getDoctorInfo(String DoctorInformation) throws Exception
	{
		checkCommunication();
		if(communication!=null)
		{
			communication.getDoctorInformation(DoctorInformation);
		}
	}
	/**
	 * ����������µĵ���·������
	 * @throws IOException
	 * @throws MessageITException
	 */
	public void Emergency_Request() throws IOException, MessageITException
	{
		checkCommunication();
		if(communication!=null)
		{
			communication.Emergency_Request();
		}
	}
	/**
	 * ͨ���������в�ѯ
	 * @param x
	 * @param y
	 * @throws Exception
	 */
	public void Query_By_Coordinate(double x,double y) throws Exception
	{
		checkCommunication();
		if(communication!=null)
		{
			communication.Query_By_Coordinate(x, y);
		}
	}
	/**
	 * �ͻ��˵Ķ�λ���ݷ��͵�������
	 * @param x
	 * @param y
	 * @throws IOException
	 * @throws MessageITException
	 */
	public void sendLocationDataToServer(double x,double y) throws IOException, MessageITException
	{
		checkCommunication();
		if(communication!=null)
		{
			communication.sendLocationDataToServer(x, y);
		}
	}
	/**
	 * �����ͼ������Ϣ
	 */
	public void requestMapInfo() {
		/*
		 * checkCommunication(); communication.requestMapInfo(0);
		 */}

	/**
	 * ���ö�λʱ���˲����ͣ��Դ˸Ľ���λ����
	 * 
	 * @param filterType �˲�����
	 */
	void setFilter(String filterType) {

	}

	/**
	 * �����������Ӧ�ó�������ʱ���ã���Ҫ����Setting�еľ�̬��Ա������
	 * 
	 * @return ��������سɷ���true ���򷵻�false
	 */
	public boolean loadConfigure() {
		if (configureManager == null)
			configureManager = new XMLConfigurationManager(null);
		return configureManager.loadConfigurationFile();
	}

	/**
	 * �����������Ӧ�ó����˳�ʱ����Setting�еľ�̬��Ա���������������ļ���
	 */
	public void saveConfigure() {
		if (configureManager == null)
			configureManager = new XMLConfigurationManager(null);
		configureManager.saveConfigurationFile();
	}

	/**
	 * spotter��ʼ������
	 * 
	 * @param context Context����
	 */



	/**
	 * ����{@link com.ubirtls.mapprovider.MapTileProvider}��
	 * getDrawableAsynchronous����
	 * 
	 * @param tile ��Ƭ���ݱ�ʾ
	 * @return Drawable����
	 */
	public Drawable getMapTileAsync(final MapTile tile) {
		if (mapProvider != null)
			return mapProvider.getDrawableAsynchronous(tile);
		return null;
	}

	/**
	 * �����Ƭ tile��Ӧ�Ŀɻ��ƶ��� ͬ����ʽ
	 * 
	 * @param tile ������ʶһ����Ƭ
	 * @return ����Ƭ�Ŀɻ��ƶ���
	 */
	public Drawable getMapTile(final MapTile tile) {
		if (mapProvider != null)
			return mapProvider.getDrawableSynchronism(tile);
		return null;
	}

	/**
	 * ����ڴ��еĻ�������
	 */
	public void clearData() {
		if (mapProvider != null)
			mapProvider.clearRoomData();
	}

	/**
	 * ������ػ���ĵ�ͼ��ͼƬ
	 */
	public void deleteLocalMap() {
		if (mapProvider != null)
			mapProvider.clearMapData();
	}

	/**
	 * ȷ����Ƭ�������㹻��
	 * 
	 * @param num ��Ҫ�������Ƭ����
	 */
	public void ensureCapacity(int num) {
		mapProvider.ensureCapacity(num);
	}

	/**
	 * ����û��켣
	 */
	public void clearTrack() {
		if (trackListener != null)
			trackListener.trackIsCleared();
	}

	// public void getRoute(Coordinate startPoint, Coordinate
	// endPoint){}//��ô���㵽�յ��һ������·����֪ͨ��ʽ���첽��
	// public RouteInfo getRoute(Coordinate startPoint, Coordinate
	// endPoint){}//��ô���㵽�յ��һ������·�������÷��أ�ͬ����ʽ��
	// public void getDistance(Coordinate startPoint, Coordinate
	// endPoint){}//��ô���㵽�յ�ľ��롣֪ͨ��ʽ���첽��
	// public double getDistance(Coordinate startPoint, Coordinate
	// endPoint){}//��ô���㵽�յ�ľ��롣���÷��أ�ͬ����ʽ��
	// public void searchPOIByCoordinate(Coordinate
	// point){}//���ĳ�������ϸ��Ϣ��֪ͨ��ʽ���첽��
	// public POIInfo searchPOIByCoordinate(Coordinate
	// point){}//���ĳ�������ϸ��Ϣ�����÷��أ�ͬ����ʽ��
	// public void searchPOIByKeyWords (String keywords){}//���ؼ���������֪ͨ��ʽ���첽��
	// public POIInfo[] searchPOIByKeyWords (String keywords){}//
	// ���ؼ������������÷��أ�ͬ����ʽ��
	// public void searchPOIByAleph (String aleph)//������ĸ������֪ͨ��ʽ���첽��
	// public POIInfo[] searchPOIByAleph (String keywords){}// ������ĸ���������÷��أ�ͬ����ʽ��
	// public void searchPOIByInfrastructure (String
	// Infrastructure){}//��������ʩ������֪ͨ��ʽ���첽��
	// public POIInfo[] searchPOIByInfrastructure (String Infrastructure){}//
	// ��������ʩ���������÷��أ�ͬ����ʽ��
	// public void searchPOIByHistory (){}//����ʷ��¼������֪ͨ��ʽ���첽��
	// public POIInfo[] searchPOIByHistory (){}// ����ʷ��¼���������÷��أ�ͬ����ʽ��
	// public void searchPOIByCollect (int type){}//���ղ�����������֪ͨ��ʽ���첽��
	// public POIInfo[] searchPOIByCollect (String keywords){}//
	// ���ղ��������������÷��أ�ͬ����ʽ��
	// public void clearHistoryRecords(){}//�����ʷ��¼
	// public void deleteCurrentPath(){}//ɾ����ǰ����·��
	// public void saveCollectRecords(int type, String POIInfo){}//�����ͱ����ղص�
	// public void clearCollectRecords(int type){}//����������ղص�
	/**
	 * ���������С��С���С���
	 * 
	 * @param type
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setFontSize(int type) {

	}

	/**
	 * �������ԣ����塢���塢Ӣ��
	 * 
	 * @param type ��ʶ ��������
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setLanguage(int type) {

	}

	/**
	 * ������ͼģʽ
	 * 
	 * @param type ��ʶ��ͼģʽ����
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setViewMode(int type) {

	}

	/**
	 * ������ʾģʽ
	 * 
	 * @param type ��ʾģʽ����
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setShowMode(int type) {

	}

	/**
	 * ����ģ���ٶ�
	 * 
	 * @param type ģ���ٶ�����
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setSimulationSpeed(int type) {

	}

	/**
	 * �����Ƿ���
	 * 
	 * @param boolMonitor true��ʾ������� false��ʾ���������
	 * @deprecated �����ļ��Ĺ����Ѿ�ʹ��Android��SharedPreferences����
	 */
	public void setMonitorMode(boolean boolMonitor) {

	}

	/**
	 * �ָ��������á�
	 * 
	 * @deprecated �޸ĺ�δʹ�øú���
	 */
	public void recoverFactorySetting() {

	}

	/**
	 * ����ͨ�Ų����������׽���ͨ�ŵ�IP��ַ���˿ںš���������ı�ʶ����λ����ı�ʶ
	 * 
	 * @param ip ip��ַ
	 * @param port �˿ں�
	 * @param serverName ��������ı�ʶ
	 * @param locationName ��λ����ı�ʶ
	 */
	public void setCommInfo(String ip, int port, String serverName,
			String locationName) {
		// ip��ַ���߶˿ںŸı䣬����Ѿ�����������Ҫ�ر������ٴ�
		if ((!Setting.IP_ADDRESS.equals(ip)) || (Setting.PORT != port)) {
			if (isConnected()) {
				closeCommunication();
				Setting.IP_ADDRESS = ip;
				Setting.PORT = port;
				try {
					checkCommunication();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessageITException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Setting.IP_ADDRESS = ip;
		Setting.PORT = port;
		Setting.SERVER_ENGINE_ID = serverName;
		Setting.LOCATION_ENGINE_ID = locationName;
	}

	/**
	 * �����ݿ�
	 */
	public void openDatabase() {
	}

	/**
	 * �ر����ݿ�
	 */
	public void closeDatabase() {

	}

	/**
	 * ִ��sql���
	 * 
	 * @param sql ��Ҫִ�е�sql���
	 */
	public void executeSql(String sql) {

	}
	/**
	 * ���ص�ͼ�б�
	 * @return ���ػ�������е�ͼ�б�����ͼ����
	 */
	public String[] getMapList(){
		return mapProvider.BrowserMap();
	}
	/**
	 * ����������ͼ
	 * @param region ������ͼ��Ӧ������
	 * @return ������ͼ�ɹ� ����true ���򷵻�false
	 */
	public boolean changeMap(String region) {
		if (region != null) {
			//�ȱ��浱ǰ��ֵ
			MapTileConfigure tileInfoLoader = new MapTileConfigure();
/*			tileInfoLoader.saveMapParams(TileSystem.REGION_ID, TileSystem.TILE_WIDTH_SIZE, TileSystem.TILE_HEIGHT_SIZE,
					TileSystem.WHOLE_MAP_WIDTH_SIZE, TileSystem.WHOLE_MAP_HEIGHT_SIZE, TileSystem.MINX, TileSystem.MINY, TileSystem.MAXX, TileSystem.MAXY,
					TileSystem.MIN_LEVEL, TileSystem.MAX_LEVEL);*/
			MapInfo info = tileInfoLoader.getTileInfo(region);
			if (info != null) {
				for (MapInfoRequestListener listener : mapInfoListeners)
					listener.mapInfoRequested(info.regionID,
							info.tileWidthSize, info.tileHeightSize,
							info.mapWidthSize, info.mapHeightSize, info.minX,
							info.minY, info.maxX, info.maxY, info.minLevel,
							info.maxLevel);
				return true;
			}
		}
		return false;
	}

	/**
	 * �a���µ�ƫ�ƽY������λ���㣩
	 * @param stepLength ���L
	 * @param heading ����
	 */
	public void notifyLocation(double x, double y){
		boolean track = false;
		boolean location = false;
		Log.i("locationListeners", String.valueOf(locationListeners.size()));
		for (LocationChangeListener listener : locationListeners){
			/*			listener.displacementCahanged(stepLength, heading);
			 */			
			if(listener instanceof PathOverlay && !track){
				 listener.locationChanged(new TwoDCoordinate(x,y));
				 track = true;
			 }
			 if(listener instanceof MyLocationOverlay && !location){
				 listener.locationChanged(new TwoDCoordinate(x,y));
				 location = true;
			 }
		}
	}
	@Override
	public void handleEvent(EventObject event) {
		// TODO Auto-generated method stub
		if (event == null)
			return;
		// ��λ����¼�
		if (event instanceof LocationResultEvent) {
			LocationResultEvent locationEvent = (LocationResultEvent) event;
			for (LocationChangeListener listener : locationListeners)
				listener.locationChanged(locationEvent.result);
		}
		//���ֵ����¼�
		else if(event instanceof FirePointChangeEvent)
		{
			FirePointChangeEvent firepointevent=(FirePointChangeEvent)event;
			for(LocationChangeListener listener:locationListeners)
				listener.firepointchanged(firepointevent.result);
		}
		//��ȡҽ����Ϣ�¼�
		else if(event instanceof DoctorInformationEvent)
		{
			DoctorInformationEvent result_event=(DoctorInformationEvent)event;
			for(GetDoctorListener listener:getDoctorListeners)
				listener.GetDoctorInfo(result_event.result);
		}
		//�����¼�
		else if(event instanceof NavigateEvent)
		{
			NavigateEvent navigate_event=(NavigateEvent)event;
			for(NavigateRequestListener listener:navigateListeners)
				listener.NavigateRoadRequest(navigate_event.result);
		}
		//��ѯ�¼�
		else if(event instanceof QueryEvent)
		{
			QueryEvent query_event=(QueryEvent)event;
			for(QueryListener listener:queryListeners)
				listener.query_point_name(query_event.result);
		}
		// ��ͼ������Ϣ�����¼�
		else if (event instanceof MapInfoEvent) {
			MapInfoEvent mapEvent = (MapInfoEvent) event;
			for (MapInfoRequestListener listener : mapInfoListeners)
				listener.mapInfoRequested(mapEvent.result.regionID,
						mapEvent.result.tileWidthSize,
						mapEvent.result.tileHeightSize,
						mapEvent.result.mapWidthSize,
						mapEvent.result.mapHeightSize, mapEvent.result.minX,
						mapEvent.result.minY, mapEvent.result.maxX,
						mapEvent.result.maxY, mapEvent.result.minLevel,
						mapEvent.result.maxLevel);
		}
		// �޸������¼�
		else if (event instanceof ChangePasswordEvent) {
			ChangePasswordEvent changeEvent = (ChangePasswordEvent) event;
			for (AccountCheckListener listener : accountListeners)
				listener.changePasswordCheck(changeEvent.isSuccess);
		}
		// ��¼�����֤�¼�
		else if (event instanceof AuthenticationEvent) {
			AuthenticationEvent authenticationEvent = (AuthenticationEvent) event;
			for (AccountCheckListener listener : accountListeners)
				listener.loginCheck(authenticationEvent.result.isSuccess,
						authenticationEvent.result.isUserNameCorrect,
						authenticationEvent.result.isPasswordCorrect);
		}
		// ע���¼�
		else if (event instanceof RegisterEvent) {
			RegisterEvent registerEvent = (RegisterEvent) event;
			for (AccountCheckListener listener : accountListeners)
				listener.registerCheck(registerEvent.isSuccess);
		}
		else if(event instanceof UradioLocationEvent)
		{
			UradioLocationEvent uradioEvent=(UradioLocationEvent)event;
			for(UradioLocationListener listener:uradioLocationListeners)
				listener.getUradioLocation(uradioEvent.coordinate);
		}

	}

	@Override
	public void mapTileRequestCompleted(MapTile tile, Drawable drawable) {
		// TODO Auto-generated method stub
		if (observer != null)
			observer.letInvalidate();
	}

	@Override
	public void mapTileRequestFailed(MapTile tile) {
		// TODO Auto-generated method stub

	}

}
