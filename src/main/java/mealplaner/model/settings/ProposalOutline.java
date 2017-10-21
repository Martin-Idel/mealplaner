package mealplaner.model.settings;

import java.time.LocalDate;

public class ProposalOutline {
	private final int numberOfDays;
	private final boolean includedToday;
	private final boolean shallBeRandomised;
	private final LocalDate dateToday;

	public ProposalOutline(int numberOfDays, boolean includedToday, boolean shallBeRandomised,
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
}
