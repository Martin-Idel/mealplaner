package mealplaner.recipes.model;

public class Ingredient {
  private final String name;
  private final IngredientType type;
  private final Measure measure;

  public Ingredient() {
    this("no name", IngredientType.OTHER, Measure.NONE);
  }

  private Ingredient(String name, IngredientType type, Measure measure) {
    this.name = name;
    this.type = type;
    this.measure = measure;
  }

  public static Ingredient ingredient(String name, IngredientType type, Measure measure) {
    return new Ingredient(name, type, measure);
  }

  public static Ingredient emptyIngredient() {
    return new Ingredient("", IngredientType.OTHER, Measure.NONE);
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
    return "Ingredient [name=" + name + ", type=" + type + ", measure=" + measure
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((measure == null) ? 0 : measure.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    return measure == other.measure && name.equals(other.name) && type == other.type;
  }
}
