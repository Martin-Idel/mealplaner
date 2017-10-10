package mealplaner.recipes.provider;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.recipes.model.Ingredient;

@XmlRootElement
public class IngredientProvider {
	@XmlElementWrapper(name = "ingredients")
	@XmlElement(name = "ingredient")
	private List<Ingredient> ingredients;

	public IngredientProvider() {
		this(new ArrayList<>());
	}

	public IngredientProvider(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public List<Ingredient> getIngredients() {
		ingredients.sort((ingredient1, ingredient2) -> ingredient1.getName()
				.compareTo(ingredient2.getName()));
		return ingredients;
	}

	public void add(Ingredient ingredient) {
		ingredients.add(ingredient);
	}

	public boolean remove(Ingredient ingredient) {
		return ingredients.remove(ingredient);
	}

	public void replace(Ingredient oldIngredient, Ingredient newIngredient) {
		if (ingredients.remove(oldIngredient)) {
			ingredients.add(newIngredient);
		}
	}

	public int size() {
		return ingredients.size();
	}
}
