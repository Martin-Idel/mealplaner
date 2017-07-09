package mealplaner.io;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showInputDialog;
import static mealplaner.errorhandling.ErrorKeys.MSG_BKU_CLASS_NOT_FOUND;
import static mealplaner.errorhandling.ErrorKeys.MSG_BKU_FILE_NOT_FOUND;
import static mealplaner.errorhandling.ErrorKeys.MSG_CLASS_NOT_FOUND;
import static mealplaner.errorhandling.ErrorKeys.MSG_FILE_NOT_FOUND;
import static mealplaner.errorhandling.ErrorKeys.MSG_IOEX;
import static mealplaner.gui.commons.MessageDialog.errorMessages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.MealplanerData;
import mealplaner.errorhandling.ErrorMessages;
import mealplaner.errorhandling.MealException;

public class FileIOGui {
	private ResourceBundle messages;
	private JFrame frame;

	public FileIOGui(ResourceBundle messages, JFrame frame) {
		this.messages = messages;
		this.frame = frame;
	}

	public MealplanerData loadDatabase() {
		MealplanerData mealPlan = new MealplanerData();
		try {
			MealplanerData mealPlanLoaded = MealplanerFileLoader.load("save01.ser");
			mealPlan = mealPlanLoaded; // do not inline: This will only be
										// done when load was successful
			mealPlan.getMealListData().sortMealsAccordingToName();
		} catch (FileNotFoundException exc) {
			errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_FILE_NOT_FOUND));
		} catch (IOException exc) {
			errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_IOEX));
		} catch (MealException exc) {
			errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_CLASS_NOT_FOUND));
		}
		return mealPlan;
	}

	public Optional<MealplanerData> loadBackup() {
		String bak = showInputDialog(frame, messages.getString("createLoadBackup"), "*.ser");
		if (bak != null) {
			MealplanerData mealPlan = new MealplanerData();
			try {
				mealPlan = MealplanerFileLoader.load(bak);
			} catch (FileNotFoundException exc) {
				errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_BKU_FILE_NOT_FOUND));
			} catch (IOException exc) {
				errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_IOEX));
			} catch (MealException exc) {
				errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_BKU_CLASS_NOT_FOUND));
			}
			return of(mealPlan);
		}
		return empty();
	}

	public void saveDatabase(MealplanerData mealPlan) {
		try {
			MealplanerFileSaver.save(mealPlan, "save01.ser");
			JOptionPane.showMessageDialog(frame, messages.getString("successSave"),
					messages.getString("successHeading"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException exc) {
			errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_IOEX));
		}
	}

	public void createBackup(MealplanerData mealPlan) {
		String bak = showInputDialog(frame, messages.getString("createLoadBackup"), "*.ser");
		if (bak != null) {
			try {
				MealplanerFileSaver.save(mealPlan, bak);
				JOptionPane.showMessageDialog(frame, messages.getString("successSave"),
						messages.getString("successHeading"), JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException exc) {
				errorMessages(frame, exc, ErrorMessages.formatMessage(MSG_IOEX));
			}
		}
	}
}
