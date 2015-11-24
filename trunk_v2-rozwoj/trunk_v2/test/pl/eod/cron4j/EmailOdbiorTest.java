/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedProperty;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.eod2.encje.DcPlikImport;
import pl.eod2.encje.DcPlikImportJpaController;

/**
 *
 * @author 103039
 */
public class EmailOdbiorTest {
    @ManagedProperty(value = "#{EmailOdbior}")
    EmailOdbior emailOdb;
    
    public EmailOdbiorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.err.println("@BeforeClass");
        //tworzenie fikcyjnych emaili
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

    @Test
    public void testAutoObsluga() throws Exception {
        DcPlikImportJpaController dcpiC = new DcPlikImportJpaController();
        
        for(DcPlikImport pi: dcpiC.findDcPlikImportEntities()){
            EmailMoj em=new EmailMoj();
            em.setNadawca("sssss");
            em.setTemat(pi.getNazwa()+"  temat");
            em.setZalaczniki(new ArrayList<EmailZalacznik>());
            EmailZalacznik ez=new EmailZalacznik();
            ez.setNazwa(pi.getNazwa());
            ez.setPlik(pi.getDcPlikImportBin().getPlik());
            em.getZalaczniki().add(ez);
            this.emailOdb.getEmaile().add(em);
        }
    }
        /*System.out.println("autoObsluga");
        EmailOdbior instance = new EmailOdbior();
        instance.autoObsluga();
        fail("The test case is a prototype.");
    }

    @Test
    public void testGetEmaile() {
        System.out.println("getEmaile");
        EmailOdbior instance = new EmailOdbior();
        List<EmailMoj> expResult = null;
        List<EmailMoj> result = instance.getEmaile();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    @Test
    public void testSetEmaile() {
        System.out.println("setEmaile");
        List<EmailMoj> emaile = null;
        EmailOdbior instance = new EmailOdbior();
        instance.setEmaile(emaile);
        fail("The test case is a prototype.");
    }*/
    
}
