/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.plikiUpload;

import java.io.IOException;
import java.io.OutputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod2.encje.DcPlik;

@ManagedBean(name = "WyswietlPdf")
@SessionScoped
public class WyswietlPdf {
    public DcPlik plik;
    
    public void paintFota(OutputStream stream, Object object) throws IOException {
        news = new Newsy();
        news.setIdnewsy((Integer) object);
        news = newsyImp.find(news);
        stream.write(news.getFota());
    }

    public DcPlik getPlik() {
        return plik;
    }

    public void setPlik(DcPlik plik) {
        this.plik = plik;
    }
    
    
}
