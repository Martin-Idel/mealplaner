// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.ingredientextension;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SeasonalityFact implements IngredientFact, IngredientFactXml {
  private Seasonality seasonality;
  private Set<String> mainSeasonMonths;
  private Set<String> offSeasonMonths;

  public SeasonalityFact() {
    this.seasonality = Seasonality.NON_SEASONAL;
    this.mainSeasonMonths = new HashSet<>();
    this.offSeasonMonths = new HashSet<>();
  }

  public SeasonalityFact(Seasonality seasonality) {
    this.seasonality = seasonality;
    this.mainSeasonMonths = new HashSet<>();
    this.offSeasonMonths = new HashSet<>();
  }

  public SeasonalityFact(Seasonality seasonality, Set<String> mainSeasonMonths, Set<String> offSeasonMonths) {
    this.seasonality = seasonality;
    this.mainSeasonMonths = new HashSet<>(mainSeasonMonths);
    this.offSeasonMonths = new HashSet<>(offSeasonMonths);
  }

  public Seasonality getSeasonality() {
    return seasonality;
  }

  public Set<String> getMainSeasonMonths() {
    return unmodifiableSet(mainSeasonMonths);
  }

  public Set<String> getOffSeasonMonths() {
    return unmodifiableSet(offSeasonMonths);
  }

  public void setSeasonality(Seasonality seasonality) {
    this.seasonality = seasonality;
  }

  public void setMainSeasonMonths(Set<String> months) {
    this.mainSeasonMonths = new HashSet<>(months);
  }

  public void setOffSeasonMonths(Set<String> months) {
    this.offSeasonMonths = new HashSet<>(months);
  }

  public Map<String, Set<String>> getSeasons() {
    Map<String, Set<String>> seasons = new HashMap<>();
    seasons.put("main", new HashSet<>(mainSeasonMonths));
    seasons.put("off", new HashSet<>(offSeasonMonths));
    return seasons;
  }

  public void setSeasons(Map<String, Set<String>> seasons) {
    this.mainSeasonMonths = new HashSet<>(seasons.getOrDefault("main", emptySet()));
    this.offSeasonMonths = new HashSet<>(seasons.getOrDefault("off", emptySet()));
  }

  @Override
  public IngredientFactXml convertToXml() {
    return this;
  }

  @Override
  public IngredientFact convertToFact() {
    return this;
  }

  @Override
  public String toString() {
    return seasonality.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeasonalityFact that = (SeasonalityFact) o;
    return seasonality == that.seasonality
        && mainSeasonMonths.equals(that.mainSeasonMonths)
        && offSeasonMonths.equals(that.offSeasonMonths);
  }

  @Override
  public int hashCode() {
    int result = seasonality != null ? seasonality.hashCode() : 0;
    result = 31 * result + mainSeasonMonths.hashCode();
    result = 31 * result + offSeasonMonths.hashCode();
    return result;
  }
}
