package mealplaner.plugins.vegetarian.ingredientextension;

import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SeasonalFact implements IngredientFact, IngredientFactXml {

  @XmlElementWrapper(name = "inSeason")
  @XmlElement(name = "month")
  public Set<Month> inSeason;

  @XmlElementWrapper(name = "offSeason")
  @XmlElement(name = "month")
  public Set<Month> offSeason;

  public boolean strongSeasonalAffinity;

  public SeasonalFact() {
    inSeason = new HashSet<>();
    offSeason = new HashSet<>();
    strongSeasonalAffinity = false;
  }

  public SeasonalFact(Set<Month> inSeason, Set<Month> offSeason, boolean strongSeasonalAffinity) {
    this.inSeason = inSeason;
    this.offSeason = offSeason;
    this.strongSeasonalAffinity = strongSeasonalAffinity;
  }

  @Override
  public IngredientFactXml convertToXml() {
    return this;
  }

  @Override
  public IngredientFact convertToFact() {
    return this;
  }

  public boolean inSeason(Month month) {
    return inSeason.contains(month) || offSeason.contains(month);
  }

  public boolean inHighSeason(Month month) {
    return inSeason.contains(month);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SeasonalFact that = (SeasonalFact) o;

    return strongSeasonalAffinity == that.strongSeasonalAffinity
        && inSeason.equals(that.inSeason)
        && offSeason.equals(that.offSeason);
  }

  @Override
  public int hashCode() {
    int result = inSeason.hashCode();
    result = 31 * result + offSeason.hashCode();
    result = 31 * result + (strongSeasonalAffinity ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SeasonalFact{" +
        "inSeason=" + inSeason +
        ", offSeason=" + offSeason +
        ", strongSeasonalAffinity=" + strongSeasonalAffinity +
        '}';
  }
}
