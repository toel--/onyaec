/*
 * Reader interface
 */
package se.toel.app.onyaec;

import java.io.Closeable;

/**
 *
 * @author toel
 */
public interface ReaderIF extends Closeable {
    
    void open(String filepath) throws Exception;
    String[] getLineValues() throws Exception;
    
    
}
