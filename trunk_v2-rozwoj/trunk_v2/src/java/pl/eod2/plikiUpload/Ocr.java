/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.plikiUpload;

import java.io.File;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;


public class Ocr {
    Tesseract instance;
    
    public Ocr(){
        ImageIO.scanForPlugins();
        //System.err.println("tttttttttteeeeeeeeeeeeeeeocr"+imageFile.exists());
        instance = new Tesseract(); // JNA Direct Mapping
        instance.setLanguage("pol");
        instance.setPageSegMode(3);
        instance.setHocr(true);
        //Tesseract instance = Tesseract.getInstance();  // JNA Interface Mapping
        //

        /*try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println("eeeeeeeeeeeeeeeocr"+e.getMessage());
        }*/
    }
    
    public String oceeruj(File file){
        String result="błąd OCR";
        try {
            result = instance.doOCR(file);
            //System.out.println(result);
        } catch (TesseractException e) {
            System.err.println("eeeeeeeeeeeeeeeocr"+e.getMessage());
        }
        return result;
    }
    
}
