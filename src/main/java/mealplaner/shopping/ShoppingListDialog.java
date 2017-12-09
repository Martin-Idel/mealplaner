package mealplaner.shopping;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.shopping.ShoppingListUtils.missingRecipesForCompleteList;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.Proposal;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingListDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private ShoppingTable shoppingTable;
	private Table table;

	public ShoppingListDialog(JFrame parent) {
		super(parent, BUNDLES.message("createShoppingListDialogTitle"), true);
		frame = parent;
		shoppingTable = new ShoppingTable();
	}

	public void showDialog(Proposal proposal, IngredientProvider ingredientProvider) {
		createTable(proposal, ingredientProvider);
		dispose();
	}

	private void createTable(Proposal proposal, IngredientProvider ingredientProvider) {
		if (missingRecipesForCompleteList(proposal)) {
			disposeIfUserWantsTo();
		}
		ShoppingList shoppingList = createShoppingList(proposal);
		display(shoppingList, ingredientProvider);
	}

	private void disposeIfUserWantsTo() {
		int result = JOptionPane.showConfirmDialog(frame,
				BUNDLES.message("notAllRecipesExist"));
		if (result == JOptionPane.NO_OPTION) {
			dispose();
		}
	}

	private void display(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
		table = shoppingTable.setupTable(shoppingList, ingredientProvider);
		JPanel buttonPanel = displayButtons();
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		table.addScrollingTableToPane(dataPanel);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(dataPanel);
		setSize(300, 300);
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	private JPanel displayButtons() {
		return new ButtonPanelBuilder()
				.addButton(BUNDLES.message("printButton"),
						BUNDLES.message("printButtonMnemonic"),
						action -> table.printTable(frame))
				.addOkButton(action -> dispose())
				.build();
	}
}
