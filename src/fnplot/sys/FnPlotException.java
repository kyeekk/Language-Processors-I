/*
 * FnPlotException.java
 * Created on 8-Nov-2016, 10:20:14 AM
 */

package fnplot.sys;

/**
 * Parent exception class for all exceptions that can be raised while processing
 * a FnPlot program.
 * 
 * @author newts
 */
public class FnPlotException extends Exception {

    private static final long serialVersionUID = 1L;

    public FnPlotException() {
        super("FnPlot Error");
    }
    
    public FnPlotException(String msg) {
        super(msg);
    }
    
    public FnPlotException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
