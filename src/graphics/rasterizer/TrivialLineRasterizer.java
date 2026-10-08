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

        midPoint(start.getX(), end.getX(), start.getY(), end.getY(), line.getColor());
    }

    private void midPoint(int x1, int y1, int x2, int y2, int color)
    {
        // calculate dx & dy
        int dx = x2 - x1;
        int dy = y2 - y1;

        // initial value of decision
        // parameter d
        int d = dy - (dx/2);
        int x = x1, y = y1;

        // Plot initial given point
        // putpixel(x,y) can be used to
        // print pixel of line in graphics

        // iterate through value of X
        while (x < x2)
        {
            x++;

            // E or East is chosen
            if (d < 0)
                d = d + dy;

                // NE or North East is chosen
            else
            {
                d += (dy - dx);
                y++;
            }

            // Plot intermediate points
            // putpixel(x,y) is used to print
            // pixel of line in graphics
            System.out.print(x +"," + y + "\n");
            raster.setPixel(x, y, color);
        }
    }

}
