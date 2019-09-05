// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static java.util.UUID.nameUUIDFromBytes;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class Ingredient {
  private static final Ingredient EMPTY_INGREDIENT = new Ingredient(
      nameUUIDFromBytes("".getBytes(StandardCharsets.UTF_8)),
      "",
      IngredientType.OTHER,
      Measures.DEFAULT_MEASURES);

  private final UUID uuid;
  private final String name;
  private final IngredientType type;
  private final Measures measures;

  private Ingredient() {
    this(UUID.randomUUID(), "no name", IngredientType.OTHER, Measures.DEFAULT_MEASURES);
  }

  private Ingredient(UUID uuid, String name, IngredientType type, Measures measures) {
    this.uuid = uuid;
    this.name = name;
    this.type = type;
    this.measures = measures;
  }

  public static Ingredient ingredient(String name, IngredientType type, Measures measures) {
    return new Ingredient(UUID.randomUUID(), name, type, measures);
  }

  public static Ingredient ingredientWithUuid(UUID uuid, String name, IngredientType type,
                                              Measures measures) {
    return new Ingredient(uuid, name, type, measures);
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

  public Measure getPrimaryMeasure() {
    return measures.getPrimaryMeasure();
  }

  public boolean equalIds(Ingredient ingredient) {
    return this.uuid.equals(ingredient.getId());
  }

  @Override
  public String toString() {
    return "Ingredient [uuid=" + uuid.toString() + ", name=" + name + ", type=" + type
        + ", measures=" + measures.toString() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uuid.hashCode();
    result = prime * result + measures.hashCode();
    result = prime * result + name.hashCode();
    result = prime * result + type.hashCode();
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
        && type == other.type;
  }
}
