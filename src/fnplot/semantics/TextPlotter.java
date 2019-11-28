/*
 * GraphPlotter.java
 * Created on 17-Nov-2016, 9:02:14 AM
 */

/*
 * Copyright (C) 2016 newts
 * Produced as part of course software for COMP3652 at UWI, Mona
 * If you have any questions about this software, please contact
 * the author.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fnplot.semantics;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author newts
 */
public class TextPlotter implements Plotter {
    PrintWriter out;
    
    public TextPlotter(PrintStream outStream) {
        out = new PrintWriter(outStream);
    }

    @Override
    public double[] sample(double low, double hi) {
        // produce samples that are about 1 pixel apart at current resolution
        int width = 100;
        double interval = (hi - low) / width;
        double[] result = new double[width + 1];
        double x = low;
        for (int i = 0; i < width; i++) {
            result[i] = x;
            x = x + interval;
        }
        result[width] = hi;
        return result;
    }

    @Override
    public void plot(Point2D[] points) {
        float[] xs = new float[points.length];
        float[] ys = new float[points.length];
        for (int i = 0; i < points.length; i++) {
            xs[i] = (float) points[i].getX();
            ys[i] = (float) points[i].getY();
        }
        
        int n = 4;   // no. of points per line
        int p = 0;
        out.println("<PATH:");
        for (int i = 0; i < points.length/n; i++) {
            out.print("  ");
            for (int j = 0; j < n - 1; j++) {
                out.format("(%.4f, %.4f), ", xs[p], ys[p]);
                p = p + 1;
            }
            out.format("(%.4f, %.4f)%n", xs[p], ys[p]);
            p = p + 1;
        }
        if (p < points.length) {
            out.print("  ");
            while (p < points.length - 1) {
                out.format("(%.4f, %.4f), ", xs[p], ys[p]);
                p = p + 1;
            }
            out.format("(%.4f, %.4f)%n", xs[p], ys[p]);
        }
        out.println(">");
        out.flush();
    }

    @Override
    public void clear() {
        out.println("<CLEAR>");
        out.flush();
    }

}
