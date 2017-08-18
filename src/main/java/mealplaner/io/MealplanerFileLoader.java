package mealplaner.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mealplaner.MealplanerData;
import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public class MealplanerFileLoader {

	public static MealplanerData load(String name)
			throws FileNotFoundException, IOException, MealException {
		try (FileInputStream fs1 = new FileInputStream(name);
				ObjectInputStream os1 = new ObjectInputStream(fs1)) {
			return constructMealplanerFromFile(os1);
		} catch (ClassNotFoundException exc) {
			throw new MealException("Corrupted Save File - some classes were not found");
		} catch (ClassCastException exc) {
			throw new MealException("Corrupted Save File - some classes were not saved correctly");
		} catch (MealException exc) {
			throw new MealException("Corrupted Save File");
		}
	}

	@SuppressWarnings("unchecked")
	private static MealplanerData constructMealplanerFromFile(ObjectInputStream os1)
			throws IOException, ClassNotFoundException, MealException {

		Date date = (Date) os1.readObject();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		MealplanerCalendar cal = new MealplanerCalendar(calendar);
		List<Meal> meals = (ArrayList<Meal>) os1.readObject();
		Settings[] defaultSettings = (Settings[]) os1.readObject();
		Proposal proposal = (Proposal) os1.readObject();

		return new MealplanerData(meals, cal, defaultSettings, proposal);
	}
}
