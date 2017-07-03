package mealplaner;

import javax.swing.SwingUtilities;

import mealplaner.gui.MainGUI;

/**
 * Martin Idel, 02.02.2016 (last update) Kochplaner: contains main. Creates
 * MenuPlaner and MainGUI - everything else is taken care of.
 **/

public class Kochplaner {
	public static void main(String args[]) {
		MealplanerData bla = new MealplanerData();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainGUI(bla);
			}
		});
	}
}