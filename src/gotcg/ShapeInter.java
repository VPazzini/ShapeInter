package gotcg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//Grupo: Andrei Costa, Mikael Poetsch, Vinicius Pazzini
class ShapeInter {

    private BufferedImageDrawer buffid;
    private int width;
    private int height;
    private int steps;
    private TriangulatedImage t[] = new TriangulatedImage[16];
    private ArrayList<Point> points;
    private int i;
    private int x;

    public ShapeInter(BufferedImageDrawer bid) {
        Eight e = new Eight();
        e.calculateOctant(200, 200, 150);
        points = e.getCompleteList();

        buffid = bid;

        width = 150;
        height = 200;

        steps = 100;

        startImages();
    }

    private void startImages() {
        TriangulatedImage.triangles = this.trianglePoints();

        t[0] = new TriangulatedImage(width, height, "1st.png", 0);
        t[1] = new TriangulatedImage(width, height, "2nd.png", 1);
        t[2] = new TriangulatedImage(width, height, "3rd.png", 2);
        t[3] = new TriangulatedImage(width, height, "4th.png", 3);
        t[4] = new TriangulatedImage(width, height, "5th.png", 4);
        t[5] = new TriangulatedImage(width, height, "6th.png", 5);
        t[6] = new TriangulatedImage(width, height, "7th.png", 6);
        t[7] = new TriangulatedImage(width, height, "8th.png", 7);
        t[8] = new TriangulatedImage(width, height, "9th.png", 8);
        t[9] = new TriangulatedImage(width, height, "10th.png", 9);
        t[10] = new TriangulatedImage(width, height, "11th.png", 10);
        t[11] = new TriangulatedImage(width, height, "amy.png", 11);
        t[12] = new TriangulatedImage(width, height, "clara.png", 12);
        t[13] = new TriangulatedImage(width, height, "river.png", 13);
        t[14] = new TriangulatedImage(width, height, "rory.png", 14);
        t[15] = new TriangulatedImage(width, height, "rory\'s dad.png", 15);
    }

    public void run() {
        TriangulatedImage a, b;
        Point pointA, pointB;
        int nextImage;
        double step = (double) points.size() / 16;

        for (int thisImage = 0; thisImage < 16; thisImage++) {

            if (thisImage == 15) {
                nextImage = 0;
            } else {
                nextImage = thisImage + 1;
            }

            a = t[thisImage];
            pointA = points.get((int) (thisImage * step));
            b = t[nextImage];
            pointB = points.get((int) (nextImage * step));

            for (int j = 0; j < steps; j++) {
                double alpha = (double) j / steps;

                int pointX = (int) ((1 - alpha) * pointA.x + alpha * pointB.x);
                int pointY = (int) ((1 - alpha) * pointA.y + alpha * pointB.y);

                buffid.g2dbi.drawImage(a.mixWith(b, alpha), pointX, pointY, null);
                buffid.repaint();
            }
        }
    }

    private static double[] convexCombination(double[] a, double[] b, double alpha) {
        double[] result = new double[6];

        result[0] = (1 - alpha) * a[0] + alpha * b[0];
        result[1] = (1 - alpha) * a[1] + alpha * b[1];
        result[2] = (1 - alpha) * a[2] + alpha * b[2];
        result[3] = (1 - alpha) * a[3] + alpha * b[3];
        result[4] = (1 - alpha) * a[4] + alpha * b[4];
        result[5] = (1 - alpha) * a[5] + alpha * b[5];

        return result;
    }

    public static void main(String args[]) {
        int width = 600;
        int height = 1000;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        BufferedImageDrawer bid = new BufferedImageDrawer(bi, width, height);
        bid.setTitle("Regeneration Cycle");

        ShapeInter mcs = new ShapeInter(bid);

        mcs.run();
    }

    private int[][] trianglePoints() {
        int[][] tri = new int[16][3];

        tri[0][0] = 0;
        tri[0][1] = 4;
        tri[0][2] = 3;

        tri[1][0] = 0;
        tri[1][1] = 1;
        tri[1][2] = 4;

        tri[2][0] = 1;
        tri[2][1] = 2;
        tri[2][2] = 5;

        tri[3][0] = 2;
        tri[3][1] = 5;
        tri[3][2] = 6;

        tri[4][0] = 6;
        tri[4][1] = 9;
        tri[4][2] = 12;

        tri[5][0] = 11;
        tri[5][1] = 9;
        tri[5][2] = 12;

        tri[6][0] = 8;
        tri[6][1] = 10;
        tri[6][2] = 11;

        tri[7][0] = 3;
        tri[7][1] = 8;
        tri[7][2] = 10;

        tri[8][0] = 3;
        tri[8][1] = 7;
        tri[8][2] = 8;

        tri[9][0] = 3;
        tri[9][1] = 4;
        tri[9][2] = 7;

        tri[10][0] = 1;
        tri[10][1] = 4;
        tri[10][2] = 7;

        tri[11][0] = 1;
        tri[11][1] = 5;
        tri[11][2] = 7;

        tri[12][0] = 5;
        tri[12][1] = 6;
        tri[12][2] = 7;

        tri[13][0] = 9;
        tri[13][1] = 6;
        tri[13][2] = 7;

        tri[14][0] = 7;
        tri[14][1] = 9;
        tri[14][2] = 11;

        tri[15][0] = 7;
        tri[15][1] = 8;
        tri[15][2] = 11;

        return tri;
    }
}
