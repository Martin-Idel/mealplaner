package mealplaner.shopping;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.Pair.of;
import static mealplaner.shopping.ShoppingList.emptyList;

import java.awt.BorderLayout;
import java.util.Optional;

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
		ShoppingList shoppingList = createShoppingList(proposal);
		display(shoppingList, ingredientProvider);
	}

	private ShoppingList createShoppingList(Proposal proposal) {
		if (proposal.getProposalList()
				.stream()
				.filter(meal -> !meal.getRecipe().isPresent())
				.findAny()
				.isPresent()) {
			int result = JOptionPane.showConfirmDialog(frame,
					BUNDLES.message("notAllRecipesExist"));
			if (result == JOptionPane.NO_OPTION) {
				dispose();
				return emptyList();
			}
		}
		return ShoppingList.from(proposal.getMealsAndSettings().stream()
				.map(pair -> of(pair.left.getRecipe(), pair.right.getNumberOfPeople()))
				.filter(pair -> pair.left.isPresent())
				.map(pair -> pair.mapLeft(Optional::get))
				.collect(toList()));
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
