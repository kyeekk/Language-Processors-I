/*
 * CmdLine.java
 * Created on 15-Dec-2016, 1:07:06 PM
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

package fnplot.sys;

import fnplot.semantics.Environment;
import fnplot.semantics.Evaluator;
import fnplot.semantics.TextPlotter;
import fnplot.syntax.ArithProgram;
import fnplot.syntax.FnPlotLexer;
import fnplot.syntax.FnPlotParser;
import fnplot.values.FnPlotValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author newts
 */
public class CmdLine {

    static String PROMPT = "Eval>";
    Evaluator interp;
    
    public CmdLine() {
        interp = new Evaluator();
        // need to set text-based plotter for interp
        interp.setPlotter(new TextPlotter(System.out));
    }
    
    public void repl(Reader reader) {
        while (true) { // we want a better termination condition
            System.out.print(PROMPT);
            parseEvalShow(reader);
        }
    }
    
    /**
     * Read contents from reader, parse it as an FnPlot program, evaluate it,
     * and finally show its result.
     * @param reader The input stream reader containing the program
     */
    public void parseEvalShow(Reader reader) {
	FnPlotParser parser;
	ArithProgram program = null;
//	Evaluator interp = new Evaluator();
        Environment<FnPlotValue<?>> env = interp.getGlobalEnv();
	
	try {
	    parser = new FnPlotParser(new FnPlotLexer(reader));
	    program = (ArithProgram) parser.parse().value;
	} catch (Exception e) {
	    System.out.println("Parse Error: " + e.getMessage());
	}

	if (program != null)
	    try {
		Object result;
		result = program.visit(interp, env);
		System.out.println("\nResult: " + result);
	    } catch (FnPlotException e) {
		System.out.println(e.getMessage());
	    }
    }
    
    public static void main(String[] args) {
        CmdLine cmd = new CmdLine();
        Reader r;
        for (String fname : args) {
            try {
                if (fname.equals("-")) {
                    r = new InputStreamReader(System.in);
                    System.out.println(PROMPT);
                } else {
                    r = new FileReader(new File(fname));
                }
                cmd.parseEvalShow(r);
            } catch (FileNotFoundException ex) {
                System.out.format("Warning: Could not find file %s.  Skipping it.%n", fname);
            }
        }
    }
}
