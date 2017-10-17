package mealplaner.io;

import static java.lang.Long.parseLong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import mealplaner.model.Meal;

public final class XMLHelpers {
	private static final Logger logger = LoggerFactory.getLogger(XMLHelpers.class);

	private XMLHelpers() {
	}

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
			logger.warn(String.format(
					"The %s of element could not be read or contains an invalid Enum.", tagName));
		}
		return enumType;
	}

	public static boolean readBoolean(boolean defaultType, Element currentNode, String tagName) {
		boolean enumType = defaultType;
		try {
			enumType = Boolean.valueOf(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			logger.warn(String.format(
					"The %s of element could not be read or contains an invalid Enum.", tagName));
		}
		return enumType;
	}

	public static int readInt(int defaultType, Element currentNode, String tagName) {
		int enumType = defaultType;
		try {
			enumType = Integer.parseInt(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			logger.warn(String.format(
					"The %s of element could not be read or contains an invalid integer.",
					tagName));
		}
		return enumType;
	}

	public static String readString(String defaultType, Element currentNode, String tagName) {
		String name = defaultType;
		try {
			name = currentNode.getElementsByTagName(tagName).item(0).getTextContent();
		} catch (NullPointerException exception) {
			logger.warn(String.format("The %s of element could not be read.", tagName));
		}
		return name;
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
			logger.warn("The time of a calendar could not be read.");
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
				logger.warn("A meal in a MealList could not be read properly");
			}
		}
		return meals;
	}

	public static <T> T logFailedXmlRetrieval(T fallback, String message, Element node) {
		logger.error(String.format("Element %s not found. Fallback to %s", message,
				fallback.toString()));
		return fallback;
	}
}
