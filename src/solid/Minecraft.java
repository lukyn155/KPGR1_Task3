package solid;

import transforms.Point3D;

public class Minecraft extends Solid {
    public Minecraft() {
        type = "axis";
        // vb
        vb.add(new Point3D(0, 0, 0)); // v0
        //Zelená je x
        vb.add(new Point3D(0.2, 0, 0)); // v1
        //Modrá je Y
        vb.add(new Point3D(0, 0.2, 0)); // v2
        //Žlutá je Z
        vb.add(new Point3D(0, 0, 0.2)); // v3

        // ib
        addIndices(
                0, 1,
                0, 2,
                0, 3
        );
    }
}
