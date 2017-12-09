package mealplaner.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.recipes.provider.IngredientProvider;

public class IngredientIO {

	public static final String INGREDIENTS_FILE = "ingredients.xml";

	public static IngredientProvider readXml() {
		try {
			JAXBContext jc = JAXBContext.newInstance(IngredientProvider.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File xml = new File(INGREDIENTS_FILE);
			IngredientProvider provider = (IngredientProvider) unmarshaller.unmarshal(xml);
			return provider;
		} catch (JAXBException e) {
			throw new MealException("Provider for ingredients could not be loaded.");
		}
	}

	public static void saveXml(IngredientProvider ingredients) {
		try (OutputStream outputStream = new FileOutputStream(INGREDIENTS_FILE)) {
			JAXBContext jc = JAXBContext.newInstance(IngredientProvider.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(ingredients, outputStream);
		} catch (JAXBException e) {
			throw new MealException("Provider for ingredients could not be loaded.");
		} catch (FileNotFoundException e) {
			throw new MealException("File" + INGREDIENTS_FILE + "was not be found.");
		} catch (IOException e1) {
			throw new MealException("File" + INGREDIENTS_FILE + "could not be loaded.");
		}
	}
}
