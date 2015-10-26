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
    
    protected String getSeparator() {
        String s = getConfigValue("Text", "separator", "tab");
        switch (s) {
            case "tab": s="\t"; break;
            case "comma": s=","; break;
            case "semicolon": s=";"; break;
        }
        return s;
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
    
    
    
}
