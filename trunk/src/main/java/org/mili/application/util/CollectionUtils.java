package org.mili.application.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtils {

	public static <T> Set<T> subtract(Set<T> first, Set<T> second) {
		Set<T> result = new HashSet<T>(first);
		for (T element : second) {
			if (first.contains(element)) {
				result.remove(first);
			}
		}
		return result;
	}

}
