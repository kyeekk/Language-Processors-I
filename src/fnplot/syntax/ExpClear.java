package fnplot.syntax;

import fnplot.sys.FnPlotException;
import fnplot.semantics.Visitor;

public class ExpClear extends Exp{

    public ExpClear() {}

    @Override
    public <S, T> T visit(Visitor<S, T> v, S state) throws FnPlotException {
        return v.visitClear(this, state);
    }

    @Override
    public String toString() {
        return "Clear graph";
    }

    
}