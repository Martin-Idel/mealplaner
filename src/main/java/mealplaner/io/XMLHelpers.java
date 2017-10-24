package mealplaner.io;

import static java.time.LocalDate.now;
import static java.time.LocalDate.of;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

	public static Optional<Node> getFirstNode(Element node, String tagName) {
		NodeList list = node.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			return of(list.item(0));
		} else {
			logger.warn(String.format("The node with name %s could not be found.", tagName));
			return empty();
		}
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

	public static boolean readBoolean(boolean defaultBool, Element currentNode, String tagName) {
		boolean bool = defaultBool;
		try {
			bool = Boolean.valueOf(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			logger.warn(String.format(
					"The %s of element could not be read or contains an invalid Enum.", tagName));
		}
		return bool;
	}

	public static int readInt(int defaultNumber, Element currentNode, String tagName) {
		int number = defaultNumber;
		try {
			number = Integer.parseInt(currentNode.getElementsByTagName(tagName).item(0)
					.getTextContent());
		} catch (NullPointerException | IllegalArgumentException exception) {
			logger.warn(String.format(
					"The %s of element could not be read or contains an invalid integer.",
					tagName));
		}
		return number;
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

	public static Element writeMealList(Document saveFileContent, List<Meal> meals,
			String nodeName) {
		Element mealListNode = saveFileContent.createElement(nodeName);
		meals.stream().map(meal -> Meal.writeMeal(saveFileContent, meal, "meal"))
				.forEach(mealListNode::appendChild);
		return mealListNode;
	}

	public static List<Meal> parseMealList(Element mealListNode) {
		NodeList elementsByTagName = mealListNode.getElementsByTagName("meal");
		List<Meal> meals = new ArrayList<>();
		for (int i = 0; i < elementsByTagName.getLength(); i++) {
			if (elementsByTagName.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Meal meal = Meal.readMeal((Element) elementsByTagName.item(i));
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

	public static void logEmptyOptional(Optional<?> optional, String message, Element node) {
		if (!optional.isPresent()) {
			logger.error(String.format("Element %s not found.", message));
		}
	}

	public static LocalDate parseDate(Element calendarNode) {
		LocalDate nowDate = now();
		int dayOfMonth = readInt(nowDate.getDayOfMonth(), calendarNode, "dayOfMonth");
		Month month = readEnum(nowDate.getMonth(), Month::valueOf, calendarNode, "month");
		int year = readInt(nowDate.getYear(), calendarNode, "year");
		return of(year, month, dayOfMonth);
	}

	public static Element writeDate(Document saveFileContent, LocalDate date,
			String tagName) {
		Element dateNode = saveFileContent.createElement(tagName);
		dateNode.appendChild(
				createTextNode(saveFileContent, "dayOfMonth",
						() -> Integer.toString(date.getDayOfMonth())));
		dateNode.appendChild(
				createTextNode(saveFileContent, "month",
						() -> date.getMonth().toString()));
		dateNode.appendChild(
				createTextNode(saveFileContent, "year",
						() -> Integer.toString(date.getYear())));
		return dateNode;
	}
}
