package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.gui.commons.ButtonPanelBuilder.justDisposeListener;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.BundleStore;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.model.settings.Settings;

public class DefaultSettingsInput extends SettingsInput {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JScrollPane tablescroll;
	private JPanel buttonPanel;
	private SettingTable settingTable;
	private BundleStore bundles;

	public DefaultSettingsInput(JFrame parentFrame, BundleStore bundles) {
		super(parentFrame, bundles.message("settingsUpdateDefaultTitle"));
		this.parentFrame = parentFrame;
		this.bundles = bundles;
	}

	public Optional<Settings[]> showDialog(Settings[] settings) {
		setup(settings);
		setVisible(true);
		return getEnteredSettings();
	}

	protected void setup(Settings[] defaultSettings) {
		settingTable = new SettingTable(defaultSettings, bundles);
		tablescroll = new JScrollPane(settingTable.setupTable());
		settingTable.removeDateColumn();

		buttonPanel = new ButtonPanelBuilder(bundles)
				.addSaveButton(getSaveListener(settingTable))
				.addExitButton(justDisposeListener(this))
				.build();

		addPanel(tablescroll, BorderLayout.CENTER);
		addPanel(buttonPanel, BorderLayout.SOUTH);
		adjustPanesTo(parentFrame);
	}
}
