package gotcg;

import java.awt.*;
import java.awt.geom.*;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;

/**
 * A simple example for transforming one triangulated image into another one.
 * For the animation, the doube buffering technique is applied in the same way
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
    private TriangulatedImage t1;
    //The second triangulated image.
    private TriangulatedImage t2;
    //This is used for generating/storing the intermediate images.
    private BufferedImage mix;
    //A variable which is increased stepwise from 0 to 1. It is needed
    //for the computation of the convex combinations.
    private double alpha;
    //The change of alpha in each step: deltAlpha = 1.0/steps
    private double deltaAlpha;

    /**
     * Constructor
     *
     * @param bid The window in which the transformation is shown.
     */
    ShapeInter(BufferedImageDrawer bid) {
        buffid = bid;

        width = 170;
        height = 300;

        steps = 100;

        deltaAlpha = 1.0 / steps;

        alpha = 0;

        //This object is used for loading the two images.
        Image loadedImage;

        //Generating the first triangulated image:
        t1 = new TriangulatedImage();

        //Define the size.
        t1.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Generate the Graphics2D object.
        Graphics2D g2dt1 = t1.bi.createGraphics();

        //Load the image and draw it on the corresponding BufferedImage.
        loadedImage = new javax.swing.ImageIcon("9th_doctor.jpg").getImage();
        g2dt1.drawImage(loadedImage, 0, 0, null);

        //Definition of the points for the triangulation.
        t1.tPoints = new Point2D[8];


        t1.tPoints[0] = new Point2D.Double(0, 0);
        t1.tPoints[1] = new Point2D.Double(167, 0);
        t1.tPoints[2] = new Point2D.Double(167, 90);
        t1.tPoints[3] = new Point2D.Double(167, 297);
        t1.tPoints[4] = new Point2D.Double(0, 297);
        t1.tPoints[5] = new Point2D.Double(0, 90);
        t1.tPoints[6] = new Point2D.Double(111, 90);
        t1.tPoints[7] = new Point2D.Double(111, 123);


        //Definition of the triangles.
        t1.triangles = new int[8][3];

        t1.triangles[0][0] = 0;
        t1.triangles[0][1] = 5;
        t1.triangles[0][2] = 6;

        t1.triangles[1][0] = 0;
        t1.triangles[1][1] = 1;
        t1.triangles[1][2] = 6;

        t1.triangles[2][0] = 1;
        t1.triangles[2][1] = 2;
        t1.triangles[2][2] = 6;

        t1.triangles[3][0] = 2;
        t1.triangles[3][1] = 6;
        t1.triangles[3][2] = 7;

        t1.triangles[4][0] = 2;
        t1.triangles[4][1] = 3;
        t1.triangles[4][2] = 7;

        t1.triangles[5][0] = 3;
        t1.triangles[5][1] = 4;
        t1.triangles[5][2] = 7;

        t1.triangles[6][0] = 4;
        t1.triangles[6][1] = 5;
        t1.triangles[6][2] = 7;

        t1.triangles[7][0] = 5;
        t1.triangles[7][1] = 6;
        t1.triangles[7][2] = 7;

        //The same for the second image.
        t2 = new TriangulatedImage();

        t2.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt2 = t2.bi.createGraphics();

        loadedImage = new javax.swing.ImageIcon("10th_doctor.jpg").getImage();

        g2dt2.drawImage(loadedImage, 0, 0, null);

        t2.tPoints = new Point2D[8];
        t2.tPoints[0] = new Point2D.Double(0, 0);
        t2.tPoints[1] = new Point2D.Double(167, 0);
        t2.tPoints[2] = new Point2D.Double(167, 112);
        t2.tPoints[3] = new Point2D.Double(167, 297);
        t2.tPoints[4] = new Point2D.Double(0, 297);
        t2.tPoints[5] = new Point2D.Double(0, 112);
        t2.tPoints[6] = new Point2D.Double(71, 112);
        t2.tPoints[7] = new Point2D.Double(71, 144);


        //The indexing for the triangles must be the same as in the
        //the first image.
        t2.triangles = t1.triangles;

    }

    //This method is called in regular intervals. This method computes
    //the updated image/frame and calls the repaint method to draw the
    //updated image on the window.
    int i = 0;
    public void run() {

        //Since this method is called arbitrarily often, interpolation must only
        //be carred out while alpha is between 0 and 1.
        if (alpha >= 0 && alpha <= 1) {
            //Generate the interpolated image.
            mix = t1.mixWith(t2, alpha);

            //Draw the interpolated image on the BufferedImage.
            buffid.g2dbi.drawImage(mix, 50, 50, null);

            //Call the method for updating the window.
            buffid.repaint();
        }

        //Increment alpha.
        alpha = alpha + deltaAlpha;

    }

    public static void main(String args[]) {

        //Width of the window.
        int width = 400;
        //Height of the window.
        int height = 500;

        //Specifies (in milliseconds) when the frame should be updated.
        int delay = 100;

        //The BufferedImage to be drawn in the window.
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);


        //The window in which everything is drawn.
        BufferedImageDrawer bid = new BufferedImageDrawer(bi, width, height);
        bid.setTitle("Transforming shape and colour");

        //The TimerTask in which the repeated computations for drawing take place.
        ShapeInter mcs = new ShapeInter(bid);


        Timer t = new Timer();
        t.scheduleAtFixedRate(mcs, 0, delay);

    }
}
