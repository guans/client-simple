package com.ubirtls;

import com.ubirtls.event.DoctorResult;

public interface GetDoctorListener {
	/**
	 * 用户获得用户名的监听
	 * @param username 医生的用户名
	 * @param phone 医生的手机号码
	 */
	public abstract void GetDoctorInfo(DoctorResult reselt);
	
}
