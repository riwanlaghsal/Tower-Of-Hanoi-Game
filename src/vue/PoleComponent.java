package vue;

import models.Pole;
import models.Disk;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PoleComponent extends JComponent {
    private Pole linkedPole;
    private List<DiskComponent> diskComponents = new ArrayList<>();
    private Color diskColor = new Color(70, 130, 180);
    public static final int WIDTH = 200;
    public static final int HEIGHT = 400;
    private boolean isSelected = false;

    public PoleComponent(Pole linkedPole) {
        this.linkedPole = linkedPole;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int poleX = WIDTH/2 - 5;
        g.setColor(isSelected ? Color.RED : Color.BLACK);
        g.fillRect(poleX, 50, 10, HEIGHT - 100);

        int y = HEIGHT - 70;
        for (Disk disk : linkedPole.getDisks()) {
            if (isSelected && disk == linkedPole.getDisks().peek()) {
                continue;
            }
            int diskWidth = 30 + disk.getSize() * 20;
            int x = (WIDTH - diskWidth) / 2;

            g.setColor(new Color(70, 130, 180));
            g.fillRoundRect(x, y, diskWidth, 20, 10, 10);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, diskWidth, 20, 10, 10);

            y -= 25;
        }

        if (isSelected && !linkedPole.isEmpty()) {
            Disk topDisk = linkedPole.getDisks().peek();
            int diskWidth = 30 + topDisk.getSize() * 20;
            int x = (WIDTH - diskWidth) / 2;

            g.setColor(new Color(100, 160, 210));
            g.fillRoundRect(x, 20, diskWidth, 20, 10, 10);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, 20, diskWidth, 20, 10, 10);
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }
}