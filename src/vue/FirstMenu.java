package vue;

import models.Player;
import models.DataManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;

public class FirstMenu {

    private JFrame frame;
    private JTextField playerNameField;
    private JButton continueButton;

    public FirstMenu() {
        checkExistingPlayer();
    }

    private void checkExistingPlayer() {
        try {
            Player existingPlayer = DataManager.loadPlayerData();

            if (existingPlayer != null) {
                showContinueMenu();
            } else {
                initializeCreateProfile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur de chargement des données du joueur.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            initializeCreateProfile();
        }
    }

    private void initializeCreateProfile() {
        frame = new JFrame("Créer un profil de joueur");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        String imagePath = "/images/imageMainMenu.png";
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image backgroundImage;
                try {
                    backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    System.err.println("Erreur : Impossible de charger l'image de fond !");
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Bienvenue ! Créez votre profil de joueur", SwingConstants.CENTER);
        styleLabel(titleLabel, 28f);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centralPanel = new JPanel();
        centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
        centralPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Nom du Joueur :", SwingConstants.CENTER);
        styleLabel(nameLabel, 18f);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        playerNameField = new JTextField();
        styleTextField(playerNameField);
        playerNameField.setMaximumSize(new Dimension(300, 30));

        centralPanel.add(nameLabel);
        centralPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centralPanel.add(playerNameField);

        continueButton = new JButton("Continuer");
        styleButtonWithHover(continueButton);
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.setMaximumSize(new Dimension(200, 40));
        continueButton.addActionListener(e -> {
            try {
                handleContinue();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Erreur lors de la sauvegarde des données.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(centralPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(continueButton);
        backgroundPanel.add(Box.createVerticalGlue());

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private void showContinueMenu() {
        frame = new JFrame("Profil trouvé");
        frame.setSize(800, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        String imagePath = "/images/imageMainMenu.png";
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image backgroundImage;
                try {
                    backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    System.err.println("Erreur : Impossible de charger l'image de fond !");
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Bon retour parmi nous !", SwingConstants.CENTER);
        styleLabel(titleLabel, 28f);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton continueButton = new JButton("Continuer");
        styleButtonWithHover(continueButton); 
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> {
            frame.dispose();
            new MainMenuGUI();
        });

        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(continueButton);
        backgroundPanel.add(Box.createVerticalGlue());

        frame.setContentPane(backgroundPanel);
        frame.setVisible(true);
    }

    private void styleButtonWithHover(JButton button) {
        button.setFont(loadPixelFont(20f));
        button.setBackground(new Color(139, 69, 19));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Color normalBackground = new Color(139, 69, 19);
        Color hoverBackground = new Color(160, 82, 45);
        Color normalForeground = Color.WHITE;
        Color hoverForeground = new Color(255, 223, 186);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBackground);
                button.setForeground(hoverForeground);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalBackground);
                button.setForeground(normalForeground);
            }
        });
    }

    private void handleContinue() throws IOException {
        String playerName = playerNameField.getText().trim();

        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Le nom du joueur ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player player = new Player(1);
        player.setName(playerName);

        DataManager.savePlayerData(player);

        frame.dispose();
        new MainMenuGUI();
    }

    private void styleLabel(JLabel label, float fontSize) {
        label.setFont(loadPixelFont(fontSize));
        label.setForeground(Color.BLACK);
        label.putClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey, null);
        label.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }

    private void styleButton(JButton button) {
        button.setFont(loadPixelFont(16f));
        button.setBackground(new Color(139, 69, 19));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(loadPixelFont(14f));
        textField.setBackground(new Color(245, 245, 245));
        textField.setForeground(Color.BLACK);
        textField.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2));
    }

    private Font loadPixelFont(float size) {
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                is.close();
                return customFont.deriveFont(size);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de la police : " + e.getMessage());
        }
        return new Font("Arial", Font.PLAIN, (int) size);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FirstMenu::new);
    }
}