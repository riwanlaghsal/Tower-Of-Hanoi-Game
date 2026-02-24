package vue;

import controlleurs.HanoiController;
import models.GameSolver;
import models.HanoiGame;
import models.Player;
import models.Pole;
import java.util.Vector;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.*;

public class HanoiGUI extends JFrame implements Observer {
    private HanoiGame game;
    private boolean useAutoSolver;
    private JPanel gamePanel;
    // private List<Pole> modelPoles = new ArrayList<>();
    private final Vector<PoleComponent> poleComponents = new Vector<>();
    private JButton historyButton;
    private JButton undoButton;
    private JButton solveButton;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JPanel timerPanel;
    private javax.swing.Timer uiTimer;

    public HanoiGUI(HanoiGame game) {
        this.game = game;
        this.game.addObserver(this);
        this.gamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
        setupWindow();
        new HanoiController(game, this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                uiTimer.stop();
            }
        });
    }

    public HanoiGUI(HanoiGame game, Player player) {
        this(game);
        game.setPlayer(player);
    }

    private void setupWindow() {
        setTitle("Tour de Hanoï - Projet POO");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel("/images/imagedefond.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);

        initPoles();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 600));


        JPanel topPanel = new JPanel(new BorderLayout());

        topPanel.add(gamePanel, BorderLayout.CENTER);

        JPanel timerContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerContainer.setOpaque(false);

        setupTimer();
        timerPanel.setPreferredSize(new Dimension(300, 60));
        timerPanel.setMaximumSize(new Dimension(300, 60));
        timerContainer.add(timerPanel);
        topPanel.add(timerContainer, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(new Color(240, 240, 240));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scoreLabel = new JLabel("Score : 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scorePanel.add(scoreLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        solveButton = createButton("Soluce", new Color(34, 139, 34));
        undoButton = createButton("Annuler", new Color(220, 53, 69));
        historyButton = createButton("Historique", new Color(0, 123, 255));

        JButton backButton = createBackButton();
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        navPanel.add(backButton);
        navPanel.setBounds(650, 10, 140, 50);

        layeredPane.add(topPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(bottomPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(navPanel, JLayeredPane.PALETTE_LAYER);



        buttonPanel.add(historyButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(solveButton);

        bottomPanel.add(scorePanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.setBounds(0, 0, 800, 600);
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        JButton menuButton = createBackButton();
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        menuPanel.setOpaque(false);
        menuPanel.add(menuButton);
        menuPanel.setBounds(670, 10, 120, 50);
        layeredPane.add(menuPanel, JLayeredPane.PALETTE_LAYER);


        add(layeredPane, BorderLayout.CENTER);


        undoAction();
        historyButton.addActionListener(e -> showHistoryWindow());
        solveAction();

        mainPanel.setOpaque(false);
        topPanel.setOpaque(false);
        gamePanel.setOpaque(false);
        buttonPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        scorePanel.setOpaque(false);
        layeredPane.setOpaque(false);

        setVisible(true);
        setResizable(false);
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


    private JButton createBackButton() {
        JButton backButton = new JButton(" MENU →");

        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(70, 70, 70));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);

        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 50), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        backButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(new Color(90, 90, 90));
            }
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(new Color(70, 70, 70));
            }
        });

        backButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Voulez-vous vraiment quitter la partie en cours ?",
                    "Retour au menu",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                try {
                    if (game.getTime() != null && game.getTime().isRunning()) {
                        game.getTime().stop();
                    }
                    if (uiTimer.isRunning()) {
                        uiTimer.stop();
                    }

                    openMainMenu();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Erreur lors de l'ouverture du menu principal : " + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        return backButton;
    }

    private void initPoles() {
        gamePanel.removeAll();
        poleComponents.clear();

        for (int i = 0; i < 3; i++) {
            PoleComponent poleComp = new PoleComponent(game.getPole(i));
            poleComp.setPreferredSize(new Dimension(200, 400));
            poleComponents.add(poleComp);
            gamePanel.add(poleComp);
        }
    }

    private void setupTimer() {
        timerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 20, 20),
                        getWidth(), 0, new Color(40, 40, 40));
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(255, 50, 50, 100));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 10, 10);
            }
        };
        timerPanel.setPreferredSize(new Dimension(250, 70));
        timerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        timerLabel = new JLabel("00:00.00", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(
                        RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB
                );
                super.paintComponent(g2);
            }
        };
        timerLabel.setForeground(new Color(255, 80, 80));
        try {
            InputStream is = getClass().getResourceAsStream("/fonts/digital-7.ttf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);

                customFont = customFont.deriveFont(Font.BOLD, 50f);

                timerLabel.setFont(customFont);

                is.close();
            } else {
                System.err.println("Police personnalisée non trouvée, utilisation de la police par défaut");
                timerLabel.setFont(new Font("Courier New", Font.BOLD, 38));
            }
        } catch (IOException | FontFormatException e) {
            System.err.println("Erreur lors du chargement de la police: " + e.getMessage());
            timerLabel.setFont(new Font("Courier New", Font.BOLD, 38));
        }


        timerPanel.add(timerLabel, BorderLayout.CENTER);

        uiTimer = new Timer(100, e -> updateTimerDisplay());
        uiTimer.start();
    }

    private void updateTimerDisplay() {
        if (game != null && game.getTime() != null && game.getTime().isRunning()) {
            timerLabel.setText(game.getTime().getFormattedTime());
        } else {
            timerLabel.setText("00:00.00");
        }
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(150, 45));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 2),
                BorderFactory.createEmptyBorder(8, 25, 8, 25)
        ));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public List<PoleComponent> getPoleComponents() {
        return poleComponents;
    }

    public void refreshAllPoles() {
        for (PoleComponent pc : poleComponents) {
            pc.repaint();
        }
    }

    public void update(Observable o, Object arg) {
        if (o instanceof HanoiGame) {
            int count = (arg != null) ? (int)arg : game.getMoveCount();
            scoreLabel.setText("Score: " + count);
            refreshAllPoles();
            game.endGame();
            if (game.isGameOver()) {
                if (game.getTime() != null && game.getTime().isRunning()) {
                    game.getTime().stop();
                    uiTimer.stop();
                }
            }
        }
    }

    public JButton getHistoryButton() { return historyButton; }
    public JButton getUndoButton() { return undoButton; }
    public JButton getSolveButton() { return solveButton; }

    public void undoAction(){
        this.undoButton.addActionListener(e -> {
            String result = game.getHistory().undoLastMove(game);
            System.out.println(result);
            game.hasChanged();
            game.notifyObservers();
            refreshAllPoles();
        });
    }

    private void showHistoryWindow() {
        JDialog historyDialog = new JDialog(this, "Historique des Coups", false);
        historyDialog.setSize(300, this.getHeight());
        historyDialog.setLocationRelativeTo(this);

        JTextArea historyText = new JTextArea();
        historyText.setEditable(false);
        historyText.setFont(new Font("Consolas", Font.PLAIN, 14));
        historyText.setMargin(new Insets(15, 20, 15, 20));

        JScrollPane scrollPane = new JScrollPane(historyText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        historyDialog.add(scrollPane);

        String formattedHistory = formatHistory(game.getHistory().getMoves());
        historyText.setText(formattedHistory);

        historyText.setCaretPosition(0);

        historyDialog.setVisible(true);
    }

    private String formatHistory(Stack<String> moves) {
        if (moves.isEmpty()) return " Aucun mouvement enregistré...";

        StringBuilder sb = new StringBuilder();
        int counter = 1;

        for (String move : moves) {
            sb.append(String.format(" ⎡ Coup #%03d ⎦\n", counter++))
                    .append(String.format(" │ De Poteau %s \n", move.split(" -> ")[0]))
                    .append(String.format(" │ Vers Poteau %s \n", move.split(" -> ")[1]))
                    .append(" ╰──────────────────────╯\n\n");
        }

        return sb.toString();
    }

    public void solveAction(){
        solveButton.addActionListener(e -> {
            useAutoSolver = true;
            solveButton.setEnabled(false);
            GameSolver.solve(game);

            javax.swing.Timer enableTimer = new javax.swing.Timer(2000, evt -> solveButton.setEnabled(true));
            enableTimer.setRepeats(false);
            enableTimer.start();
        });
    }

    public void enableSolveButton() {
        solveButton.setEnabled(true);
    }

    public void showResultDialog(boolean isOptimal) {
        JDialog resultDialog = new JDialog(this, "Partie terminée", true);
        resultDialog.setSize(350, 250);
        resultDialog.setLocationRelativeTo(this);
        resultDialog.setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));


        JLabel message;
        if (useAutoSolver) {
            message = new JLabel("Solution automatique utilisée!");
        }else {
            message = new JLabel(isOptimal ? "Félicitations ! Solution optimale !" : "Solution non optimale");
        }
        message.setFont(new Font("Segoe UI", Font.BOLD, 16));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel details = new JLabel(
                String.format("Coups: %d (Minimum possible: %d)",
                        game.getMoveCount(),
                        (int)(Math.pow(2, game.getNumberOfDisks()) - 1))
        );
        details.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));


        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> {
            resultDialog.dispose();
            try {
                openMainMenu();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        buttonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonPanel.add(quitButton);

        contentPanel.add(message);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(details);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(buttonPanel);

        resultDialog.add(contentPanel, BorderLayout.CENTER);
        resultDialog.setVisible(true);
    }

    public void showResultTimed(boolean success) {
        JDialog resultDialog = new JDialog(this, "Résultat", true);
        resultDialog.setLayout(new BorderLayout());
        resultDialog.setSize(450, 300);
        resultDialog.setLocationRelativeTo(this);

        String message = success
                ? "Félicitations ! Vous avez réussi dans le temps imparti !"
                : "Dommage, vous avez dépassé le temps imparti !";

        JLabel resultLabel = new JLabel(message, JLabel.CENTER);
        resultDialog.add(resultLabel, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton quitButton = new JButton("Quitter");
        quitButton.addActionListener(e -> {
            try {
                openMainMenu();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            resultDialog.dispose();
        });
        buttonsPanel.add(quitButton);

        resultDialog.add(buttonsPanel, BorderLayout.SOUTH);

        resultDialog.setVisible(true);
    }



    private void openMainMenu() throws IOException {
        new MainMenuGUI();
        dispose();
    }

    private void resetGame() {
        if (game.getTime() != null && game.getTime().isRunning()) {
            game.getTime().stop();
        }

        game.startGame(game.getDifficulty(), game.getNumberOfDisks());

        initPoles();

        scoreLabel.setText("Score: 0");

        if (!uiTimer.isRunning()) {
            uiTimer.start();
        }

        undoButton.setEnabled(true);
        solveButton.setEnabled(true);
        historyButton.setEnabled(true);

        useAutoSolver = false;

        refreshAllPoles();
        revalidate();
        repaint();
    }

}