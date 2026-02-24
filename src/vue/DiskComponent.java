package vue;

import models.Disk;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class DiskComponent extends JComponent {
    public static final int HEIGHT = 20;
    private final Disk disk;
    private int width;

    public DiskComponent(Disk disk) {
        this.disk = disk;
        this.width = 30 + disk.getSize() * 20;
        setPreferredSize(new Dimension(width, HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, width, HEIGHT);
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, HEIGHT - 1);
    }

    public Disk getDisk() {
        return disk;
    }

    public int getDiskWidth() {
        return width;
    }

    public void setY(int y) {
        setLocation(getX(), y);
    }

    public void setPosition(int x, int y) {
        setLocation(x, y);
    }


}
