package mealplaner.recipes.gui.dialogs.ingredients;

import static mealplaner.commons.BundleStore.BUNDLES;

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

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.inputfields.ComboBoxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonEmptyTextInputField;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;

public class IngredientsInput extends JDialog {
  private static final long serialVersionUID = 1L;
  private final JFrame parentFrame;
  private JPanel ingredientCreationPanel;
  private JPanel buttonPanel;

  private final transient InputField<Optional<String>> nameField;
  private final transient InputField<IngredientType> typeField;
  private final transient InputField<Measure> measureField;

  private final transient List<Ingredient> ingredients;

  public IngredientsInput(JFrame parent) {
    super(parent, BUNDLES.message("ingredientInputDialogTitle"), true);
    this.parentFrame = parent;

    nameField = new NonEmptyTextInputField(BUNDLES.message("insertIngredientName"));
    typeField = new ComboBoxInputField<IngredientType>(
        BUNDLES.message("insertTypeLength"),
        IngredientType.class,
        IngredientType.OTHER);
    measureField = new ComboBoxInputField<Measure>(
        BUNDLES.message("insertMeasure"),
        Measure.class,
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
    buttonPanel = new ButtonPanelBuilder()
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
