package mealplaner.commons;

import java.util.function.Predicate;

public class Utils {
	public static <T> Predicate<T> not(Predicate<T> p) {
		return t -> !p.test(t);
	}
}
