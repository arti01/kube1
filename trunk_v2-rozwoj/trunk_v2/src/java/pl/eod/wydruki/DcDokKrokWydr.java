/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.wydruki;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import pl.eod2.encje.DcDokumentKrok;
import pl.eod2.encje.DcDokumentKrokUzytkownik;

public class DcDokKrokWydr {

    int id;
    int lp;
    String typNazwa;
    String status;
    List<DcDokKrokUserWydr> krokiUserList = new ArrayList<>();

    DcDokKrokWydr(DcDokumentKrok krok) {
        id = krok.getId();
        lp = krok.getLp();
        typNazwa = krok.getDcAckeptTypKroku().getNazwa();
        status = krok.getAkcept().getNazwa();
        for (DcDokumentKrokUzytkownik krokUser : krok.getDcKrokUzytkownikaList()) {
            DcDokKrokUserWydr w = new DcDokKrokUserWydr(krokUser);
            krokiUserList.add(w);
        }
    }

    DcDokKrokWydr() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public String getTypNazwa() {
        return typNazwa;
    }

    public void setTypNazwa(String typNazwa) {
        this.typNazwa = typNazwa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlElementWrapper(name = "KrokiUserList")
    @XmlElement(name = "krokUser")
    public List<DcDokKrokUserWydr> getKrokiUserList() {
        return krokiUserList;
    }
}
