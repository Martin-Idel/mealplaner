package mealplaner.gui.dialogs.mealinput;

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
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import mealplaner.BundleStore;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.ComboBoxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonEmptyTextInputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.gui.commons.TextField;
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
	private InputField<String> commentField;

	private BundleStore bundles;

	public MealInput(JFrame parent, BundleStore bundles) {
		super(parent, bundles.message("mealInputDialogTitle"), true);
		this.parentFrame = parent;
		this.bundles = bundles;

		nameField = new NonEmptyTextInputField(bundles.message("insertMealName"));
		cookingTimeField = new ComboBoxInputField<CookingTime>(
				bundles.message("insertMealLength"),
				CookingTime.class,
				getCookingTimeStrings(bundles),
				CookingTime.SHORT);
		sidedishField = new ComboBoxInputField<Sidedish>(
				bundles.message("insertMealSidedish"),
				Sidedish.class,
				getSidedishStrings(bundles),
				Sidedish.NONE);
		obligatoryUtensilField = new ComboBoxInputField<ObligatoryUtensil>(
				bundles.message("insertMealUtensil"),
				ObligatoryUtensil.class,
				getObligatoryUtensilStrings(bundles),
				ObligatoryUtensil.POT);
		daysPassedField = new NonnegativeIntegerInputField(
				bundles.message("insertMealLastCooked"),
				new NonnegativeInteger(0));
		preferenceField = new ComboBoxInputField<CookingPreference>(
				bundles.message("insertMealPopularity"),
				CookingPreference.class,
				getCookingPreferenceStrings(bundles),
				CookingPreference.NO_PREFERENCE);
		commentField = new TextField(bundles.message("insertMealComment"));
	}

	protected void display(ActionListener saveListener) {
		mealCreationPanel = new JPanel();
		mealCreationPanel.setLayout(new GridLayout(0, 2));

		allFields().forEach(field -> field.addToPanel(mealCreationPanel));

		buttonPanel = new ButtonPanelBuilder(bundles)
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
		allFields().forEach(InputField::resetField);
	}

	protected Optional<Meal> getMealAndShowDialog() {
		Optional<Meal> mealFromInput = getMealFromUserInput();
		if (!mealFromInput.isPresent()) {
			JOptionPane.showMessageDialog(null, bundles.message("menuNameChoiceEmpty"),
					bundles.message("errorHeading"), JOptionPane.INFORMATION_MESSAGE);
		}
		return mealFromInput;
	}

	private Stream<InputField<?>> allFields() {
		return Arrays.asList(nameField, cookingTimeField, sidedishField, obligatoryUtensilField,
				daysPassedField, preferenceField, commentField).stream();
	}

	private Optional<Meal> getMealFromUserInput() {
		return nameField.getUserInput().isPresent() ? of(new Meal(
				nameField.getUserInput().get(),
				cookingTimeField.getUserInput(),
				sidedishField.getUserInput(),
				obligatoryUtensilField.getUserInput(),
				preferenceField.getUserInput(),
				daysPassedField.getUserInput().value,
				commentField.getUserInput()))
				: Optional.empty();
	}
}