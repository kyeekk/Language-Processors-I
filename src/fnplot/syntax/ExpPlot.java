package fnplot.syntax;

import fnplot.semantics.Visitor;
import fnplot.sys.FnPlotException;
import fnplot.values.FnPlotValue;

import java.util.ArrayList;

/**
 *
 * @author newts
 */
public class ExpPlot  extends Exp {
    
    Exp map;
    String item;
    Double start;
    Double end;

    public ExpPlot() {
        super();
    }

    public ExpPlot(Exp map, String item, FnPlotValue start, FnPlotValue end) throws FnPlotException{
        this.map = map;
        this.item = item;
        this.start = start.doubleValue();
        this.end = end.doubleValue();
    }

    /**
     * @return the map
     */
    public Exp getMap() {
        return map;
    }

    /**
     * @return the item
     */
    public String getItem() {
        return item;
    }

    /**
     * @return the start
     */
    public Double getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public Double getEnd() {
        return end;
    }
    
    @Override
    public <S, T> T visit(Visitor<S, T> v, S state) throws FnPlotException {
        return v.visitFunPlot(this, state);
    }

    @Override
    public String toString() {
        return String.format("(plot (%s) for %s in [%s : %s])", this.map, this.item, this.start, this.end);
    }

}