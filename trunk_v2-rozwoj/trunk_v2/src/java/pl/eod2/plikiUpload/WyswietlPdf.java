/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.plikiUpload;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jws.soap.InitParam;
import pl.eod2.encje.DcPlik;

@ManagedBean(name = "WyswietlPdf")
@SessionScoped
public class WyswietlPdf {
    public DcPlik plik;

    public WyswietlPdf() {
        System.out.println("cosnt");
    }
    
    @PostConstruct
    public void ttt(){
        System.out.println("sposts");
    }
    
    public void paintFota(OutputStream stream, Object object) {
        System.out.println("ssssssssssssssssssssssss");
        plik=(DcPlik) object;
        try {
            System.out.println(plik.getNazwa()+plik.getId());
            stream.write(plik.getPlik());
            System.out.println(plik.getNazwa());
        } catch (IOException ex) {
            System.out.println(plik.getNazwa()+plik.getDataDodania());
            Logger.getLogger(WyswietlPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DcPlik getPlik() {
        return plik;
    }

    public void setPlik(DcPlik plik) {
        this.plik = plik;
    }
    
    
}
