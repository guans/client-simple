package com.ubirtls;

import com.ubirtls.event.DoctorResult;

public interface GetDoctorListener {
	/**
	 * �û�����û����ļ���
	 * @param username ҽ�����û���
	 * @param phone ҽ�����ֻ�����
	 */
	public abstract void GetDoctorInfo(DoctorResult reselt);
	
}
