package mealplaner.recipes.model;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XmlHelpers.createTextNode;
import static mealplaner.recipes.model.QuantitativeIngredient.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.io.XmlHelpers;

public class Recipe {
  private static final Logger logger = LoggerFactory.getLogger(Recipe.class);

  private final NonnegativeInteger numberOfPortions;
  private Map<Ingredient, NonnegativeInteger> ingredients;

  private Recipe(NonnegativeInteger numberOfPortions,
      Map<Ingredient, NonnegativeInteger> ingredients) {
    this.numberOfPortions = numberOfPortions;
    this.ingredients = ingredients;
  }

  public static Recipe from(NonnegativeInteger numberOfPortions,
      Map<Ingredient, NonnegativeInteger> ingredients) {
    return new Recipe(numberOfPortions, ingredients);
  }

  public static Recipe from(NonnegativeInteger numberOfPortions,
      List<QuantitativeIngredient> ingredients) {
    Map<Ingredient, NonnegativeInteger> ingredientMap = ingredients.stream()
        .collect(groupingBy(QuantitativeIngredient::getIngredient,
            reducing(ZERO,
                QuantitativeIngredient::getAmount,
                (amount1, amount2) -> amount1.add(amount2))));
    return new Recipe(numberOfPortions, ingredientMap);
  }

  public static Recipe createRecipe() {
    return new Recipe(ONE, new HashMap<>());
  }

  public NonnegativeInteger getNumberOfPortions() {
    return numberOfPortions;
  }

  public Map<Ingredient, NonnegativeInteger> getIngredientsFor(
      final NonnegativeInteger numberOfPeople) {
    return ingredients.entrySet().stream()
        .collect(toMap(Entry::getKey,
            entry -> entry.getValue()
                .multiplyBy(numberOfPeople)
                .divideBy(numberOfPortions)));
  }

  public Map<Ingredient, NonnegativeInteger> getIngredientsAsIs() {
    return ingredients;
  }

  public List<QuantitativeIngredient> getIngredientListFor(
      final NonnegativeInteger numberOfPeople) {
    return getIngredientListWithMultipliedAmount(value -> value
        .multiplyBy(numberOfPeople)
        .divideBy(numberOfPortions));
  }

  public List<QuantitativeIngredient> getIngredientListAsIs() {
    return getIngredientListWithMultipliedAmount(Function.identity());
  }

  private List<QuantitativeIngredient> getIngredientListWithMultipliedAmount(
      Function<NonnegativeInteger, NonnegativeInteger> mapValues) {
    return ingredients.entrySet()
        .stream()
        .map(entry -> create(entry.getKey(), mapValues.apply(entry.getValue())))
        .collect(toList());
  }

  @Override
  public String toString() {
    return "Recipe [numberOfPortions=" + numberOfPortions + ", ingredients=" + ingredients
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (ingredients == null ? 0 : ingredients.hashCode());
    result = prime * result + numberOfPortions.value;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Recipe other = (Recipe) obj;
    if (!ingredients.equals(other.ingredients)
        || !numberOfPortions.equals(other.numberOfPortions)) {
      return false;
    }
    return true;
  }

  public static Element writeRecipe(Document saveFileContent, Recipe recipe,
      String elementName) {
    Element recipeNode = saveFileContent.createElement(elementName);
    recipeNode.appendChild(createTextNode(saveFileContent, "numberOfPortions",
        () -> Integer.toString(recipe.getNumberOfPortions().value)));
    for (Map.Entry<Ingredient, NonnegativeInteger> entry : recipe
        .getIngredientsFor(recipe.getNumberOfPortions()).entrySet()) {
      Element ingredientEntry = saveFileContent.createElement("recipePart");
      ingredientEntry.appendChild(
          Ingredient.generateXml(saveFileContent, entry.getKey(), "ingredient"));
      ingredientEntry.appendChild(createTextNode(saveFileContent,
          "amount", () -> Integer.toString(entry.getValue().value)));
      recipeNode.appendChild(ingredientEntry);
    }
    return recipeNode;
  }

  public static Recipe loadRecipe(Element currentRecipe) {
    NonnegativeInteger numberOfPortions = nonNegative(
        XmlHelpers.readInt(1, currentRecipe, "numberOfPortions"));
    NodeList elementsByTagName = currentRecipe.getElementsByTagName("recipePart");
    Map<Ingredient, NonnegativeInteger> entries = new HashMap<>();
    for (int i = 0; i < elementsByTagName.getLength(); i++) {
      if (elementsByTagName.item(i).getNodeType() == Node.ELEMENT_NODE) {
        Element recipePart = (Element) elementsByTagName.item(i);
        Node ingredientsNode = recipePart.getElementsByTagName("ingredient").item(0);
        Ingredient ingredient = ingredientsNode.getNodeType() == Node.ELEMENT_NODE
            ? Ingredient.loadFromXml((Element) ingredientsNode)
            : XmlHelpers.logFailedXmlRetrieval(new Ingredient(),
                currentRecipe.toString(),
                currentRecipe);
        Node measureNode = recipePart.getElementsByTagName("amount").item(0);
        int measure = measureNode.getNodeType() == Node.ELEMENT_NODE
            ? Integer.parseInt(((Element) measureNode).getTextContent())
            : 0;
        entries.put(ingredient, nonNegative(measure));
      } else {
        logger.warn("A recipe could not be read properly, since the node type of "
            + elementsByTagName.item(i).getNodeName() + "was not an Element");
      }
    }
    return new Recipe(numberOfPortions, entries);
  }
}
