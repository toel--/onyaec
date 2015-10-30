/*
 * Oh no, yet another Excel converter (onyaec)
 * converts from Excel to txt format and txt to Excel
 */
package se.toel.app.onyaec;

import java.util.HashMap;
import java.util.Map;
import se.toel.app.onyaec.impl.Excel97Writer;
import se.toel.app.onyaec.impl.ExcelReader;
import se.toel.app.onyaec.impl.ExcelWriter;
import se.toel.app.onyaec.impl.TextReader;
import se.toel.app.onyaec.impl.TextWriter;
import se.toel.util.Closer;
import se.toel.util.Dev;
import se.toel.util.FileUtils;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class Main {

    private static final String ver="1.0.0";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        IniFile ini = new IniFile("config.ini");
        Map<String, String> conf = new HashMap<>();
        
        if (args.length<2) {
            showUsage();
        } else {
            
            String src = null;
            String dst = null;
            
            for (int i=0; i<args.length; i++) {
                
                String s = args[i];
                if (s.startsWith("-")) {
                    String[] ss = s.split(":");
                    if (ss.length==2) {
                        String key=ss[0].substring(1);
                        String value=ss[1];
                        if (conf.containsKey(key)) {
                            key += "_2";
                        }
                        conf.put(key, value);
                    }
                } else {
                    if (s.contains(".")) {
                        if (src==null) src=s; else dst=s;
                    }
                }
            }
            
            if (!FileUtils.checkCanReadFile(src)) {
                abort("Can not find source file "+src);
            }
            if (!FileUtils.checkPathToFileExists(dst)) {
                abort("Can not find target directory for destination file "+dst);
            }
            
            
            
            String from = FileUtils.getFileNameExtention(src.toLowerCase());
            String to = FileUtils.getFileNameExtention(dst.toLowerCase());
            conf.put("src", src);
            conf.put("dst", dst);
            
            ReaderIF reader = null;
            WriterIF writer = null;
            
            switch (from) {
                case "txt": reader = new TextReader(ini, conf); break;
                case "xls": case "xlsx": reader = new ExcelReader(ini, conf); break;
                default: abort("source file type '"+from+"' not supported");
            }
            
            switch (to) {
                case "xls": writer = new Excel97Writer(ini, conf); break;
                case "xlsx": writer = new ExcelWriter(ini, conf); break;
                case "txt": writer = new TextWriter(ini, conf); break;
                default: abort("destination file type "+to+" not supported");
            }
            
            try {
                if (reader!=null && writer!=null) {
                    reader.open(src);
                    writer.open(dst);
                    String[] values = reader.getLineValues();
                    while (values!=null) {
                        writer.setLineValues(values);
                        values = reader.getLineValues();
                    }

                }
            } catch (Exception e) {
                Dev.error("While converting", e);
            }
            
            Closer.close(reader);
            Closer.close(writer);
            
        }
        
        ini.save();
    }
    
    private static void showUsage() {
        
        System.out.println("onyaec (Oh no! Yet another Excel converter) ver "+ver);
        System.out.println("  Toël Hartmann 2015");
        System.out.println("  Syntax:");
        System.out.println("    java -jar onyaec [params] [src] [dst]");
        System.out.println("  where:");
        System.out.println("    [params] to override the value in the config.ini file. ex: -encoding:UTF-8 -boldFirstRow:true -freezeFirstRow:true");
        System.out.println("    [src] the source file");
        System.out.println("    [dst] the destination file");
        System.exit(1);
        
    }
    
    private static void abort(String msg) {
        
        System.out.println("Error: "+msg);
        System.out.println("");
        showUsage();
        
    }
    
    
    
}
