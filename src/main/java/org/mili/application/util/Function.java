package org.mili.application.util;

public interface Function<R, P> {
	R exec(P parameter) throws Exception;
}
