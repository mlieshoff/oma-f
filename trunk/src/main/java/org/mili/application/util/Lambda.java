package org.mili.application.util;

public interface Lambda<T> {
	T exec(Object... params) throws Exception;
}
