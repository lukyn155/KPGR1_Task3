package solid;

import transforms.Point3D;

import java.awt.*;

public class Pyramid extends Solid {
    public Pyramid() {
        // vb
        vb.add(new Point3D(0, 0, 0)); // v0
        vb.add(new Point3D(1, 0, 0)); // v1
        vb.add(new Point3D(1, 1, 0)); // v2
        vb.add(new Point3D(0, 1, 0)); // v3
        vb.add(new Point3D(0.5, 0.5, 1)); // v4

        // ib
        addIndices(
                0, 1,
                1, 2,
                2, 3,
                3, 0,
                0, 4,
                1, 4,
                2, 4,
                3, 4
        );

        color = Color.PINK;
    }
}
