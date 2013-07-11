package gotcg;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class TriangulatedImage {

    BufferedImage bi;
    Point2D[] tPoints;

    int[][] triangles;

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

        mix.triangles = this.triangles;

        int rgbValueThis;
        int rgbValueTi;
        int rgbValueMix;
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
        Point2D[] threePoints = new Point2D[3];;
        Point2D.Double pixel = new Point2D.Double();
        int tNo;
        boolean notFound;

        for (int i = 0; i < mix.bi.getWidth(); i++) {
            for (int j = 0; j < mix.bi.getHeight(); j++) {
                pixel.setLocation(i, j);

                tNo = 0;

                notFound = true;
                while (tNo < mix.triangles.length && notFound) {

                    for (int k = 0; k < 3; k++) {
                        threePoints[k] = mix.tPoints[mix.triangles[tNo][k]];
                    }

                    t = triangleCoordinates(pixel, threePoints);

                    if (isConvexCombination(t)) {
                        notFound = false;
                    } else {
                        tNo++;
                    }
                }

                if (!notFound) {
                    aux = 0;
                    for (int k = 0; k < 3; k++) {
                        aux = aux + t[k] * this.tPoints[this.triangles[tNo][k]].getX();
                    }
                    xInt = (int) Math.round(aux);
                    xInt = Math.max(0, xInt);
                    xInt = Math.min(this.bi.getWidth() - 1, xInt);

                    aux = 0;
                    for (int k = 0; k < 3; k++) {
                        aux = aux + t[k] * this.tPoints[this.triangles[tNo][k]].getY();
                    }
                    yInt = (int) Math.round(aux);
                    yInt = Math.max(0, yInt);
                    yInt = Math.min(this.bi.getHeight() - 1, yInt);

                    rgbValueThis = this.bi.getRGB(xInt, yInt);
                    thisColour = new Color(rgbValueThis);

                    aux = 0;
                    for (int k = 0; k < 3; k++) {
                        aux = aux + t[k] * ti.tPoints[ti.triangles[tNo][k]].getX();
                    }
                    xInt = (int) Math.round(aux);

                    aux = 0;
                    for (int k = 0; k < 3; k++) {
                        aux = aux + t[k] * ti.tPoints[ti.triangles[tNo][k]].getY();
                    }
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
}
