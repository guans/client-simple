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
	 * controller的单一实例,初始为null。
	 */
	private static Controller singleInstance = null;

	/**
	 * 账户检查的监听者
	 */
	private List<AccountCheckListener> accountListeners;

	/**
	 * 位置变化的监听者
	 */
	private List<LocationChangeListener> locationListeners;

	/**
	 * web service 请求的监听者
	 */
	private List<WSRequestListener> wsListeners;

	/**
	 * 地图描述信息请求的监听者
	 */
	private List<MapInfoRequestListener> mapInfoListeners;

	/** maker item监听 */
	private List<ItemListener> itemListeners;
	/**医生信息的监听*/
	private List<GetDoctorListener> getDoctorListeners;
	/**导航监听*/
	private List<NavigateRequestListener> navigateListeners;
	/**查询监听*/
	private List<QueryListener> queryListeners;
	/**获取初始位置监听者*/
	private List<UradioLocationListener> uradioLocationListeners;
	/** 观察者 */
	private Observer observer = null;

	/** 轨迹管理监听者 */
	private TrackManangerListener trackListener = null;

	/**
	 * 地图提供的类，用来获取显示的地图，可以是本地也可以是从网络获取。
	 */
	public MapTileProvider mapProvider;

	/**
	 * 通信类，用来向消息中间件发送消息
	 */
	private Communication communication;

	/**
	 * 
	 */
	private static MOMClient client = null;

	/**
	 * 配置管理，用来存取配置信息
	 */
	private IConfigureManager configureManager;
	/**
	 * wifi适配器的mac地址
	 */
	private String mac = null;
	// private SpatialQueryService spatialQueryService;//空间查询服务
	// private RangingService rangingService;//测距服务，主要用于测距请求。
	// private RouteFindService routeFindService;//路径查找服务，主要用于导航路径的请求
	// private IFileManager fileManager;//文件管理，主要用于管理历史查询记录以及收藏点的文件存取。
	/**
	 * 构造函数，初始化成员变量。私有成员函数。
	 */
	private Controller() {
		/* 初始化监听者 */
		accountListeners = new ArrayList<AccountCheckListener>();
		locationListeners = new ArrayList<LocationChangeListener>();
		wsListeners = new ArrayList<WSRequestListener>();
		mapInfoListeners = new ArrayList<MapInfoRequestListener>();
		itemListeners = new ArrayList<ItemListener>();
		getDoctorListeners=new ArrayList<GetDoctorListener>();
		navigateListeners=new ArrayList<NavigateRequestListener>();
		queryListeners=new ArrayList<QueryListener>();
		uradioLocationListeners=new ArrayList<UradioLocationListener>();
		// 设置map provider责任链，先从本地获取MapTile,再从webservice方式获取
		mapProvider = new MapTileFileProvider();
		mapProvider.setNext(new MapTileDownloader());
		mapProvider.setCallback(this);
		/* mapProvider = new MapTileDownloader(); */
		mapInfoListeners.add(mapProvider);
		mapInfoListeners.add(new TileSystem());
	}

	/**
	 * 获得Controller的单一实例对象
	 * 
	 * @return Controller类的唯一实例
	 */
	public static synchronized Controller getInstance() {
		if (singleInstance == null)
			singleInstance = new Controller();
		return singleInstance;
	}

	/**
	 * 添加账户检查监听者
	 * 
	 * @param listener 账户检查监听者
	 */
	public void addAccountCheckListerner(AccountCheckListener listener) {
		if (listener != null)
			accountListeners.add(listener);
	}

	/**
	 * 添加位置变化监听者
	 * 
	 * @param listener 位置变化监听者
	 */
	public void addLocationChangeListerner(LocationChangeListener listener) {
		if (listener != null)
			locationListeners.add(listener);
	}

	/**
	 * 添加web service请求监听者
	 * 
	 * @param listener web service请求监听者
	 */
	public void addWSRequestListerner(WSRequestListener listener) {
		if (listener != null)
			wsListeners.add(listener);
	}

	/**
	 * 添加MapInfo请求监听者
	 * 
	 * @param listener MapInfoRequestListener对象
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
	 * 添加获取医生信息的监听者
	 * @param listener GetDoctorListener对象
	 */
	public void addGetDoctorInfoListener(GetDoctorListener listener)
	{
		if(listener!=null)
			getDoctorListeners.add(listener);
	}
	/**
	 * 添加导航请求的监听者
	 * @param listener NavigateRequestListener导航请求对象
	 */
	public void addNavigateRequestListener(NavigateRequestListener listener)
	{
		if(listener!=null)
			navigateListeners.add(listener);
	}
	/**
	 * 添加查询请求监听者
	 * @param listener QueryListener查询请求对象
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
	 * 设置观察者 当有瓦片数据等需要更新时会通知该观察者
	 * 
	 * @param observer Observer对象
	 */
	public void setInvalidateObserver(Observer observer) {
		this.observer = observer;
	}

	/**
	 * 设置轨迹监听者
	 * 
	 * @param trackListener {@link TrackManangerListener}对象 用于通知对轨迹进行处理
	 */
	public void setTrackListener(TrackManangerListener trackListener) {
		this.trackListener = trackListener;
	}

	/**
	 * 添加标签Item
	 * 
	 * @param item OverlayItem对象
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
	 * 使用之前检测communication是否已初始化，若未初始化，则先进行初始化
	 * 
	 * @throws MessageITException
	 * @throws IOException
	 */
	private void checkCommunication() throws IOException, MessageITException {
		// 需要communication的时候再初始化
		if (communication == null) {
			client = new MOMClient(Setting.IP_ADDRESS, Setting.PORT, Setting.MAC); 
			MessageListenerImpl messageListener = new MessageListenerImpl();
			messageListener.addEventHandler(this);
			client.setMessageListener(messageListener);
			/* 获取用户名作为消息中间件中的客户端标识 */
			client.connect();
			communication = new Communication(client);		
		}
	}
	/**
	 * 检查是否已经和消息中间件建立连接
	 * 
	 * @return 已经建立连接就返回true 否则返回false
	 */
	private boolean isConnected() {
		if ((communication != null) && (client != null))
			return true;
		return false;
	}

	/**
	 * 关闭和消息中间件的通信 关闭连接
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
	 * 登录函数。会调用Communication相应的函数实现。
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception {
		
		checkCommunication();
		communication.login(username, password);
		
		//checkClient();
		//sender.login(username, password);
	}
   
	/**
	 * 注册函数。会调用ServiceEngine相应的函数实现。
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @throws Exception 
	 */
	public void register(String username, String password, String email)
			throws Exception {
		checkCommunication();
		if (communication != null)
			communication.register(username, password, email);
	}
	/**
	 * 注销函数
	 * @param username 用户名
	 * @throws MessageITException 
	 * @throws IOException 
	 */
	public void logout(String username) throws IOException, MessageITException{
		checkCommunication();
		if (communication != null)
			communication.logout(username);
	}
	/**
	 * 导航函数.会调用相应的Communicate中的函数进行通信
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
	 * 查询。关键字查询、匹配查询等
	 * @param point_name 用户输入的查询名称
	 * @throws Exception 
	 */
	public void name_Query(String point_name) throws Exception
	{
		checkCommunication();
		if(communication!=null)
			communication.name_Query(point_name);
	}
	/**
	 * 请求获取自身的位置信息
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
	 * 获取医生基本信息的请求
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
	 * 紧急情况的下的导航路径请求
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
	 * 通过坐标点进行查询
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
	 * 客户端的定位数据发送到服务器
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
	 * 请求地图描述信息
	 */
	public void requestMapInfo() {
		/*
		 * checkCommunication(); communication.requestMapInfo(0);
		 */}

	/**
	 * 设置定位时的滤波类型，以此改进定位精度
	 * 
	 * @param filterType 滤波类型
	 */
	void setFilter(String filterType) {

	}

	/**
	 * 加载配置项，在应用程序启动时调用，主要设置Setting中的静态成员变量。
	 * 
	 * @return 设置项加载成返回true 否则返回false
	 */
	public boolean loadConfigure() {
		if (configureManager == null)
			configureManager = new XMLConfigurationManager(null);
		return configureManager.loadConfigurationFile();
	}

	/**
	 * 保存配置项，在应用程序退出时，将Setting中的静态成员变量保存在配置文件中
	 */
	public void saveConfigure() {
		if (configureManager == null)
			configureManager = new XMLConfigurationManager(null);
		configureManager.saveConfigurationFile();
	}

	/**
	 * spotter初始化并打开
	 * 
	 * @param context Context对象
	 */



	/**
	 * 调用{@link com.ubirtls.mapprovider.MapTileProvider}的
	 * getDrawableAsynchronous方法
	 * 
	 * @param tile 瓦片数据标示
	 * @return Drawable对象
	 */
	public Drawable getMapTileAsync(final MapTile tile) {
		if (mapProvider != null)
			return mapProvider.getDrawableAsynchronous(tile);
		return null;
	}

	/**
	 * 获得瓦片 tile对应的可绘制对象 同步方式
	 * 
	 * @param tile 用来标识一个瓦片
	 * @return 该瓦片的可绘制对象
	 */
	public Drawable getMapTile(final MapTile tile) {
		if (mapProvider != null)
			return mapProvider.getDrawableSynchronism(tile);
		return null;
	}

	/**
	 * 清除内存中的缓存数据
	 */
	public void clearData() {
		if (mapProvider != null)
			mapProvider.clearRoomData();
	}

	/**
	 * 清除本地缓存的地图、图片
	 */
	public void deleteLocalMap() {
		if (mapProvider != null)
			mapProvider.clearMapData();
	}

	/**
	 * 确保瓦片缓存区足够大
	 * 
	 * @param num 需要缓存的瓦片数量
	 */
	public void ensureCapacity(int num) {
		mapProvider.ensureCapacity(num);
	}

	/**
	 * 清空用户轨迹
	 */
	public void clearTrack() {
		if (trackListener != null)
			trackListener.trackIsCleared();
	}

	// public void getRoute(Coordinate startPoint, Coordinate
	// endPoint){}//获得从起点到终点的一条导航路径。通知方式，异步。
	// public RouteInfo getRoute(Coordinate startPoint, Coordinate
	// endPoint){}//获得从起点到终点的一条导航路径。调用返回，同步方式。
	// public void getDistance(Coordinate startPoint, Coordinate
	// endPoint){}//获得从起点到终点的距离。通知方式，异步。
	// public double getDistance(Coordinate startPoint, Coordinate
	// endPoint){}//获得从起点到终点的距离。调用返回，同步方式。
	// public void searchPOIByCoordinate(Coordinate
	// point){}//获得某个点的详细信息。通知方式，异步。
	// public POIInfo searchPOIByCoordinate(Coordinate
	// point){}//获得某个点的详细信息。调用返回，同步方式。
	// public void searchPOIByKeyWords (String keywords){}//按关键字搜索。通知方式，异步。
	// public POIInfo[] searchPOIByKeyWords (String keywords){}//
	// 按关键字搜索。调用返回，同步方式。
	// public void searchPOIByAleph (String aleph)//按首字母搜索。通知方式，异步。
	// public POIInfo[] searchPOIByAleph (String keywords){}// 按首字母搜索。调用返回，同步方式。
	// public void searchPOIByInfrastructure (String
	// Infrastructure){}//按基础设施搜索。通知方式，异步。
	// public POIInfo[] searchPOIByInfrastructure (String Infrastructure){}//
	// 按基础设施搜索。调用返回，同步方式。
	// public void searchPOIByHistory (){}//按历史记录搜索。通知方式，异步。
	// public POIInfo[] searchPOIByHistory (){}// 按历史记录搜索。调用返回，同步方式。
	// public void searchPOIByCollect (int type){}//按收藏类型搜索。通知方式，异步。
	// public POIInfo[] searchPOIByCollect (String keywords){}//
	// 按收藏类型搜索。调用返回，同步方式。
	// public void clearHistoryRecords(){}//清空历史记录
	// public void deleteCurrentPath(){}//删除当前导航路径
	// public void saveCollectRecords(int type, String POIInfo){}//按类型保存收藏点
	// public void clearCollectRecords(int type){}//按类型清空收藏点
	/**
	 * 设置字体大小，小、中、大
	 * 
	 * @param type
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setFontSize(int type) {

	}

	/**
	 * 设置语言，简体、繁体、英语
	 * 
	 * @param type 标识 语言类型
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setLanguage(int type) {

	}

	/**
	 * 设置视图模式
	 * 
	 * @param type 标识视图模式类型
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setViewMode(int type) {

	}

	/**
	 * 设置显示模式
	 * 
	 * @param type 显示模式类型
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setShowMode(int type) {

	}

	/**
	 * 设置模拟速度
	 * 
	 * @param type 模拟速度类型
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setSimulationSpeed(int type) {

	}

	/**
	 * 设置是否监控
	 * 
	 * @param boolMonitor true表示允许被监控 false表示不允许被监控
	 * @deprecated 配置文件的功能已经使用Android的SharedPreferences代替
	 */
	public void setMonitorMode(boolean boolMonitor) {

	}

	/**
	 * 恢复出厂设置。
	 * 
	 * @deprecated 修改后未使用该函数
	 */
	public void recoverFactorySetting() {

	}

	/**
	 * 设置通信参数，包括套接字通信的IP地址，端口号。服务引擎的标识，定位引擎的标识
	 * 
	 * @param ip ip地址
	 * @param port 端口号
	 * @param serverName 服务引擎的标识
	 * @param locationName 定位引擎的标识
	 */
	public void setCommInfo(String ip, int port, String serverName,
			String locationName) {
		// ip地址或者端口号改变，如果已经建立连接需要关闭连接再打开
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
	 * 打开数据库
	 */
	public void openDatabase() {
	}

	/**
	 * 关闭数据库
	 */
	public void closeDatabase() {

	}

	/**
	 * 执行sql语句
	 * 
	 * @param sql 需要执行的sql语句
	 */
	public void executeSql(String sql) {

	}
	/**
	 * 返回地图列表
	 * @return 本地缓存的所有地图列表——地图名称
	 */
	public String[] getMapList(){
		return mapProvider.BrowserMap();
	}
	/**
	 * 主动更换地图
	 * @param region 更换地图对应的区域
	 * @return 更换地图成功 返回true 否则返回false
	 */
	public boolean changeMap(String region) {
		if (region != null) {
			//先保存当前的值
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
	 * 產生新的偏移結果（航位推算）
	 * @param stepLength 步長
	 * @param heading 朝向
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
		// 定位结果事件
		if (event instanceof LocationResultEvent) {
			LocationResultEvent locationEvent = (LocationResultEvent) event;
			for (LocationChangeListener listener : locationListeners)
				listener.locationChanged(locationEvent.result);
		}
		//火灾点结果事件
		else if(event instanceof FirePointChangeEvent)
		{
			FirePointChangeEvent firepointevent=(FirePointChangeEvent)event;
			for(LocationChangeListener listener:locationListeners)
				listener.firepointchanged(firepointevent.result);
		}
		//获取医生信息事件
		else if(event instanceof DoctorInformationEvent)
		{
			DoctorInformationEvent result_event=(DoctorInformationEvent)event;
			for(GetDoctorListener listener:getDoctorListeners)
				listener.GetDoctorInfo(result_event.result);
		}
		//导航事件
		else if(event instanceof NavigateEvent)
		{
			NavigateEvent navigate_event=(NavigateEvent)event;
			for(NavigateRequestListener listener:navigateListeners)
				listener.NavigateRoadRequest(navigate_event.result);
		}
		//查询事件
		else if(event instanceof QueryEvent)
		{
			QueryEvent query_event=(QueryEvent)event;
			for(QueryListener listener:queryListeners)
				listener.query_point_name(query_event.result);
		}
		// 地图描述信息请求事件
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
		// 修改密码事件
		else if (event instanceof ChangePasswordEvent) {
			ChangePasswordEvent changeEvent = (ChangePasswordEvent) event;
			for (AccountCheckListener listener : accountListeners)
				listener.changePasswordCheck(changeEvent.isSuccess);
		}
		// 登录身份认证事件
		else if (event instanceof AuthenticationEvent) {
			AuthenticationEvent authenticationEvent = (AuthenticationEvent) event;
			for (AccountCheckListener listener : accountListeners)
				listener.loginCheck(authenticationEvent.result.isSuccess,
						authenticationEvent.result.isUserNameCorrect,
						authenticationEvent.result.isPasswordCorrect);
		}
		// 注册事件
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
