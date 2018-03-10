package mealplaner.xml;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.MealdatabaseXml;
import mealplaner.xml.model.RecipeXml;

public final class XmlReading {
  private static final Logger logger = LoggerFactory.getLogger(XmlReading.class);

  private XmlReading() {
  }

  public static List<Meal> loadXml(String filePath) {
    int versionNumber = checkVersion(filePath);
    if (versionNumber == 1) {
      MealdatabaseXml database = readXml(filePath);
      return convertDataBaseFromXml(database);
    } else {
      // TODO: Delete once saves have been ported
      return new ArrayList<>();
      // try {
      // return MealplanerFileLoader.load(filePath).getMeals();
      // } catch (MealException | IOException e) {
      // return new ArrayList<>();
      // }
    }
  }

  private static int checkVersion(String filePath) {
    try {
      File inputFile = new File(filePath);
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
      Document parsedDocument = documentBuilder.parse(inputFile);
      parsedDocument.getDocumentElement().normalize();
      return getVersionNumberFromTopLevelVersionNode(parsedDocument);
    } catch (ParserConfigurationException ex) {
      logger.warn("Exception on parsing XML with relative path " + filePath + ".");
    } catch (SAXException ex) {
      logger.warn("Exception on parsing XML with relative path " + filePath + ".");
    } catch (IOException ex) {
      logger.warn("Exception on parsing XML with relative path " + filePath
          + " Maybe the file is missing?");
    }
    return 0;
  }

  private static int getVersionNumberFromTopLevelVersionNode(Document parsedDocument) {
    Node modelNode = parsedDocument.getFirstChild();
    if (modelNode != null) {
      NodeList allChildren = modelNode.getChildNodes();
      for (int i = 0; i < allChildren.getLength(); i++) {
        Node potentialVersionNode = allChildren.item(i);
        if (potentialVersionNode.getNodeType() == Element.ELEMENT_NODE &&
            potentialVersionNode.getNodeName().equals("version")) {
          try {
            return Integer.parseInt(potentialVersionNode.getTextContent());
          } catch (NumberFormatException ex) {
            logger.warn("There is a top-level node with name \"version\" "
                + " which does not contain a version number but instead contains: "
                + potentialVersionNode.getTextContent());
          }
        }
      }
    }
    logger.warn("Parser could not find a toplevel XML element named \"version\"."
        + " Are you sure you have a valid save file?");
    return 0;
  }

  private static MealdatabaseXml readXml(String savePath) {
    try {
      JAXBContext jc = JAXBContext.newInstance(MealdatabaseXml.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      File xml = new File(savePath);
      return (MealdatabaseXml) unmarshaller.unmarshal(xml);
    } catch (JAXBException e) {
      throw new MealException("Provider for ingredients could not be loaded.", e);
    }
  }

  private static List<Meal> convertDataBaseFromXml(MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> Meal.createMeal(
            meal.name,
            meal.cookingTime,
            meal.sidedish,
            meal.obligatoryUtensil,
            meal.cookingPreference,
            nonNegative(meal.daysPassed),
            meal.comment,
            convertRecipeFromXml(meal.recipe)))
        .forEach(meal -> modelMeals.add(meal));
    return modelMeals;
  }

  private static Optional<Recipe> convertRecipeFromXml(RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<Ingredient, NonnegativeInteger> nonnegativeIntegerMap = recipe.ingredients
        .entrySet()
        .stream()
        .collect(toMap(e -> e.getKey(), e -> nonNegative(e.getValue())));
    return of(Recipe.from(nonNegative(recipe.numberOfPortions), nonnegativeIntegerMap));
  }
}
