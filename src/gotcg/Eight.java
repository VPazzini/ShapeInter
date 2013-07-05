package gotcg;

import java.awt.HeadlessException;
import java.awt.Point;
import java.util.ArrayList;

public class Eight {
    private ArrayList<Point>[] octantPointsC1;
    private ArrayList<Point>[] octantPointsC2;
    
    public Eight() throws HeadlessException {
        this.octantPointsC1 = new ArrayList[8];
        this.octantPointsC2 = new ArrayList[8];
        for (int i = 0; i < 8; i++) {
            this.octantPointsC1[i] = new ArrayList<>();
            this.octantPointsC2[i] = new ArrayList<>();
        }
    }

    private void calculateAllOctants(int xc, int yc, int x, int y, int R) {
        int i = 0;
        this.octantPointsC1[i++].add(new Point(xc + x, yc - y));
        this.octantPointsC1[i++].add(0, new Point(xc + y, yc - x));
        this.octantPointsC1[i++].add(new Point(xc + y, yc + x));
        this.octantPointsC1[i++].add(0, new Point(xc + x, yc + y));

        //Quadrante
        this.octantPointsC1[i++].add(new Point(xc - x, yc + y));
        this.octantPointsC1[i++].add(0, new Point(xc - y, yc + x));
        this.octantPointsC1[i++].add(new Point(xc - y, yc - x));
        this.octantPointsC1[i++].add(0, new Point(xc - x, yc - y));


        yc += 2 * R;
        i = 0;

        this.octantPointsC2[i++].add(new Point(xc + x, yc - y));
        this.octantPointsC2[i++].add(0, new Point(xc + y, yc - x));
        this.octantPointsC2[i++].add(new Point(xc + y, yc + x));
        this.octantPointsC2[i++].add(0, new Point(xc + x, yc + y));

        //Quadrante
        this.octantPointsC2[i++].add(new Point(xc - x, yc + y));
        this.octantPointsC2[i++].add(0, new Point(xc - y, yc + x));
        this.octantPointsC2[i++].add(new Point(xc - y, yc - x));
        this.octantPointsC2[i++].add(0, new Point(xc - x, yc - y));

    }

    public void calculateOctant(int xc, int yc, int R) {
        int x = 0;
        int y = R;
        int d = 1 - R;
        calculateAllOctants(xc, yc, x, y, R);

        while (y > x) {
            if (d < 0) {
                d += x * 2 + 3;
                x++;
            } else {
                d += 2 * x - 2 * y + 5;
                x++;
                y--;
            }
            calculateAllOctants(xc, yc, x, y, R);
        }
    }

    public ArrayList<Point> getOctantPointsC1() {
        ArrayList<Point> temp = new ArrayList<>();
        for (ArrayList<Point> l : this.octantPointsC1) {
            for (Point p : l) {
                temp.add(p);
            }
        }
        return temp;
    }

    public ArrayList<Point> getOctantPointsC2() {
        ArrayList<Point> temp = new ArrayList<>();
        for (ArrayList<Point> l : this.octantPointsC2) {
            for (Point p : l) {
                temp.add(p);
            }
        }
        return temp;
    }

    public ArrayList<Point> getInvOctantPointsC2() {
        ArrayList<Point> temp = new ArrayList<>();
        for (ArrayList<Point> l : this.octantPointsC2) {
            for (Point p : l) {
                temp.add(0, p);
            }
        }
        return temp;
    }

    private int getOctantPointsC1Lenght(){
        int tam = 0;
        for(ArrayList l : this.octantPointsC1){
            tam += l.size();
        }
        return tam;
    }
    
    public ArrayList<Point> getCompleteList() {
        ArrayList<Point> temp;
        temp = this.getOctantPointsC1();
        int middle = this.getOctantPointsC1Lenght() / 2;
        temp.addAll(middle, this.getInvOctantPointsC2());
        return temp;
    }
}
