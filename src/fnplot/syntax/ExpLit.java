package fnplot.syntax;

import fnplot.semantics.Visitor;
import fnplot.sys.FnPlotException;
import fnplot.values.FnPlotValue;

/**
 * Class to represent numerical literals in the AST.
 * @author newts
 */
public class ExpLit extends Exp {

    FnPlotValue<?> val;
    
    public ExpLit(FnPlotValue<?> v) {
        val = v;
    }

    public ExpLit(Integer v) {
        val = FnPlotValue.make(v);
    }
    
    public ExpLit(Double v) {
        val = FnPlotValue.make(v);
    }

    public FnPlotValue<?> getVal() {
        return val;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws FnPlotException {
        return v.visitExpLit(this, arg);
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
