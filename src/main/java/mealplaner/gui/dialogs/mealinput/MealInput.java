package mealplaner.gui.dialogs.mealinput;
/**
 * Martin Idel
 * MealInput: extends JDialog to become the GUI to enter new menus.
 * Several constructors using the data bank of Mealplaner or not. 
 **/

import static java.util.Optional.of;
import static mealplaner.gui.model.EnumToStringRepresentation.getCookingPreferenceStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getCookingTimeStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getObligatoryUtensilStrings;
import static mealplaner.gui.model.EnumToStringRepresentation.getSidedishStrings;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.ComboBoxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonEmptyTextInputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public abstract class MealInput extends JDialog implements ErrorKeys {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel mealCreationPanel;
	private JPanel buttonPanel;

	private InputField<Optional<String>> nameField;
	private InputField<CookingTime> cookingTimeField;
	private InputField<Sidedish> sidedishField;
	private InputField<ObligatoryUtensil> obligatoryUtensilField;
	private InputField<NonnegativeInteger> daysPassedField;
	private InputField<CookingPreference> preferenceField;

	private ResourceBundle messages;

	public MealInput(JFrame parent, ResourceBundle parentMessages) {
		super(parent, parentMessages.getString("menuFrameTitle"), true);
		parentFrame = parent;
		messages = parentMessages;

		nameField = new NonEmptyTextInputField(messages.getString("menuNameInsert"));
		cookingTimeField = new ComboBoxInputField<CookingTime>(
				messages.getString("menuLengthChoice"),
				CookingTime.class,
				getCookingTimeStrings(messages),
				CookingTime.SHORT);
		sidedishField = new ComboBoxInputField<Sidedish>(
				messages.getString("menuSideChoice"),
				Sidedish.class,
				getSidedishStrings(messages),
				Sidedish.NONE);
		obligatoryUtensilField = new ComboBoxInputField<ObligatoryUtensil>(
				messages.getString("menuUtensilChoice"),
				ObligatoryUtensil.class,
				getObligatoryUtensilStrings(messages),
				ObligatoryUtensil.POT);
		daysPassedField = new NonnegativeIntegerInputField(messages.getString("menuLasttimeChoice"),
				new NonnegativeInteger(0));
		preferenceField = new ComboBoxInputField<CookingPreference>(
				messages.getString("menuPopularChoice"),
				CookingPreference.class,
				getCookingPreferenceStrings(messages),
				CookingPreference.NO_PREFERENCE);
	}

	protected void display(ActionListener saveListener) {
		mealCreationPanel = new JPanel();
		mealCreationPanel.setLayout(new GridLayout(0, 2));

		Arrays.asList(nameField, cookingTimeField, sidedishField, obligatoryUtensilField, daysPassedField,
				preferenceField)
				.stream()
				.forEach(field -> field.addToPanel(mealCreationPanel));

		buttonPanel = new ButtonPanelBuilder(messages)
				.addSaveButton(saveListener)
				.addCancelDialogButton(this)
				.build();

		setupPanelsInDialogFrame();
		setVisible(true);
	}

	private void setupPanelsInDialogFrame() {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BorderLayout());
		dialogPanel.add(mealCreationPanel, BorderLayout.CENTER);
		dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(dialogPanel);
		pack();
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	protected void resetFields() {
		Arrays.asList(nameField, cookingTimeField, sidedishField, obligatoryUtensilField, daysPassedField,
				preferenceField)
				.stream()
				.forEach(InputField::resetField);
	}

	protected Optional<Meal> getMealAndShowDialog() {
		Optional<Meal> mealFromInput = getMealFromUserInput();
		if (!mealFromInput.isPresent()) {
			JOptionPane.showMessageDialog(null, messages.getString("menuChoiceEmpty"),
					messages.getString("errorHeading"), JOptionPane.INFORMATION_MESSAGE);
		}
		return mealFromInput;
	}

	private Optional<Meal> getMealFromUserInput() {
		return nameField.getUserInput().isPresent() ? of(new Meal(
				nameField.getUserInput().get(),
				cookingTimeField.getUserInput(),
				sidedishField.getUserInput(),
				obligatoryUtensilField.getUserInput(),
				preferenceField.getUserInput(),
				daysPassedField.getUserInput().value))
				: Optional.empty();
	}
}