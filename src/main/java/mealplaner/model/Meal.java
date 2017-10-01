package mealplaner.model;

import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.readEnum;

import java.io.Serializable;
import java.util.function.Supplier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.errorhandling.Logger;
import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class Meal implements Serializable, Comparable<Meal> {
	private static final long serialVersionUID = 1L;
	private String name;
	private CookingTime cookingTime;
	private Sidedish sidedish;
	private ObligatoryUtensil obligatoryUtensil;
	private CookingPreference cookingPreference;
	private int daysPassed;
	private String comment;

	public Meal(String name, CookingTime cookingTime, Sidedish sideDish,
			ObligatoryUtensil obligatoryUtensil, CookingPreference cookingPreference,
			int daysPassed, String comment)
			throws MealException {
		setName(name);
		this.cookingTime = cookingTime;
		this.sidedish = sideDish;
		this.obligatoryUtensil = obligatoryUtensil;
		this.cookingPreference = cookingPreference;
		setDaysPassed(daysPassed);
		this.setComment(comment);
	}

	public Meal(Meal meal) {
		setName(meal.getName());
		this.cookingTime = meal.getCookingTime();
		this.sidedish = meal.getSidedish();
		this.obligatoryUtensil = meal.getObligatoryUtensil();
		this.cookingPreference = meal.getCookingPreference();
		setDaysPassed(meal.getDaysPassed());
		this.setComment(meal.getComment());
	}

	@Override
	public int compareTo(Meal otherMeal) {
		return this.getName().compareToIgnoreCase(otherMeal.getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws MealException {
		if (name.trim().isEmpty()) {
			throw new MealException("Name is empty or consists only of whitespace");
		} else {
			this.name = name.trim();
		}
	}

	public int getDaysPassed() {
		return daysPassed;
	}

	public void setDaysPassed(int daysPassed) throws MealException {
		if (daysPassed >= 0) {
			this.daysPassed = daysPassed;
		} else {
			throw new MealException("Number must be nonnegative");
		}
	}

	public void addDaysPassed(int daysPassedSince) throws MealException {
		setDaysPassed(daysPassed + daysPassedSince);
	}

	public CookingTime getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(CookingTime cookingTime) {
		this.cookingTime = cookingTime;
	}

	public Sidedish getSidedish() {
		return sidedish;
	}

	public void setSidedish(Sidedish sidedish) {
		this.sidedish = sidedish;
	}

	public ObligatoryUtensil getObligatoryUtensil() {
		return obligatoryUtensil;
	}

	public void setObligatoryUtensil(ObligatoryUtensil obligatoryUtensil) {
		this.obligatoryUtensil = obligatoryUtensil;
	}

	public CookingPreference getCookingPreference() {
		return cookingPreference;
	}

	public void setCookingPreference(CookingPreference cookingPreference) {
		this.cookingPreference = cookingPreference;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return name;
	}

	public static Element generateXml(Document saveFileContent, Meal meal) {
		Element mealNode = saveFileContent.createElement("meal");
		mealNode.setAttribute("name", meal.getName());

		mealNode.appendChild(createTextNode(saveFileContent, "comment", () -> meal.getComment()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"cookingTime",
				() -> meal.getCookingTime().name()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"sideDish",
				() -> meal.getSidedish().name()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"utensil",
				() -> meal.getObligatoryUtensil().name()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"preference",
				() -> meal.getCookingPreference().name()));
		mealNode.appendChild(createTextNode(saveFileContent,
				"lastCooked",
				() -> Integer.toString(meal.getDaysPassed())));
		return mealNode;
	}

	public static Meal loadFromXml(Element currentMeal) {
		String name = readString("corruptedName",
				() -> currentMeal.getAttributes().getNamedItem("name").getTextContent(),
				currentMeal,
				"name");
		String comment = readString("",
				() -> currentMeal.getElementsByTagName("comment").item(0).getTextContent(),
				currentMeal,
				"comment");
		CookingTime cookingTime = readEnum(CookingTime.VERY_SHORT,
				CookingTime::valueOf, currentMeal, "cookingTime");
		Sidedish sidedish = readEnum(Sidedish.NONE, Sidedish::valueOf, currentMeal, "sidedish");
		ObligatoryUtensil obligatoryUtensil = readEnum(ObligatoryUtensil.POT,
				ObligatoryUtensil::valueOf, currentMeal, "utensil");
		CookingPreference cookingPreference = readEnum(CookingPreference.NO_PREFERENCE,
				CookingPreference::valueOf, currentMeal, "preference");
		int daysLastCooked = parseInteger(currentMeal);
		return new Meal(name, cookingTime, sidedish, obligatoryUtensil,
				cookingPreference, daysLastCooked, comment);
	}

	private static int parseInteger(Element currentMeal) {
		int daysLastCooked = 0;
		try {
			daysLastCooked = Integer
					.parseInt(currentMeal.getElementsByTagName("lastCooked").item(0)
							.getTextContent());
		} catch (NullPointerException | NumberFormatException exception) {
			Logger.logParsingError(
					"The number of days passed of element " + currentMeal.toString()
							+ " could not be read or contains an invalid number.");
		}
		if (daysLastCooked < 0) {
			daysLastCooked = 0;
			Logger.logParsingError("The number of days must be nonnegative.");
		}
		return daysLastCooked;
	}

	private static String readString(String defaultType, Supplier<String> getElement,
			Element currentMeal, String tagName) {
		String name = defaultType;
		try {
			name = getElement.get();
		} catch (NullPointerException exception) {
			Logger.logParsingError(String.format(
					"The %s of element " + currentMeal.toString() + " could not be read", tagName));
		}
		return name;
	}
}