package fnplot.syntax;

import fnplot.semantics.Visitor;
import fnplot.sys.FnPlotException;

public class ExpVar extends Exp {

    String var;

    public ExpVar(String id) {
	var = id;
    }

    public String getVar() {
	return var;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg) throws FnPlotException {
	return v.visitExpVar(this, arg);
    }

    @Override
    public String toString() {
	return var;
    }
}
