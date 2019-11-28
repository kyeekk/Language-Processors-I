package fnplot.sys;

import fnplot.semantics.Environment;
import fnplot.semantics.Evaluator;
import fnplot.syntax.ArithProgram;
import fnplot.syntax.FnPlotLexer;
import fnplot.syntax.FnPlotParser;
import java.io.*;

public class Repl {

    public static final String PROMPT = "Eval>";

    public static void main(String args[]) {
	repl(new Environment());
    }

    public static void repl(Environment env){
	InputStreamReader reader = new InputStreamReader(System.in);
	while (true) {
	    parseEvalShow(reader, env);
	}
    }

    public static void parseEvalShow(Reader reader,
				     Environment env) {
	FnPlotParser parser;
	ArithProgram program = null;
	Evaluator interp = new Evaluator();
	System.out.print(PROMPT);
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

}
