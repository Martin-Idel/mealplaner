// SPDX-License-Identifier: MIT

package mealplaner.commons;

import java.util.function.Function;

public final class Pair<L, R> {
  public final R right;
  public final L left;

  private Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }

  public static <L, R> Pair<L, R> of(L left, R right) {
    return new Pair<>(left, right);
  }

  public <S> Pair<S, R> mapLeft(Function<L, S> function) {
    return of(function.apply(this.left), this.right);
  }

  public <S> Pair<L, S> mapRight(Function<R, S> function) {
    return of(this.left, function.apply(this.right));
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Pair)) {
      return false;
    }
    Pair<?, ?> otherPair = (Pair<?, ?>) other;
    return this.left.equals(otherPair.left) && this.right.equals(otherPair.right);
  }

  @Override
  public int hashCode() {
    return left.hashCode() ^ right.hashCode();
  }

  @Override
  public String toString() {
    return "{left: " + left + ", right: " + right + "}";
  }
}
