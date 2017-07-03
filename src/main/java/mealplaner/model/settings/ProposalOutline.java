package mealplaner.model.settings;

import java.util.Date;

public class ProposalOutline {
	private int numberOfDays;
	private boolean includedToday;
	private boolean shallBeRandomised;
	private Date dateToday;

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
