package mealplaner.commons;

import java.util.function.Predicate;

public final class Utils {
  private Utils() {
  }

  public static <T> Predicate<T> not(Predicate<T> p) {
    return t -> !p.test(t);
  }
}
