package syntax_analyzer.global_helpers;

import java.util.Collection;

public class ArrayHelper {
	public static <T> boolean inArray(T obj, Collection<T> collection) {
		for(T elem: collection) {
			if(elem.equals(obj)) {
				return true;
			}
		}
		
		return false;
	}
}
