package graphics.rasterizer;

import model.Line;
import model.Point;
import model.Polygon;

import java.util.List;

public class PolygonRasterizer implements IPolygonRasterizer{
    private final TrivialLineRasterizer lineRasterizerizer;

    public PolygonRasterizer(TrivialLineRasterizer lineRasterizerizer) {
        this.lineRasterizerizer = lineRasterizerizer;
    }

    @Override
    public void rasterize(Polygon polygon) {
        List<Line> lines = polygon.getLines();

        rasterizeLines(lines);

        if(lines.size() > 1){
            Line firstLine = lines.getFirst();
            Line lastLine = lines.getLast();

            Point start = firstLine.getEndPoint();
            Point end = lastLine.getEndPoint();
            int color = firstLine.getColor();

            lineRasterizerizer.rasterize(new Line(start, end, color));
        }
    }

    private void rasterizeLines(List<Line> lines){
        for(Line line : lines){
            lineRasterizerizer.rasterize(line);
        }
    }
}
