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

    public int getColor() {
        return color;
    }
}
