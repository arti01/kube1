/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.wydruki;

import java.text.SimpleDateFormat;
import pl.eod2.encje.DcDokumentKrokUzytkownik;

public class DcDokKrokUserWydr {

    int id;
    String fullname;
    String akcept;
    String data;
    String info;

    DcDokKrokUserWydr(DcDokumentKrokUzytkownik krokUser) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        id = krokUser.getId();
        fullname = krokUser.getIdUser().getFullname();
        akcept = krokUser.getAkcept().getNazwa();
        try {
            data = sdf.format(krokUser.getDataAkcept());
        } catch (NullPointerException ex) {
            data = "brak daty";
        }
        info = krokUser.getInformacja();
    }

    DcDokKrokUserWydr() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAkcept() {
        return akcept;
    }

    public void setAkcept(String akcept) {
        this.akcept = akcept;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
