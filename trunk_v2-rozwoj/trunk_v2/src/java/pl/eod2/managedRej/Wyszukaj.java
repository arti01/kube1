/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRej;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcPlik;

@ManagedBean(name = "WyszukajRej")
@SessionScoped
public class Wyszukaj extends Rejestracja {

    public Date dataRejOd;
    public Date dataRejDo;
    public Date dataDokOd;
    public Date dataDokDo;

    @ManagedProperty(value = "#{RejestracjaRej}")
    private Rejestracja rej;

    public Wyszukaj() throws InstantiationException, IllegalAccessException {
    }

    public String form() {
        obiekt = new DcDokument();
        //obiekt.getRodzajId()
        return "/dcrej/wyszukForm?faces-redirect=true";
    }

    class DokRozsz extends DcDokument{
        public String info;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
        
    }
    
    public String wyszukaj() throws ParseException {
        rej.refreshObiekt();
        List<DokRozsz> wynikLi = new ArrayList();
        wynik.addAll(dcC.findByExample(obiekt, dataRejOd, dataRejDo, dataDokOd, dataDokDo));

        DcPlik plik = new DcPlik();
        plik.setTresc(obiekt.getOpis());
        for (DcPlik plikW : dcPlikC.findByExample(plik)) {
            wynik.add(plikW.getIdDok());
        }
        lista.setWrappedData(wynik);

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

    public Rejestracja getRej() {
        return rej;
    }

    public void setRej(Rejestracja rej) {
        this.rej = rej;
    }

}
