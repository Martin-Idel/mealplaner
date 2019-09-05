// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import static java.util.UUID.nameUUIDFromBytes;
import static mealplaner.model.recipes.Measures.DEFAULT_MEASURES;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.io.xml.util.UuidAdapter;
import mealplaner.model.recipes.IngredientType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IngredientXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public final UUID uuid;
  public final String name;
  public final IngredientType type;
  public final MeasuresXml measures;

  public IngredientXml() {
    this(nameUUIDFromBytes("noname".getBytes(StandardCharsets.UTF_8)), "noname", IngredientType.OTHER,
        new MeasuresXml(DEFAULT_MEASURES));
  }

  public IngredientXml(UUID uuid, String name, IngredientType type, MeasuresXml measures) {
    this.uuid = uuid;
    this.name = name;
    this.type = type;
    this.measures = measures;
  }
}
