package vue;

import models.Player;
import models.DataManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Map;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import java.awt.Component;



public class MainMenuGUI extends JFrame {
    private Player currentPlayer;

    public MainMenuGUI(Player player) {
        this.currentPlayer = player;
        setupWindow();
        loadPlayerData();
    }

    public MainMenuGUI() {
        setupWindow();
        loadPlayerData();
    }

    private void setupWindow() {
        setTitle("Menu Principal - Tours de Hanoi");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        MainMenuGUI.BackgroundPanel backgroundPanel = new MainMenuGUI.BackgroundPanel("/images/imageMainMenu.png");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(800, 150));
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JButton playButton = createMenuButton("Jouer");
        JButton statsButton = createMenuButton("Statistiques");
        JButton quitButton = createMenuButton("Quitter");

        mainPanel.add(playButton, gbc);
        mainPanel.add(statsButton, gbc);
        mainPanel.add(quitButton, gbc);

        add(mainPanel);
        setResizable(false);
        setVisible(true);
    }

    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                URL imageUrl = getClass().getResource(imagePath);
                if (imageUrl == null) {
                    throw new IllegalArgumentException("Image non trouvée : " + imagePath);
                }
                backgroundImage = new ImageIcon(imageUrl).getImage();
            } catch (Exception e) {
                System.err.println("Erreur de chargement de l'image : " + e.getMessage());
                setBackground(Color.BLACK);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(new Color(135, 51, 33));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

                customFont = customFont.deriveFont(Font.BOLD, 20f);

                button.setFont(customFont);

                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                button.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            button.setFont(new Font("Courier New", Font.BOLD, 38));
        }

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 51, 33));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(135, 51, 33));
            }
        });

        switch(text) {
            case "Jouer":
                button.addActionListener(e -> startGame());
                break;
            case "Statistiques":
                button.addActionListener(e -> showStats());
                break;
            case "Quitter":
                button.addActionListener(e -> System.exit(0));
                break;
        }

        return button;
    }

    private void startGame() {
        new DifficultySelectionGUI(currentPlayer);
        dispose();
    }

    private void showStats() {
        String filePath = "player.json";

        Map<String, Object> jsonData = readJsonFile(filePath);

        if (jsonData == null || !jsonData.containsKey("stats")) {
            JOptionPane.showMessageDialog(this, "Impossible de lire les données du joueur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Map<String, Object> statsData = (Map<String, Object>) jsonData.get("stats");

        String[] statNames = {
                "Parties totales", "Dernier score", "Meilleur score",
                "Score moyen", "Dernier temps", "Meilleur temps", "Challenges accomplis"
        };
        String[] jsonKeys = {
                "totalGames", "lastScore", "bestScore",
                "averageScore", "lastTime", "bestTime", "totalChallengesCompleted"
        };

        String[] columnNames = { "Statistique", "Valeur" };
        Object[][] tableData = new Object[statNames.length][2];

        for (int i = 0; i < statNames.length; i++) {
            tableData[i][0] = statNames[i];
            tableData[i][1] = statsData.getOrDefault(jsonKeys[i], "Non disponible");
        }

        JFrame statsFrame = new JFrame("Statistiques du joueur");
        statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statsFrame.setSize(this.getWidth(), this.getHeight());
        statsFrame.setLocationRelativeTo(this);

        String imagePath = "/images/imagedefond.jpg";
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Image backgroundImage = null;
                try {
                    backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
                } catch (Exception e) {
                    System.err.println("Erreur : Impossible de charger l'image de fond !");
                }

                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        JTable statsTable = new JTable(tableData, columnNames);

        statsTable.setBackground(new Color(139, 69, 19));
        statsTable.setForeground(Color.WHITE);
        statsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        statsTable.getTableHeader().setBackground(new Color(160, 82, 45));
        statsTable.getTableHeader().setForeground(Color.WHITE);
        statsTable.setGridColor(new Color(210, 180, 140));
        statsTable.setRowHeight(40);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setBackground(new Color(139, 69, 19));
        renderer.setForeground(Color.WHITE);
        statsTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        statsTable.getColumnModel().getColumn(1).setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

                customFont = customFont.deriveFont(Font.BOLD, 20f);

                statsTable.setFont(customFont);

                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                statsTable.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            statsTable.setFont(new Font("Courier New", Font.BOLD, 38));
        }

        JPanel challengesOuterPanel = new JPanel();
        challengesOuterPanel.setOpaque(false);
        challengesOuterPanel.setLayout(new GridLayout(3, 1, 0, 10));
        challengesOuterPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 0, 50));

        Font challengeFont = null;
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                challengeFont = Font.createFont(Font.TRUETYPE_FONT, is);
                challengeFont = challengeFont.deriveFont(Font.BOLD, 20f);
                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                challengeFont = new Font("Courier New", Font.BOLD, 18);
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            challengeFont = new Font("Courier New", Font.BOLD, 18);
        }

        JPanel challenge1Panel = createChallengePanel("Finir le mode EASY de manière optimale", challengeFont);
        JPanel challenge2Panel = createChallengePanel("Finir le mode MEDIUM de manière optimale", challengeFont);
        JPanel challenge3Panel = createChallengePanel("Finir le mode HARD de manière optimale", challengeFont);

        challengesOuterPanel.add(challenge1Panel);
        challengesOuterPanel.add(challenge2Panel);
        challengesOuterPanel.add(challenge3Panel);

        JButton quitButton = new JButton("Quitter");

        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

                customFont = customFont.deriveFont(Font.BOLD, 20f);

                quitButton.setFont(customFont);

                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                quitButton.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            quitButton.setFont(new Font("Courier New", Font.BOLD, 38));
        }
        quitButton.setBackground(new Color(139, 69, 19));
        quitButton.addActionListener(e -> statsFrame.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(quitButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(challengesOuterPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(southPanel, BorderLayout.SOUTH);

        statsFrame.setContentPane(backgroundPanel);
        statsFrame.setVisible(true);
    }

    private JPanel createChallengePanel(String text, Font font) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(139, 69, 19));
        panel.setPreferredSize(new Dimension(300, 50));

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(font);

        panel.add(label, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createLineBorder(new Color(210, 180, 140), 2));

        return panel;
    }

    private Map<String, Object> readJsonFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private void loadPlayerData() {
        try {
            currentPlayer = DataManager.loadPlayerData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Création d'un nouveau profil...");
            currentPlayer = new Player(1);
            currentPlayer.setName("Joueur 1");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new MainMenuGUI();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Erreur lors du démarrage: " + e.getMessage());
            }
        });
    }
}