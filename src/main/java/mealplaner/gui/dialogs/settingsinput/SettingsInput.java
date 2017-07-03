package mealplaner.gui.dialogs.settingsinput;
/**
 * Martin Idel,
 * SettingsInput: Enter options for settings (extends JDialog-Extension).
 * Two modes: We can change the default settings or create settings for proposal.
 * This is a GUI-class, so it needs to interact with MainGUI. It is called by ProposalSummary and it actually calls the method for proposals of Menuplaner.
 **/

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.errorhandling.ErrorKeys;
import mealplaner.model.settings.Settings;

public abstract class SettingsInput extends JDialog implements ErrorKeys {
	private static final long serialVersionUID = 1L;
	private JPanel dataPanel;
	private Optional<Settings[]> enteredSettings = Optional.empty();

	public SettingsInput(JFrame parentFrame, Settings[] defaultSettings, String label) {
		super(parentFrame, label, true);
	}

	public Optional<Settings[]> showDialog() {
		setVisible(true);
		return enteredSettings;
	}

	protected void setup(Settings[] defaultSettings) {
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new BorderLayout());
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