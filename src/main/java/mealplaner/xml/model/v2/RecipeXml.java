package mealplaner.xml.model.v2;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.xml.util.UuidMapAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecipeXml {
  public int numberOfPortions;
  @XmlJavaTypeAdapter(value = UuidMapAdapter.class)
  public Map<UUID, Integer> uuid;

  public RecipeXml() {
    this(1, new HashMap<>());
  }

  public RecipeXml(Integer numberOfPortions, Map<UUID, Integer> uuid) {
    this.numberOfPortions = numberOfPortions;
    this.uuid = uuid;
  }
}
