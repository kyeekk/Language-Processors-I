/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fnplot.values;

import fnplot.sys.FnPlotException;

/**
 *
 * @author newts
 * Created on 14-Oct-2016
 */
public class FnPlotReal extends FnPlotValue<FnPlotReal> {
    
    double value;

    public FnPlotReal() {
        this(0D);
    }

    public FnPlotReal(Double v) {
        value = v;
    }
    
    @Override
    public FnPlotType getType() {
        return FnPlotType.REAL;
    }
    
    @Override
    public FnPlotReal add(FnPlotValue<?> arg) throws FnPlotException {
        return make(value + arg.doubleValue());
    }

    /**
     * Subtract the given value from this value.
     * @param arg The value to be subtracted
     * @return The difference as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotReal sub(FnPlotValue<?> arg) throws FnPlotException {
        return make(value - arg.doubleValue());
    }

    /**
     * Multiply the given value by this value.
     * @param arg The multiplicand
     * @return The product as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotReal mul(FnPlotValue<?> arg) throws FnPlotException {
        return make(value * arg.doubleValue());
    }

    /**
     * Divide the given value by this value.
     * @param arg The divisor
     * @return The quotient as a new instance of FnPlotValue
     * @throws fnplot.sys.FnPlotException
     */
    @Override
    public FnPlotReal div(FnPlotValue<?> arg) throws FnPlotException {
        return make(value / arg.doubleValue());
    }

    /**
     * Compute the remainder of dividing this value by the given value.
     * @param arg The divisor
     * @return The residue modulo arg as a new instance of FnPlotValue
     * @throws fnplot.values.TypeFnPlotException
     */
    @Override
    public FnPlotReal mod(FnPlotValue<?> arg) throws FnPlotException {
        if (arg.isInteger()) {
            return make(value % arg.intValue());
        } else {
            return make(value % arg.doubleValue());
        }
    }
    @Override
    public FnPlotReal pow(FnPlotValue<?> arg) throws FnPlotException {
        return make(Math.pow(value , arg.doubleValue()));
    }
    
    @Override
    public int intValue() {
        return (int) value;
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
