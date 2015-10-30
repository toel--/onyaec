/*
 * Write 
 */
package se.toel.app.onyaec.impl;

import se.toel.app.onyaec.WriterIF;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.toel.util.Dev;
import se.toel.util.FileUtils;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class ExcelWriter extends Common implements WriterIF {

    /***************************************************************************
     * Variables
     **************************************************************************/
    private String filepath;
    private XSSFWorkbook wb;
    private XSSFCellStyle titleStyle;
    private XSSFSheet sheet;
    int line=0;
    int nbCols=0;
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public ExcelWriter(IniFile ini, Map<String, String> conf) {
        this.ini = ini;
        this.conf = conf;
    }
    
    /***************************************************************************
     * Public methods
     **************************************************************************/
    
    @Override
    public void open(String filepath) throws Exception {
        
        this.filepath = filepath;
        String author   = ini.getValue("Excel", "Author", "Toël Hartmann");
        String keywords = ini.getValue("Excel", "Keywords", "");
        
        wb = new XSSFWorkbook();
        // Set some properties 
        POIXMLProperties xmlProps = wb.getProperties(); 
        if (xmlProps != null) { 

            // ..and from that. it's core peoperties 
            POIXMLProperties.CoreProperties coreProps =  xmlProps.getCoreProperties(); 
            if(coreProps!= null) { 
                coreProps.setCreator(author);
                coreProps.setKeywords(keywords);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                coreProps.setCreated(sdf.format(new Date()));
            } 
        }
        
        // Create the cell style for column titles
        titleStyle = wb.createCellStyle();
        XSSFFont font = wb.createFont();
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(font);

        String sheetName = getConfigValue("Excel", "sheet", null);
        if (sheetName==null || sheetName.isEmpty()) sheetName=FileUtils.getFileNameWithoutExtention(filepath);
        sheet = wb.createSheet(sheetName);
        if (getConfigValue("Excel", "freezeFirstRow", "true").equalsIgnoreCase("true")) {
            sheet.createFreezePane(0,1);
        }
        
    }

    @Override
    public void setLineValues(String[] values) throws Exception {
        
        XSSFCell cell;
        int x = 0;
        
        if (values.length>nbCols) nbCols=values.length;
        boolean firstRow = (line==0);
        boolean bold = getConfigValue("Excel", "boldFirstRow", "true").equalsIgnoreCase("true");
        x=0;
        XSSFRow xssfrow = sheet.createRow(line++);
        for (String value : values) {
            cell = xssfrow.createCell(x++);
            cell.setCellValue(value);
            if (firstRow && bold) cell.setCellStyle(titleStyle);
        }
            
    }

    @Override
    public void close() throws IOException {
        
        // Resize columns
        for (int i=0; i<nbCols; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Save the Excel file
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            wb.write(fos);
        } catch (Exception e) {
            Dev.error("While saving", e);
        }
        
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
}
