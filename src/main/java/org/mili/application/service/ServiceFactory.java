package org.mili.application.service;

import org.mili.application.util.Factory;


public class ServiceFactory extends Factory {
	private static final ServiceFactory INSTANCE = new ServiceFactory();

	public static <T> T getService(Class<T> clazz) {
		try {
			return INSTANCE.create(clazz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
}
