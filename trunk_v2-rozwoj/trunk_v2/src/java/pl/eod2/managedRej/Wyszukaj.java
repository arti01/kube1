/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRej;

import java.text.ParseException;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod2.encje.DcDokument;

@ManagedBean(name = "WyszukajRej")
@SessionScoped
public class Wyszukaj extends Rejestracja  {
    public Date dataRejOd;
    public Date dataRejDo;
    public Date dataDokOd;
    public Date dataDokDo;

    public Wyszukaj() throws InstantiationException, IllegalAccessException {
    }
    

    public String form() {
        obiekt=new DcDokument();
        //obiekt.getRodzajId()
        return "/dcrej/wyszukForm?faces-redirect=true";
    }
    
    public String wyszukaj() throws ParseException{
        lista.setWrappedData(dcC.findByExample(obiekt, dataRejOd, dataRejDo, dataDokOd, dataDokDo));
        return "/dcrej/wyszukList?faces-redirect=true";
    }

    public Date getDataRejOd() {
        return dataRejOd;
    }

    public void setDataRejOd(Date dataRejOd) {
        this.dataRejOd = dataRejOd;
    }

    public Date getDataRejDo() {
        return dataRejDo;
    }

    public void setDataRejDo(Date dataRejDo) {
        this.dataRejDo = dataRejDo;
    }

    public Date getDataDokOd() {
        return dataDokOd;
    }

    public void setDataDokOd(Date dataDokOd) {
        this.dataDokOd = dataDokOd;
    }

    public Date getDataDokDo() {
        return dataDokDo;
    }

    public void setDataDokDo(Date dataDokDo) {
        this.dataDokDo = dataDokDo;
    }

}
