// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.commons.NonnegativeFraction.ZERO;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.io.xml.util.NonnegativeFractionAdapter;
import mealplaner.io.xml.util.UuidAdapter;
import mealplaner.model.recipes.Measure;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuantitativeIngredientXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID uuid;
  @XmlJavaTypeAdapter(value = NonnegativeFractionAdapter.class)
  public final NonnegativeFraction amount;
  public final Measure measure;

  public QuantitativeIngredientXml() {
    this(nameUUIDFromBytes("noname".getBytes(StandardCharsets.UTF_8)), ZERO, Measure.NONE);
  }

  public QuantitativeIngredientXml(UUID uuid, NonnegativeFraction amount, Measure measure) {
    this.uuid = uuid;
    this.amount = amount;
    this.measure = measure;
  }
}
