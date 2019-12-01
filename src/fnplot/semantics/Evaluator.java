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
import fnplot.syntax.ExpPlot;
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
    public void setPlotter(final Plotter plotter) {
        this.plotter = plotter;
    }

    /**
     * Visit a node representing the overall program. This will be similar to
     * visiting the sequence of statements that make up the program, but is provided
     * as a separate method so that any top-level, one-time actions can be taken to
     * initialise the context for the program, if necessary.
     * 
     * @param p   The program node to be traversed.
     * @param arg The environment to be used while traversing the program.
     * @return The result of the last statement of the program, after evaluating all
     *         the preceding ones in order.
     * @throws FnPlotException if any of the statements in the body of the program
     *                         throws an exception.
     */
    @Override
    public FnPlotValue<?> visitArithProgram(final ArithProgram p, final Environment<FnPlotValue<?>> arg)
            throws FnPlotException {
        result = p.getSeq().visit(this, arg);
        return result;
    }

    @Override
    public FnPlotValue<?> visitStmtSequence(final StmtSequence sseq, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        final ArrayList<Statement> seq = sseq.getSeq();
        final Iterator<Statement> iter = seq.iterator();
        result = FnPlotValue.make(0); // default result
        for (final Statement s : seq) {
            result = s.visit(this, env);
        }
        // return last value evaluated
        return result;
    }

    @Override
    public FnPlotValue<?> visitStmtDefinition(final StmtDefinition sd, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        result = sd.getExp().visit(this, env);
        env.put(sd.getVar(), result);
        return result;
    }

    @Override
    public FnPlotValue<?> visitStmtLet(final StmtLet let, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        final ArrayList<Binding> bindings = let.getBindings();
        final Exp body = let.getBody();

        final int size = bindings.size();
        final String[] vars = new String[size];
        final FnPlotValue<?>[] vals = new FnPlotValue<?>[size];
        Binding b;
        for (int i = 0; i < size; i++) {
            b = bindings.get(i);
            vars[i] = b.getVar();
            // evaluate each expression in bindings
            result = b.getValExp().visit(this, env);
            vals[i] = result;
        }
        // create new env as child of current
        final Environment<FnPlotValue<?>> newEnv = new Environment<>(vars, vals, env);
        return body.visit(this, newEnv);
    }

    @Override
    public FnPlotValue<?> visitFunDefn(final ExpFunction defn, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        final FnPlotFunction c = new FnPlotFunction(defn, env);
        return c;
    }

    @Override
    public FnPlotValue<?> visitFunCall(final ExpFunCall callExp, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        final String name = callExp.getName();
        final ArrayList<Exp> args = callExp.getArguments();
        final FnPlotFunction fun = (FnPlotFunction) env.get(name);
        final ArrayList<FnPlotValue> values = new ArrayList<>();
        for (final Exp funarg : args) {
            values.add(funarg.visit(this, env));
        }
        final Environment newEnv = new Environment(fun.getFunExp().getParameters(), values, fun.getClosingEnv());
        return fun.getFunExp().getBody().visit(this, newEnv);
    }

    @Override
    public FnPlotValue<?> visitFunPlot(final ExpPlot exp, final Environment<FnPlotValue<?>> env)
            throws FnPlotException {
        final String id = exp.getItem();
        final Double start = exp.getStart();
        final Double end = exp.getEnd();
        final Exp fun = exp.getMap();
        final double[] xpoints = plotter.sample(start, end);
        final Point2D[] ypoints = new Point2D[xpoints.length];

        final Environment<FnPlotValue<?>> newEnv = new Environment(new ArrayList<>(), new ArrayList<>(), env);
        FnPlotValue y;
        for (int x = 0; x < xpoints.length; x++) {
            newEnv.put(id, FnPlotValue.make(xpoints[x]));
            y = fun.visit(this, newEnv);
            ypoints[x] = new Point2D.Double(xpoints[x], y.doubleValue());
        }

        this.plotter.plot(ypoints);
        return null;
    }

    @Override
    public FnPlotValue<?> visitClear(final ExpClear exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        this.plotter.clear();
        return exp.visit(this, env);       
    }



    @Override
    public FnPlotValue<?> visitExpAdd(final ExpAdd exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = exp.getExpL().visit(this, arg);
        val2 = exp.getExpR().visit(this, arg);
        return val1.add(val2);
    }

    @Override
    public FnPlotValue<?> visitExpSub(final ExpSub exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = exp.getExpL().visit(this, arg);
        val2 = exp.getExpR().visit(this, arg);
        return val1.sub(val2);
    }

    @Override
    public FnPlotValue<?> visitExpMul(final ExpMul exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
        val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
        return val1.mul(val2);
    }

    @Override
    public FnPlotValue<?> visitExpPow(final ExpPow exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
        val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
        return val1.pow(val2);
    }

    @Override
    public FnPlotValue<?> visitExpDiv(final ExpDiv exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
        val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
        return val1.div(val2);
    }

    @Override
    public FnPlotValue<?> visitExpMod(final ExpMod exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        FnPlotValue<?> val1, val2;
        val1 = (FnPlotValue) exp.getExpL().visit(this, arg);
        val2 = (FnPlotValue) exp.getExpR().visit(this, arg);
        return val1.mod(val2);
    }

    @Override
    public FnPlotValue<?> visitExpLit(final ExpLit exp, final Environment<FnPlotValue<?>> arg) throws FnPlotException {
        return exp.getVal();
    }

    @Override
    public FnPlotValue<?> visitExpVar(final ExpVar exp, final Environment<FnPlotValue<?>> env)
	throws FnPlotException {
	return env.get(exp.getVar());
    }
}
