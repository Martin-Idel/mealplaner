// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.io.xml.util.UuidAdapter;

public class ProposedMenuXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID entry;
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID main;
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID desert;
  public final int numberOfPeople;

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
