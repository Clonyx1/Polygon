package model;

public class Line {
    private final Point startPoint;
    private final Point endPoint;
    private int color;

    public Line(Point startPoint, Point endPoint, int color) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public double distanceToPoint(Point point) {
        // |AB| = sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double distanceToStart = Math.sqrt(Math.pow(point.getX() - startPoint.getX(), 2) + Math.pow(point.getY() - startPoint.getY(), 2));
        double distanceToEnd = Math.sqrt(Math.pow(point.getX() - endPoint.getX(), 2) + Math.pow(point.getY() - endPoint.getY(), 2));
        return Math.min(distanceToStart, distanceToEnd);
    }
}
