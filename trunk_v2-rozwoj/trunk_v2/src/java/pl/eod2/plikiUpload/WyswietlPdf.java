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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod2.encje.DcPlik;
import pl.eod2.encje.DcPlikJpaController;

@ManagedBean(name = "WyswietlPdf")
@SessionScoped
public class WyswietlPdf {

    private DcPlikJpaController dcPlikC;

    public void paintFota(OutputStream stream, Object object) {
        dcPlikC = new DcPlikJpaController();
        DcPlik plikW = dcPlikC.findDcPlik((int) object);
        try {
            stream.write(plikW.getPlik());
        } catch (IOException ex) {
            ex.printStackTrace();//test 
            Logger.getLogger(WyswietlPdf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }
}
