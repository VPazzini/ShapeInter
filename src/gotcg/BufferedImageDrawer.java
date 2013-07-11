package gotcg;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

class BufferedImageDrawer extends Frame {

    public BufferedImage bi;

    public Graphics2D g2dbi;

    private Graphics2D g2d;

    public BufferedImageDrawer(BufferedImage buffIm, int width, int height) {
        bi = buffIm;
        g2dbi = bi.createGraphics();

        addWindowListener(new MyFinishWindow());

        this.setTitle("Double-Buffering");
        this.setSize(width, height);
        this.setVisible(true);

    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        g2d = (Graphics2D) g;
        g2d.drawImage(bi, 0, 0, null);
    }
}
