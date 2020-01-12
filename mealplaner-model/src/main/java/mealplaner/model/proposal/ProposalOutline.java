// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import java.time.LocalDate;
import java.util.Objects;

public final class ProposalOutline {
  private final int numberOfDays;
  private final boolean includedToday;
  private final boolean shallBeRandomised;
  private final LocalDate dateToday;
  private final boolean takeDefaultSettings;

  private ProposalOutline(int numberOfDays, boolean includedToday, boolean shallBeRandomised,
                          boolean takeDefaultSettings, LocalDate dateToday) {
    this.numberOfDays = numberOfDays;
    this.includedToday = includedToday;
    this.shallBeRandomised = shallBeRandomised;
    this.takeDefaultSettings = takeDefaultSettings;
    this.dateToday = dateToday;
  }

  private static ProposalOutline outline(int numberOfDays, boolean includedToday, boolean shallBeRandomised,
                                         boolean takeDefaultSettings, LocalDate dateToday) {
    return new ProposalOutline(numberOfDays, includedToday, shallBeRandomised, takeDefaultSettings,
        dateToday);
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

  public boolean takeDefaultSettings() {
    return takeDefaultSettings;
  }

  public static class ProposalOutlineBuilder {
    private final int numberOfDays;
    private boolean includedToday = false;
    private boolean shallBeRandomised = false;
    private boolean takeDefaultSettings = false;
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
      shallBeRandomised = true;
      return this;
    }

    public ProposalOutlineBuilder takeDefaultSettings() {
      takeDefaultSettings = true;
      return this;
    }

    public ProposalOutline build() {
      return outline(numberOfDays, includedToday, shallBeRandomised, takeDefaultSettings,  // NOPMD
          dateToday);  // NOPMD
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProposalOutline that = (ProposalOutline) o;
    return numberOfDays == that.numberOfDays
        && includedToday == that.includedToday
        && shallBeRandomised == that.shallBeRandomised
        && takeDefaultSettings == that.takeDefaultSettings
        && Objects.equals(dateToday, that.dateToday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numberOfDays, includedToday, shallBeRandomised, dateToday, takeDefaultSettings);
  }
}
