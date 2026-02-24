package vue;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    public SettingsWindow(JFrame parent) {
        super(parent, "Paramètres", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JCheckBox soundToggle = new JCheckBox("Activer les sons", true);
        JSlider difficultySlider = new JSlider(1, 3, 2);

        panel.add(new JLabel("Options audio:"));
        panel.add(soundToggle);
        panel.add(new JLabel("Niveau de difficulté:"));
        panel.add(difficultySlider);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        add(panel);

        setVisible(true);
    }
}