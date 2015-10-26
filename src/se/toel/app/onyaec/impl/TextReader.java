/*
 * Text reader class
 */
package se.toel.app.onyaec.impl;

import se.toel.app.onyaec.ReaderIF;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class TextReader extends Common implements ReaderIF {

    private BufferedReader br;
    private String separator;
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    /**
     * @param ini
     * @param conf */
    public TextReader(IniFile ini, Map<String, String> conf) {
        this.ini = ini;
        this.conf = conf;
        separator = getSeparator();
    }
    
    /***************************************************************************
     * Public methods
     **************************************************************************/
    @Override
    public void open(String filepath) throws Exception {
        
        String encoding = getConfigValue("Text", "encoding", "UTF-8");
        Reader isr = new InputStreamReader(new FileInputStream(filepath), encoding);
        br = new BufferedReader(isr);
        
    }
    
    
    @Override
    public String[] getLineValues() throws Exception {
        
        String line = br.readLine();
        if (line==null) return null;
                
        String[] values = line.split(separator);
        return values;
        
    }
    
    
    @Override
    public void close() throws IOException {
        br.close();
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
    
    
}
