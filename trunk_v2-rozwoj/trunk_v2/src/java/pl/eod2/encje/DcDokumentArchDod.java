/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import pl.eod.encje.Uzytkownik;

@Embeddable
public class DcDokumentArchDod implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="data_wyp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataWypozycz;
     //- data wypożyczenia
   
    @Column(name="data_pl_zwr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPlanZwrot;
    //- data planowanego zwrotu
   
    @Column(name="data_real_zwr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRzeczZwrot;
    //data_real_zwr - data rzeczywistego zwrotu

    @Column(name="imie_nazwisko")
    private String imieNazwisko="";
    //nazwisko_imie - nazwisko i imię osoby wypożyczającej
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uzytkownik wydal;
    //id użytkownika który jest archiwistą i wydał dany dokument

    public Date getDataWypozycz() {
        return dataWypozycz;
    }

    public void setDataWypozycz(Date dataWypozycz) {
        this.dataWypozycz = dataWypozycz;
    }

    public Date getDataPlanZwrot() {
        return dataPlanZwrot;
    }

    public void setDataPlanZwrot(Date dataPlanZwrot) {
        this.dataPlanZwrot = dataPlanZwrot;
    }

    public Date getDataRzeczZwrot() {
        return dataRzeczZwrot;
    }

    public void setDataRzeczZwrot(Date dataRzeczZwrot) {
        this.dataRzeczZwrot = dataRzeczZwrot;
    }

    public String getImieNazwisko() {
        return imieNazwisko;
    }

    public void setImieNazwisko(String imieNazwisko) {
        this.imieNazwisko = imieNazwisko;
    }

    public Uzytkownik getWydal() {
        return wydal;
    }

    public void setWydal(Uzytkownik wydal) {
        this.wydal = wydal;
    }

}
