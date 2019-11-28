package fnplot.syntax;

import fnplot.semantics.Visitor;
import fnplot.sys.FnPlotException;

public class ArithProgram extends Exp {
    StmtSequence seq;

    public ArithProgram(StmtSequence s) {
	seq = s;
    }

    public StmtSequence getSeq() {
	return seq;
    }

    @Override
    public <S, T> T visit(Visitor<S, T> v, S arg)
	throws FnPlotException {
	return v.visitArithProgram(this, arg);
    }

    @Override
    public String toString() {
	return seq.toString();
    }
}
