package mealplaner.recipes.model;

import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.readEnum;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.io.XMLHelpers;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Ingredient {
	private final String name;
	private final IngredientType type;
	private final Measure measure;

	public Ingredient() {
		this("no name", IngredientType.OTHER, Measure.NONE);
	}

	public Ingredient(String name, IngredientType type, Measure measure) {
		this.name = name;
		this.type = type;
		this.measure = measure;
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

	public static Element generateXml(Document saveFileContent, Ingredient ingredient,
			String elementName) {
		Element ingredientNode = saveFileContent.createElement(elementName);

		ingredientNode
				.appendChild(createTextNode(saveFileContent, "name", () -> ingredient.getName()));
		ingredientNode.appendChild(
				createTextNode(saveFileContent, "type", () -> ingredient.getType().name()));
		ingredientNode.appendChild(createTextNode(saveFileContent, "measure",
				() -> ingredient.getMeasure().name()));
		return ingredientNode;
	}

	public static Ingredient loadFromXml(Element currentIngredient) {
		String name = XMLHelpers.readString("Something", currentIngredient, "name");
		IngredientType type = readEnum(IngredientType.OTHER,
				IngredientType::valueOf, currentIngredient, "type");
		Measure measure = readEnum(Measure.NONE,
				Measure::valueOf, currentIngredient, "measure");
		return new Ingredient(name, type, measure);
	}

	@Override
	public String toString() {
		return name;
	}

	// @Override
	// public String toString() {
	// return "Ingredient [name=" + name + ", type=" + type + ", measure=" + measure
	// + "]";
	// }

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
		if (measure != other.measure || !name.equals(other.name) || type != other.type) {
			return false;
		}
		return true;
	}
}
