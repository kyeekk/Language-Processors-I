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

/**
 * The Plotter is a device that performs the graphing of a given function over 
 * a set (range of input values).  The plotter determines based on its own
 * capabilities (and maybe characteristics of the function) how to sample the
 * input space, and provides a method to accept a collection of points to be
 * plotted.  The idea is that the interpretor should first call the sample method
 * to obtain a set of input points, compute the function's outputs for those inputs
 * and then call the plot method of the plotter on the collection of pairs of
 * points generated in that way.
 * @author newts
 */
public interface Plotter {
    
    public double[] sample(double low, double hi);
    
    public void plot(Point2D[] points);
    
    public void clear();
    
}
