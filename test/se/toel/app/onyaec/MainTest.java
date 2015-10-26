/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.toel.app.onyaec;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import se.toel.util.FileUtils;

/**
 *
 * @author toel
 */
public class MainTest {
    
    public MainTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testTxt2xls() {
        
        System.out.println("txt2xls");
        String[] args = {"data/samples/ELANDERS_DE_CustomerChangesEnrichmentKP17_2015-10-13.txt", "build/test/ELANDERS_DE_CustomerChangesEnrichmentKP17_2015-10-13.xls"};
        Main.main(args);
        
        assert(FileUtils.checkCanReadFile(args[1]));
        
        
    }
    
    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testTxt2xlsx() {
        
        System.out.println("txt2xlsx");
        String[] args = {"data/samples/ELANDERS_DE_CustomerChangesEnrichmentKP17_2015-10-13.txt", "build/test/ELANDERS_DE_CustomerChangesEnrichmentKP17_2015-10-13.xlsx"};
        Main.main(args);
        
        assert(FileUtils.checkCanReadFile(args[1]));
        
        
    }
    
}
