package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.model.settings.Settings.createSettings;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.BundleStore;
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
	private BundleStore bundles;

	public ProposalSettingsInput(JFrame parentFrame, BundleStore bundles) {
		super(parentFrame, bundles.message("settingsUpdateProposeTitle"));
		this.parentFrame = parentFrame;
		this.bundles = bundles;
	}

	public Optional<Settings[]> showDialog(DefaultSettings settings, ProposalOutline outline) {
		setup(settings, outline);
		setVisible(true);
		return getEnteredSettings();
	}

	private Settings[] createSettingsForTable(int numberOfDays, boolean today) {
		Settings[] settings = new Settings[numberOfDays];
		Arrays.fill(settings, createSettings());
		return settings;
	}

	protected void setup(DefaultSettings defaultSettings, ProposalOutline outline) {
		Settings[] tableSettings = createSettingsForTable(outline.getNumberOfDays(),
				outline.isIncludedToday());
		LocalDate date = outline.isIncludedToday() ? outline.getDateToday()
				: outline.getDateToday().plusDays(1);
		settingTable = new SettingTable(tableSettings, date, bundles);
		tablescroll = new JScrollPane(settingTable.setupTable());

		buttonPanel = new ButtonPanelBuilder(bundles)
				.addButton(bundles.message("useDefaultButton"),
						bundles.message("useDefaultButtonMnemonic"),
						action -> settingTable.useDefaultSettings(defaultSettings))
				.addCancelDialogButton(this)
				.addOkButton(getSaveListener(settingTable))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
	}
}
