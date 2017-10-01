package mealplaner.io;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.MealplanerData;
import mealplaner.errorhandling.MealException;

public class MealplanerFileSaver {

	public static void save(MealplanerData mealplaner, String name) throws IOException {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();

			Document saveFileContent = documentBuilder.newDocument();
			Element root = saveFileContent.createElement("mealplaner");
			saveFileContent.appendChild(root);

			root.appendChild(MealplanerData.generateXml(saveFileContent, mealplaner));

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(saveFileContent);
			StreamResult result = new StreamResult(new File("save.xml"));

			transformer.transform(source, result);
		} catch (ParserConfigurationException exception) {
			throw new MealException(
					"Internal Error: Something went wrong when creating an XML document.");
		} catch (TransformerConfigurationException e) {
			throw new MealException(
					"Internal Error: Something went wrong when creating an XML document.");
		} catch (TransformerException e) {
			throw new MealException(
					"Internal Error: Something went wrong when creating an XML document.");
		}
	}
}
