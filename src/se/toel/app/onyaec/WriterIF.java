/*
 * Writer interface
 */
package se.toel.app.onyaec;

import java.io.Closeable;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author toel
 */
public interface WriterIF extends Closeable {
    
    void open(String filepath) throws Exception;
    void setLineValues(String[] values) throws Exception;
    Workbook getWorkbook();
    
}
