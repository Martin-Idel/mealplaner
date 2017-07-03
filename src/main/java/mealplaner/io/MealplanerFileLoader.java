package mealplaner.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Calendar;

import mealplaner.MealplanerData;
import mealplaner.errorhandling.MealException;
import mealplaner.model.MealListData;
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public class MealplanerFileLoader {

	public static MealplanerData load(String name) throws FileNotFoundException, IOException, MealException {
		try (FileInputStream fs1 = new FileInputStream(name); ObjectInputStream os1 = new ObjectInputStream(fs1)) {
			return constructMealplanerFromFile(os1);
		} catch (ClassNotFoundException exc) {
			throw new MealException("Corrupted Save File - some classes were not found");
		} catch (MealException exc) {
			throw new MealException("Corrupted Save File");
		}
	}

	private static MealplanerData constructMealplanerFromFile(ObjectInputStream os1)
			throws IOException, ClassNotFoundException, MealException {

		MealplanerCalendar cal = new MealplanerCalendar();
		cal.setCalendar((Calendar) os1.readObject());
		MealListData mealListData = (MealListData) os1.readObject();
		Settings[] defaultSettings = (Settings[]) os1.readObject();
		Proposal proposal = (Proposal) os1.readObject();

		return new MealplanerData(mealListData, cal, defaultSettings, proposal);
	}
}
