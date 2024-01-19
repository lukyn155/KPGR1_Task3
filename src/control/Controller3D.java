package control;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import rasterize.Raster;
import renderer.WiredRenderer;
import solid.*;
import transforms.*;
import view.Panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Controller3D implements Controller {
    private final Panel panel;

    private LineRasterizer rasterizer;
    private WiredRenderer renderer;

    private Camera camera;
    private Mat4 proj;

    private int firstX;
    private int firstY;

    private int secondX;
    private int secondY;
    double azimut = 90;
    double zenit = 0;

    int pX = 0;
    int pY = -2;
    double pZ = 0.3;
    final double step = 0.25;

    private String projType = "persp";

    public Controller3D(Panel panel) {
        this.panel = panel;
        initObjects(panel.getRaster());
        initListeners(panel);

        update();
    }

    public void initObjects(Raster raster) {
        rasterizer = new LineRasterizerGraphics(raster);
        renderer = new WiredRenderer(rasterizer);

        camera = new Camera(
                new Vec3D(pX, pY, pZ),
                Math.toRadians(azimut),
                Math.toRadians(zenit),
                1,
                true
        );

        proj = new Mat4PerspRH(
                Math.PI / 4,
                raster.getHeight() / (double) raster.getWidth(),
                0.1,
                20
        );
    }

    @Override
    public void initListeners(Panel panel) {
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {

                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    firstX = e.getX();
                    firstY = e.getY();
                    // rasterizer.rasterize(x, y, e.getX(),e.getY(), Color.RED);
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                }

                update();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //TODO
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        //TODO
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.isControlDown()) return;

                if (e.isShiftDown()) {
                    //TODO
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    secondX = e.getX();
                    secondY = e.getY();

                    int dx = secondX - firstX;
                    int dy = secondY - firstY;

                    zenit -= (double) 180 * dy / panel.getHeight();
                    if (zenit > 90) zenit = 90;
                    if (zenit < -90) zenit = -90;


                    azimut -= (double) 180 * dx / panel.getWidth();
                    azimut = azimut % 360;

                    firstX = secondX;
                    firstY = secondY;
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    //TODO
                } else if (SwingUtilities.isMiddleMouseButton(e)) {
                    //TODO
                }
                update();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // na klávesu C vymazat plátno
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    //TODO
                } else if (e.getKeyCode() == KeyEvent.VK_O) {
                    proj = new Mat4OrthoRH(
                            3,
                            3,
                            0.1,
                            20
                    );
                    projType = "ort";
                } else if (e.getKeyCode() == KeyEvent.VK_P) {
                    proj = new Mat4PerspRH(
                            Math.PI / 4,
                            panel.getRaster().getHeight() / (double) panel.getRaster().getWidth(),
                            0.1,
                            20
                    );
                    projType = "persp";
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(step);
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(step);
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(step);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(step);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    camera = camera.up(step);
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    camera = camera.down(step);
                }
                update();
            }
        });

        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.resize();
                initObjects(panel.getRaster());
            }
        });
    }

    private void update() {
        panel.clear();

        renderer.setProj(proj);
        camera = camera.withAzimuth(Math.toRadians(azimut)).withZenith(Math.toRadians(zenit));
        renderer.setView(camera.getViewMatrix());

        Solid cube = new Cube();
        Solid minecraft = new Minecraft();
        Solid pyramid = new Pyramid();
        Point3D[] bpoints = new Point3D[]{
                new Point3D(0,0,0),
                new Point3D(0,1,0),
                new Point3D(1,1,0),
                new Point3D(1,1,1),
        };
        Point3D[] fpoints = new Point3D[]{
                new Point3D(0,1,1),
                new Point3D(1,0,0),
                new Point3D(1,1,0),
                new Point3D(0,-1,-1),
        };
        Point3D[] cpoints = new Point3D[]{
                new Point3D(0,1,1),
                new Point3D(0.5,0,1),
                new Point3D(0,0.5,0),
                new Point3D(0,0,1),
        };
        Solid bezier = new Curve(Cubic.BEZIER, bpoints, Color.yellow);
        Solid ferguson = new Curve(Cubic.FERGUSON, fpoints, Color.white);
        Solid coonsov = new Curve(Cubic.COONS, cpoints, Color.CYAN);

        Mat4 matTrans = new Mat4Identity();
        matTrans = matTrans.mul(new Mat4RotY(45).mul(new Mat4Scale(0.5)).mul(new Mat4Transl(0,-1,0.3)).mul(new Mat4RotX(-15)));
        Mat4 matTrans3 = new Mat4Identity();
        matTrans3 = matTrans3.mul(new Mat4Transl(-1.5, 1, 0));

        cube.setModel(matTrans);
        bezier.setModel(matTrans);
        ferguson.setModel(matTrans);
        coonsov.setModel(matTrans);
        pyramid.setModel(matTrans3);
        renderer.render(cube, panel.getWidth(), panel.getHeight(), projType);
        renderer.render(pyramid, panel.getWidth(), panel.getHeight(), projType);
        renderer.render(minecraft, panel.getWidth(), panel.getHeight(), projType);
        renderer.render(bezier, panel.getWidth(), panel.getHeight(), projType);
        renderer.render(ferguson, panel.getWidth(), panel.getHeight(), projType);
        renderer.render(coonsov, panel.getWidth(), panel.getHeight(), projType);

        panel.repaint();
    }

    private void hardClear() {
        panel.clear();
    }

}
