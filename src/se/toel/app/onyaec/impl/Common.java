/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.toel.app.onyaec.impl;

import java.util.Map;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class Common {
    
    /***************************************************************************
     * Variables
     **************************************************************************/
    protected IniFile ini;
    protected Map<String, String> conf;
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    
    /***************************************************************************
     * Protected methods
     **************************************************************************/
    protected String getConfigValue(String section, String key, String defValue) {
        
        String value = ini.getValue(section, key, defValue);
        if (conf.containsKey(key)) value = conf.get(key);
        return value;
        
    }
    
    protected String getSeparator(boolean isWriter) {
        String key = "separator";
        String s = null;
        if (isWriter) {
            s = getConfigValue("Text", key+"_2", null);
        }
        if (s==null) s = getConfigValue("Text", key, "tab");
        switch (s) {
            case "tab": s="\t"; break;
            case "comma": s=","; break;
            case "semicolon": s=";"; break;
        }
        return s;
    }
    
    protected String getEndOfLine(boolean isWriter) {
        String key = "endOfLine";
        String s = null;
        if (isWriter) {
            s = getConfigValue("Text", key+"_2", null);
        }
        if (s==null) s = getConfigValue("Text", key, "CRLF");
        switch (s) {
            case "CRLF": s="\r\n"; break;
            case "LF": s="\n"; break;
        }
        return s;
    }
    
    protected String getEncoding(boolean isWriter) {
        String key = "encoding";
        String s = null;
        if (isWriter) {
            s = getConfigValue("Text", key+"_2", null);
        }
        if (s==null) s = getConfigValue("Text", key, "UTF-8");
        return s;
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
    
    
    
}
