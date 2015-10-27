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
import se.toel.util.Closer;
import se.toel.util.Dev;
import se.toel.util.FileUtils;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        IniFile ini = new IniFile("config.ini");
        Map<String, String> conf = new HashMap<>();
        
        if (args.length<2) {
            showUsage();
        } else {
            
            String src = args[args.length-2];
            String dst = args[args.length-1];
            
            if (!FileUtils.checkCanReadFile(src)) {
                abort("Can not find source file "+src);
            }
            if (!FileUtils.checkPathToFileExists(dst)) {
                abort("Can not find target directory for destination file "+dst);
            }
            
            for (int i=0; i<args.length-2; i++) {
                
                String s = args[i];
                String[] ss = s.split(":");
                if (ss.length==2) {
                    conf.put(ss[0].substring(1), ss[1]);
                }
                
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
        
        System.out.println("onyaec (Oh no! Yet another Excel converter) ver 0.1.0");
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
