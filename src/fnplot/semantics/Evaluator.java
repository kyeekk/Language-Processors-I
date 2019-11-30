package fnplot.semantics;

import fnplot.syntax.StmtLet;
import fnplot.syntax.Statement;
import fnplot.syntax.StmtDefinition;
import fnplot.syntax.StmtSequence;
import fnplot.syntax.ExpLit;
import fnplot.syntax.ExpDiv;
import fnplot.syntax.ExpMul;
import fnplot.syntax.ExpPow;
import fnplot.syntax.ExpAdd;
import fnplot.syntax.ExpVar;
import fnplot.syntax.ExpMod;
import fnplot.syntax.ExpSub;
import fnplot.syntax.Binding;
import fnplot.syntax.ArithProgram;
import fnplot.syntax.Exp;
import fnplot.syntax.ExpFunction;
import fnplot.syntax.ExpFunCall;
import fnplot.sys.FnPlotException;
import fnplot.values.FnPlotFunction;
import fnplot.values.FnPlotReal;
import fnplot.values.FnPlotValue;
import java.awt.geom.Point2D;
import java.util.*;

public class Evaluator 
    implements Visitor<Environment<FnPlotValue<?>>, FnPlotValue<?>> {
    /* For this visitor, the argument passed to all visit
       methods will be the environment object that used to
       be passed to the eval method in the first style of
       implementation. */

    // allocate state here
    protected FnPlotValue<?> result;	// result of evaluation

    /**
     * The global environment associated with this evaluator.
     */
    protected Environment<FnPlotValue<?>> globalEnv;
    
    /**
     * The plotting device used by this interpreter.
     */
    private Plotter plotter;

    public Evaluator() {
	// perform initialisations here
	result = FnPlotValue.make(0);
        globalEnv = new Environment<>();
    }
    
    /**
     * @return The global environment used by this evaluator.  This will be the
     * parent environemnt of all environments that might arise during the 
     * tree walk of an AST that this Evaluator instance may perform.
     */
    public Environment<FnPlotValue<?>> getGlobalEnv() {
        return globalEnv;
    }

    /**
     * @return The plotting device currently being used by this interpreter
     */
    public Plotter getPlotter() {
        return plotter;
    }

    /**
     * Set the plotting device.
     * @param plotter The plotting device to be used by this interpreter.
     */
    public void setPlotter(Plotter plotter) {
        this.plotter = plotter;
    }

    /**
     * Visit a node representing the overall program.  This will be similar to
     * visiting the sequence of statements that make up the program, but is
     * provided as a separate method so that any top-level, one-time actions 
     * can be taken to initialise the context for the program, if necessary.
     * @param p The program node to be traversed.
     * @param arg The environment to be used while traversing the program.
     * @return The result of the last statement of the program, after evaluating
     * all the preceding ones in order.
     * @throws FnPlotException if any of the statements in the body of the 
     * program throws an exception.
     */
    @Override
    public FnPlotValue<?> visitArithProgram(ArithProgram p, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	result = p.getSeq().visit(this, arg);
	return result;
    }

    @Override
    public FnPlotValue<?> visitStmtSequence(StmtSequence sseq, Environment<FnPlotValue<?>> env)
	throws FnPlotException {
	ArrayList<Statement> seq = sseq.getSeq();
	Iterator<Statement> iter = seq.iterator();
	result = FnPlotValue.make(0); // default result
        for (Statement s : seq) {
            result = s.visit(this, env);
        }
	// return last value evaluated
	return result;
    }

    @Override
    public FnPlotValue<?> visitStmtDefinition(StmtDefinition sd, Environment<FnPlotValue<?>> env)
	throws FnPlotException {
	result = sd.getExp().visit(this, env);
	env.put(sd.getVar(), result);
	return result;
    }

    @Override
    public FnPlotValue<?> visitStmtLet(StmtLet let, Environment<FnPlotValue<?>> env) 
	throws FnPlotException {
	ArrayList<Binding> bindings = let.getBindings();
	Exp body = let.getBody();

	int size = bindings.size();
	String[] vars = new String[size];
	FnPlotValue<?>[] vals = new FnPlotValue<?>[size];
	Binding b;
	for (int i = 0; i < size; i++) {
	    b = bindings.get(i);
	    vars[i] = b.getVar();
	    // evaluate each expression in bindings
	    result = b.getValExp().visit(this, env);
	    vals[i] = result;
	}
	// create new env as child of current
	Environment<FnPlotValue<?>> newEnv = new Environment<> (vars, vals, env);
	return body.visit(this, newEnv);
    }

    @Override
    public FnPlotValue<?> visitFunDefn(ExpFunction defn, Environment<FnPlotValue<?>> env)
	throws FnPlotException {
    FnPlotFunction c = new FnPlotFunction(defn, env);
    return c;
    }

    @Override
    public FnPlotValue<?> visitFunCall(ExpFunCall callExp, Environment<FnPlotValue<?>> env)
    throws FnPlotException 
    {
        String name = callExp.getName();
        ArrayList<Exp> args = callExp.getArguments();
        FnPlotFunction fun = (FnPlotFunction) env.get(name);
        ArrayList<FnPlotValue> values = new ArrayList<>();
        for (Exp funarg : args){
            values.add(funarg.visit(this, env));
        }
        Environment newEnv = new Environment(fun.getFunExp().getParameters(), values, fun.getClosingEnv());
        return fun.getFunExp().getBody().visit(this, newEnv);    
    }

    @Override
    public FnPlotValue<?> visitExpAdd(ExpAdd exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = exp.getExpL().visit(this, arg);
	val2 = exp.getExpR().visit(this, arg);
	return val1.add(val2);
    }

    @Override
    public FnPlotValue<?> visitExpSub(ExpSub exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = exp.getExpL().visit(this, arg);
	val2 = exp.getExpR().visit(this, arg);
	return val1.sub(val2);
    }

    @Override
    public FnPlotValue<?> visitExpMul(ExpMul exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
	val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
	return val1.mul(val2);
    }

    @Override
    public FnPlotValue<?> visitExpPow(ExpPow exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
	val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
	return val1.pow(val2);
    }

    @Override
    public FnPlotValue<?> visitExpDiv(ExpDiv exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
	val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
	return val1.div(val2);
    }

    @Override
    public FnPlotValue<?> visitExpMod(ExpMod exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	FnPlotValue<?> val1, val2;
	val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
	val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
	return val1.mod(val2);
    }

    @Override
    public FnPlotValue<?> visitExpLit(ExpLit exp, Environment<FnPlotValue<?>> arg)
	throws FnPlotException {
	return exp.getVal();
    }

    @Override
    public FnPlotValue<?> visitExpVar(ExpVar exp, Environment<FnPlotValue<?>> env)
	throws FnPlotException {
	return env.get(exp.getVar());
    }
}
