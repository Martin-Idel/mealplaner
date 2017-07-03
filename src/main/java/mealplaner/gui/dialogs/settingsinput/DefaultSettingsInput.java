package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.gui.commons.ButtonPanelBuilder.justDisposeListener;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.settings.Settings;

public class DefaultSettingsInput extends SettingsInput {
	private static final long serialVersionUID = 1L;
	private JScrollPane tablescroll;
	private JPanel buttonPanel;
	private SettingTable settingTable;

	public DefaultSettingsInput(JFrame parentFrame, Settings[] defaultSettings, ResourceBundle parentMes) {
		super(parentFrame, defaultSettings, parentMes.getString("settingsUpdatePropose"));
		setup(defaultSettings);

		settingTable = new SettingTable(defaultSettings, parentMes);
		tablescroll = new JScrollPane(settingTable.setupTable());
		settingTable.removeDateColumn();

		buttonPanel = new ButtonPanelBuilder(parentMes)
				.addSaveButton(getSaveListener(settingTable))
				.addExitButton(justDisposeListener(this))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
	}
}
