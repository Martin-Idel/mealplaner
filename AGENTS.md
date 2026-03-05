# Mealplaner App

This project contains a small desktop application for meal planning. You enter recipies into a database and then, based
on past cooking and your preferences (settings), it will generate a proposal for what to cook. You can also generate a
shopping list directly from there.

The project is a Java project and currently uses XML for saving the data and Swing as GUI.

## Project structure

This project is a Gradle project.

Core model files are provided by mealplaner-model.
The core functionality such as the proposal is found in mealplaner-core.

A lot of functionality is provided by "plugins", which are added in the mealplaner-plugins folder. Plugins can extend
meals by "MealFacts" and Ingredients by "IngredientFacts" as well as display their information and add a ProposalStep to
change the proposal.

The GUI is separated and located in mealplaner-gui and mealplaner-gui-commons.

## Plugin Development

When developing a new plugin, follow these guidelines:

### Plugin Structure

- Create your plugin under `mealplaner-plugins/<plugin-name>/`
- Implement the necessary plugin interfaces (e.g., `MealFact`, `IngredientFact`, `ProposalStep`)
- Register your plugin via the `PluginDescription` interface and add a META-INF service file

### Message Bundles

- All user-facing strings should be internationalized
- Place message bundles in `src/main/resources/`
- English: `PluginMessagesBundle.properties`
- German: `PluginMessagesBundle_de.properties`

### Testing

- **Bundle Tests**: Include a `{PluginName}BundleTest` class that uses `bundletests.BundleCommons.allMessageTests()` to
  verify:
    - All keys exist in both English and German bundles
    - No duplicate keys in either bundle
    - No unnecessary keys (keys present but nowhere in the code)
    - All bundle lookups use string literals (not variables)
- See existing plugins like `VegetarianMessagesBundleTest` for examples
- **Note**: The `seasonal` plugin intentionally omits the bundle test because it uses programmatic month name lookups
  with variables, which violates the "string literals only" rule. This is documented as a deliberate design choice.

### GUI Components

- Use `DialogWindow` for dialogs (not `JOptionPane.showOptionDialog`)
- Use `ButtonPanelBuilder` for button panels
- Follow existing patterns in other plugins (e.g., `ShowPhotoDialog`, `MeasureInputDialog`)

#### GUI Patterns Reference

This project defines consistent patterns for building GUI components. When creating new GUI elements, look at these
locations:

**Dialogs:**

- Pattern: Use `DialogWindow` with `arrangeWithSize(width, height)` before `setVisible()`, call `dispose()` in button
  listeners
- Example: `mealplaner-gui/src/main/java/mealplaner/gui/dialogs/ingredients/MeasureInputDialog.java`

**Button Panels:**

- Pattern: Use `ButtonPanelBuilder.builder("dialogName")` to create OK/Cancel buttons with actions
- Example: `mealplaner-gui/src/main/java/mealplaner/gui/dialogs/ingredients/MeasureInputDialog.java`

**Tables:**

- Pattern: Use `FlexibleTableBuilder` with `TableColumnBuilder` methods like `withEnumContent()`, `withContent()`,
  `withColumnName()`
- Use `getValueFromOrderedList()` and `setValueToOrderedImmutableList()` for editable columns
- Use `overwriteTableCellRenderer()` for custom renderers
- Example: `mealplaner-gui/src/main/java/mealplaner/gui/tabbedpanes/ingredientsedit/IngredientsEditTable.java`

**Input Fields (for dialogs):**

- Pattern: Implement anonymous `InputField<T>` with `addToPanel()`, `getUserInput()`, `resetField()`, `getOrdering()`
- Example: `mealplaner-plugins/seasonal/src/main/java/mealplaner/plugins/seasonal/gui/SeasonInputExtension.java`

**Common Utilities:**

- `GridPanel`: Helper for creating grid layouts (`mealplaner-gui-commons`)
- `DialogWindow`: Standardized dialog wrapper (`mealplaner-gui-commons`)
- `ButtonPanelBuilder`: Standardized button panel creation (`mealplaner-gui-commons`)
- `FlexibleTableBuilder`: Standardized table building (`mealplaner-gui-commons`)
- `TableColumnBuilder`: Column configuration helpers (`mealplaner-gui-commons`)

**Constants Helper Pattern:**

- Create utility classes for constants (colors, i18n lookups) to avoid duplication
- Example: `mealplaner-plugins/seasonal/src/main/java/mealplaner/plugins/seasonal/util/SeasonalConstants.java`

## Development setup

This project is a Gradle project.

Each module has its own unit tests. Unit tests try to test functionality over implementation therefore testing the
interface and not its implementation.
The project tries to use Red-Green-Testing: First write tests that are red, then make them green.
Especially when fixing bugs: Always create a test first and verify that it is red, then provide the bug fix.
You can run unittests via

```
./gradlew test
```

The project uses checkstyle, PMD, and SpotBugs for static code analysis and codestyle.
Codestyle should be cleaned before every commit.

End-2-end tests are provided in mealplaner-e2e. This subpackage provides two sets of tests:

- xmlsmoketests which check roundtrip data saving and loading
- guitests which are true e2e tests
  The guitests require a display, so if you are in a headless environment, don't run them.

