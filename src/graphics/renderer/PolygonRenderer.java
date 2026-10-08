package graphics.renderer;

import graphics.rasterizer.PolygonRasterizer;
import graphics.rasterizer.TrivialLineRasterizer;
import model.Line;
import model.Polygon;
import view.Canvas;

public class PolygonRenderer implements IPolygonRenderer {
    private final Canvas canvas;
    private final TrivialLineRasterizer lineRasterizer;
    private final PolygonRasterizer polygonRasterizer;

    public PolygonRenderer(Canvas canvas) {
        this.canvas = canvas;
        this.lineRasterizer = new TrivialLineRasterizer(canvas.getRaster());
        this.polygonRasterizer = new PolygonRasterizer(lineRasterizer);
    }

    @Override 
    public void render(Polygon polygon, Line previewLine) {
        canvas.clear();
        polygonRasterizer.rasterize(polygon);

        if (previewLine != null) {
            lineRasterizer.rasterize(previewLine);
        }

        canvas.repaint();
    }
}
