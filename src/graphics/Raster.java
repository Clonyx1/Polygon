package graphics;

import java.awt.*;

public interface Raster<P> {

    void setPixel(int x, int y, P value);

    void clear(P value);

    void present(Graphics graphics);

    int getWidth();

    int getHeight();

}
