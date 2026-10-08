package graphics.rasterizer;

import graphics.Raster;
import model.Line;
import model.Point;

public class TrivialLineRasterizer implements ILineRasterizer {

    private final Raster<Integer> raster;

    public TrivialLineRasterizer(Raster<Integer> raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        Point start = line.getStartPoint();
        Point end = line.getEndPoint();

        Point a = new Point(start.getX(), start.getY());
        Point b = new Point(end.getX(), end.getY());

        rasterizeSegment(a, b, line.getColor());
    }


    //Použit midpoint algoritmus implementován podle pseudokódu v prezentaci
    private void rasterizeSegment(Point a, Point b, int color)
    {
        if(Math.abs(b.getX() - a.getX()) > 1 || Math.abs(b.getY() - a.getY()) > 1){
            midPoint(a, b, color);
        }

    }
    private void midPoint(Point a, Point b, int color){
        int mx = (a.getX() + b.getX()) / 2;
        int my = (a.getY() + b.getY()) / 2;
        Point middlePoint = new Point(mx, my);

        raster.setPixel(middlePoint.getX(), middlePoint.getY(), color);

        if(Math.abs(a.getX() - mx) > 1 || Math.abs(a.getY() - my) > 1){
            midPoint(a, middlePoint, color);
        }
        if(Math.abs(b.getX() - mx) > 1 || Math.abs(b.getY() - my) > 1){
            midPoint(middlePoint, b, color);
        }
    }
}
