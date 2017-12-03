package mealplaner.gui.dialogs.settingsinput;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.model.settings.Settings;

public abstract class SettingsInput extends JDialog {
	private static final long serialVersionUID = 1L;
	protected JPanel dataPanel;
	private Optional<Settings[]> enteredSettings = Optional.empty();

	public SettingsInput(JFrame parentFrame, String label) {
		super(parentFrame, label, true);
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new BorderLayout());
	}

	protected Optional<Settings[]> getEnteredSettings() {
		return enteredSettings;
	}

	protected ActionListener getSaveListener(SettingTable settingTable) {
		return action -> {
			enteredSettings = Optional.of(settingTable.getSettings());
			setVisible(false);
			dispose();
		};
	}

	protected void addPanel(Component component, Object constraints) {
		dataPanel.add(component, constraints);
	}

	protected void adjustPanesTo(JFrame parentFrame) {
		getContentPane().add(dataPanel);
		setSize(550, 210);
		setLocationRelativeTo(parentFrame);
	}
}