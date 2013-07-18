package gotcg;

import java.awt.Point;
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

    public ShapeInter(BufferedImageDrawer bid) {
        Eight e = new Eight();
        e.calculateOctant(200, 200, 100);
        points = e.getCompleteList();

        buffid = bid;

        width = 150;
        height = 200;
        steps = 20;

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
        int nextImage = 0;
        double step = (double) points.size() / 16;

        for (int thisImage = 0; thisImage < 16; thisImage++) {
            pointA = points.get((int) (thisImage * step));
            
            if (thisImage == 15) {
                nextImage = 0;
                pointB = points.get(0);
            } else {
                nextImage += 1;
                pointB = points.get((int) (nextImage * step));
            }

            for (int j = 0; j < steps; j++) {
                double alpha = (double) j / steps;

                int pointX = (int) ((1 - alpha) * pointA.x + alpha * pointB.x);
                int pointY = (int) ((1 - alpha) * pointA.y + alpha * pointB.y);

                buffid.g2dbi.drawImage(t[thisImage].mixWith(t[nextImage], alpha), pointX, pointY, null);
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
        int[][] tri = new int[61][3];

        tri[0][0] = 31;
        tri[0][1] = 30;
        tri[0][2] = 27;

        tri[1][0] = 24;
        tri[1][1] = 30;
        tri[1][2] = 27;

        tri[2][0] = 26;
        tri[2][1] = 27;
        tri[2][2] = 31;

        tri[3][0] = 23;
        tri[3][1] = 24;
        tri[3][2] = 30;

        tri[4][0] = 33;
        tri[4][1] = 26;
        tri[4][2] = 31;

        tri[5][0] = 19;
        tri[5][1] = 23;
        tri[5][2] = 24;

        tri[6][0] = 22;
        tri[6][1] = 33;
        tri[6][2] = 26;

        tri[7][0] = 19;
        tri[7][1] = 20;
        tri[7][2] = 24;

        tri[8][0] = 21;
        tri[8][1] = 22;
        tri[8][2] = 26;

        tri[9][0] = 20;
        tri[9][1] = 24;
        tri[9][2] = 25;

        tri[10][0] = 21;
        tri[10][1] = 25;
        tri[10][2] = 26;

        tri[11][0] = 24;
        tri[11][1] = 25;
        tri[11][2] = 27;

        tri[12][0] = 25;
        tri[12][1] = 26;
        tri[12][2] = 27;

        tri[13][0] = 11;
        tri[13][1] = 12;
        tri[13][2] = 13;

        tri[14][0] = 20;
        tri[14][1] = 21;
        tri[14][2] = 25;

        tri[15][0] = 16;
        tri[15][1] = 20;
        tri[15][2] = 21;

        tri[16][0] = 15;
        tri[16][1] = 19;
        tri[16][2] = 20;

        tri[17][0] = 17;
        tri[17][1] = 21;
        tri[17][2] = 22;

        tri[18][0] = 10;
        tri[18][1] = 15;
        tri[18][2] = 20;

        tri[19][0] = 13;
        tri[19][1] = 17;
        tri[19][2] = 21;

        tri[20][0] = 10;
        tri[20][1] = 16;
        tri[20][2] = 20;

        tri[21][0] = 13;
        tri[21][1] = 16;
        tri[21][2] = 21;

        tri[22][0] = 9;
        tri[22][1] = 10;
        tri[22][2] = 16;

        tri[23][0] = 13;
        tri[23][1] = 14;
        tri[23][2] = 16;

        tri[24][0] = 9;
        tri[24][1] = 14;
        tri[24][2] = 16;

        tri[25][0] = 2;
        tri[25][1] = 9;
        tri[25][2] = 14;

        tri[26][0] = 7;
        tri[26][1] = 10;
        tri[26][2] = 15;

        tri[27][0] = 12;
        tri[27][1] = 13;
        tri[27][2] = 17;

        tri[28][0] = 5;
        tri[28][1] = 7;
        tri[28][2] = 15;

        tri[29][0] = 6;
        tri[29][1] = 12;
        tri[29][2] = 17;
        
        tri[30][0] = 5;
        tri[30][1] = 7;
        tri[30][2] = 8;

        tri[31][0] = 6;
        tri[31][1] = 11;
        tri[31][2] = 12;

        tri[32][0] = 2;
        tri[32][1] = 8;
        tri[32][2] = 9;

        tri[33][0] = 2;
        tri[33][1] = 11;
        tri[33][2] = 14;

        tri[34][0] = 3;
        tri[34][1] = 5;
        tri[34][2] = 8;

        tri[35][0] = 4;
        tri[35][1] = 6;
        tri[35][2] = 11;

        tri[36][0] = 2;
        tri[36][1] = 3;
        tri[36][2] = 8;

        tri[37][0] = 2;
        tri[37][1] = 4;
        tri[37][2] = 11;

        tri[38][0] = 34;
        tri[38][1] = 28;
        tri[38][2] = 30;

        tri[39][0] = 28;
        tri[39][1] = 30;
        tri[39][2] = 31;
        
        tri[40][0] = 28;
        tri[40][1] = 29;
        tri[40][2] = 31;

        tri[41][0] = 23;
        tri[41][1] = 34;
        tri[41][2] = 30;

        tri[42][0] = 33;
        tri[42][1] = 29;
        tri[42][2] = 31;

        tri[43][0] = 19;
        tri[43][1] = 23;
        tri[43][2] = 34;

        tri[44][0] = 22;
        tri[44][1] = 33;
        tri[44][2] = 29;

        tri[45][0] = 15;
        tri[45][1] = 19;
        tri[45][2] = 34;

        tri[46][0] = 17;
        tri[46][1] = 22;
        tri[46][2] = 29;

        tri[47][0] = 32;
        tri[47][1] = 15;
        tri[47][2] = 34;

        tri[48][0] = 17;
        tri[48][1] = 18;
        tri[48][2] = 29;

        tri[49][0] = 32;
        tri[49][1] = 15;
        tri[49][2] = 0;
        
        tri[50][0] = 17;
        tri[50][1] = 18;
        tri[50][2] = 1;

        tri[51][0] = 0;
        tri[51][1] = 5;
        tri[51][2] = 15;

        tri[52][0] = 1;
        tri[52][1] = 6;
        tri[52][2] = 17;

        tri[53][0] = 0;
        tri[53][1] = 3;
        tri[53][2] = 5;

        tri[54][0] = 1;
        tri[54][1] = 4;
        tri[54][2] = 6;

        tri[55][0] = 0;
        tri[55][1] = 2;
        tri[55][2] = 3;

        tri[56][0] = 1;
        tri[56][1] = 2;
        tri[56][2] = 4;

        tri[57][0] = 0;
        tri[57][1] = 1;
        tri[57][2] = 2;

        tri[58][0] = 7;
        tri[58][1] = 8;
        tri[58][2] = 10;

        tri[59][0] = 8;
        tri[59][1] = 9;
        tri[59][2] = 10;
        
        tri[60][0] = 11;
        tri[60][1] = 13;
        tri[60][2] = 14;        
        return tri;
    }
}
