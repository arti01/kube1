/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.plikiUpload;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@ManagedBean
@SessionScoped
public class Ocr {
    Tesseract instance;
    
    @PostConstruct
    private void init(){
        ImageIO.scanForPlugins();
        //System.err.println("tttttttttteeeeeeeeeeeeeeeocr"+imageFile.exists());
        instance = new Tesseract(); // JNA Direct Mapping
        String tessdata=Tesseract.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        tessdata=tessdata.replace("tess4j-3.0.jar", "");
        tessdata=tessdata.replaceFirst("/","");
        System.err.println(tessdata);
        instance.setDatapath(tessdata);
        instance.setLanguage("pol");
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
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println("eeeeeeeeeeeeeeeocr"+e.getMessage());
        }
        return result;
    }
    
}
