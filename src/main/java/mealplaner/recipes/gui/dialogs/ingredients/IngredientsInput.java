package mealplaner.recipes.gui.dialogs.ingredients;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import mealplaner.BundleStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.ComboBoxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonEmptyTextInputField;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;

public class IngredientsInput extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel ingredientCreationPanel;
	private JPanel buttonPanel;

	private InputField<Optional<String>> nameField;
	private InputField<IngredientType> typeField;
	private InputField<Measure> measureField;

	private List<Ingredient> ingredients;
	private BundleStore bundles;

	public IngredientsInput(JFrame parent, BundleStore bundles) {
		super(parent, bundles.message("ingredientInputDialogTitle"), true);
		this.parentFrame = parent;
		this.bundles = bundles;

		nameField = new NonEmptyTextInputField(bundles.message("insertIngredientName"));
		typeField = new ComboBoxInputField<IngredientType>(
				bundles.message("insertTypeLength"),
				IngredientType.class,
				IngredientType.getIngredientTypeStrings(bundles),
				IngredientType.OTHER);
		measureField = new ComboBoxInputField<Measure>(
				bundles.message("insertMeasure"),
				Measure.class,
				Measure.getMeasureStrings(bundles),
				Measure.NONE);

		ingredients = new ArrayList<>();
	}

	public List<Ingredient> showDialog() {
		setupDialog(action -> {
			if (nameField.getUserInput().isPresent()) {
				ingredients.add(new Ingredient(nameField.getUserInput().get(),
						typeField.getUserInput(),
						measureField.getUserInput()));
				resetFields();
			}
		});
		dispose();
		return ingredients;
	}

	private void setupDialog(ActionListener saveListener) {
		ingredientCreationPanel = new JPanel();
		ingredientCreationPanel.setLayout(new GridLayout(0, 2));

		allFields().forEach(field -> field.addToPanel(ingredientCreationPanel));
		buttonPanel = new ButtonPanelBuilder(bundles)
				.addSaveButton(saveListener)
				.addCancelDialogButton(this)
				.build();

		setupPanelsInDialogFrame();
		resetFields();
		setVisible(true);
	}

	private void setupPanelsInDialogFrame() {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BorderLayout());
		dialogPanel.add(ingredientCreationPanel, BorderLayout.CENTER);
		dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(dialogPanel);
		pack();
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private void resetFields() {
		Arrays.asList(nameField, typeField, measureField).forEach(InputField::resetField);
	}

	private Stream<InputField<?>> allFields() {
		return Arrays.asList(nameField, typeField, measureField).stream();
	}
}
