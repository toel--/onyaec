/*
 * Write 
 */
package se.toel.app.onyaec.impl;

import se.toel.app.onyaec.WriterIF;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import se.toel.util.FileUtils;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class Excel97Writer extends Common implements WriterIF {

    /***************************************************************************
     * Variables
     **************************************************************************/
    private String filepath;
    private HSSFWorkbook wb;
    private HSSFCellStyle titleStyle;
    private HSSFSheet sheet;
    int line=0;
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    public Excel97Writer(IniFile ini, Map<String, String> conf) {
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
        
        wb = new HSSFWorkbook();
        // Set some properties
        wb.createInformationProperties();
        wb.getSummaryInformation().setAuthor(author);
        wb.getSummaryInformation().setKeywords(keywords);
        wb.getSummaryInformation().setCreateDateTime(new Date());

        // Create the cell style for column titles
        titleStyle = wb.createCellStyle();
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(font);

        String sheetName = getConfigValue("Excel", "sheet", null);
        if (sheetName==null) sheetName=FileUtils.getFileNameWithoutExtention(filepath);
        sheet = wb.createSheet(sheetName);
        if (getConfigValue("Excel", "freezeFirstRow", "true").equalsIgnoreCase("true")) {
            sheet.createFreezePane(0,1);
        }
        
    }

    @Override
    public void setLineValues(String[] values) throws Exception {
        
        HSSFCell cell;
        int x = 0;
        
        boolean firstRow = (line==0);
        boolean bold = getConfigValue("Excel", "boldFirstRow", "true").equalsIgnoreCase("true");
        x=0;
        HSSFRow hssfrow = sheet.createRow(line++);
        for (String value : values) {
            cell = hssfrow.createCell(x++);
            cell.setCellValue(value);
            if (firstRow && bold) cell.setCellStyle(titleStyle);
        }
            
    }

    @Override
    public void close() throws IOException {
        
        // Save the Excel file
        try (FileOutputStream fos = new FileOutputStream(filepath)) {
            wb.write(fos);
        }
        
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    
}
