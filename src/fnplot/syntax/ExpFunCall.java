/*
 * ExpFunction.java
 * Created on 14-Nov-2016, 9:41:02 PM
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
import java.util.ArrayList;

/**
 *
 * @author newts
 */
public class ExpFunCall  extends Exp {
    
    ArrayList<Exp> arguments;
    String name;

    public ExpFunCall() {
        super();
    }

    public ExpFunCall(String name, ArrayList<Exp> args, Exp body) {
        this.name = name;
        this.arguments = args;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    
    public ArrayList<Exp> getArguments() {
        return arguments;
    }
    
    @Override
    public <S, T> T visit(Visitor<S, T> v, S state) throws FnPlotException {
        return v.visitFunCall(this, state);
    }

    @Override
    public String toString() {
        StringBuilder argStr = new StringBuilder("");
        int n = arguments.size();
        if (n > 0) {
            argStr.append(arguments.get(0));
            for(int i = 1; i < n; i++) {
                argStr.append(", ");
                argStr.append(arguments.get(i));
            }
        }
        return String.format("%s(%s)", name, argStr.toString());
    }

}
