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
        //vector
        double dx = endPoint.getX() - startPoint.getX();
        double dy = endPoint.getY() - startPoint.getY();
        //|AB|^2
        double lengthSquared = dx * dx + dy * dy;

        if (lengthSquared == 0) {
            return startPoint.distanceBetween(point);
        }
        //t = (P-A*AB) / |AB|^2
        double t = ((point.getX() - startPoint.getX()) * dx
                + (point.getY() - startPoint.getY()) * dy) / lengthSquared;
        //Make sure point is on the line segment
        //Is 0-1, works as percentage of the line segment length, where 0 is startPoint and 1 is endPoint
        t = Math.max(0, Math.min(1, t));

        double closestX = startPoint.getX() + t * dx;
        double closestY = startPoint.getY() + t * dy;
        //Euclidean distance from point to closest point on the line segment
        return Math.hypot(point.getX() - closestX, point.getY() - closestY);
    }
}
