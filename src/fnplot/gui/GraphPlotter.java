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

package fnplot.gui;

import cs34q.gfx.GraphingPanel;
import fnplot.semantics.Plotter;
import java.awt.geom.Point2D;

/**
 *
 * @author newts
 */
public class GraphPlotter implements Plotter {
    
    private GraphingPanel gPanel;

    public GraphPlotter(GraphingPanel panel) {
        this.gPanel = panel;
    }
    
    

    @Override
    public double[] sample(double low, double hi) {
        // produce samples that are about 1 pixel apart at current resolution
        int width = gPanel.getWidth();
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
        gPanel.drawPath(xs, ys);
    }

    @Override
    public void clear() {
        gPanel.clear();
    }

}
