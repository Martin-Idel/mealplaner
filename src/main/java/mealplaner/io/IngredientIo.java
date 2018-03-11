package mealplaner.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.recipes.provider.IngredientProvider;
import mealplaner.xml.IngredientsWriter;

public final class IngredientIo {
  private IngredientIo() {
  }

  public static IngredientProvider readXml(String savePath) {
    try {
      JAXBContext jc = JAXBContext.newInstance(IngredientProvider.class);
      Unmarshaller unmarshaller = jc.createUnmarshaller();
      File xml = new File(savePath);
      IngredientProvider provider = (IngredientProvider) unmarshaller.unmarshal(xml);
      provider.setSavePath(savePath);
      return provider;
    } catch (JAXBException e) {
      throw new MealException("Provider for ingredients could not be loaded.", e);
    }
  }

  public static void saveXml(IngredientProvider ingredients) {
    IngredientsWriter.saveXml(ingredients.getIngredients(), ingredients.getSavePath());
  }
}
