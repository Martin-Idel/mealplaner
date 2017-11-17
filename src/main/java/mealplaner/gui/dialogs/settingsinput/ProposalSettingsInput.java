package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.model.settings.Settings.createSettings;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class ProposalSettingsInput extends SettingsInput {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JScrollPane tablescroll;
	private JPanel buttonPanel;
	private SettingTable settingTable;

	public ProposalSettingsInput(JFrame parentFrame) {
		super(parentFrame, BUNDLES.message("settingsUpdateProposeTitle"));
		this.parentFrame = parentFrame;
	}

	public Optional<Settings[]> showDialog(DefaultSettings settings, ProposalOutline outline) {
		setup(settings, outline);
		setVisible(true);
		return getEnteredSettings();
	}

	private List<Settings> createSettingsForTable(int numberOfDays, boolean today) {
		Settings[] settings = new Settings[numberOfDays];
		Arrays.fill(settings, createSettings());
		return Arrays.asList(settings);
	}

	protected void setup(DefaultSettings defaultSettings, ProposalOutline outline) {
		List<Settings> tableSettings = createSettingsForTable(outline.getNumberOfDays(),
				outline.isIncludedToday());
		LocalDate date = outline.isIncludedToday() ? outline.getDateToday()
				: outline.getDateToday().plusDays(1);
		settingTable = new SettingTable(tableSettings, date);
		tablescroll = new JScrollPane(settingTable.setupTable());

		buttonPanel = new ButtonPanelBuilder()
				.addButton(BUNDLES.message("useDefaultButton"),
						BUNDLES.message("useDefaultButtonMnemonic"),
						action -> settingTable.useDefaultSettings(defaultSettings))
				.addCancelDialogButton(this)
				.addOkButton(getSaveListener(settingTable))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
	}
}
