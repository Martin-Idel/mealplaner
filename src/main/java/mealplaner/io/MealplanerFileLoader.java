package mealplaner.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import mealplaner.MealplanerData;
import mealplaner.commons.errorhandling.MealException;

public class MealplanerFileLoader {

  public static MealplanerData load(String name)
      throws FileNotFoundException, IOException, MealException {
    try {
      return readXml();
    } catch (ClassCastException exc) {
      throw new MealException("Corrupted Save File - some classes were not saved correctly");
    } catch (MealException exc) {
      throw new MealException("Corrupted Save File");
    }
  }

  private static MealplanerData readXml() throws IOException {
    try {
      File inputFile = new File("save.xml");
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
      Document parsedDocument = documentBuilder.parse(inputFile);
      parsedDocument.getDocumentElement().normalize();

      return MealplanerData.parseMealplanerData((Element) parsedDocument.getDocumentElement()
          .getElementsByTagName("mealplaner").item(0));

    } catch (ParserConfigurationException e) {
      throw new MealException(
          "Internal Error: Something went wrong when creating an XML document.");
    } catch (SAXException e) {
      throw new MealException(
          "Internal Error: Something went wrong when creating an XML document.");
    }
  }
}
