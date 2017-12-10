package mealplaner.model;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XmlHelpers.createTextNode;
import static mealplaner.io.XmlHelpers.readEnum;
import static mealplaner.recipes.model.Recipe.createRecipe;

import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.XmlHelpers;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.model.Recipe;

public class Meal implements Comparable<Meal> {
  public static final Meal EMPTY_MEAL = new Meal("EMPTY",
      CookingTime.SHORT,
      Sidedish.NONE,
      ObligatoryUtensil.CASSEROLE,
      CookingPreference.RARE, ZERO, "", empty());

  private static final Logger logger = LoggerFactory.getLogger(Settings.class);

  private String name;
  private CookingTime cookingTime;
  private Sidedish sidedish;
  private ObligatoryUtensil obligatoryUtensil;
  private CookingPreference cookingPreference;
  private NonnegativeInteger daysPassed;
  private String comment;
  private Optional<Recipe> recipe;

  private Meal(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      NonnegativeInteger daysPassed,
      String comment,
      Optional<Recipe> recipe)
      throws MealException {
    setName(name);
    this.cookingTime = cookingTime;
    this.sidedish = sideDish;
    this.obligatoryUtensil = obligatoryUtensil;
    this.cookingPreference = cookingPreference;
    this.daysPassed = daysPassed;
    this.comment = comment;
    this.recipe = recipe;
  }

  public static Meal createMeal(String name,
      CookingTime cookingTime,
      Sidedish sideDish,
      ObligatoryUtensil obligatoryUtensil,
      CookingPreference cookingPreference,
      NonnegativeInteger daysPassed,
      String comment,
      Optional<Recipe> recipe) throws MealException {
    return new Meal(name,
        cookingTime,
        sideDish,
        obligatoryUtensil,
        cookingPreference,
        daysPassed,
        comment,
        recipe);
  }

  public static Meal copy(Meal meal) {
    return new Meal(meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getDaysPassed(),
        meal.getComment(),
        meal.getRecipe());
  }

  public Meal addRecipe(Optional<Recipe> recipe) {
    Meal newMeal = copy(this);
    newMeal.recipe = recipe;
    return newMeal;
  }

  public static Meal createEmptyMeal() {
    return EMPTY_MEAL;
  }

  public String getName() {
    return name;
  }

  public NonnegativeInteger getDaysPassed() {
    return daysPassed;
  }

  public int getDaysPassedAsInteger() {
    return daysPassed.value;
  }

  public CookingTime getCookingTime() {
    return cookingTime;
  }

  public Sidedish getSidedish() {
    return sidedish;
  }

  public ObligatoryUtensil getObligatoryUtensil() {
    return obligatoryUtensil;
  }

  public CookingPreference getCookingPreference() {
    return cookingPreference;
  }

  public String getComment() {
    return comment;
  }

  public Optional<Recipe> getRecipe() {
    return recipe;
  }

  @Override
  public int compareTo(Meal otherMeal) {
    return this.getName().compareToIgnoreCase(otherMeal.getName());
  }

  @Override
  public String toString() {
    return "[" + name + ", "
        + cookingTime + ", "
        + sidedish + ", "
        + obligatoryUtensil + ", "
        + cookingPreference + ", "
        + daysPassed + ", "
        + comment + ", "
        + recipe + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((comment == null) ? 0 : comment.hashCode());
    result = prime * result + ((cookingPreference == null) ? 0 : cookingPreference.hashCode());
    result = prime * result + ((cookingTime == null) ? 0 : cookingTime.hashCode());
    result = prime * result + daysPassed.value;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((obligatoryUtensil == null) ? 0 : obligatoryUtensil.hashCode());
    result = prime * result + ((sidedish == null) ? 0 : sidedish.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Meal other = (Meal) obj;
    if (!name.equals(other.name)
        || !comment.equals(other.comment)
        || cookingPreference != other.cookingPreference
        || cookingTime != other.cookingTime
        || obligatoryUtensil != other.obligatoryUtensil
        || sidedish != other.sidedish
        || !daysPassed.equals(other.daysPassed)) {
      return false;
    }
    return true;
  }

  public static Element writeMeal(Document saveFileContent, Meal meal, String elementName) {
    Element mealNode = saveFileContent.createElement(elementName);
    mealNode.setAttribute("name", meal.getName());

    mealNode.appendChild(createTextNode(saveFileContent, "comment", () -> meal.getComment()));
    mealNode.appendChild(createTextNode(saveFileContent,
        "cookingTime",
        () -> meal.getCookingTime().name()));
    mealNode.appendChild(createTextNode(saveFileContent,
        "sidedish",
        () -> meal.getSidedish().name()));
    mealNode.appendChild(createTextNode(saveFileContent,
        "utensil",
        () -> meal.getObligatoryUtensil().name()));
    mealNode.appendChild(createTextNode(saveFileContent,
        "preference",
        () -> meal.getCookingPreference().name()));
    mealNode.appendChild(createTextNode(saveFileContent,
        "lastCooked",
        () -> Integer.toString(meal.getDaysPassed().value)));
    if (meal.getRecipe().isPresent()) {
      mealNode.appendChild(
          Recipe.writeRecipe(saveFileContent, meal.getRecipe().get(), "recipe"));
    }
    return mealNode;
  }

  public static Meal readMeal(Element currentMeal) {
    String name = readString("corruptedName",
        () -> currentMeal.getAttributes().getNamedItem("name").getTextContent(),
        "name");
    String comment = XmlHelpers.readString("", currentMeal, "comment");
    CookingTime cookingTime = readEnum(CookingTime.VERY_SHORT,
        CookingTime::valueOf, currentMeal, "cookingTime");
    Sidedish sidedish = readEnum(Sidedish.NONE, Sidedish::valueOf, currentMeal, "sidedish");
    ObligatoryUtensil obligatoryUtensil = readEnum(ObligatoryUtensil.POT,
        ObligatoryUtensil::valueOf, currentMeal, "utensil");
    CookingPreference cookingPreference = readEnum(CookingPreference.NO_PREFERENCE,
        CookingPreference::valueOf, currentMeal, "preference");
    NonnegativeInteger daysLastCooked = nonNegative(parseInteger(currentMeal));
    NodeList recipeNodeList = currentMeal.getElementsByTagName("recipe");
    Optional<Recipe> recipeOptional = empty();
    if (recipeNodeList.getLength() != 0) {
      Recipe recipe = recipeNodeList.item(0).getNodeType() == Node.ELEMENT_NODE
          ? Recipe.loadRecipe((Element) recipeNodeList.item(0))
          : XmlHelpers.logFailedXmlRetrieval(createRecipe(), "recipe", currentMeal);
      recipeOptional = of(recipe);
    }
    return new Meal(name, cookingTime, sidedish, obligatoryUtensil,
        cookingPreference, daysLastCooked, comment, recipeOptional);
  }

  private static int parseInteger(Element currentMeal) {
    int daysLastCooked = 0;
    try {
      daysLastCooked = Integer
          .parseInt(currentMeal.getElementsByTagName("lastCooked").item(0)
              .getTextContent());
    } catch (NullPointerException | NumberFormatException exception) {
      logger.warn(
          "The number of days passed of a meal could not be read or contains an invalid number.");
    }
    if (daysLastCooked < 0) {
      daysLastCooked = 0;
      logger.warn("The number of days must be nonnegative.");
    }
    return daysLastCooked;
  }

  private static String readString(String defaultType, Supplier<String> getElement,
      String tagName) {
    String name = defaultType;
    try {
      name = getElement.get();
    } catch (NullPointerException exception) {
      logger.warn(String.format("The %s of a meal could not be read", tagName));
    }
    return name;
  }

  private void setName(String name) throws MealException {
    if (name.trim().isEmpty()) {
      throw new MealException("Name is empty or consists only of whitespace");
    } else {
      this.name = name.trim();
    }
  }
}