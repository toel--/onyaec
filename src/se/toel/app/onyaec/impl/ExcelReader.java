/*
 * Text reader class
 */
package se.toel.app.onyaec.impl;

import se.toel.app.onyaec.ReaderIF;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.toel.util.IniFile;

/**
 *
 * @author toel
 */
public class ExcelReader extends Common implements ReaderIF {

    /***************************************************************************
     * Variables
     **************************************************************************/
    private int rowCount, rowIndex=0;
    private int colCount, colIndex=0;
    Workbook wb = null;
    private Sheet sheet = null;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /***************************************************************************
     * Constructor
     **************************************************************************/
    /**
     * @param ini
     * @param conf */
    public ExcelReader(IniFile ini, Map<String, String> conf) {
        this.ini = ini;
        this.conf = conf;
    }
    
    /***************************************************************************
     * Public methods
     **************************************************************************/
    
    /**
     * @param filepath
     * @throws java.lang.Exception */
    @Override
    public void open(String filepath) throws Exception {
        
        try (FileInputStream fis = new FileInputStream(filepath)) {
            if (filepath.toLowerCase().endsWith("xls")) {
                wb = new HSSFWorkbook(fis);
            } else if (filepath.toLowerCase().endsWith("xlsx")) {
                wb = new XSSFWorkbook(fis);
            }
        }

        if (wb==null) {
            throw new Exception("The file "+filepath+" is not an Excel file.");
        }

        // Sheets names tends to include spaces, index then for easier use
        Map<String, Sheet> sheets = new HashMap<>();
        for (int i=wb.getNumberOfSheets(); i>0; i--) {
            sheet = wb.getSheetAt(i-1);
            String name = sheet.getSheetName().trim();
            sheets.put(name, sheet);
        }
        
        String sheetName = getConfigValue("Excel", "sheet", null);
        if (sheetName!=null && !sheetName.isEmpty()) {
            if (sheets.containsKey(sheetName)) {
                sheet = sheets.get(sheetName);
            } else {
                throw new Exception("Sheet '"+sheetName+"' not found");
            }
        }
        
        rowCount = sheet.getPhysicalNumberOfRows();
        
    }
    
    
    @Override
    public String[] getLineValues() throws Exception {
        
        if (rowIndex==rowCount) return null;
        
        Row row = sheet.getRow(rowIndex);
        Cell cell;
        if (rowIndex==0) {
            // Compute the rowCount
            colCount=0;
            cell = row.getCell(colCount);
            String value = getCellValue(cell);
            while (value.length()>0) {                                             // Assume title rows has all columns filled: Avoid empty columns at the end of the table
                colCount++;
                cell = row.getCell(colCount);
                value = getCellValue(cell);
            }
        }
        
        String[] values = new String[colCount];
        for (colIndex=0; colIndex<colCount; colIndex++) {
            cell = row.getCell(colIndex);
            String value = getCellValue(cell);
            values[colIndex]=value;
        }
        
        rowIndex++;
        return values;
        
    }
    
    
    @Override
    public void close() throws IOException {
        wb.close();
    }
    
    /***************************************************************************
     * Private methods
     **************************************************************************/
    /** Get the cell value as a string with the expected formating */
    private String getCellValue(Cell cell) {
        
        String value = "";
        
        if (cell==null) return(value);
        
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if(DateUtil.isCellDateFormatted(cell)) {
                    value = sdf.format(cell.getDateCellValue());
                    value = value.replace(" 00:00:00", "");
                } else {
                    double d = cell.getNumericCellValue();
                    value = String.valueOf(d);
                    if (value.endsWith(".0")) {
                        value = value.substring(0, value.length()-2);
                    } else {
                        int p = value.indexOf("E");
                        if (p>0) {
                            Double v = Double.valueOf(value.substring(0,p));
                            int e = Integer.valueOf(value.substring(p+1));
                            value = String.valueOf(Math.round(v*(Math.pow(10, e))));
                        }
                    }
                    // Force format # ###
                    //if (value.length()>2) {
                    //    StringBuilder sb = new StringBuilder();
                    //    int l = value.length();
                    //    for (int i=0; i<l; i++) {
                    //        if (((l-i) % 3)==0) sb.append(" ");
                    //        sb.append(value.charAt(i));
                    //    }
                    //    value = sb.toString();
                    //}
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                value = cell.toString();
                //cell.getCellFormula();
                break;
        }
                
        return value;
    }
    
    
}
