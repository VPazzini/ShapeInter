package gotcg;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
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
    private BufferedImage mix1;
    private double alpha;
    private double deltaAlpha;
    private ArrayList<Point> points;
    private int i;
    private int p;
    private int x;
    private int pointsPace;
    
    public ShapeInter(BufferedImageDrawer bid) {
        Eight e = new Eight();
        e.calculateOctant(200, 150, 150);
        points = e.getCompleteList();
        
        buffid = bid;

        width = 150;
        height = 200;

        steps = 107;

        deltaAlpha = 1.0 / steps;

        alpha = 0;

        pointsPace = (int) ((int) (points.size() / 16.0) / steps);
        
        TriangulatedImage.triangles = this.trianglePoints();

        Image loadedImage;

        t[0] = new TriangulatedImage();
        t[0].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt0 = t[0].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("1st.png").getImage();
        g2dt0.drawImage(loadedImage, 0, 0, null);
        t[0].tPoints = this.imagePoints(0);
       
        t[1] = new TriangulatedImage();
        t[1].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt1 = t[1].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("2nd.png").getImage();
        g2dt1.drawImage(loadedImage, 0, 0, null);
        t[1].tPoints = this.imagePoints(1);
        //--------------------------
        
        t[2] = new TriangulatedImage();
        t[2].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt2 = t[2].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("3rd.png").getImage();
        g2dt2.drawImage(loadedImage, 0, 0, null);
        t[2].tPoints = this.imagePoints(2);
        //--------------------------

        t[3] = new TriangulatedImage();
        t[3].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt3 = t[3].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("4th.png").getImage();
        g2dt3.drawImage(loadedImage, 0, 0, null);
        t[3].tPoints = this.imagePoints(3);
        //--------------------------

        t[4] = new TriangulatedImage();
        t[4].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt4 = t[4].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("5th.png").getImage();
        g2dt4.drawImage(loadedImage, 0, 0, null);
        t[4].tPoints = this.imagePoints(4);
        //--------------------------

        t[5] = new TriangulatedImage();
        t[5].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt5 = t[5].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("6th.png").getImage();
        g2dt5.drawImage(loadedImage, 0, 0, null);
        t[5].tPoints = this.imagePoints(5);
        //--------------------------

        t[6] = new TriangulatedImage();
        t[6].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt6 = t[6].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("7th.png").getImage();
        g2dt6.drawImage(loadedImage, 0, 0, null);
        t[6].tPoints = this.imagePoints(6);
        //--------------------------

        t[7] = new TriangulatedImage();
        t[7].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt7 = t[7].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("8th.png").getImage();
        g2dt7.drawImage(loadedImage, 0, 0, null);
        t[7].tPoints = this.imagePoints(7);
        //--------------------------

        t[8] = new TriangulatedImage();
        t[8].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt8 = t[8].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("9th.png").getImage();
        g2dt8.drawImage(loadedImage, 0, 0, null);
        t[8].tPoints = this.imagePoints(8);
        //--------------------------

        t[9] = new TriangulatedImage();
        t[9].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt9 = t[9].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("10th.png").getImage();
        g2dt9.drawImage(loadedImage, 0, 0, null);
        t[9].tPoints = this.imagePoints(9);
        //--------------------------

        t[10] = new TriangulatedImage();
        t[10].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt10 = t[10].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("11th.png").getImage();
        g2dt10.drawImage(loadedImage, 0, 0, null);
        t[10].tPoints = this.imagePoints(10);
        //--------------------------

        t[11] = new TriangulatedImage();
        t[11].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt11 = t[11].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("amy.png").getImage();
        g2dt11.drawImage(loadedImage, 0, 0, null);
        t[11].tPoints = this.imagePoints(11);
        //--------------------------

        t[12] = new TriangulatedImage();
        t[12].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt12 = t[12].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("clara.png").getImage();
        g2dt12.drawImage(loadedImage, 0, 0, null);
        t[12].tPoints = this.imagePoints(12);
        //--------------------------

        t[13] = new TriangulatedImage();
        t[13].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt13 = t[13].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("river.png").getImage();
        g2dt13.drawImage(loadedImage, 0, 0, null);
        t[13].tPoints = this.imagePoints(13);
        //--------------------------

        t[14] = new TriangulatedImage();
        t[14].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt14 = t[14].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("rory.png").getImage();
        g2dt14.drawImage(loadedImage, 0, 0, null);
        t[14].tPoints = this.imagePoints(14);
        //--------------------------

        t[15] = new TriangulatedImage();
        t[15].bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt15 = t[15].bi.createGraphics();
        loadedImage = new javax.swing.ImageIcon("rory\'s dad.png").getImage();
        g2dt15.drawImage(loadedImage, 0, 0, null);
        t[15].tPoints = this.imagePoints(15);
    }

    public void run() {
        AffineTransform transImg1;
        AffineTransform transImg2;
        AffineTransform intermediateTransform;
        double[] initialMatrix = new double[6];
        double[] finalMatrix = new double[6];
        boolean flag = false;
        
        while (true) {
            if (alpha >= 0 && alpha <= 1 - deltaAlpha) {                
                if (flag) {
                    mix1 = t[15].mixWith(t[0], alpha);
                } else {
                    mix1 = t[i].mixWith(t[i+1], alpha);
                }

                transImg1 = new AffineTransform();
                transImg2 = new AffineTransform();
                if(alpha == 0){
                    p = i*107;
                }
                
                transImg1.translate(points.get(p).x, points.get(p).y);
                
                if (alpha + 2*deltaAlpha > 1){
                    p = (i+1)*107-pointsPace;
                }
                if(p+pointsPace >= points.size()){
                    break;
                }
                
                transImg2.translate(points.get(p + pointsPace).x, points.get(p + pointsPace).y);
                transImg1.getMatrix(initialMatrix);
                transImg2.getMatrix(finalMatrix);

                int pace = 1;
                for (int k = 0; k < pace; k++) {                    
                    intermediateTransform = new AffineTransform(convexCombination(initialMatrix, finalMatrix, k / (double) pace));
                    buffid.g2dbi.drawImage(mix1, intermediateTransform, null);
                    buffid.repaint();                    
                }
                
                alpha += deltaAlpha;
                p += pointsPace;
            } else {
                if (i < 15) {
                    i++;
                }
                if (i == 15) {
                    flag = true;
                }
                alpha = 0;
            }
        }
    }

    private static double[] convexCombination(double[] a, double[] b, double alpha) {
        double[] result = new double[6];
        double lalpha = 1 - alpha;

        result[0] = lalpha * a[0] + alpha * b[0];
        result[1] = lalpha * a[1] + alpha * b[1];
        result[2] = lalpha * a[2] + alpha * b[2];
        result[3] = lalpha * a[3] + alpha * b[3];
        result[4] = lalpha * a[4] + alpha * b[4];
        result[5] = lalpha * a[5] + alpha * b[5];

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
    
    private Point2D[] imagePoints(int image){
        Point2D[] _2dpoints = new Point2D[13];
        //posicoes do array
        //[0]canto superior esquerdo
        //[1]meio superior
        //[2]canto supedior direito
        //[3]meio esquerdo
        //[4]olho esquerdo
        //[5]olho direito
        //[6]meio direito
        //[7]nariz
        //[8]boca canto esquerdo
        //[9]boca canto direito
        //[10]canto inferior esquerdo
        //[11]meio inferior
        //[12]canto inferior direito
        
        switch(image){
            case 0:
                //1st doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(46, 97);
                _2dpoints[5] = new Point2D.Double(96, 96);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(66, 140);
                _2dpoints[8] = new Point2D.Double(53, 154);
                _2dpoints[9] = new Point2D.Double(89, 154);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 1:
                //2nd doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(58, 89);
                _2dpoints[5] = new Point2D.Double(98, 92);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(75, 124);
                _2dpoints[8] = new Point2D.Double(55, 148);
                _2dpoints[9] = new Point2D.Double(97, 143);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 2:
                //3rd doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(46, 84);
                _2dpoints[5] = new Point2D.Double(81, 83);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(55, 111);
                _2dpoints[8] = new Point2D.Double(45, 128);
                _2dpoints[9] = new Point2D.Double(81, 128);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 3:
                //4th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(56, 90);
                _2dpoints[5] = new Point2D.Double(101, 88);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(83, 113);
                _2dpoints[8] = new Point2D.Double(58, 136);
                _2dpoints[9] = new Point2D.Double(102, 133);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 4:
                //5th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(49, 87);
                _2dpoints[5] = new Point2D.Double(89, 84);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(66, 106);
                _2dpoints[8] = new Point2D.Double(56, 132);
                _2dpoints[9] = new Point2D.Double(90, 127);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 5:
                //6th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(51, 96);
                _2dpoints[5] = new Point2D.Double(91, 92);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(77, 121);
                _2dpoints[8] = new Point2D.Double(64, 143);
                _2dpoints[9] = new Point2D.Double(95, 139);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 6:
                //7th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(58, 80);
                _2dpoints[5] = new Point2D.Double(99, 82);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(88, 102);
                _2dpoints[8] = new Point2D.Double(59, 127);
                _2dpoints[9] = new Point2D.Double(96, 128);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 7:
                //8th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(54, 83);
                _2dpoints[5] = new Point2D.Double(98, 81);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(78, 114);
                _2dpoints[8] = new Point2D.Double(62, 135);
                _2dpoints[9] = new Point2D.Double(99, 133);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 8:
                //9th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(68, 91);
                _2dpoints[5] = new Point2D.Double(112, 90);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(104, 124);
                _2dpoints[8] = new Point2D.Double(77, 138);
                _2dpoints[9] = new Point2D.Double(111, 133);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 9:
                //10th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(56, 102);
                _2dpoints[5] = new Point2D.Double(103, 106);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(75, 140);
                _2dpoints[8] = new Point2D.Double(60, 152);
                _2dpoints[9] = new Point2D.Double(90, 156);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 10:
                //11th doctor
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(46, 94);
                _2dpoints[5] = new Point2D.Double(90, 98);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(68, 134);
                _2dpoints[8] = new Point2D.Double(50, 154);
                _2dpoints[9] = new Point2D.Double(88, 149);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 11:
                //amy pond
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(48, 72);
                _2dpoints[5] = new Point2D.Double(100, 72);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(74, 107);
                _2dpoints[8] = new Point2D.Double(58, 132);
                _2dpoints[9] = new Point2D.Double(95, 132);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 12:
                //clara oswald
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(52, 88);
                _2dpoints[5] = new Point2D.Double(99, 86);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(80, 112);
                _2dpoints[8] = new Point2D.Double(64, 138);
                _2dpoints[9] = new Point2D.Double(96, 134);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 13:
                //river song
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(52, 82);
                _2dpoints[5] = new Point2D.Double(103, 78);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(86, 109);
                _2dpoints[8] = new Point2D.Double(61, 130);
                _2dpoints[9] = new Point2D.Double(102, 127);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;
            case 14:
                //rory pond
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(39, 84);
                _2dpoints[5] = new Point2D.Double(80, 80);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(57, 110);
                _2dpoints[8] = new Point2D.Double(50, 131);
                _2dpoints[9] = new Point2D.Double(76, 129);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;  
            case 15:
                //rory's dad
                _2dpoints[0] = new Point2D.Double(0, 0);
                _2dpoints[1] = new Point2D.Double(75, 0);
                _2dpoints[2] = new Point2D.Double(150, 0);
                _2dpoints[3] = new Point2D.Double(0, 100);
                _2dpoints[4] = new Point2D.Double(57, 87);
                _2dpoints[5] = new Point2D.Double(104, 83);
                _2dpoints[6] = new Point2D.Double(150, 100);
                _2dpoints[7] = new Point2D.Double(75, 115);
                _2dpoints[8] = new Point2D.Double(62, 137);
                _2dpoints[9] = new Point2D.Double(96, 138);
                _2dpoints[10] = new Point2D.Double(0, 200);
                _2dpoints[11] = new Point2D.Double(75, 200);
                _2dpoints[12] = new Point2D.Double(150, 200);
                break;            
        } 
        return _2dpoints;
    }
    
    private int[][] trianglePoints(){   
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
