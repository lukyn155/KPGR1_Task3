package renderer;

import rasterize.LineRasterizer;
import solid.Solid;
import transforms.Col;
import transforms.Mat4;
import transforms.Point3D;
import transforms.Vec3D;

import java.awt.*;

public class WiredRenderer {
    private LineRasterizer lineRasterizer;

    private Mat4 view, proj;

    public WiredRenderer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void render(Solid solid, int width, int height, String projType) {
        Color[] colors = new Color[5];
        colors[0] = Color.GREEN;
        colors[2] = Color.BLUE;
        colors[4] = Color.YELLOW;
        for (int i = 0; i < solid.getIb().size(); i += 2) {
            int indexA = solid.getIb().get(i);
            int indexB = solid.getIb().get(i + 1);

            Point3D a = solid.getVb().get(indexA);
            Point3D b = solid.getVb().get(indexB);

            // modelovací tranformace
            a = a.mul(solid.getModel());
            b = b.mul(solid.getModel());

            // pohledová tranformace
            a = a.mul(view);
            b = b.mul(view);

            // projekční tranformace
            a = a.mul(proj);
            b = b.mul(proj);

            // Ořezání
            if (((-a.getW()) <= a.getX() && a.getX() <= a.getW()) && ((-a.getW()) <= a.getY() && a.getY() <= a.getW()) && (0 <= a.getZ() && a.getZ() <= a.getW())) {
                if (((-b.getW()) <= b.getX() && b.getX() <= b.getW()) && ((-b.getW()) <= b.getY() && b.getY() <= b.getW()) && (0 <= b.getZ() && b.getZ() <= b.getW())) {
                    Vec3D v = new Vec3D();
                    Vec3D w = new Vec3D();
                    if (a.dehomog().isPresent())
                        v = a.dehomog().get();
                    if (b.dehomog().isPresent())
                        w = b.dehomog().get();

                    Vec3D newIndexA = v.mul(new Vec3D(1, -1, 1));
                    newIndexA = newIndexA.add(new Vec3D(1, 1, 0));
                    newIndexA = newIndexA.mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) / 2, 1));

                    Vec3D newIndexB = w.mul(new Vec3D(1, -1, 1));
                    newIndexB = newIndexB.add(new Vec3D(1, 1, 0));
                    newIndexB = newIndexB.mul(new Vec3D((double) (width - 1) / 2, (double) (height - 1) / 2, 1));

                    if (solid.getType() == "Solid") {
                        // Rasterizace
                        lineRasterizer.rasterize(
                                (int) Math.round(newIndexA.getX()), (int) Math.round(newIndexA.getY()),
                                (int) Math.round(newIndexB.getX()), (int) Math.round(newIndexB.getY()),
                                solid.getColor()
                        );
                    } else {
                        // Rasterizace
                        lineRasterizer.rasterize(
                                (int) Math.round(newIndexA.getX()), (int) Math.round(newIndexA.getY()),
                                (int) Math.round(newIndexB.getX()), (int) Math.round(newIndexB.getY()),
                                colors[i]
                        );
                    }
                }
            }
        }
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }
}

