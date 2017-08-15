package mealplaner.commons;

public class Pair<L, R> {
	public final R right;
	public final L left;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public static <L, R> Pair<L, R> of(L left, R right) {
		return new Pair<L, R>(left, right);
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
}
