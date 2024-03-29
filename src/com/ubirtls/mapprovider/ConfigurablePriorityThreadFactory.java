/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ubirtls.mapprovider;

import java.util.concurrent.ThreadFactory;

/**
 * 
 * @author �����
 */

public class ConfigurablePriorityThreadFactory implements ThreadFactory {

	private final int mPriority;
	private final String mName;

	public ConfigurablePriorityThreadFactory(int priority, String name) {
		mPriority = priority;
		mName = name;
	}

	public Thread newThread(Runnable runnable) {
		final Thread thread = new Thread(runnable);
		thread.setPriority(mPriority);
		if (mName != null) {
			thread.setName(mName);
		}
		return thread;
	}

}
