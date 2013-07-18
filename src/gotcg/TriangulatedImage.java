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

    private static double[] triangleCoordinates(Point2D v, Point2D[] triangle) {
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

    private static boolean isConvexCombination(double[] t) {
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
        Point2D[] _2dpoints = new Point2D[35];
        _2dpoints[0] = new Point2D.Double(5, 5);
        _2dpoints[1] = new Point2D.Double(140, 5);
        _2dpoints[18] = new Point2D.Double(140, 100);
        _2dpoints[28] = new Point2D.Double(75, 190);
        _2dpoints[29] = new Point2D.Double(140, 190);
        _2dpoints[32] = new Point2D.Double(5, 100);
        _2dpoints[34] = new Point2D.Double(5, 190);
                
        switch (image) {
            case 0:
                //1st doctor
                _2dpoints[2] = new Point2D.Double(67, 75);
                _2dpoints[3] = new Point2D.Double(45, 63);
                _2dpoints[4] = new Point2D.Double(99, 62);
                _2dpoints[5] = new Point2D.Double(27, 73);
                _2dpoints[6] = new Point2D.Double(115, 73);
                _2dpoints[7] = new Point2D.Double(36, 98);
                _2dpoints[8] = new Point2D.Double(48, 93);
                _2dpoints[9] = new Point2D.Double(58, 101);
                _2dpoints[10] = new Point2D.Double(46, 103);
                _2dpoints[11] = new Point2D.Double(94, 93);
                _2dpoints[12] = new Point2D.Double(102, 98);
                _2dpoints[13] = new Point2D.Double(93, 103);
                _2dpoints[14] = new Point2D.Double(80, 98);
                _2dpoints[15] = new Point2D.Double(24, 107);
                _2dpoints[16] = new Point2D.Double(67, 117);
                _2dpoints[17] = new Point2D.Double(122, 111);
                _2dpoints[19] = new Point2D.Double(35, 144);
                _2dpoints[20] = new Point2D.Double(56, 132);
                _2dpoints[21] = new Point2D.Double(81, 135);
                _2dpoints[22] = new Point2D.Double(114, 152);
                _2dpoints[23] = new Point2D.Double(44, 168);
                _2dpoints[24] = new Point2D.Double(52, 152);
                _2dpoints[25] = new Point2D.Double(70, 154);
                _2dpoints[26] = new Point2D.Double(90, 152);
                _2dpoints[27] = new Point2D.Double(70, 157);
                _2dpoints[30] = new Point2D.Double(57, 183);
                _2dpoints[31] = new Point2D.Double(90, 183);
                _2dpoints[33] = new Point2D.Double(103, 168);
                break;
            case 1:
                //2nd doctor
                _2dpoints[2] = new Point2D.Double(80, 70);
                _2dpoints[3] = new Point2D.Double(62, 59);
                _2dpoints[4] = new Point2D.Double(99, 67);
                _2dpoints[5] = new Point2D.Double(43, 69);
                _2dpoints[6] = new Point2D.Double(117, 89);
                _2dpoints[7] = new Point2D.Double(50, 88);
                _2dpoints[8] = new Point2D.Double(56, 84);
                _2dpoints[9] = new Point2D.Double(68, 90);
                _2dpoints[10] = new Point2D.Double(58, 94);
                _2dpoints[11] = new Point2D.Double(98, 87);
                _2dpoints[12] = new Point2D.Double(105, 92);
                _2dpoints[13] = new Point2D.Double(97, 97);
                _2dpoints[14] = new Point2D.Double(90, 92);
                _2dpoints[15] = new Point2D.Double(31, 111);
                _2dpoints[16] = new Point2D.Double(76, 110);
                _2dpoints[17] = new Point2D.Double(120, 117);
                _2dpoints[19] = new Point2D.Double(36, 135);
                _2dpoints[20] = new Point2D.Double(64, 122);
                _2dpoints[21] = new Point2D.Double(87, 122);
                _2dpoints[22] = new Point2D.Double(115, 139);
                _2dpoints[23] = new Point2D.Double(49, 166);
                _2dpoints[24] = new Point2D.Double(59, 145);
                _2dpoints[25] = new Point2D.Double(76, 143);
                _2dpoints[26] = new Point2D.Double(95, 142);
                _2dpoints[27] = new Point2D.Double(76, 145);
                _2dpoints[30] = new Point2D.Double(64, 182);
                _2dpoints[31] = new Point2D.Double(80, 182);
                _2dpoints[33] = new Point2D.Double(104, 162);
                break;
             case 2:
                //3rd doctor
                _2dpoints[2] = new Point2D.Double(61, 61);//testa
                _2dpoints[3] = new Point2D.Double(48, 66);//testa esquerda
                _2dpoints[4] = new Point2D.Double(83, 66);//testa direita
                _2dpoints[5] = new Point2D.Double(33, 73);
                _2dpoints[6] = new Point2D.Double(104, 74);
                _2dpoints[7] = new Point2D.Double(38, 84);//olho esquerdo esquerda
                _2dpoints[8] = new Point2D.Double(45, 79);//olho esquerdo cima
                _2dpoints[9] = new Point2D.Double(53, 84);//olho esquerdo direita
                _2dpoints[10] = new Point2D.Double(44, 89);//olho esquerdo baixo
                _2dpoints[11] = new Point2D.Double(82, 80);//olho direito cima
                _2dpoints[12] = new Point2D.Double(89, 84);//olho direito direita
                _2dpoints[13] = new Point2D.Double(82, 88);//olho direito baixo
                _2dpoints[14] = new Point2D.Double(74, 84);//olho direito esquerda
                _2dpoints[15] = new Point2D.Double(29, 100);//orelha esquerda
                _2dpoints[16] = new Point2D.Double(59, 84);//nariz meio
                _2dpoints[17] = new Point2D.Double(113, 118);//orelha direita
                _2dpoints[19] = new Point2D.Double(32, 122);//bochecha esquerda
                _2dpoints[20] = new Point2D.Double(48, 110);//nariz esquerdo
                _2dpoints[21] = new Point2D.Double(73, 110);//nariz direito
                _2dpoints[22] = new Point2D.Double(112, 129);//bochecha direita
                _2dpoints[23] = new Point2D.Double(37, 141);
                _2dpoints[24] = new Point2D.Double(45, 128);//boca esquerda
                _2dpoints[25] = new Point2D.Double(61, 127);//boca cima
                _2dpoints[26] = new Point2D.Double(82, 129);//boca direita
                _2dpoints[27] = new Point2D.Double(61, 131);//boca baixo
                _2dpoints[30] = new Point2D.Double(46, 152);//queixo esquerda
                _2dpoints[31] = new Point2D.Double(78, 153);//queixo direita
                _2dpoints[33] = new Point2D.Double(109, 135);
                break;
            case 3:
                //4th doctor
                _2dpoints[2] = new Point2D.Double(77, 77);//testa
                _2dpoints[3] = new Point2D.Double(57, 76);//testa esquerda
                _2dpoints[4] = new Point2D.Double(96, 70);//testa direita
                _2dpoints[5] = new Point2D.Double(34, 88);
                _2dpoints[6] = new Point2D.Double(109, 76);
                _2dpoints[7] = new Point2D.Double(49, 92);//olho esquerdo esquerda
                _2dpoints[8] = new Point2D.Double(57, 84);//olho esquerdo cima
                _2dpoints[9] = new Point2D.Double(66, 90);//olho esquerdo direita
                _2dpoints[10] = new Point2D.Double(56, 94);//olho esquerdo baixo
                _2dpoints[11] = new Point2D.Double(98, 83);//olho direito cima
                _2dpoints[12] = new Point2D.Double(107, 89);//olho direito direita
                _2dpoints[13] = new Point2D.Double(99, 93);//olho direito baixo
                _2dpoints[14] = new Point2D.Double(89, 90);//olho direito esquerda
                _2dpoints[15] = new Point2D.Double(29, 123);//orelha esquerda
                _2dpoints[16] = new Point2D.Double(82, 98);//nariz meio
                _2dpoints[17] = new Point2D.Double(122, 118);//orelha direita
                _2dpoints[19] = new Point2D.Double(33, 140);//bochecha esquerda
                _2dpoints[20] = new Point2D.Double(70, 112);//nariz esquerdo
                _2dpoints[21] = new Point2D.Double(94, 111);//nariz direito
                _2dpoints[22] = new Point2D.Double(119, 137);//bochecha direita
                _2dpoints[23] = new Point2D.Double(44, 160);
                _2dpoints[24] = new Point2D.Double(57, 133);//boca esquerda
                _2dpoints[25] = new Point2D.Double(82, 127);//boca cima
                _2dpoints[26] = new Point2D.Double(105, 130);//boca direita
                _2dpoints[27] = new Point2D.Double(83, 149);//boca baixo
                _2dpoints[30] = new Point2D.Double(72, 171);//queixo esquerda
                _2dpoints[31] = new Point2D.Double(104, 165);//queixo direita
                _2dpoints[33] = new Point2D.Double(117, 143);
                break;
            case 4:
                //5th doctor
                _2dpoints[2] = new Point2D.Double(67, 53);//testa
                _2dpoints[3] = new Point2D.Double(39, 43);//testa esquerda
                _2dpoints[4] = new Point2D.Double(93, 58);//testa direita
                _2dpoints[5] = new Point2D.Double(27, 73);
                _2dpoints[6] = new Point2D.Double(112, 70);
                _2dpoints[7] = new Point2D.Double(42, 89);//olho esquerdo esquerda
                _2dpoints[8] = new Point2D.Double(51, 83);//olho esquerdo cima
                _2dpoints[9] = new Point2D.Double(57, 86);//olho esquerdo direita
                _2dpoints[10] = new Point2D.Double(50, 90);//olho esquerdo baixo
                _2dpoints[11] = new Point2D.Double(87, 80);//olho direito cima
                _2dpoints[12] = new Point2D.Double(96, 83);//olho direito direita
                _2dpoints[13] = new Point2D.Double(86, 86);//olho direito baixo
                _2dpoints[14] = new Point2D.Double(83, 83);//olho direito esquerda
                _2dpoints[15] = new Point2D.Double(30, 109);//orelha esquerda
                _2dpoints[16] = new Point2D.Double(67, 91);//nariz meio
                _2dpoints[17] = new Point2D.Double(122, 99);//orelha direita
                _2dpoints[19] = new Point2D.Double(37, 130);//bochecha esquerda
                _2dpoints[20] = new Point2D.Double(57, 110);//nariz esquerdo
                _2dpoints[21] = new Point2D.Double(82, 107);//nariz direito
                _2dpoints[22] = new Point2D.Double(120, 128);//bochecha direita
                _2dpoints[23] = new Point2D.Double(54, 148);
                _2dpoints[24] = new Point2D.Double(55, 129);//boca esquerda
                _2dpoints[25] = new Point2D.Double(68, 125);//boca cima
                _2dpoints[26] = new Point2D.Double(91, 125);//boca direita
                _2dpoints[27] = new Point2D.Double(71, 134);//boca baixo
                _2dpoints[30] = new Point2D.Double(64, 157);//queixo esquerda
                _2dpoints[31] = new Point2D.Double(88, 155);//queixo direita
                _2dpoints[33] = new Point2D.Double(109, 143);
                break;
            case 5:
                //6th doctor
                _2dpoints[2] = new Point2D.Double(67, 66);//testa
                _2dpoints[3] = new Point2D.Double(51, 64);//testa esquerda
                _2dpoints[4] = new Point2D.Double(87, 59);//testa direita
                _2dpoints[5] = new Point2D.Double(33, 71);
                _2dpoints[6] = new Point2D.Double(108, 69);
                _2dpoints[7] = new Point2D.Double(43, 97);//olho esquerdo esquerda
                _2dpoints[8] = new Point2D.Double(52, 92);//olho esquerdo cima
                _2dpoints[9] = new Point2D.Double(61, 96);//olho esquerdo direita
                _2dpoints[10] = new Point2D.Double(52, 102);//olho esquerdo baixo
                _2dpoints[11] = new Point2D.Double(90, 87);//olho direito cima
                _2dpoints[12] = new Point2D.Double(101, 91);//olho direito direita
                _2dpoints[13] = new Point2D.Double(91, 98);//olho direito baixo
                _2dpoints[14] = new Point2D.Double(82, 93);//olho direito esquerda
                _2dpoints[15] = new Point2D.Double(30, 116);//orelha esquerda
                _2dpoints[16] = new Point2D.Double(74, 104);//nariz meio
                _2dpoints[17] = new Point2D.Double(121, 107);//orelha direita
                _2dpoints[19] = new Point2D.Double(38, 134);//bochecha esquerda
                _2dpoints[20] = new Point2D.Double(65, 121);//nariz esquerdo
                _2dpoints[21] = new Point2D.Double(89, 119);//nariz direito
                _2dpoints[22] = new Point2D.Double(119, 132);//bochecha direita
                _2dpoints[23] = new Point2D.Double(52, 156);
                _2dpoints[24] = new Point2D.Double(62, 142);//boca esquerda
                _2dpoints[25] = new Point2D.Double(79, 135);//boca cima
                _2dpoints[26] = new Point2D.Double(97, 137);//boca direita
                _2dpoints[27] = new Point2D.Double(73, 143);//boca baixo
                _2dpoints[30] = new Point2D.Double(70, 168);//queixo esquerda
                _2dpoints[31] = new Point2D.Double(90, 167);//queixo direita
                _2dpoints[33] = new Point2D.Double(103, 157);
                break;
            default:
                _2dpoints[2] = new Point2D.Double(72, 42);
                _2dpoints[3] = new Point2D.Double(49, 24);
                _2dpoints[4] = new Point2D.Double(105, 32);
                _2dpoints[5] = new Point2D.Double(27, 53);
                _2dpoints[6] = new Point2D.Double(115, 61);
                _2dpoints[7] = new Point2D.Double(36, 98);
                _2dpoints[8] = new Point2D.Double(48, 93);
                _2dpoints[9] = new Point2D.Double(58, 101);
                _2dpoints[10] = new Point2D.Double(46, 103);
                _2dpoints[11] = new Point2D.Double(94, 93);
                _2dpoints[12] = new Point2D.Double(102, 98);
                _2dpoints[13] = new Point2D.Double(93, 103);
                _2dpoints[14] = new Point2D.Double(80, 98);
                _2dpoints[15] = new Point2D.Double(24, 107);
                _2dpoints[16] = new Point2D.Double(67, 117);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(33, 144);
                _2dpoints[20] = new Point2D.Double(56, 132);
                _2dpoints[21] = new Point2D.Double(81, 135);
                _2dpoints[22] = new Point2D.Double(114, 152);
                _2dpoints[23] = new Point2D.Double(40, 160);
                _2dpoints[24] = new Point2D.Double(103, 168);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[27] = new Point2D.Double(5, 190);
                _2dpoints[30] = new Point2D.Double(57, 183);
                _2dpoints[31] = new Point2D.Double(90, 183);
                _2dpoints[33] = new Point2D.Double(90, 183);
                break;
        }/*
            case 3:
                //4th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 4:
                //5th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 5:
                //6th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 6:
                //7th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 7:
                //8th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 8:
                //9th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 9:
                //10th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 10:
                //11th doctor
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 11:
                //amy pond
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 12:
                //clara oswald
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 13:
                //river song
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 14:
                //rory pond
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
            case 15:
                //rory's dad
                _2dpoints[2] = new Point2D.Double(46, 97);
                _2dpoints[3] = new Point2D.Double(96, 96);
                _2dpoints[4] = new Point2D.Double(66, 140);
                _2dpoints[5] = new Point2D.Double(53, 154);
                _2dpoints[6] = new Point2D.Double(89, 154);
                _2dpoints[7] = new Point2D.Double(46, 97);
                _2dpoints[8] = new Point2D.Double(96, 96);
                _2dpoints[9] = new Point2D.Double(66, 140);
                _2dpoints[10] = new Point2D.Double(53, 154);
                _2dpoints[11] = new Point2D.Double(89, 154);
                _2dpoints[12] = new Point2D.Double(46, 97);
                _2dpoints[13] = new Point2D.Double(96, 96);
                _2dpoints[15] = new Point2D.Double(66, 140);
                _2dpoints[16] = new Point2D.Double(53, 154);
                _2dpoints[17] = new Point2D.Double(89, 154);
                _2dpoints[19] = new Point2D.Double(46, 97);
                _2dpoints[20] = new Point2D.Double(96, 96);
                _2dpoints[21] = new Point2D.Double(66, 140);
                _2dpoints[22] = new Point2D.Double(53, 154);
                _2dpoints[23] = new Point2D.Double(89, 154);
                _2dpoints[24] = new Point2D.Double(66, 140);
                _2dpoints[25] = new Point2D.Double(53, 154);
                _2dpoints[26] = new Point2D.Double(89, 154);
                _2dpoints[30] = new Point2D.Double(53, 154);
                _2dpoints[31] = new Point2D.Double(89, 154);
                break;
        }*/
        return _2dpoints;
    }
}
