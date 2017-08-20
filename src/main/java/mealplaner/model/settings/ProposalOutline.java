package mealplaner.model.settings;

import java.util.Date;

public class ProposalOutline {
	private final int numberOfDays;
	private final boolean includedToday;
	private final boolean shallBeRandomised;
	private final Date dateToday;

	public ProposalOutline(int numberOfDays, boolean includedToday, boolean shallBeRandomised,
			Date dateToday) {
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

	public Date getDateToday() {
		return dateToday;
	}
}
