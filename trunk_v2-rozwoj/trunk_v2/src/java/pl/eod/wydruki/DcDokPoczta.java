/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.wydruki;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import pl.eod.encje.Struktura;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentKrok;

@XmlRootElement(name = "DcDokPoczta")
public class DcDokPoczta {

    int id;
    String nazwa;
    String symbolDok;
    String dataDok;
    String rodzajNazwa;
    String kontrahentNazwa;
    String kontrahentAdres;
    String teczkaNazwa;
    String wprowadzil;
    String zrodloNazwa;
    String dataWydruku;
    private List<DcDokKrokWydr> dcDokKrokWydrList = new ArrayList<>();

    public DcDokPoczta(DcDokument doc) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        id = doc.getId();
        nazwa = doc.getNazwa();
        kontrahentNazwa = doc.getKontrahentId().getNazwa();
        kontrahentAdres = doc.getKontrahentId().getAdres();
        symbolDok = doc.getSymbolDok();
        dataDok=sdf.format(doc.getDataDok());
        rodzajNazwa=doc.getRodzajId().getNazwa();
        teczkaNazwa=doc.getTeczkaId().getNazwa();
        wprowadzil=doc.getUserId().getFullname();
        zrodloNazwa=doc.getZrodloId().getNazwa();

        List<DcDokumentKrok> krokiSort = doc.getDcDokKrok();

        Collections.sort(krokiSort, new Comparator<DcDokumentKrok>() {
            @Override
            public int compare(DcDokumentKrok o1, DcDokumentKrok o2) {
                int wynik;
                try {
                    if (o1.getLp() <= o2.getLp()) {
                        wynik = -1;
                    } else {
                        wynik = 1;
                    }
                } catch (NullPointerException mpe) {
                    wynik = 0;
                }
                return wynik;
            }
        });

        for (DcDokumentKrok krok : krokiSort) {
            DcDokKrokWydr dKr = new DcDokKrokWydr(krok);
            //System.err.println(krok.getLp());
            //System.err.println(dKr.getLp());
            dcDokKrokWydrList.add(dKr);
        }
        //dcDokKrokWydrList=doc.getDcDokKrok();
    }

    DcDokPoczta() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getKontrahentNazwa() {
        return kontrahentNazwa;
    }

    public void setKontrahentNazwa(String kontrahentNazwa) {
        this.kontrahentNazwa = kontrahentNazwa;
    }

    public String getKontrahentAdres() {
        return kontrahentAdres;
    }

    public void setKontrahentAdres(String kontrahentAdres) {
        this.kontrahentAdres = kontrahentAdres;
    }

    public String getDataWydruku() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date());
    }

    public void setDataWydruku(String dataWydruku) {
        this.dataWydruku = dataWydruku;
    }

    @XmlElementWrapper(name = "krokList")
    @XmlElement(name = "dcDokKrokWyd")
    public List<DcDokKrokWydr> getDcDokKrokWydrList() {
        return dcDokKrokWydrList;
    }

    public void setDcDokKrokWydrList(List<DcDokKrokWydr> dcDokKrokWydrList) {
        this.dcDokKrokWydrList = dcDokKrokWydrList;
    }

    public String getSymbolDok() {
        return symbolDok;
    }

    public void setSymbolDok(String symbolDok) {
        this.symbolDok = symbolDok;
    }

    public String getDataDok() {
        return dataDok;
    }

    public void setDataDok(String dataDok) {
        this.dataDok = dataDok;
    }

    public String getRodzajNazwa() {
        return rodzajNazwa;
    }

    public void setRodzajNazwa(String rodzajNazwa) {
        this.rodzajNazwa = rodzajNazwa;
    }

    public String getTeczkaNazwa() {
        return teczkaNazwa;
    }

    public void setTeczkaNazwa(String teczkaNazwa) {
        this.teczkaNazwa = teczkaNazwa;
    }

    public String getWprowadzil() {
        return wprowadzil;
    }

    public void setWprowadzil(String wprowadzil) {
        this.wprowadzil = wprowadzil;
    }

    public String getZrodloNazwa() {
        return zrodloNazwa;
    }

    public void setZrodloNazwa(String zrodloNazwa) {
        this.zrodloNazwa = zrodloNazwa;
    }

}
