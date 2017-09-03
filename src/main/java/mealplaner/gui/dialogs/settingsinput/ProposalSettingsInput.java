package mealplaner.gui.dialogs.settingsinput;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.BundleStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
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

	public Optional<Settings[]> showDialog(Settings[] settings, ProposalOutline outline) {
		setup(settings, outline);
		setVisible(true);
		return getEnteredSettings();
	}

	private Calendar createCalendarForTable(Date date, boolean today) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (!today) {
			calendar.add(Calendar.DATE, 1);
		}
		return calendar;
	}

	private Settings[] createSettingsForTable(int numberOfDays, boolean today) {
		Settings[] settings = new Settings[numberOfDays];
		Arrays.fill(settings, new Settings());
		return settings;
	}

	protected void setup(Settings[] defaultSettings, ProposalOutline outline) {
		Settings[] tableSettings = createSettingsForTable(outline.getNumberOfDays(),
				outline.isIncludedToday());
		Calendar calendar = createCalendarForTable(outline.getDateToday(),
				outline.isIncludedToday());
		settingTable = new SettingTable(tableSettings, calendar, bundles);
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
