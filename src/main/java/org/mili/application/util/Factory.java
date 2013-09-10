package org.mili.application.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Factory {

	private Map<Class<?>, Object> cache = new ConcurrentHashMap<Class<?>, Object>();
	
	protected <T> T create(Class<T> clazz) throws InstantiationException, IllegalAccessException {
		Object instance = cache.get(clazz);
		if (instance == null) {
			instance = clazz.newInstance(); 
		}
		return (T) instance;
	}
	
}
