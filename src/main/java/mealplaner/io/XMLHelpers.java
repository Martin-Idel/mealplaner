package mealplaner.io;

import static java.lang.Long.parseLong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.errorhandling.Logger;
import mealplaner.model.Meal;

public class XMLHelpers {

	public static Element createTextNode(Document doc, String name,
			Supplier<String> stringRepresentationOfField) {
		Element textNode = doc.createElement(name);
		textNode.appendChild(doc.createTextNode(stringRepresentationOfField.get()));
		return textNode;
	}

	public static <T extends Enum<T>> T readEnum(T defaultType, Function<String, T> valueOf,
			Element currentNode, String tagName) {
		T enumType = defaultType;
		try {
			enumType = valueOf.apply(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			Logger.logParsingError(
					String.format("The %s of element " + currentNode.toString()
							+ " could not be read or contains an invalid Enum.", tagName));
		}
		return enumType;
	}

	public static boolean readBoolean(boolean defaultType, Element currentNode, String tagName) {
		boolean enumType = defaultType;
		try {
			enumType = Boolean.valueOf(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			Logger.logParsingError(
					String.format("The %s of element " + currentNode.toString()
							+ " could not be read or contains an invalid Enum.", tagName));
		}
		return enumType;
	}

	public static Element saveCalendarToXml(Document saveDocument, Calendar calendar) {
		Element calNode = saveDocument.createElement("calendar");
		calNode.appendChild(
				createTextNode(saveDocument, "time",
						() -> Long.toString(calendar.getTime().getTime())));
		return calNode;
	}

	public static Calendar getCalendarFromXml(Element calendarNode) {
		Calendar cal = Calendar.getInstance();
		try {
			long time = parseLong(
					calendarNode.getElementsByTagName("time").item(0).getTextContent());
			cal.setTimeInMillis(time);
		} catch (NumberFormatException exc) {
			Logger.logParsingError(String.format(
					"The time of calendar " + calendarNode.toString() + " could not be read"));
		}
		return cal;
	}

	public static Element saveMealsToXml(Document saveFileContent, List<Meal> meals,
			String nodeName) {
		Element mealListNode = saveFileContent.createElement(nodeName);
		meals.stream().map(meal -> Meal.generateXml(saveFileContent, meal, "meal"))
				.forEach(mealListNode::appendChild);
		return mealListNode;
	}

	public static List<Meal> getMealListFromXml(Element mealListNode) {
		NodeList elementsByTagName = mealListNode.getElementsByTagName("meal");
		List<Meal> meals = new ArrayList<>();
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			if (elementsByTagName.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Meal meal = Meal.loadFromXml((Element) elementsByTagName.item(i));
				meals.add(meal);
			} else {
				Logger.logParsingError(
						"A meal in " + mealListNode.toString() + " could not be read properly");
			}
		}
		return meals;
	}

	public static <T> T logFailedXmlRetrieval(T fallback, String message, Element node) {
		Logger.logParsingError(String
				.format("Element %s not found in node " + node.toString(), message));
		return fallback;
	}
}
