package vue;

import models.DataManager;
import models.Difficulty;
import models.Player;
import models.HanoiGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DifficultySelectionGUI extends JFrame {
    private Player player;

    public DifficultySelectionGUI(Player player) {
        this.player = player;
        setupWindow();
    }

    private void setupWindow() {
        setTitle("Choix de la difficulté");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        DifficultySelectionGUI.BackgroundPanel backgroundPanel = new DifficultySelectionGUI.BackgroundPanel("/images/imagedefond.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Choisissez votre difficulté", JLabel.CENTER);
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
                customFont = customFont.deriveFont(Font.BOLD, 20f);
                titleLabel.setFont(customFont);
                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                titleLabel.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            titleLabel.setFont(new Font("Courier New", Font.BOLD, 38));
        }
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Mode Normal"));

        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Mode Chronométré"));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton easyButton = createDifficultyButton("EASY", new Color(235, 174, 153), "FACILE");
        JButton mediumButton = createDifficultyButton("MEDIUM", new Color(238, 119, 77), "MOYEN");
        JButton hardButton = createDifficultyButton("HARD", new Color(196, 67, 23), "DIFFICILE");

        leftPanel.add(easyButton);
        leftPanel.add(mediumButton);
        leftPanel.add(hardButton);

        JButton timedEasyButton = createDifficultyButton("TIMED_EASY", new Color(235, 174, 153), "FACILE CHRONO");
        JButton timedMediumButton = createDifficultyButton("TIMED_MEDIUM", new Color(238, 119, 77), "MOYEN CHRONO");
        JButton timedHardButton = createDifficultyButton("TIMED_HARD", new Color(196, 67, 23), "DIFFICILE CHRONO");

        rightPanel.add(timedEasyButton);
        rightPanel.add(timedMediumButton);
        rightPanel.add(timedHardButton);

        JButton freeButton = createDifficultyButton("FREE", new Color(135, 51, 33), "MODE LIBRE");
        freeButton.setPreferredSize(new Dimension(200, 60));
        bottomPanel.add(freeButton);

        JPanel columnsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        columnsPanel.add(leftPanel);
        columnsPanel.add(rightPanel);

        centerPanel.add(columnsPanel, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Retour");
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/pixel.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

                customFont = customFont.deriveFont(Font.BOLD, 20f);

                backButton.setFont(customFont);

                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                backButton.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            backButton.setFont(new Font("Courier New", Font.BOLD, 38));
        }
        backButton.addActionListener(e -> {
            new MainMenuGUI();
            dispose();
        });

        JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navigationPanel.add(backButton);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);

        mainPanel.setOpaque(false);
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        navigationPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        columnsPanel.setOpaque(false);

        add(mainPanel);
        setResizable(false);
        setVisible(true);
    }

    private JButton createDifficultyButton(String difficultyName, Color backgroundColor, String displayText) {
        JButton button = new JButton(displayText);
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(180, 60));

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
                button.setBackground(backgroundColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }

        });

        button.addActionListener(e -> {
            try {
                if (difficultyName.equals("FREE")) {
                    handleFreeMode();
                } else if (difficultyName.startsWith("TIMED_")) {
                    models.Difficulty selectedDifficulty = models.Difficulty.valueOf(difficultyName);
                    startTimedGame(selectedDifficulty);
                } else {
                    models.Difficulty selectedDifficulty = models.Difficulty.valueOf(difficultyName);
                    startGame(selectedDifficulty);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la sélection de la difficulté: " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return button;
    }

    private void startTimedGame(models.Difficulty difficulty) throws IOException {
        models.HanoiGame game = new models.HanoiGame();
        game.setPlayer(player);

        int timeLimit = 0;

        switch (difficulty) {
            case TIMED_EASY:
                timeLimit = 30;
                break;
            case TIMED_MEDIUM:
                timeLimit = 90;
                break;
            case TIMED_HARD:
                timeLimit = 210;
                break;
        }

        game.startGame(difficulty, 0);
        game.getTime().setTimeLimit(timeLimit);
        HanoiGUI gameGUI = new HanoiGUI(game);
        game.setGui(gameGUI);
        dispose();
    }


    private void handleFreeMode() {
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(3, 3, 10, 1);
        JSpinner spinner = new JSpinner(spinnerModel);
        spinner.setFont(new Font("Arial", Font.PLAIN, 16));


        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre de disques:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(spinner, gbc);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Configuration du Mode Libre",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int numberOfDisks = (int) spinner.getValue();
            startGameFreeMode(models.Difficulty.FREE, numberOfDisks);
        }
    }


    private void startGameFreeMode(models.Difficulty difficulty, int numberOfDisks) {


        models.HanoiGame game = new models.HanoiGame();
        game.setPlayer(player);
        game.startGame(difficulty, numberOfDisks);
        HanoiGUI gameGUI = new HanoiGUI(game);
        game.setGui(gameGUI);
        dispose();
    }




    private void startGame(models.Difficulty difficulty) throws IOException {
        models.HanoiGame game = new models.HanoiGame(player, difficulty);
        HanoiGUI gameGUI = new HanoiGUI(game);
        game.setGui(gameGUI);
        dispose();
    }

    public class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                URL imageUrl = getClass().getResource("/images/imagedefond.jpg");
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
}