/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcDokumentArch;
import pl.eod2.encje.DcDokumentArchDane;
import pl.eod2.encje.DcDokumentArchKontr;
import pl.eod2.encje.DcDokumentStatus;
import pl.eod2.encje.DcTeczka;
import pl.eod2.encje.DcTeczkaKontr;

@ManagedBean(name = "PoczekalniaArch")
@SessionScoped
public class Poczekalnia extends AbstMg<DcDokumentArch, DcDokumentArchKontr> {

    private List<DcDokumentArch> wybrane = new ArrayList<>();
    private List<DcDokumentArch> doWybrania = new ArrayList<>();
    private List<DcTeczka> doWybraniaTeczki = new ArrayList<>();
    private List<DcTeczka> wybraneTeczki = new ArrayList<>();
    private String typWyboru = "";
    private DcDokumentArchDane dcDokArchDane = new DcDokumentArchDane();
    private DcTeczkaKontr dcTeczC;

    public Poczekalnia() throws InstantiationException, IllegalAccessException {
        super("/dcarch/listPoczekalnia", new DcDokumentArchKontr(), new DcDokumentArch());
    }

    @Override
    public void refresh() throws InstantiationException, IllegalAccessException {
        List<DcDokumentArch> suma = new ArrayList<>();
        suma.addAll(dcC.findStatus(6));
        suma.addAll(dcC.findStatus(7));
        lista.setWrappedData(suma);
        obiekt = obiekt.getClass().newInstance();
        error = null;
        wybrane.clear();
    }

    @Override
    public String list() throws InstantiationException, IllegalAccessException {
        refresh();
        return "/dcarch/listPoczekalnia";
    }

    @SuppressWarnings("unchecked")
    public void archPojDok() {
        typWyboru = "pojedyncze";
        wybrane.clear();
        dcDokArchDane = new DcDokumentArchDane();
        for (DcDokumentArch dok : (List<DcDokumentArch>) lista.getWrappedData()) {
            if (dok.isWyborZnacznik()) {
                wybrane.add(dok);
            }
        }
        doWybrania = (List<DcDokumentArch>) lista.getWrappedData();
    }

    public void przeniesPoj() throws InstantiationException, IllegalAccessException {
        dcDokArchDane.setArchData(new Date());
        for (DcDokumentArch dok : wybrane) {
            if (dok.getDokStatusId().getId() != 7) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "błędny status: "+dok.getDokStatusId().getNazwa(), "błędny status: "+dok.getDokStatusId().getNazwa());
                UIComponent zapisz = UIComponent.getCurrentComponent(context);
                context.addMessage(zapisz.getClientId(context), message);
                continue;
            }
            dok.setDokArchDane(dcDokArchDane);
            dok.setDokStatusId(new DcDokumentStatus(8));
            dcC.edit(dok);
        }
        refresh();
    }

    public void archTeczki() {
        typWyboru = "teczki";
        wybraneTeczki.clear();
        dcTeczC = new DcTeczkaKontr();
        doWybraniaTeczki = dcTeczC.findDlaStatus(new DcDokumentStatus(3));
    }

    public void przeniesTeczki() throws InstantiationException, IllegalAccessException {
        dcDokArchDane.setArchData(new Date());
        for (DcTeczka tc : wybraneTeczki) {
            for (DcDokumentArch dok : tc.getDcDokumentListPoczDoArch()) {
                dok.setDokArchDane(dcDokArchDane);
                dok.setDokStatusId(new DcDokumentStatus(8));
                dcC.edit(dok);
            }
        }
        refresh();
    }

    public List<DcDokumentArch> getWybrane() {
        return wybrane;
    }

    public void setWybrane(List<DcDokumentArch> wybrane) {
        this.wybrane = wybrane;
    }

    public List<DcDokumentArch> getDoWybrania() {
        return doWybrania;
    }

    public void setDoWybrania(List<DcDokumentArch> doWybrania) {
        this.doWybrania = doWybrania;
    }

    public List<DcTeczka> getDoWybraniaTeczki() {
        return doWybraniaTeczki;
    }

    public void setDoWybraniaTeczki(List<DcTeczka> doWybraniaTeczki) {
        this.doWybraniaTeczki = doWybraniaTeczki;
    }

    public List<DcTeczka> getWybraneTeczki() {
        return wybraneTeczki;
    }

    public void setWybraneTeczki(List<DcTeczka> wybraneTeczki) {
        this.wybraneTeczki = wybraneTeczki;
    }

    public String getTypWyboru() {
        return typWyboru;
    }

    public void setTypWyboru(String typWyboru) {
        this.typWyboru = typWyboru;
    }

    public DcDokumentArchDane getDcDokArchDane() {
        return dcDokArchDane;
    }

    public void setDcDokArchDane(DcDokumentArchDane dcDokArchDane) {
        this.dcDokArchDane = dcDokArchDane;
    }

}
