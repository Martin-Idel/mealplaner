// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.ingredientextension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.plugins.seasonal.SeasonalityPlugin;
import testcommons.PluginsUtils;

class SeasonalityFactTest {
  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new SeasonalityPlugin());
  }

  @Test
  void defaultConstructorCreatesNonSeasonalFact() {
    SeasonalityFact fact = new SeasonalityFact();

    assertThat(fact.getSeasonality()).isEqualTo(Seasonality.NON_SEASONAL);
    assertThat(fact.getMainSeasonMonths()).isEmpty();
    assertThat(fact.getOffSeasonMonths()).isEmpty();
  }

  @Test
  void constructorWithSeasonalityCreatesCorrectFact() {
    SeasonalityFact fact = new SeasonalityFact(Seasonality.VERY_SEASONAL);

    assertThat(fact.getSeasonality()).isEqualTo(Seasonality.VERY_SEASONAL);
    assertThat(fact.getMainSeasonMonths()).isEmpty();
    assertThat(fact.getOffSeasonMonths()).isEmpty();
  }

  @Test
  void constructorWithSeasonsCreatesCorrectFact() {
    Set<String> mainSeason = new HashSet<>();
    mainSeason.add("JUNE");
    mainSeason.add("JULY");

    Set<String> offSeason = new HashSet<>();
    offSeason.add("JANUARY");
    offSeason.add("FEBRUARY");

    SeasonalityFact fact = new SeasonalityFact(Seasonality.MILDLY_SEASONAL, mainSeason, offSeason);

    assertThat(fact.getSeasonality()).isEqualTo(Seasonality.MILDLY_SEASONAL);
    assertThat(fact.getMainSeasonMonths()).containsExactlyInAnyOrder("JUNE", "JULY");
    assertThat(fact.getOffSeasonMonths()).containsExactlyInAnyOrder("JANUARY", "FEBRUARY");
  }

  @Test
  void setSeasonalityUpdatesSeasonality() {
    SeasonalityFact fact = new SeasonalityFact(Seasonality.NON_SEASONAL);

    fact.setSeasonality(Seasonality.MILDLY_SEASONAL);

    assertThat(fact.getSeasonality()).isEqualTo(Seasonality.MILDLY_SEASONAL);
  }

  @Test
  void setMainSeasonMonthsUpdatesMainSeason() {
    SeasonalityFact fact = new SeasonalityFact();
    Set<String> months = new HashSet<>();
    months.add("JUNE");

    fact.setMainSeasonMonths(months);

    assertThat(fact.getMainSeasonMonths()).containsExactly("JUNE");
  }

  @Test
  void setOffSeasonMonthsUpdatesOffSeason() {
    SeasonalityFact fact = new SeasonalityFact();
    Set<String> months = new HashSet<>();
    months.add("JANUARY");

    fact.setOffSeasonMonths(months);

    assertThat(fact.getOffSeasonMonths()).containsExactly("JANUARY");
  }

  @Test
  void equalityWorksCorrectly() {
    SeasonalityFact fact1 = new SeasonalityFact(Seasonality.VERY_SEASONAL);
    fact1.setMainSeasonMonths(Set.of("JUNE", "JULY"));

    SeasonalityFact fact2 = new SeasonalityFact(Seasonality.VERY_SEASONAL);
    fact2.setMainSeasonMonths(Set.of("JUNE", "JULY"));

    SeasonalityFact fact3 = new SeasonalityFact(Seasonality.MILDLY_SEASONAL);

    assertThat(fact1).isEqualTo(fact2);
    assertThat(fact1).isNotEqualTo(fact3);
  }

  @Test
  void hashCodeWorksCorrectly() {
    SeasonalityFact fact1 = new SeasonalityFact(Seasonality.VERY_SEASONAL);
    fact1.setMainSeasonMonths(Set.of("JUNE"));

    SeasonalityFact fact2 = new SeasonalityFact(Seasonality.VERY_SEASONAL);
    fact2.setMainSeasonMonths(Set.of("JUNE"));

    assertThat(fact1.hashCode()).isEqualTo(fact2.hashCode());
  }

  @Test
  void toStringReturnsSeasonalityString() {
    SeasonalityFact fact = new SeasonalityFact(Seasonality.VERY_SEASONAL);

    String result = fact.toString();

    assertThat(result).isNotEmpty();
  }
}