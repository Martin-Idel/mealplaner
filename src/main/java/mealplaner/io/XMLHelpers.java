package mealplaner.io;

import java.util.function.Function;
import java.util.function.Supplier;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.errorhandling.Logger;

public class XMLHelpers {

	public static Element node(Document doc, String name,
			Supplier<String> stringRepresentationOfField) {
		Element mealFieldNode = doc.createElement(name);
		mealFieldNode.appendChild(doc.createTextNode(stringRepresentationOfField.get()));
		return mealFieldNode;
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
}
