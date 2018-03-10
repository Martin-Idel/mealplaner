package mealplaner.xml;

import static java.util.stream.Collectors.toMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.MealXml;
import mealplaner.xml.model.MealdatabaseXml;
import mealplaner.xml.model.RecipeXml;

public final class XmlWriting {
  private XmlWriting() {
  }

  public static void saveXml(List<Meal> data, String filePath) {
    MealdatabaseXml mealDataBase = convertDataBaseToXml(data);
    try (OutputStream outputStream = new FileOutputStream(filePath)) {
      JAXBContext jc = JAXBContext.newInstance(MealdatabaseXml.class);
      Marshaller marshaller = jc.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(mealDataBase, outputStream);
    } catch (JAXBException e) {
      throw new MealException("Provider for ingredients could not be loaded.", e);
    } catch (FileNotFoundException e) {
      throw new MealException("File " + filePath + " was not be found.", e);
    } catch (IOException e) {
      throw new MealException("File " + filePath + " could not be loaded.", e);
    }
  }

  private static MealdatabaseXml convertDataBaseToXml(List<Meal> data) {
    List<MealXml> mealXmls = new ArrayList<>();
    data.stream()
        .map(meal -> new MealXml(
            meal.getName(),
            meal.getCookingTime(),
            meal.getSidedish(),
            meal.getObligatoryUtensil(),
            meal.getCookingPreference(),
            meal.getDaysPassed(),
            meal.getComment(),
            convertRecipeToXml(meal.getRecipe())))
        .forEach(mealXml -> mealXmls.add(mealXml));
    return new MealdatabaseXml(mealXmls, 1);
  }

  private static RecipeXml convertRecipeToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      Map<Ingredient, Integer> integerMap = recipe.getIngredientsAsIs()
          .entrySet()
          .stream()
          .collect(toMap(e -> e.getKey(), e -> e.getValue().value));
      return new RecipeXml(recipe.getNumberOfPortions().value,
          integerMap);
    }
    return null;
  }
}
