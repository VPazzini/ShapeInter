package gotcg;

import java.awt.*;
import java.awt.geom.*;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple example for transforming one triangulated image into another one.
 * For the animation, the double buffering technique is applied in the same way
 * as in the clock example in the class DoubeBufferingClockExample.
 * 
* @author Frank Klawonn Last change 31.05.2005
 * 
* @see TriangulatedImage
 * @see BufferImageDrawer
 * @see DoubeBufferingClockExample
 */
class ShapeInter extends TimerTask {

    //The window in which the transformation is shown.
    private BufferedImageDrawer buffid;
    //The two images to be transformed into each other will be scaled to 
    //this size.
    private int width;
    private int height;
    //The number of steps (frames) for the transformation.
    private int steps;
    //The first triangulated image.
    private TriangulatedImage t[] = new TriangulatedImage[16];
    //The second triangulated image.
    //private TriangulatedImage t2;
    //This is used for generating/storing the intermediate images.
    private BufferedImage mix1;
    //A variable which is increased stepwise from 0 to 1. It is needed
    //for the computation of the convex combinations.
    private double alpha;
    //The change of alpha in each step: deltAlpha = 1.0/steps
    private double deltaAlpha;
    private ArrayList<Point> points;

    /**
     * Constructor
     *
     * @param bid The window in which the transformation is shown.
     */
    public ShapeInter(BufferedImageDrawer bid) {
        Eight e = new Eight();
        e.calculateOctant(200, 200, 150);
        points = e.getCompleteList();
        
        buffid = bid;

        width = 80;
        height = 80;

        steps = 107;

        deltaAlpha = 1.0 / steps;

        alpha = 0;

        pointsPace = (int) ((int) (points.size() / 16.0) / steps);

        //This object is used for loading the two images.
        Image loadedImage;

        //Generating the first triangulated image:
        t[0] = new TriangulatedImage();

        //Define the size.
        t[0].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Generate the Graphics2D object.
        Graphics2D g2dt0 = t[0].bi.createGraphics();

        //Load the image and draw it on the corresponding BufferedImage.
        loadedImage = new javax.swing.ImageIcon("1st.jpg").getImage();
        g2dt0.drawImage(loadedImage, 0, 0, null);

        //Definition of the points for the triangulation.
        t[0].tPoints = new Point2D[8];


        /*t1.tPoints[0] = new Point2D.Double(0, 0);
         t1.tPoints[1] = new Point2D.Double(167, 0);
         t1.tPoints[2] = new Point2D.Double(167, 90);
         t1.tPoints[3] = new Point2D.Double(167, 297);
         t1.tPoints[4] = new Point2D.Double(0, 297);
         t1.tPoints[5] = new Point2D.Double(0, 90);
         t1.tPoints[6] = new Point2D.Double(111, 90);
         t1.tPoints[7] = new Point2D.Double(111, 123);*/

        t[0].tPoints[0] = new Point2D.Double(0, 0);
        t[0].tPoints[1] = new Point2D.Double(80, 0);
        t[0].tPoints[2] = new Point2D.Double(80, 36);
        t[0].tPoints[3] = new Point2D.Double(80, 80);
        t[0].tPoints[4] = new Point2D.Double(0, 80);
        t[0].tPoints[5] = new Point2D.Double(0, 36);
        t[0].tPoints[6] = new Point2D.Double(31, 36);
        t[0].tPoints[7] = new Point2D.Double(30, 52);


        //Definition of the triangles.
        t[0].triangles = new int[8][3];

        t[0].triangles[0][0] = 0;
        t[0].triangles[0][1] = 5;
        t[0].triangles[0][2] = 6;

        t[0].triangles[1][0] = 0;
        t[0].triangles[1][1] = 1;
        t[0].triangles[1][2] = 6;

        t[0].triangles[2][0] = 1;
        t[0].triangles[2][1] = 2;
        t[0].triangles[2][2] = 6;

        t[0].triangles[3][0] = 2;
        t[0].triangles[3][1] = 6;
        t[0].triangles[3][2] = 7;

        t[0].triangles[4][0] = 2;
        t[0].triangles[4][1] = 3;
        t[0].triangles[4][2] = 7;

        t[0].triangles[5][0] = 3;
        t[0].triangles[5][1] = 4;
        t[0].triangles[5][2] = 7;

        t[0].triangles[6][0] = 4;
        t[0].triangles[6][1] = 5;
        t[0].triangles[6][2] = 7;

        t[0].triangles[7][0] = 5;
        t[0].triangles[7][1] = 6;
        t[0].triangles[7][2] = 7;



        //The same for the forth image.
        t[1] = new TriangulatedImage();

        t[1].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt1 = t[1].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("2nd.jpg").getImage();

        g2dt1.drawImage(loadedImage, 0, 0, null);

        t[1].tPoints = new Point2D[8];
        t[1].tPoints[0] = new Point2D.Double(0, 0);
        t[1].tPoints[1] = new Point2D.Double(80, 0);
        t[1].tPoints[2] = new Point2D.Double(80, 45);
        t[1].tPoints[3] = new Point2D.Double(80, 80);
        t[1].tPoints[4] = new Point2D.Double(0, 80);
        t[1].tPoints[5] = new Point2D.Double(0, 45);
        t[1].tPoints[6] = new Point2D.Double(38, 45);
        t[1].tPoints[7] = new Point2D.Double(38, 61);
        t[1].triangles = t[0].triangles;
        //--------------------------

        //The same for the fitfh image.
        t[2] = new TriangulatedImage();

        t[2].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt2 = t[2].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("3rd.jpg").getImage();

        g2dt2.drawImage(loadedImage, 0, 0, null);

        t[2].tPoints = new Point2D[8];
        t[2].tPoints[0] = new Point2D.Double(0, 0);
        t[2].tPoints[1] = new Point2D.Double(80, 0);
        t[2].tPoints[2] = new Point2D.Double(80, 41);
        t[2].tPoints[3] = new Point2D.Double(80, 80);
        t[2].tPoints[4] = new Point2D.Double(0, 80);
        t[2].tPoints[5] = new Point2D.Double(0, 41);
        t[2].tPoints[6] = new Point2D.Double(52, 41);
        t[2].tPoints[7] = new Point2D.Double(52, 57);
        t[2].triangles = t[0].triangles;
        //--------------------------

        t[3] = new TriangulatedImage();

        t[3].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt3 = t[3].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("4th.jpg").getImage();

        g2dt3.drawImage(loadedImage, 0, 0, null);

        t[3].tPoints = new Point2D[8];
        t[3].tPoints[0] = new Point2D.Double(0, 0);
        t[3].tPoints[1] = new Point2D.Double(80, 0);
        t[3].tPoints[2] = new Point2D.Double(80, 37);
        t[3].tPoints[3] = new Point2D.Double(80, 80);
        t[3].tPoints[4] = new Point2D.Double(0, 80);
        t[3].tPoints[5] = new Point2D.Double(0, 37);
        t[3].tPoints[6] = new Point2D.Double(38, 37);
        t[3].tPoints[7] = new Point2D.Double(38, 54);
        t[3].triangles = t[0].triangles;
        //--------------------------

        t[4] = new TriangulatedImage();

        t[4].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt4 = t[4].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("5th.jpg").getImage();

        g2dt4.drawImage(loadedImage, 0, 0, null);

        t[4].tPoints = new Point2D[8];
        t[4].tPoints[0] = new Point2D.Double(0, 0);
        t[4].tPoints[1] = new Point2D.Double(80, 0);
        t[4].tPoints[2] = new Point2D.Double(80, 44);
        t[4].tPoints[3] = new Point2D.Double(80, 80);
        t[4].tPoints[4] = new Point2D.Double(0, 80);
        t[4].tPoints[5] = new Point2D.Double(0, 44);
        t[4].tPoints[6] = new Point2D.Double(32, 44);
        t[4].tPoints[7] = new Point2D.Double(32, 48);
        t[4].triangles = t[0].triangles;
        //--------------------------


        t[5] = new TriangulatedImage();

        t[5].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt5 = t[5].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("6th.jpg").getImage();

        g2dt5.drawImage(loadedImage, 0, 0, null);

        t[5].tPoints = new Point2D[8];
        t[5].tPoints[0] = new Point2D.Double(0, 0);
        t[5].tPoints[1] = new Point2D.Double(80, 0);
        t[5].tPoints[2] = new Point2D.Double(80, 47);
        t[5].tPoints[3] = new Point2D.Double(80, 80);
        t[5].tPoints[4] = new Point2D.Double(0, 80);
        t[5].tPoints[5] = new Point2D.Double(0, 47);
        t[5].tPoints[6] = new Point2D.Double(47, 47);
        t[5].tPoints[7] = new Point2D.Double(46, 62);
        t[5].triangles = t[0].triangles;
        //--------------------------

        t[6] = new TriangulatedImage();

        t[6].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt6 = t[6].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("7th.jpg").getImage();

        g2dt6.drawImage(loadedImage, 0, 0, null);

        t[6].tPoints = new Point2D[8];
        t[6].tPoints[0] = new Point2D.Double(0, 0);
        t[6].tPoints[1] = new Point2D.Double(80, 0);
        t[6].tPoints[2] = new Point2D.Double(80, 41);
        t[6].tPoints[3] = new Point2D.Double(80, 80);
        t[6].tPoints[4] = new Point2D.Double(0, 80);
        t[6].tPoints[5] = new Point2D.Double(0, 41);
        t[6].tPoints[6] = new Point2D.Double(24, 41);
        t[6].tPoints[7] = new Point2D.Double(24, 55);
        t[6].triangles = t[0].triangles;
        //--------------------------

        t[7] = new TriangulatedImage();

        t[7].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt7 = t[7].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("8th.jpg").getImage();

        g2dt7.drawImage(loadedImage, 0, 0, null);

        t[7].tPoints = new Point2D[8];
        t[7].tPoints[0] = new Point2D.Double(0, 0);
        t[7].tPoints[1] = new Point2D.Double(80, 0);
        t[7].tPoints[2] = new Point2D.Double(80, 44);
        t[7].tPoints[3] = new Point2D.Double(80, 80);
        t[7].tPoints[4] = new Point2D.Double(0, 80);
        t[7].tPoints[5] = new Point2D.Double(0, 44);
        t[7].tPoints[6] = new Point2D.Double(41, 44);
        t[7].tPoints[7] = new Point2D.Double(41, 64);
        t[7].triangles = t[0].triangles;
        //--------------------------

        t[8] = new TriangulatedImage();

        t[8].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt8 = t[8].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("9th.jpg").getImage();

        g2dt8.drawImage(loadedImage, 0, 0, null);

        t[8].tPoints = new Point2D[8];
        t[8].tPoints[0] = new Point2D.Double(0, 0);
        t[8].tPoints[1] = new Point2D.Double(80, 0);
        t[8].tPoints[2] = new Point2D.Double(80, 45);
        t[8].tPoints[3] = new Point2D.Double(80, 80);
        t[8].tPoints[4] = new Point2D.Double(0, 80);
        t[8].tPoints[5] = new Point2D.Double(0, 45);
        t[8].tPoints[6] = new Point2D.Double(56, 45);
        t[8].tPoints[7] = new Point2D.Double(56, 62);
        t[8].triangles = t[0].triangles;
        //--------------------------

        t[9] = new TriangulatedImage();

        t[9].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt9 = t[9].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("10th.jpg").getImage();

        g2dt9.drawImage(loadedImage, 0, 0, null);

        t[9].tPoints = new Point2D[8];
        t[9].tPoints[0] = new Point2D.Double(0, 0);
        t[9].tPoints[1] = new Point2D.Double(80, 0);
        t[9].tPoints[2] = new Point2D.Double(80, 48);
        t[9].tPoints[3] = new Point2D.Double(80, 80);
        t[9].tPoints[4] = new Point2D.Double(0, 80);
        t[9].tPoints[5] = new Point2D.Double(0, 48);
        t[9].tPoints[6] = new Point2D.Double(35, 49);
        t[9].tPoints[7] = new Point2D.Double(36, 65);
        t[9].triangles = t[0].triangles;
        //--------------------------

        t[10] = new TriangulatedImage();

        t[10].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt10 = t[10].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("11th.jpg").getImage();

        g2dt10.drawImage(loadedImage, 0, 0, null);

        t[10].tPoints = new Point2D[8];
        t[10].tPoints[0] = new Point2D.Double(0, 0);
        t[10].tPoints[1] = new Point2D.Double(80, 0);
        t[10].tPoints[2] = new Point2D.Double(80, 38);
        t[10].tPoints[3] = new Point2D.Double(80, 80);
        t[10].tPoints[4] = new Point2D.Double(0, 80);
        t[10].tPoints[5] = new Point2D.Double(0, 38);
        t[10].tPoints[6] = new Point2D.Double(25, 38);
        t[10].tPoints[7] = new Point2D.Double(25, 52);
        t[10].triangles = t[0].triangles;
        //--------------------------

        t[11] = new TriangulatedImage();

        t[11].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt11 = t[11].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("amelia.jpg").getImage();

        g2dt11.drawImage(loadedImage, 0, 0, null);

        t[11].tPoints = new Point2D[8];
        t[11].tPoints[0] = new Point2D.Double(0, 0);
        t[11].tPoints[1] = new Point2D.Double(80, 0);
        t[11].tPoints[2] = new Point2D.Double(80, 41);
        t[11].tPoints[3] = new Point2D.Double(80, 80);
        t[11].tPoints[4] = new Point2D.Double(0, 80);
        t[11].tPoints[5] = new Point2D.Double(0, 41);
        t[11].tPoints[6] = new Point2D.Double(30, 41);
        t[11].tPoints[7] = new Point2D.Double(30, 56);
        t[11].triangles = t[0].triangles;
        //--------------------------

        t[12] = new TriangulatedImage();

        t[12].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt12 = t[12].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("clara.jpg").getImage();

        g2dt12.drawImage(loadedImage, 0, 0, null);

        t[12].tPoints = new Point2D[8];
        t[12].tPoints[0] = new Point2D.Double(0, 0);
        t[12].tPoints[1] = new Point2D.Double(80, 0);
        t[12].tPoints[2] = new Point2D.Double(80, 42);
        t[12].tPoints[3] = new Point2D.Double(80, 80);
        t[12].tPoints[4] = new Point2D.Double(0, 80);
        t[12].tPoints[5] = new Point2D.Double(0, 42);
        t[12].tPoints[6] = new Point2D.Double(29, 42);
        t[12].tPoints[7] = new Point2D.Double(29, 52);
        t[12].triangles = t[0].triangles;
        //--------------------------

        t[13] = new TriangulatedImage();

        t[13].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt13 = t[13].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("river.jpg").getImage();

        g2dt13.drawImage(loadedImage, 0, 0, null);

        t[13].tPoints = new Point2D[8];
        t[13].tPoints[0] = new Point2D.Double(0, 0);
        t[13].tPoints[1] = new Point2D.Double(80, 0);
        t[13].tPoints[2] = new Point2D.Double(80, 38);
        t[13].tPoints[3] = new Point2D.Double(80, 80);
        t[13].tPoints[4] = new Point2D.Double(0, 80);
        t[13].tPoints[5] = new Point2D.Double(0, 38);
        t[13].tPoints[6] = new Point2D.Double(42, 38);
        t[13].tPoints[7] = new Point2D.Double(42, 52);
        t[13].triangles = t[0].triangles;
        //--------------------------

        t[14] = new TriangulatedImage();

        t[14].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt14 = t[14].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("rory.jpg").getImage();

        g2dt14.drawImage(loadedImage, 0, 0, null);

        t[14].tPoints = new Point2D[8];
        t[14].tPoints[0] = new Point2D.Double(0, 0);
        t[14].tPoints[1] = new Point2D.Double(80, 0);
        t[14].tPoints[2] = new Point2D.Double(80, 42);
        t[14].tPoints[3] = new Point2D.Double(80, 80);
        t[14].tPoints[4] = new Point2D.Double(0, 80);
        t[14].tPoints[5] = new Point2D.Double(0, 42);
        t[14].tPoints[6] = new Point2D.Double(28, 42);
        t[14].tPoints[7] = new Point2D.Double(28, 57);
        t[14].triangles = t[0].triangles;
        //--------------------------

        t[15] = new TriangulatedImage();

        t[15].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt15 = t[15].bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("roryfather.jpg").getImage();

        g2dt15.drawImage(loadedImage, 0, 0, null);

        t[15].tPoints = new Point2D[8];
        t[15].tPoints[0] = new Point2D.Double(0, 0);
        t[15].tPoints[1] = new Point2D.Double(80, 0);
        t[15].tPoints[2] = new Point2D.Double(80, 42);
        t[15].tPoints[3] = new Point2D.Double(80, 80);
        t[15].tPoints[4] = new Point2D.Double(0, 80);
        t[15].tPoints[5] = new Point2D.Double(0, 42);
        t[15].tPoints[6] = new Point2D.Double(36, 42);
        t[15].tPoints[7] = new Point2D.Double(36, 56);
        t[15].triangles = t[0].triangles;
        //--------------------------

    }
    //This method is called in regular intervals. This method computes
    //the updated image/frame and calls the repaint method to draw the
    //updated image on the window.
    int i = 0;
    int p = 0;
    int x = 0;
    int pointsPace;

    @Override
    public void run() {
        AffineTransform transImg1;
        AffineTransform transImg2;
        AffineTransform intermediateTransform;
        double[] initialMatrix = new double[6];
        double[] finalMatrix = new double[6];
        boolean flag = false;
        
        while (true) {
            //Since this method is called arbitrarily often, interpolation must only
            //be carred out while alpha is between 0 and 1.
            if (alpha >= 0 && alpha <= 1 - deltaAlpha) {
                //Generate the interpolated image.
                
                if (flag) {
                    mix1 = t[15].mixWith(t[0], alpha);
                } else {
                    mix1 = t[i].mixWith(t[i + 1], alpha);
                }

                //Draw the interpolated image on the BufferedImage.
                buffid.g2dbi.setColor(Color.BLACK);
                //buffid.g2dbi.fill(new Rectangle(0, 0, 500, 800));

                transImg1 = new AffineTransform();
                transImg2 = new AffineTransform();
                if(alpha == 0){
                    p = i*107;
                }
                transImg1.translate(points.get(p).x, points.get(p).y);
                if (alpha + 2*deltaAlpha > 1){
                    p = (i+1)*107-pointsPace;
                }
                transImg2.translate(points.get(p + pointsPace).x, points.get(p + pointsPace).y);

                transImg1.getMatrix(initialMatrix);
                transImg2.getMatrix(finalMatrix);

                //System.out.println(pointsPace + "\n");

                int pace = 30;
                for (int k = 0; k < pace; k++) {
                    //System.out.println(k + "\n");

                    //buffid.g2dbi.fillRect(0, 0, width, height);
                    //buffid.repaint();
                    
                    intermediateTransform = new AffineTransform(convexCombination(initialMatrix, finalMatrix, k / (double) pace));
                    buffid.g2dbi.drawImage(mix1, intermediateTransform, null);
                    //buffid.g2dbi.drawImage(mix, 50, 50, null);

                    //Call the method for updating the window.
                    buffid.repaint();
                    
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ShapeInter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Increment alpha.
                alpha += deltaAlpha;
                p += pointsPace;
            } else {
                if (i < 15) {
                    i++;
                    alpha = 0;
                }
                if (i == 15) {
                    //i++;
                    alpha = 0;
                    flag = true;
                }
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

        //Width of the window.
        int width = 500;
        //Height of the window.
        int height = 800;

        //Specifies (in milliseconds) when the frame should be updated.
        int delay = 1;

        //The BufferedImage to be drawn in the window.
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        //The window in which everything is drawn.
        BufferedImageDrawer bid = new BufferedImageDrawer(bi, width, height);
        bid.setTitle("Whovian Shaker");

        //The TimerTask in which the repeated computations for drawing take place.
        ShapeInter mcs = new ShapeInter(bid);

        mcs.run();

        //Timer t = new Timer();
        //t.scheduleAtFixedRate(mcs, 0, delay);

    }
}
