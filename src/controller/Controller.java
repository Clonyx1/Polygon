package controller;

import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Point;
import model.Polygon;
import view.Canvas;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles user input and controls the application flow related to the {@link Canvas}.
 * The controller coordinates input events, canvas operations, and rendering updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {
    private final Polygon polygon;
    private final Canvas canvas;
    private Line previewLine;
    private final TrivialLineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- Constructors -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= */

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.polygon = new Polygon(new ArrayList<>());
        this.lineRasterizer = new TrivialLineRasterizer(canvas.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();

        canvas.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
             super.mousePressed(e);
         }
        });

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //TODO: Zkontrolovat jestli maže canvas při stisknutí klávesy C
                if(e.getKeyCode() == KeyEvent.VK_C){
                    canvas.clear();
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1){
                    startLine(e); //Při stisku tlačítka na myši se začne kreslit čára (měl by být preview)
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                finishPolygon(); //Když se pustí myš tak se čára vykreslí
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //TODO: Zobrazit preview čáru
            }
        });

        canvas.repaint();
    }

    public void render(){
        canvas.clear();

        polygonRasterizer.rasterize(polygon);

        canvas.repaint();
    }

    private void startLine(MouseEvent e){
        //TODO: Vzít poslední bod polygonu
        //Vzít getPoint(e) - aktuální bod
        //previewLine = new Line(startPoint, lastPoint, preview barva);
        render(); //Aby se vykreslil polygon
    }

    private void finishPolygon(){
        previewLine = null;
        render();
    }

    private Point getMousePosition(MouseEvent e){
        return new Point(e.getX(), e.getY());
    }
}