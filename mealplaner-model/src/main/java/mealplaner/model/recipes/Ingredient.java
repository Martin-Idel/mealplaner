// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static java.util.UUID.nameUUIDFromBytes;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.Element;

import mealplaner.commons.BundleStore;
import mealplaner.plugins.api.IngredientFact;

public final class Ingredient {
  private static final Ingredient EMPTY_INGREDIENT = new Ingredient(
      nameUUIDFromBytes("<EMPTY>".getBytes(StandardCharsets.UTF_8)),
      BundleStore.BUNDLES.message("emptyIngredient"),
      IngredientType.OTHER,
      Measures.DEFAULT_MEASURES,
      new HashMap<>(),
      new ArrayList<>());

  private final UUID uuid;
  private final String name;
  private final IngredientType type;
  private final Measures measures;
  private final Map<Class, IngredientFact> ingredientFacts;
  private final List<Element> hiddenIngredientFacts;

  private Ingredient(
      UUID uuid,
      String name,
      IngredientType type,
      Measures measures,
      Map<Class, IngredientFact> ingredientFacts,
      List<Element> hiddenIngredientFacts) {
    this.uuid = uuid;
    this.name = name;
    this.type = type;
    this.measures = measures;
    this.ingredientFacts = ingredientFacts;
    this.hiddenIngredientFacts = hiddenIngredientFacts;
  }

  static Ingredient ingredientWithUuid(
      UUID uuid,
      String name,
      IngredientType type,
      Measures measures,
      Map<Class, IngredientFact> ingredientFacts,
      List<Element> hiddenIngredientFacts) {
    return new Ingredient(uuid, name, type, measures, ingredientFacts, hiddenIngredientFacts);
  }

  public static Ingredient emptyIngredient() {
    return EMPTY_INGREDIENT;
  }

  public UUID getId() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public IngredientType getType() {
    return type;
  }

  public Measures getMeasures() {
    return measures;
  }

  public Map<Class, IngredientFact> getIngredientFacts() {
    return new HashMap<>(ingredientFacts);
  }

  public List<Element> getHiddenFacts() {
    return new ArrayList<>(hiddenIngredientFacts);
  }

  public Measure getPrimaryMeasure() {
    return measures.getPrimaryMeasure();
  }

  public boolean equalIds(Ingredient ingredient) {
    return this.uuid.equals(ingredient.getId());
  }

  /**
   * N.B.: Return value may be null
   *
   * @param name class of the ingredient fact
   * @return IngredientFact corresponding to the class if available (null otherwise)
   */
  public IngredientFact getIngredientFact(Class name) {
    return ingredientFacts.get(name);
  }

  @SuppressWarnings("unchecked")
  public <T> T getTypedIngredientFact(Class<T> name) {
    return (T)ingredientFacts.get(name);
  }


  @Override
  public String toString() {
    return "Ingredient{" + "uuid=" + uuid
        + ", name='" + name + '\'' + ", type=" + type + ", measures=" + measures
        + ", ingredientFacts=" + ingredientFacts + ", hiddenIngredientFacts=" + hiddenIngredientFacts + '}';
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uuid.hashCode();
    result = prime * result + measures.hashCode();
    result = prime * result + name.hashCode();
    result = prime * result + type.hashCode();
    result = prime * result + ingredientFacts.hashCode();
    result = prime * result + hiddenIngredientFacts.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Ingredient other = (Ingredient) obj;
    return uuid.equals(other.uuid)
        && measures.equals(other.measures)
        && name.equals(other.name)
        && type == other.type
        && ingredientFacts.equals(other.ingredientFacts)
        && hiddenIngredientFacts.equals(other.hiddenIngredientFacts);
  }
}
