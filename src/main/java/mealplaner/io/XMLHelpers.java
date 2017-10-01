package mealplaner.io;

import java.util.Calendar;
import java.util.function.Function;
import java.util.function.Supplier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.errorhandling.Logger;

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
		Element calNode = saveDocument.createElement("Calendar");
		calNode.appendChild(
				createTextNode(saveDocument, "time",
						() -> Long.toString(calendar.getTime().getTime())));
		return calNode;
	}

	public static Calendar getCalendarFromXml(Element calendarNode) {
		Calendar cal = Calendar.getInstance();
		try {
			long time = Long
					.parseLong(calendarNode.getElementsByTagName("time").item(0).getTextContent());
			cal.setTimeInMillis(time);
		} catch (NumberFormatException exc) {
			Logger.logParsingError(String.format(
					"The time of calendar " + calendarNode.toString() + " could not be read"));
		}
		return cal;
	}
}
