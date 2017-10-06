package mealplaner.recepies.model;

import static java.util.stream.Collectors.toMap;
import static mealplaner.io.XMLHelpers.createTextNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.errorhandling.Logger;
import mealplaner.io.XMLHelpers;

public class Recipe {
	private final int numberOfPortions;
	private Map<Ingredient, Integer> ingredients;

	public Recipe() {
		this(1, new HashMap<>());
	}

	public Recipe(int numberOfPortions, Map<Ingredient, Integer> ingredients) {
		this.numberOfPortions = numberOfPortions;
		this.ingredients = ingredients;
	}

	public int getNumberOfPortions() {
		return numberOfPortions;
	}

	public void put(Ingredient ingredient, int amount) {
		ingredients.putIfAbsent(ingredient, amount);
	}

	public int remove(Ingredient ingredient) {
		return ingredients.remove(ingredient);
	}

	public void replace(Ingredient ingredient, int amount) {
		ingredients.replace(ingredient, amount);
	}

	public Map<Ingredient, Integer> getRecipeFor(final int numberOfPeople) {
		return ingredients.entrySet().stream()
				.collect(toMap(Entry::getKey,
						entry -> entry.getValue() * numberOfPeople / numberOfPortions));
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

	public static Element generateXml(Document saveFileContent, Recipe recipe,
			String elementName) {
		Element recipeNode = saveFileContent.createElement(elementName);
		recipeNode.appendChild(createTextNode(saveFileContent, "numberOfPortions",
				() -> Integer.toString(recipe.getNumberOfPortions())));
		for (Map.Entry<Ingredient, Integer> entry : recipe
				.getRecipeFor(recipe.getNumberOfPortions()).entrySet()) {
			Element ingredientEntry = saveFileContent.createElement("recipePart");
			ingredientEntry.appendChild(
					Ingredient.generateXml(saveFileContent, entry.getKey(), "ingredient"));
			ingredientEntry.appendChild(createTextNode(saveFileContent,
					"amount", () -> entry.getValue().toString()));
			recipeNode.appendChild(ingredientEntry);
		}
		return recipeNode;
	}

	public static Recipe loadFromXml(Element currentRecipe) {
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
				Logger.logParsingError(
						"A meal in " + currentRecipe.toString() + " could not be read properly");
			}
		}
		return new Recipe(numberOfPortions, entries);
	}
}
