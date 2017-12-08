package mealplaner.recipes.model;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.io.XMLHelpers;

public class Recipe {
	private static final Logger logger = LoggerFactory.getLogger(Recipe.class);

	private final int numberOfPortions;
	private Map<Ingredient, Integer> ingredients;

	private Recipe(int numberOfPortions, Map<Ingredient, Integer> ingredients) {
		this.numberOfPortions = numberOfPortions;
		this.ingredients = ingredients;
	}

	public static Recipe from(int numberOfPortions, Map<Ingredient, Integer> ingredients) {
		return new Recipe(numberOfPortions, ingredients);
	}

	public static Recipe from(int numberOfPortions, List<QuantitativeIngredient> ingredients) {
		Map<Ingredient, Integer> ingredientMap = ingredients.stream()
				.collect(groupingBy(QuantitativeIngredient::getIngredient,
						summingInt(ingredient -> ingredient.getAmount().value)));
		return new Recipe(numberOfPortions, ingredientMap);
	}

	public static Recipe createRecipe() {
		return new Recipe(1, new HashMap<>());
	}

	public int getNumberOfPortions() {
		return numberOfPortions;
	}

	public Map<Ingredient, Integer> getIngredientsFor(final int numberOfPeople) {
		return ingredients.entrySet().stream()
				.collect(toMap(Entry::getKey,
						entry -> entry.getValue() * numberOfPeople / numberOfPortions));
	}

	public Map<Ingredient, Integer> getIngredientsAsIs() {
		return ingredients;
	}

	public List<QuantitativeIngredient> getIngredientListFor(final int numberOfPeople) {
		return getIngredientListWithMultipliedAmount((float) numberOfPeople / numberOfPortions);
	}

	public List<QuantitativeIngredient> getIngredientListAsIs() {
		return getIngredientListWithMultipliedAmount(1);
	}

	private List<QuantitativeIngredient> getIngredientListWithMultipliedAmount(
			final float multiplier) {
		return ingredients.entrySet()
				.stream()
				.map(entry -> builder()
						.ingredient(entry.getKey())
						.amount(nonNegative((int) (entry.getValue() * multiplier)))
						.forPeople(nonNegative(1))
						.build())
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
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + numberOfPortions;
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
				|| numberOfPortions != other.numberOfPortions) {
			return false;
		}
		return true;
	}

	public static Element writeRecipe(Document saveFileContent, Recipe recipe,
			String elementName) {
		Element recipeNode = saveFileContent.createElement(elementName);
		recipeNode.appendChild(createTextNode(saveFileContent, "numberOfPortions",
				() -> Integer.toString(recipe.getNumberOfPortions())));
		for (Map.Entry<Ingredient, Integer> entry : recipe
				.getIngredientsFor(recipe.getNumberOfPortions()).entrySet()) {
			Element ingredientEntry = saveFileContent.createElement("recipePart");
			ingredientEntry.appendChild(
					Ingredient.generateXml(saveFileContent, entry.getKey(), "ingredient"));
			ingredientEntry.appendChild(createTextNode(saveFileContent,
					"amount", () -> entry.getValue().toString()));
			recipeNode.appendChild(ingredientEntry);
		}
		return recipeNode;
	}

	public static Recipe loadRecipe(Element currentRecipe) {
		int numberOfPortions = XMLHelpers.readInt(1, currentRecipe, "numberOfPortions");
		NodeList elementsByTagName = currentRecipe.getElementsByTagName("recipePart");
		Map<Ingredient, Integer> entries = new HashMap<>();
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			if (elementsByTagName.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element recipePart = (Element) elementsByTagName.item(i);
				Node ingredientsNode = recipePart.getElementsByTagName("ingredient").item(0);
				Ingredient ingredient = ingredientsNode.getNodeType() == Node.ELEMENT_NODE
						? Ingredient.loadFromXml((Element) ingredientsNode)
						: XMLHelpers.logFailedXmlRetrieval(new Ingredient(),
								currentRecipe.toString(),
								currentRecipe);
				Node measureNode = recipePart.getElementsByTagName("amount").item(0);
				int measure = measureNode.getNodeType() == Node.ELEMENT_NODE
						? Integer.parseInt(((Element) measureNode).getTextContent())
						: 0;
				entries.put(ingredient, measure);
			} else {
				logger.warn("A recipe could not be read properly, since the node type of "
						+ elementsByTagName.item(i).getNodeName() + "was not an Element");
			}
		}
		return new Recipe(numberOfPortions, entries);
	}
}
