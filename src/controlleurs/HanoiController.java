package controlleurs;

import models.*;
import vue.*;
import exceptions.InvalidMoveException;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HanoiController extends MouseAdapter {
    private final HanoiGame game;
    private final HanoiGUI gui;
    private Pole selectedPole = null;
    private int selectedPoleIndex = -1;

    public HanoiController(HanoiGame game, HanoiGUI gui) {
        this.game = game;
        this.gui = gui;
        attachControllers();
    }

    private void attachControllers() {
        for (int i = 0; i < 3; i++) {
            gui.getPoleComponents().get(i).addMouseListener(this);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        PoleComponent clickedPole = (PoleComponent) e.getSource();
        int poleIndex = gui.getPoleComponents().indexOf(clickedPole);

        if (selectedPole == null) {
            if (!game.getPole(poleIndex).isEmpty()) {
                selectedPole = game.getPole(poleIndex);
                selectedPoleIndex = poleIndex;
                clickedPole.setSelected(true);
            }
        } else {
            try {
                game.moveDisk(selectedPoleIndex, poleIndex);
            } catch (InvalidMoveException ex) {
                JOptionPane.showMessageDialog(gui, "Erreur : " + ex.getMessage(), "Déplacement invalide", JOptionPane.WARNING_MESSAGE);
            } finally {
                gui.getPoleComponents().get(selectedPoleIndex).setSelected(false);
                selectedPole = null;
                selectedPoleIndex = -1;
                gui.refreshAllPoles();
            }
        }
    }
}