/*
 * Write 
 */
package se.toel.app.onyaec.impl;

import java.io.BufferedWriter;
import se.toel.app.onyaec.WriterIF;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class TextWriter extends Common implements WriterIF {

    /***************************************************************************
     * Variables
     **************************************************************************/
    private BufferedWriter bw;
    private final String separator, eol;
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public TextWriter(IniFile ini, Map<String, String> conf) {
        this.ini = ini;
        this.conf = conf;
        separator = getSeparator(true);
        eol = getEndOfLine(true);
    }
    
    /***************************************************************************
     * Public methods
     **************************************************************************/
    
    @Override
    public void open(String filepath) throws Exception {
        
        String encoding = getEncoding(true);
        Writer osw = new OutputStreamWriter(new FileOutputStream(filepath), encoding);
        bw = new BufferedWriter(osw);
          
    }

    @Override
    public void setLineValues(String[] values) throws Exception {
        
        int n=0;
        for (String value : values) {
            if (n++>0) bw.write(separator);
            bw.write(value);
        }
        bw.write(eol);
    }

    @Override
    public void close() throws IOException {
        
        bw.close();
        
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
}
