/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fnplot.values;

import fnplot.sys.FnPlotException;
import static fnplot.values.FnPlotValue.make;

/**
 *
 * @author newts
 * Created on 14-Nov-2016
 */
public class FnPlotInt extends FnPlotValue<FnPlotInt> {
    
    int value;

    public FnPlotInt() {
        this(0);
    }

    public FnPlotInt(Integer v) {
        value = v;
    }
    
    @Override
    public FnPlotType getType() {
        return FnPlotType.INTEGER;
    }
    
    @Override
    public FnPlotInt add(FnPlotValue<?> arg) throws FnPlotException {
        return make(value + arg.intValue());
    }

    /**
     * Subtract the given value from this value.
     * @param arg The value to be subtracted
     * @return The difference as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotInt sub(FnPlotValue<?> arg) throws FnPlotException {
        return make(value - arg.intValue());
    }

    /**
     * Multiply the given value by this value.
     * @param arg The multiplicand
     * @return The product as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotInt mul(FnPlotValue<?> arg) throws FnPlotException {
        return make(value * arg.intValue());
    }

    /**
     * Divide the given value by this value.
     * @param arg The divisor
     * @return The quotient as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotInt div(FnPlotValue<?> arg) throws FnPlotException {
        return make(value / arg.intValue());
    }

    /**
     * Compute the remainder of dividing this value by the given value.
     * @param arg The divisor
     * @return The residue modulo arg as a new instance of FnPlotValue
     * @throws fnplot.values.TypeFnPlotException
     */
    @Override
    public FnPlotInt mod(FnPlotValue<?> arg) throws FnPlotException {
        if (arg.isInteger()) {
            return make(value % arg.intValue());
        } else {
            return make(value % arg.intValue());
        }
    }

    /**
     * Compute the power of raising this value by the given value.
     * @param arg The power
     * @return The residue power arg as a new instance of FnPlotValue
     * @throws fnplot.values.TypeFnPlotException
     */
    @Override
    public FnPlotInt pow(FnPlotValue<?> arg) throws FnPlotException {
        if (arg.isInteger()) {
            return make((int)Math.pow(value , arg.intValue()));
        } else {
            return make((int)Math.pow(value , arg.intValue()));
        }
    }
    
    @Override
    public int intValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
