package solid;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {
    protected ArrayList<Point3D> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected String type = "Solid";
    private Mat4 model = new Mat4Identity();

    protected Color color = Color.RED;

    protected void addIndices(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }

    public ArrayList<Point3D> getVb() {
        return vb;
    }

    public ArrayList<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {
        return model;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {return type;}

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
