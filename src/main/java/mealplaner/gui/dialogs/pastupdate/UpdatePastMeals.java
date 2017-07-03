package mealplaner.gui.dialogs.pastupdate;
/**
 * Martin Idel,
 * UpdatePastMeals: update what you cooked since last save (a JDialog-Extension).
 * Inserts meals cooked recently. User enters all meals cooked since the last proposal.
 * Only known meals can be entered so this is checked with AutoCompleteCellEditor. The method then updates the "last cooked" item in the data bank.
 **/

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.DataStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.Meal;

public class UpdatePastMeals extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel dataPanel;
	private ResourceBundle messages;
	private DataStore mealPlan;
	private UpdateTable updateTable;
	private List<Meal> changedMeals = new ArrayList<>();

	public UpdatePastMeals(JFrame parentFrame, DataStore mealPlan, Locale parentLocale, ResourceBundle parentMes) {
		super(parentFrame, parentMes.getString("cookingProposal"), true);
		this.parentFrame = parentFrame;
		this.mealPlan = mealPlan;
		messages = parentMes;

		updateTable = new UpdateTable(parentMes, parentLocale);
	}

	public List<Meal> showDialog() {
		display();
		return changedMeals;
	}

	private void display() {
		JScrollPane tablescroll = new JScrollPane(
				updateTable.createTable(mealPlan.getCalendar().getTime(),
						mealPlan.getLastProposal(),
						mealPlan.getMealListData().getMealList()
								.toArray(new Meal[mealPlan.getMealListData().getSize()]),
						mealPlan.getDaysPassed()));
		JPanel buttonPanel = displayButtons();
		dataPanel = new JPanel();
		dataPanel.setLayout(new BorderLayout());
		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);

		if (mealPlan.getDaysPassed() != 0) {
			setSize(300, 300);
			setLocationRelativeTo(parentFrame);
			setVisible(true);
		}
	}

	private JPanel displayButtons() {
		return new ButtonPanelBuilder(messages)
				.addSaveButton(new SavingListener())
				.addCancelDialogButton(this)
				.build();
	}

	public class SavingListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			changedMeals = updateTable.returnContent();
			dispose();
		}
	}
}