package org.mili.application.dao;

import org.mili.application.util.Factory;


public class DaoFactory extends Factory {
	private static final DaoFactory INSTANCE = new DaoFactory();

	public static <T> T getDao(Class<T> clazz) {
		try {
			return INSTANCE.create(clazz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
}
