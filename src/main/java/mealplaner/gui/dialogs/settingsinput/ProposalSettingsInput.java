package mealplaner.gui.dialogs.settingsinput;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class ProposalSettingsInput extends SettingsInput {
	private static final long serialVersionUID = 1L;
	private JScrollPane tablescroll;
	private JPanel buttonPanel;
	private SettingTable settingTable;

	public ProposalSettingsInput(JFrame parentFrame, Settings[] defaultSettings, ProposalOutline outline,
			ResourceBundle parentMes, Locale parentLocale) {
		super(parentFrame, defaultSettings, parentMes.getString("settingsUpdatePropose"));
		setup(defaultSettings);

		Settings[] tableSettings = createSettingsForTable(outline.getNumberOfDays(), outline.isIncludedToday());
		Calendar calendar = createCalendarForTable(outline.getDateToday(), outline.isIncludedToday());
		settingTable = new SettingTable(tableSettings, calendar, parentMes, parentLocale);
		tablescroll = new JScrollPane(settingTable.setupTable());

		buttonPanel = new ButtonPanelBuilder(parentMes)
				.addButton("settingsButton1", "settingsButton1Mnemonic",
						action -> settingTable.useDefaultSettings(defaultSettings))
				.addCancelDialogButton(this)
				.addOkButton(getSaveListener(settingTable))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
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
}