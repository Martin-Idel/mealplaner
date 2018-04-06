package mealplaner.xml.model.v2;

import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.xml.util.UuidAdapter;

public class ProposedMenuXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public UUID entry;
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public UUID main;
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public UUID desert;
  public int numberOfPeople;

  public ProposedMenuXml() {
    this(null, UUID.randomUUID(), null, 0);
  }

  public ProposedMenuXml(UUID entry, UUID main, UUID desert, int numberOfPeople) {
    this.entry = entry;
    this.main = main;
    this.desert = desert;
    this.numberOfPeople = numberOfPeople;
  }
}
