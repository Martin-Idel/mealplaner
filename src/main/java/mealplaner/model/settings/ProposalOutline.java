package mealplaner.model.settings;

import java.time.LocalDate;

public class ProposalOutline {
	private final int numberOfDays;
	private final boolean includedToday;
	private final boolean shallBeRandomised;
	private final LocalDate dateToday;

	private ProposalOutline(int numberOfDays, boolean includedToday, boolean shallBeRandomised,
			LocalDate dateToday) {
		this.numberOfDays = numberOfDays;
		this.includedToday = includedToday;
		this.shallBeRandomised = shallBeRandomised;
		this.dateToday = dateToday;
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public boolean isIncludedToday() {
		return includedToday;
	}

	public boolean isShallBeRandomised() {
		return shallBeRandomised;
	}

	public LocalDate getDateToday() {
		return dateToday;
	}

	public static class ProposalOutlineBuilder {
		private final int numberOfDays;
		private boolean includedToday = false;
		private boolean shallBeRandomised = false;
		private final LocalDate dateToday;

		private ProposalOutlineBuilder(int numberOfDays, LocalDate dateToday) {
			this.numberOfDays = numberOfDays;
			this.dateToday = dateToday;
		}

		public static ProposalOutlineBuilder of(int numberOfDays, LocalDate dateToday) {
			return new ProposalOutlineBuilder(numberOfDays, dateToday);
		}

		public ProposalOutlineBuilder includeToday() {
			this.includedToday = true;
			return this;
		}

		public ProposalOutlineBuilder randomise() {
			shallBeRandomised = false;
			return this;
		}

		public ProposalOutline build() {
			return new ProposalOutline(numberOfDays, includedToday, shallBeRandomised, dateToday);
		}
	}
}
