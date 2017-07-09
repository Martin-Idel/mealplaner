package mealplaner;

import javax.swing.SwingUtilities;

import mealplaner.gui.MainGUI;

public class Kochplaner {
	public static void main(String args[]) {
		MealplanerData bla = new MealplanerData();
		SwingUtilities.invokeLater(() -> new MainGUI(bla));
	}
}