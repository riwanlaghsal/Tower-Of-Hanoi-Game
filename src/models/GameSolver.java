package models;


import javax.swing.*;
import java.util.Stack;
import java.util.Collections;

public class GameSolver {
    private static final int TARGET_POLE = 2;
    private static final int DELAY_MS = 500;

    public static void solve(HanoiGame game) {
        new Thread(() -> {
            try {
                int totalDisks = game.getNumberOfDisks();
                solveRecursive(game, totalDisks, TARGET_POLE);
                SwingUtilities.invokeLater(() -> game.getGui().enableSolveButton());
            } catch (Exception e) {
                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(game.getGui(), "Erreur : " + e.getMessage()));
            }
        }).start();
    }

    private static void solveRecursive(HanoiGame game, int diskSize, int target) {
        if (diskSize < 1) return;

        int currentPole = findDiskPole(game, diskSize);
        if (currentPole == target) {
            solveRecursive(game, diskSize - 1, target);
            return;
        }

        int auxiliary = 3 - currentPole - target;

        solveRecursive(game, diskSize - 1, auxiliary);

        executeMove(game, currentPole, target);

        solveRecursive(game, diskSize - 1, target);
    }

    private static int findDiskPole(HanoiGame game, int diskSize) {
        for (int i = 0; i < 3; i++) {
            if (game.getPole(i).containsDisk(diskSize)) {
                return i;
            }
        }
        throw new IllegalStateException("Disque " + diskSize + " introuvable");
    }

    private static void executeMove(HanoiGame game, int from, int to) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                game.autoMoveDisk(from, to);
                game.getGui().refreshAllPoles();
            });
            Thread.sleep(DELAY_MS);
        } catch (Exception e) {
            throw new RuntimeException("Échec du mouvement automatique", e);
        }
    }
}
