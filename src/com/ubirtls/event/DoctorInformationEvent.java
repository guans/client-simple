package com.ubirtls.event;

import java.util.EventObject;

public class DoctorInformationEvent extends EventObject{
	/**
	 * 继承EventObject类后自动添加该变量
	 */
	private static final long serialVersionUID = 1L;
	
	public static DoctorResult result;
	/**
	 * 
	 * @param source
	 */
	public DoctorInformationEvent(DoctorResult result) {
		super(result);
		// TODO Auto-generated constructor stub
		this.result=result;
	}

}
