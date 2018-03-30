package mealplaner.xml.model.v2;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;
import mealplaner.xml.util.UuidAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class IngredientXml {
  @XmlJavaTypeAdapter(value = UuidAdapter.class)
  public UUID uuid;
  public String name;
  public IngredientType type;
  public Measure measure;

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
