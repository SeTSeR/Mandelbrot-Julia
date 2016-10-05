package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Created by hp on 27.09.2016.
 */
public class Utils {

    final static int MODE_GRAY = 0;
    final static int MODE_COLOR = 1;

    public static Image calc(Complex z0, Integer k, Integer iterations, Double x0, Double y0, Double x1, Double y1, int mode) {
        final int colors = Math.min(iterations, 256);
        final int len = iterations / colors;
        final int nx = 1000, ny = 1000;
        final int width = 700, height = 500;
        final double stepx = (x1 - x0) / nx, stepy = (y1 - y0) / ny;
        final double xcoef = (width - 1) / (x1 - x0), ycoef = (height - 1) / (y0 - y1);
        final double xshift = -xcoef * x0, yshift = -ycoef * y1;
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for(Double a = x0; a <= x1; a += stepx) {
            for(Double b = y0; b <= y1; b += stepy) {
                Complex z = z0;
                int i = 0;
                while(i < iterations) {
                    if(z.abs() > 2) break;
                    z = z.power(k).add(new Complex((double)a, (double)b));
                    ++i;
                }
                int colorR = 0, colorG = 0, colorB = 0;
                if(mode == Utils.MODE_GRAY) {
                    int color = colors - i / len;
                    if (color < 0) color = 0;
                    colorR = colorG = colorB = color;
                }
                else {
                    colorB = ((iterations - i) % 16) * 16;
                    colorG = (((iterations - i) / 16) % 16) * 16;
                    colorR = (((iterations - i) / 256) % 16) * 16;
                }
                writer.setColor((int) (xcoef * a + xshift), (int) (ycoef * b + yshift), Color.rgb(colorR, colorG, colorB));
            }
        }
        return image;
    }
}
