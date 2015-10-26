/*
 * Writer interface
 */
package se.toel.app.onyaec;

import java.io.Closeable;

/**
 *
 * @author toel
 */
public interface WriterIF extends Closeable {
    
    void open(String filepath) throws Exception;
    void setLineValues(String[] values) throws Exception;
    
}
