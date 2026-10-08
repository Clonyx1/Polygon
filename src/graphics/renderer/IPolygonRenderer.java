package graphics.renderer;

import model.Line;
import model.Polygon;

public interface IPolygonRenderer {
    void render(Polygon polygon, Line previewLine);
}
