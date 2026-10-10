package controller;

import graphics.renderer.PolygonRenderer;
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
 * Handles user input and controls the application flow related to the
 * {@link Canvas}.
 * The controller coordinates input events, canvas operations, and rendering
 * updates.
 *
 * @author PGRF FIM UHK
 * @version 2026
 */
public class Controller {
    private final Polygon polygon;
    private final Canvas canvas;
    private Line previewLine;
    private final PolygonRenderer polygonRenderer;
    private boolean draggingLeft = false;
    private boolean draggingRight = false;
    private static final int LINE_COLOR = Color.WHITE.getRGB();
    private static final int PREVIEW_COLOR = Color.RED.getRGB();
    private static final int CLOSE_SNAP_DISTANCE = 2;

    public Controller(Canvas canvas) {
        this.canvas = canvas;
        this.polygon = new Polygon(new ArrayList<>());
        this.polygonRenderer = new PolygonRenderer(canvas);
    }

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
                    polygonRenderer.render(polygon, previewLine);
                }
            }
        });

        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Left click
                if (e.getButton() == MouseEvent.BUTTON1) {
                    startLine(e);
                    draggingLeft = true;
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    draggingRight = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingLeft = false;
                draggingRight = false;
                finishPolygon();
                previewLine = null;
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (previewLine != null && draggingLeft) {
                    // Current mouse position (can be outside of canvas)
                    Point mousePos = getMousePosition(e);
                    // Point to use for rendering (must be inside of canvas)
                    Point renderPos = snapToFirstPoint(getRenderPosition(mousePos));

                    previewLine = new Line(previewLine.getStartPoint(), renderPos, PREVIEW_COLOR);
                    polygonRenderer.render(polygon, previewLine);
                }
                if(draggingRight){
                    moveClosestPoint(e);
                }
            }
        });

        // Ask focus on initialization
        canvas.requestFocusInWindow();
        canvas.repaint();
    }

    private void moveClosestPoint(MouseEvent e) {
        Point mousePos = getMousePosition(e);
        List<Line> lines = polygon.getLines();

        if (lines.isEmpty()) {
            return;
        }

        Point closestPoint = findClosestPoint(mousePos, lines);

        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            if (line.getStartPoint().equals(closestPoint)) {
                line = new Line(mousePos, line.getEndPoint(), LINE_COLOR);
                lines.set(i, line);
            }
            if(line.getEndPoint().equals(closestPoint)) {
                line = new Line(line.getStartPoint(), mousePos, LINE_COLOR);
                lines.set(i, line);
            }
        }
        
        polygonRenderer.render(polygon, previewLine);
        canvas.repaint();
    }

    private Point findClosestPoint(Point mousePos, List<Line> lines) {
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Line line : lines) {
            Point start = line.getStartPoint();
            double startDistance = distanceBetween(mousePos, start);
            if (startDistance < minDistance) {
                minDistance = startDistance;
                closestPoint = start;
            }

            Point end = line.getEndPoint();
            double endDistance = distanceBetween(mousePos, end);
            if (endDistance < minDistance) {
                minDistance = endDistance;
                closestPoint = end;
            }
        }

        return closestPoint;
    }

    private double distanceBetween(Point first, Point second) {
        return Math.hypot(first.getX() - second.getX(), first.getY() - second.getY());
    }

    private void startLine(MouseEvent e) {
        Point mousePos = snapToFirstPoint(getRenderPosition(getMousePosition(e)));

        if (polygon.getLines().isEmpty()) {
            previewLine = new Line(mousePos, mousePos, PREVIEW_COLOR);
        } else {
            Point startPoint = polygon.getLines().get(polygon.getLines().size() - 1).getEndPoint();
            previewLine = new Line(startPoint, mousePos, PREVIEW_COLOR);
        }

        polygonRenderer.render(polygon, previewLine);
    }

    private Point snapToFirstPoint(Point point) {
        List<Line> lines = polygon.getLines();
        if (lines.size() < 2) {
            return point;
        }

        Point firstPoint = lines.getFirst().getStartPoint();
        if (distanceBetween(point, firstPoint) <= CLOSE_SNAP_DISTANCE) {
            return firstPoint;
        }

        return point;
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

        polygonRenderer.render(polygon, null);
    }

    private Point getMousePosition(MouseEvent e) {
        return new Point(e.getX(), e.getY());
    }

    private Point getRenderPosition(Point mousePos) {
        Point renderPos = new Point(mousePos.getX(), mousePos.getY());
        if (xOutOfBounds(renderPos.getX())) {
            int x = renderPos.getX() < 0 ? 0 : canvas.getRaster().getWidth() - 1;
            renderPos = new Point(x, renderPos.getY());
        }
        if (yOutOfBounds(renderPos.getY())) {
            int y = renderPos.getY() < 0 ? 0 : canvas.getRaster().getHeight() - 1;
            renderPos = new Point(renderPos.getX(), y);
        }

        return renderPos;
    }

    private boolean xOutOfBounds(int x) {
        if (x < 0 || x >= canvas.getRaster().getWidth()) {
            return true;
        }

        return false;
    }

    private boolean yOutOfBounds(int y) {
        if (y < 0 || y >= canvas.getRaster().getHeight()) {
            return true;
        }

        return false;
    }
}