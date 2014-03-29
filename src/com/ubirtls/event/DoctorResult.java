package com.ubirtls.event;

public class DoctorResult {
	/**医生的姓名信息*/
	public String username;
	/**医生的电话号码*/
	public String phone;
	/**
	 * 医生信息的类
	 * @param username 医生的名称
	 * @param phone 医生的电话号码
	 */
	public DoctorResult(String username,String phone)
	{
		this.username=username;
		this.phone=phone;
	}
}
