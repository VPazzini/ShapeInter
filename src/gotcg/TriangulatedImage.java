package gotcg;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class TriangulatedImage {

    BufferedImage bi;
    Point2D[] tPoints;

    static int[][] triangles;
    
    public TriangulatedImage(int width, int height, String file, int i){
        this.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dt0 = this.bi.createGraphics();
        g2dt0.drawImage(new javax.swing.ImageIcon(file).getImage(), 0, 0, null);
        this.tPoints = this.imagePoints(i);
    }
    
    public TriangulatedImage(){}

    public static double[] triangleCoordinates(Point2D v, Point2D[] triangle) {
        double[] result = new double[3];

        double d13x = triangle[0].getX() - triangle[2].getX();
        double d23x = triangle[1].getX() - triangle[2].getX();
        double dx3 = v.getX() - triangle[2].getX();
        double d13y = triangle[0].getY() - triangle[2].getY();
        double d23y = triangle[1].getY() - triangle[2].getY();
        double dy3 = v.getY() - triangle[2].getY();

        double delta = d13x * d23y - d23x * d13y;

        if (Math.abs(delta) < 0.00000001) {
            result[0] = 10;
        } else {
            result[0] = (dx3 * d23y - d23x * dy3) / delta;
            result[1] = (d13x * dy3 - dx3 * d13y) / delta;
        }


        result[2] = 1 - result[0] - result[1];

        return (result);
    }

    public static boolean isConvexCombination(double[] t) {
        boolean result;
        double sum;

        double tolerance = 0.000001;

        result = true;
        sum = 0;
        for (int i = 0; i < t.length; i++) {
            if (t[i] < 0 || t[i] > 1) {
                result = false;
            }
            sum = sum + t[i];
        }

        if (Math.abs(sum - 1) > tolerance) {
            result = false;
        }

        return (result);
    }

    public BufferedImage mixWith(TriangulatedImage ti, double alpha) {
        TriangulatedImage mix = new TriangulatedImage();

        mix.bi = new BufferedImage(this.bi.getWidth(), this.bi.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        mix.tPoints = new Point2D[this.tPoints.length];
        for (int i = 0; i < mix.tPoints.length; i++) {
            mix.tPoints[i] = new Point2D.Double((1 - alpha) * this.tPoints[i].getX()
                    + alpha * ti.tPoints[i].getX(),
                    (1 - alpha) * this.tPoints[i].getY()
                    + alpha * ti.tPoints[i].getY());
        }

        int rgbValueThis;
        int rgbValueTi;
        Color thisColour;
        Color tiColour;
        Color pixelColour;
        int rMix;
        int gMix;
        int bMix;
        int xInt;
        int yInt;
        double[] t = new double[3];
        double aux;
        Point2D[] threePoints = new Point2D[3];
        Point2D.Double pixel = new Point2D.Double();
        int tNo;
        boolean notFound;

        for (int i = 0; i < mix.bi.getWidth(); i++) {
            for (int j = 0; j < mix.bi.getHeight(); j++) {
                pixel.setLocation(i, j);

                tNo = 0;

                notFound = true;
                while (tNo < TriangulatedImage.triangles.length && notFound) {

                    threePoints[0] = mix.tPoints[TriangulatedImage.triangles[tNo][0]];
                    threePoints[1] = mix.tPoints[TriangulatedImage.triangles[tNo][1]];
                    threePoints[2] = mix.tPoints[TriangulatedImage.triangles[tNo][2]];

                    t = triangleCoordinates(pixel, threePoints);

                    if (isConvexCombination(t)) {
                        notFound = false;
                    } else {
                        tNo++;
                    }
                }

                if (!notFound) {
                    aux = t[0] * this.tPoints[TriangulatedImage.triangles[tNo][0]].getX();
                    aux = aux + t[1] * this.tPoints[TriangulatedImage.triangles[tNo][1]].getX();
                    aux = aux + t[2] * this.tPoints[TriangulatedImage.triangles[tNo][2]].getX();
                    
                    xInt = (int) Math.round(aux);
                    xInt = Math.max(0, xInt);
                    xInt = Math.min(this.bi.getWidth() - 1, xInt);
                    
                    aux = t[0] * this.tPoints[TriangulatedImage.triangles[tNo][0]].getY();
                    aux = aux + t[1] * this.tPoints[TriangulatedImage.triangles[tNo][1]].getY();
                    aux = aux + t[2] * this.tPoints[TriangulatedImage.triangles[tNo][2]].getY();
                    
                    yInt = (int) Math.round(aux);
                    yInt = Math.max(0, yInt);
                    yInt = Math.min(this.bi.getHeight() - 1, yInt);

                    rgbValueThis = this.bi.getRGB(xInt, yInt);
                    thisColour = new Color(rgbValueThis);

                    aux = t[0] * ti.tPoints[TriangulatedImage.triangles[tNo][0]].getX();
                    aux = aux + t[1] * ti.tPoints[TriangulatedImage.triangles[tNo][1]].getX();
                    aux = aux + t[2] * ti.tPoints[TriangulatedImage.triangles[tNo][2]].getX();
                    xInt = (int) Math.round(aux);

                    aux = t[0] * ti.tPoints[TriangulatedImage.triangles[tNo][0]].getY();
                    aux = aux + t[1] * ti.tPoints[TriangulatedImage.triangles[tNo][1]].getY();
                    aux = aux + t[2] * ti.tPoints[TriangulatedImage.triangles[tNo][2]].getY();
                    yInt = (int) Math.round(aux);

                    rgbValueTi = ti.bi.getRGB(xInt, yInt);
                    tiColour = new Color(rgbValueTi);
                    
                    rMix = (int) Math.round((1 - alpha) * thisColour.getRed()
                            + alpha * tiColour.getRed());

                    gMix = (int) Math.round((1 - alpha) * thisColour.getGreen()
                            + alpha * tiColour.getGreen());

                    bMix = (int) Math.round((1 - alpha) * thisColour.getBlue()
                            + alpha * tiColour.getBlue());
                    
                    pixelColour = new Color(rMix, gMix, bMix);
                    mix.bi.setRGB(i, j, pixelColour.getRGB());
                }
            }
        }
        return (mix.bi);
    }
    
    private Point2D[] imagePoints(int image) {
        Point2D[] _2dpoints = new Point2D[13];
        //posicoes do array
        _2dpoints[0] = new Point2D.Double(0, 0);//[0]canto superior esquerdo
        _2dpoints[1] = new Point2D.Double(75, 0);//[1]meio superior
        _2dpoints[2] = new Point2D.Double(150, 0);//[2]canto supedior direito
        _2dpoints[3] = new Point2D.Double(0, 100);//[3]meio esquerdo
        //[4]olho esquerdo
        //[5]olho direito
        _2dpoints[6] = new Point2D.Double(150, 100);//[6]meio direito
        //[7]nariz
        //[8]boca canto esquerdo
        //[9]boca canto direito
        _2dpoints[10] = new Point2D.Double(0, 200);//[10]canto inferior esquerdo
        _2dpoints[11] = new Point2D.Double(75, 200);//[11]meio inferior
        _2dpoints[12] = new Point2D.Double(150, 200);//[12]canto inferior direito        
        
        switch (image) {
            case 0:
                //1st doctor
                _2dpoints[4] = new Point2D.Double(46, 97);
                _2dpoints[5] = new Point2D.Double(96, 96);
                _2dpoints[7] = new Point2D.Double(66, 140);
                _2dpoints[8] = new Point2D.Double(53, 154);
                _2dpoints[9] = new Point2D.Double(89, 154);
                break;
            case 1:
                //2nd doctor
                _2dpoints[4] = new Point2D.Double(58, 89);
                _2dpoints[5] = new Point2D.Double(98, 92);
                _2dpoints[7] = new Point2D.Double(75, 124);
                _2dpoints[8] = new Point2D.Double(55, 148);
                _2dpoints[9] = new Point2D.Double(97, 143);
                break;
            case 2:
                //3rd doctor
                _2dpoints[4] = new Point2D.Double(46, 84);
                _2dpoints[5] = new Point2D.Double(81, 83);
                _2dpoints[7] = new Point2D.Double(55, 111);
                _2dpoints[8] = new Point2D.Double(45, 128);
                _2dpoints[9] = new Point2D.Double(81, 128);
                break;
            case 3:
                //4th doctor
                _2dpoints[4] = new Point2D.Double(56, 90);
                _2dpoints[5] = new Point2D.Double(101, 88);
                _2dpoints[7] = new Point2D.Double(83, 113);
                _2dpoints[8] = new Point2D.Double(58, 136);
                _2dpoints[9] = new Point2D.Double(102, 133);
                break;
            case 4:
                //5th doctor
                _2dpoints[4] = new Point2D.Double(49, 87);
                _2dpoints[5] = new Point2D.Double(89, 84);
                _2dpoints[7] = new Point2D.Double(66, 106);
                _2dpoints[8] = new Point2D.Double(56, 132);
                _2dpoints[9] = new Point2D.Double(90, 127);
                break;
            case 5:
                //6th doctor
                _2dpoints[4] = new Point2D.Double(51, 96);
                _2dpoints[5] = new Point2D.Double(91, 92);
                _2dpoints[7] = new Point2D.Double(77, 121);
                _2dpoints[8] = new Point2D.Double(64, 143);
                _2dpoints[9] = new Point2D.Double(95, 139);
                break;
            case 6:
                //7th doctor
                _2dpoints[4] = new Point2D.Double(58, 80);
                _2dpoints[5] = new Point2D.Double(99, 82);
                _2dpoints[7] = new Point2D.Double(88, 102);
                _2dpoints[8] = new Point2D.Double(59, 127);
                _2dpoints[9] = new Point2D.Double(96, 128);
                break;
            case 7:
                //8th doctor
                _2dpoints[4] = new Point2D.Double(54, 83);
                _2dpoints[5] = new Point2D.Double(98, 81);
                _2dpoints[7] = new Point2D.Double(78, 114);
                _2dpoints[8] = new Point2D.Double(62, 135);
                _2dpoints[9] = new Point2D.Double(99, 133);
                break;
            case 8:
                //9th doctor
                _2dpoints[4] = new Point2D.Double(68, 91);
                _2dpoints[5] = new Point2D.Double(112, 90);
                _2dpoints[7] = new Point2D.Double(104, 124);
                _2dpoints[8] = new Point2D.Double(77, 138);
                _2dpoints[9] = new Point2D.Double(111, 133);
                break;
            case 9:
                //10th doctor
                _2dpoints[4] = new Point2D.Double(56, 102);
                _2dpoints[5] = new Point2D.Double(103, 106);
                _2dpoints[7] = new Point2D.Double(75, 140);
                _2dpoints[8] = new Point2D.Double(60, 152);
                _2dpoints[9] = new Point2D.Double(90, 156);
                break;
            case 10:
                //11th doctor
                _2dpoints[4] = new Point2D.Double(46, 94);
                _2dpoints[5] = new Point2D.Double(90, 98);
                _2dpoints[7] = new Point2D.Double(68, 134);
                _2dpoints[8] = new Point2D.Double(50, 154);
                _2dpoints[9] = new Point2D.Double(88, 149);
                break;
            case 11:
                //amy pond
                _2dpoints[4] = new Point2D.Double(48, 72);
                _2dpoints[5] = new Point2D.Double(100, 72);
                _2dpoints[7] = new Point2D.Double(74, 107);
                _2dpoints[8] = new Point2D.Double(58, 132);
                _2dpoints[9] = new Point2D.Double(95, 132);
                break;
            case 12:
                //clara oswald
                _2dpoints[4] = new Point2D.Double(52, 88);
                _2dpoints[5] = new Point2D.Double(99, 86);
                _2dpoints[7] = new Point2D.Double(80, 112);
                _2dpoints[8] = new Point2D.Double(64, 138);
                _2dpoints[9] = new Point2D.Double(96, 134);
                break;
            case 13:
                //river song
                _2dpoints[4] = new Point2D.Double(52, 82);
                _2dpoints[5] = new Point2D.Double(103, 78);
                _2dpoints[7] = new Point2D.Double(86, 109);
                _2dpoints[8] = new Point2D.Double(61, 130);
                _2dpoints[9] = new Point2D.Double(102, 127);
                break;
            case 14:
                //rory pond
                _2dpoints[4] = new Point2D.Double(39, 84);
                _2dpoints[5] = new Point2D.Double(80, 80);
                _2dpoints[7] = new Point2D.Double(57, 110);
                _2dpoints[8] = new Point2D.Double(50, 131);
                _2dpoints[9] = new Point2D.Double(76, 129);
                break;
            case 15:
                //rory's dad
                _2dpoints[4] = new Point2D.Double(57, 87);
                _2dpoints[5] = new Point2D.Double(104, 83);
                _2dpoints[7] = new Point2D.Double(75, 115);
                _2dpoints[8] = new Point2D.Double(62, 137);
                _2dpoints[9] = new Point2D.Double(96, 138);
                break;
        }
        return _2dpoints;
    }
}
