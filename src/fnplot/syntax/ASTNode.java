/*
 * ASTNode.java
 * Created on 16-Nov-2016, 8:51:57 AM
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

package fnplot.syntax;

import fnplot.semantics.Visitor;
import fnplot.sys.FnPlotException;

/**
 *
 * @author newts
 */
public abstract class ASTNode {
    
    /**
     * Visit this expression (subtree rooted at this node in the AST) using a
     * visitor and some state that it might need.
     * @param <S> The type of the state to be passed.
     * @param <T> The type of the return value of the visitor instance
     * @param v The visitor instance
     * @param state The state of the computation inherited from the parent of 
     * this node.
     * @return The result of the visitor calling its appropriate method 
     * corresponding to the specific subclass of expression that this node 
     * represents.
     * @throws FnPlotException If a runtime error occurs.
     */
    public abstract <S, T> T visit(Visitor<S, T> v, S state) throws FnPlotException ;

    @Override
    public abstract String toString();

}
