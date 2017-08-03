package mealplaner.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import mealplaner.MealplanerData;

public class MealplanerFileSaver {

	public static void save(MealplanerData mealplaner, String name) throws IOException {
		try (FileOutputStream fos1 = new FileOutputStream(name);
				ObjectOutputStream oos1 = new ObjectOutputStream(fos1)) {
			oos1.writeObject(mealplaner.getTime());
			oos1.writeObject(mealplaner.getMealListData());
			oos1.writeObject(mealplaner.getDefaultSettings());
			oos1.writeObject(mealplaner.getLastProposal());
		}
	}
}
