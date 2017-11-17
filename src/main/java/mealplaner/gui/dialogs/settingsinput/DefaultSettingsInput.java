package mealplaner.gui.dialogs.settingsinput;

import static java.time.DayOfWeek.MONDAY;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.gui.commons.ButtonPanelBuilder.justDisposeListener;
import static mealplaner.model.settings.DefaultSettings.from;

import java.awt.BorderLayout;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class DefaultSettingsInput extends SettingsInput {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JScrollPane tablescroll;
	private JPanel buttonPanel;
	private SettingTable settingTable;

	public DefaultSettingsInput(JFrame parentFrame) {
		super(parentFrame, BUNDLES.message("settingsUpdateDefaultTitle"));
		this.parentFrame = parentFrame;
	}

	public Optional<DefaultSettings> showDialog(DefaultSettings settings) {
		setup(settings);
		setVisible(true);
		return transformSettingsToDefaultSettings(getEnteredSettings());
	}

	protected void setup(DefaultSettings defaultSettings) {
		settingTable = new SettingTable(defaultSettings);
		tablescroll = new JScrollPane(settingTable.setupTable());

		buttonPanel = new ButtonPanelBuilder()
				.addSaveButton(getSaveListener(settingTable))
				.addExitButton(justDisposeListener(this))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
	}

	private Optional<DefaultSettings> transformSettingsToDefaultSettings(
			Optional<Settings[]> settings) {
		if (settings.isPresent()) {
			Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
			for (int i = 0; i < settings.get().length; i++) {
				defaultSettings.put(MONDAY.plus(i), settings.get()[i]);
			}
			return of(from(defaultSettings));
		} else {
			return empty();
		}
	}
}
