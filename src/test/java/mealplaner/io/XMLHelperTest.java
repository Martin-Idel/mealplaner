package mealplaner.io;

import static mealplaner.io.XMLHelpers.getCalendarFromXml;
import static mealplaner.io.XMLHelpers.saveCalendarToXml;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLHelperTest {

	@Test
	public void getEnumFromTextNode() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element enumNode = saveDocument.createElement("EnumNode");
		enumNode.appendChild(saveDocument.createTextNode(TestEnum.NON_DEFAULT.name()));
		testNode.appendChild(enumNode);

		TestEnum actual = XMLHelpers.readEnum(TestEnum.DEFAULT, TestEnum::valueOf, testNode,
				"EnumNode");

		assertEquals(TestEnum.NON_DEFAULT, actual);
	}

	@Test
	public void getEnumFromTextNodeAlthoughItDoesNotExist() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element enumNode = saveDocument.createElement("EnumNode");
		testNode.appendChild(enumNode);

		TestEnum actual = XMLHelpers.readEnum(TestEnum.DEFAULT, TestEnum::valueOf, testNode,
				"EnumNode");

		assertEquals(TestEnum.DEFAULT, actual);
	}

	@Test
	public void readBoolean() throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		booleanNode.appendChild(saveDocument.createTextNode(Boolean.toString(true)));
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertTrue(actual);
	}

	@Test
	public void readBooleanFromTextNodeAlthoughItDoesNotExist()
			throws ParserConfigurationException {
		Document saveDocument = createDocument();
		Element testNode = saveDocument.createElement("testNode");
		Element booleanNode = saveDocument.createElement("BOOL");
		testNode.appendChild(booleanNode);

		boolean actual = XMLHelpers.readBoolean(false, testNode, "BOOL");

		assertFalse(actual);
	}

	@Test
	public void calendarCanBeSavedAndReadToXml() throws ParserConfigurationException {
		Calendar expected = Calendar.getInstance();
		expected.setTimeInMillis(5000000);
		Document saveDocument = createDocument();

		Calendar actual = getCalendarFromXml(saveCalendarToXml(saveDocument, expected));

		assertEquals(expected.getTimeInMillis(), actual.getTimeInMillis());
	}

	private Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveDocument = documentBuilder.newDocument();
		return saveDocument;
	}

	private enum TestEnum {
		DEFAULT, NON_DEFAULT;
	}
}
