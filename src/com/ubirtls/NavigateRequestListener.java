package com.ubirtls;

import com.ubirtls.event.NavigateResult;

public interface NavigateRequestListener {
	/**
	 * 导航路线请求
	 * @param result 导航路线对象
	 */
	public abstract void NavigateRoadRequest(NavigateResult result);

}
