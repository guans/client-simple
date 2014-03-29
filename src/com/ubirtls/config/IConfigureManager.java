package com.ubirtls.config;

/**
 * 接口 配置管理
 * 
 * @author 胡旭科
 * @version 1.0
 * @deprecated Android的Preference可以很方便的解决设置问题
 */
public interface IConfigureManager {
	/**
	 * 加载配置文件，解析给个配置项
	 * 
	 * @return 加载成功返回true 否则返回false
	 */
	public abstract boolean loadConfigurationFile();

	/**
	 * 将配置项保存
	 * 
	 * @return 保存成功返回true 否则返回false
	 */
	public abstract boolean saveConfigurationFile();
}
