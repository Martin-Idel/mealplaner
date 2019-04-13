// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v2;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.io.xml.util.UuidAdapter;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IngredientXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID uuid;
  public final String name;
  public final IngredientType type;
  public final Measure measure;

  public IngredientXml() {
    this(nameUUIDFromBytes("noname".getBytes(forName("UTF-8"))), "noname", IngredientType.OTHER,
        Measure.NONE);
  }

  public IngredientXml(UUID uuid, String name, IngredientType type, Measure measure) {
    this.uuid = uuid;
    this.name = name;
    this.type = type;
    this.measure = measure;
  }
}
