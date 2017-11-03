package mealplaner.shopping;

import static java.util.stream.Collectors.toList;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.commons.Pair.of;
import static mealplaner.shopping.ShoppingList.emptyList;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.Proposal;
import mealplaner.recipes.provider.IngredientProvider;

public class ShoppingListDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private ShoppingTable shoppingTable;

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
		System.out.println(shoppingList);
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
		return ShoppingList.from(proposal.getProposalList().stream()
				.map(meal -> of(meal.getRecipe(), 2)) // TODO: need settings in Proposal
				.filter(pair -> pair.left.isPresent())
				.map(pair -> pair.mapLeft(Optional::get))
				.collect(toList()));
	}

	private void display(ShoppingList shoppingList, IngredientProvider ingredientProvider) {
		JScrollPane tablescroll = new JScrollPane(
				shoppingTable.setupTable(shoppingList, ingredientProvider));
		JPanel buttonPanel = displayButtons();
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		dataPanel.add(tablescroll, BorderLayout.CENTER);
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
						action -> shoppingTable.printTable(frame))
				.addOkButton(action -> dispose())
				.build();
	}
}
