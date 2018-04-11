package mealplaner.model.recipes;

import static java.nio.charset.Charset.forName;
import static java.util.UUID.nameUUIDFromBytes;

import java.util.UUID;

public class Ingredient {
  private static final Ingredient EMPTY_INGREDIENT = new Ingredient(
      nameUUIDFromBytes("".getBytes(forName("UTF-8"))),
      "",
      IngredientType.OTHER,
      Measure.NONE);

  private final UUID uuid;
  private final String name;
  private final IngredientType type;
  private final Measure measure;

  private Ingredient() {
    this(UUID.randomUUID(), "no name", IngredientType.OTHER, Measure.NONE);
  }

  private Ingredient(UUID uuid, String name, IngredientType type, Measure measure) {
    this.uuid = uuid;
    this.name = name;
    this.type = type;
    this.measure = measure;
  }

  public static Ingredient ingredient(String name, IngredientType type, Measure measure) {
    return new Ingredient(UUID.randomUUID(), name, type, measure);
  }

  public static Ingredient ingredientWithUuid(UUID uuid, String name, IngredientType type,
      Measure measure) {
    return new Ingredient(uuid, name, type, measure);
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

  public Measure getMeasure() {
    return measure;
  }

  @Override
  public String toString() {
    return "Ingredient [uuid=" + uuid.toString() + ", name=" + name + ", type=" + type
        + ", measure=" + measure + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + uuid.hashCode();
    result = prime * result + measure.hashCode();
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
        && measure == other.measure
        && name.equals(other.name)
        && type == other.type;
  }
}
