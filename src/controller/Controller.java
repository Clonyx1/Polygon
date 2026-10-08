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
        this.polygonRasterizer = new PolygonRasterizer(lineRasterizer);
    }

    /* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Main functions -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=- */

    public void init() {
        canvas.clear();
        polygon.getLines().clear();
        previewLine = null;

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    polygon.getLines().clear();
                    previewLine = null;
                    canvas.clear();
                    render();
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startLine(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                finishPolygon();
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (previewLine != null) {
                    Point current = getMousePosition(e);
                    previewLine = new Line(previewLine.getStartPoint(), current, PREVIEW_COLOR);
                    render();
                }
            }
        });

        //Požadání o focus hned po inicializaci
        canvas.requestFocusInWindow();
        canvas.repaint();
    }

    public void render() {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);

        if (previewLine != null) {
            lineRasterizer.rasterize(previewLine);
        }

        canvas.repaint();
    }

    private void startLine(MouseEvent e) {
        Point currentPoint = getMousePosition(e);

        if (polygon.getLines().isEmpty()) {
            previewLine = new Line(currentPoint, currentPoint, PREVIEW_COLOR);
        } else {
            Point startPoint = polygon.getLines().get(polygon.getLines().size() - 1).getEndPoint();
            previewLine = new Line(startPoint, currentPoint, PREVIEW_COLOR);
        }

        render();
    }

    private void finishPolygon() {
        if (previewLine == null) {
            return;
        }

        Point start = previewLine.getStartPoint();
        Point end = previewLine.getEndPoint();

        if (start.getX() != end.getX() || start.getY() != end.getY()) {
            polygon.getLines().add(new Line(start, end, LINE_COLOR));
        }

        previewLine = null;
        render();
    }

    private Point getMousePosition(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }
}