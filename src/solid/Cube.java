package solid;

import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {
        // vb
        vb.add(new Point3D(0, 0, 0)); // v0
        vb.add(new Point3D(1, 0, 0)); // v1
        vb.add(new Point3D(1, 1, 0)); // v2
        vb.add(new Point3D(0, 1, 0)); // v3
        vb.add(new Point3D(0, 0, 1)); // v4
        vb.add(new Point3D(1, 0, 1)); // v5
        vb.add(new Point3D(1, 1, 1)); // v6
        vb.add(new Point3D(0, 1, 1)); // v7

        // ib
        addIndices(
                0, 1,
                1, 2,
                2, 3,
                3, 0,
                4, 5,
                5, 6,
                6, 7,
                7, 4,
                0, 4,
                1, 5,
                2, 6,
                3, 7
        );
    }
}
