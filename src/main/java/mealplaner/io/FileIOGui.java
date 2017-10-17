package mealplaner.io;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.swing.JOptionPane.showInputDialog;
import static mealplaner.gui.commons.MessageDialog.errorMessages;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.BundleStore;
import mealplaner.MealplanerData;
import mealplaner.errorhandling.MealException;

public class FileIOGui {
	private BundleStore bundles;
	private JFrame frame;
	private static final Logger logger = LoggerFactory.getLogger(FileIOGui.class);

	public FileIOGui(BundleStore bundles, JFrame frame) {
		this.bundles = bundles;
		this.frame = frame;
	}

	public MealplanerData loadDatabase() {
		MealplanerData mealPlan = new MealplanerData();
		try {
			MealplanerData mealPlanLoaded = MealplanerFileLoader.load("save01.ser");
			mealPlan = mealPlanLoaded; // do not inline: This will only be
										// done when load was successful
			mealPlan.getMeals();
		} catch (FileNotFoundException exc) {
			errorMessages(frame, exc, bundles.errorMessage("MSG_FILE_NOT_FOUND"), bundles);
			logger.error("File not found in: ", exc);
		} catch (IOException exc) {
			errorMessages(frame, exc, bundles.errorMessage("MSG_IOEX"), bundles);
			logger.error("I/O Exception in: ", exc);
		} catch (MealException exc) {
			errorMessages(frame, exc, bundles.errorMessage("MSG_CLASS_NOT_FOUND"), bundles);
			logger.error("MealException in: ", exc);
		}
		return mealPlan;
	}

	public Optional<MealplanerData> loadBackup() {
		String bak = showInputDialog(frame, bundles.message("createLoadBackup"), "*.ser");
		if (bak != null) {
			MealplanerData mealPlan = new MealplanerData();
			try {
				mealPlan = MealplanerFileLoader.load(bak);
			} catch (FileNotFoundException exc) {
				errorMessages(frame, exc, bundles.errorMessage("MSG_BKU_FILE_NOT_FOUND"), bundles);
				logger.error("File not found in: ", exc);
			} catch (IOException exc) {
				errorMessages(frame, exc, bundles.errorMessage("MSG_IOEX"), bundles);
				logger.error("I/O Exception in: ", exc);
			} catch (MealException exc) {
				errorMessages(frame, exc, bundles.errorMessage("MSG_BKU_CLASS_NOT_FOUND"), bundles);
				logger.error("MealException in: ", exc);
			}
			return of(mealPlan);
		}
		return empty();
	}

	public void saveDatabase(MealplanerData mealPlan) {
		try {
			MealplanerFileSaver.save(mealPlan, "save01.ser");
			JOptionPane.showMessageDialog(frame, bundles.message("successSave"),
					bundles.message("successHeading"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException exc) {
			errorMessages(frame, exc, bundles.errorMessage("MSG_IOEX"), bundles);
			logger.error("I/O Exception in: ", exc);
		}
	}

	public void createBackup(MealplanerData mealPlan) {
		String bak = showInputDialog(frame, bundles.message("createLoadBackup"), "*.ser");
		if (bak != null) {
			try {
				MealplanerFileSaver.save(mealPlan, bak);
				JOptionPane.showMessageDialog(frame, bundles.message("successSave"),
						bundles.message("successHeading"), JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException exc) {
				errorMessages(frame, exc, bundles.errorMessage("MSG_IOEX"), bundles);
				logger.error("I/O Exception in: ", exc);
			}
		}
	}
}
