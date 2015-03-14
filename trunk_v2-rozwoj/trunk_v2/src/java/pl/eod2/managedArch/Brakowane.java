/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcDokumentArch;
import pl.eod2.encje.DcDokumentArchDane;
import pl.eod2.encje.DcDokumentArchKontr;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedRej.Rejestracja;

@ManagedBean(name = "BrakowanieArch")
@SessionScoped
public class Brakowane extends AbstMg<DcDokumentArch, DcDokumentArchKontr> {

    private List<DcDokumentArch> wybrane = new ArrayList<>();
    private List<DcDokumentArch> doWybrania = new ArrayList<>();
    private String typWyboru = "";
    private DcDokumentArchDane dcDokArchDane = new DcDokumentArchDane();
    private int listaSize = 0;

    public Brakowane() throws InstantiationException, IllegalAccessException {
        super("/dcarch/listWbrakowanie", new DcDokumentArchKontr(), new DcDokumentArch());
        super.refresh();
    }

    @PostConstruct
    @Override
    @SuppressWarnings("unchecked")
    public void refresh() {
        try {
            super.refresh();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Brakowane.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<DcDokumentArch> l = new ArrayList<>();
        for (DcDokumentArch d : (List<DcDokumentArch>) lista.getWrappedData()) {
            if (d.isAlertBrakowanie() && d.getDokStatusId().getId() != 10) {
                l.add(d);
            }
        }
        listaSize = l.size();
        lista.setWrappedData(l);
    }

    @SuppressWarnings("unchecked")
    public void zmianaDanychArch() {
        typWyboru = "zmianaDanych";
        wybrane.clear();
        dcDokArchDane = new DcDokumentArchDane();
        for (DcDokumentArch dok : (List<DcDokumentArch>) lista.getWrappedData()) {
            if (dok.isWyborZnacznik()) {
                wybrane.add(dok);
            }
        }
        doWybrania = (List<DcDokumentArch>) lista.getWrappedData();
    }

    public void zmianaDanychArchWykonaj() throws InstantiationException, IllegalAccessException {
        dcDokArchDane.setArchData(new Date());
        for (DcDokumentArch dok : wybrane) {
            dok.setDokArchDane(dcDokArchDane);
            dcC.edit(dok);
        }
        refresh();
    }

    @SuppressWarnings("unchecked")
    public void nowaAkcja() {
        rejestracja.refreshObiekt();
        wybrane.clear();
        dcDokArchDane = new DcDokumentArchDane();
        for (DcDokumentArch dok : (List<DcDokumentArch>) lista.getWrappedData()) {
            if (dok.isWyborZnacznik()) {
                wybrane.add(dok);
            }
        }
        doWybrania = (List<DcDokumentArch>) lista.getWrappedData();
        if (wybrane.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "brak dokumentow", "brak dokumentow");
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            //throw new UnsupportedOperationException("brak dokumentow");
        } else {
            rejestracja.getObiekt().setDcArchList(wybrane);
        }
    }

    public void nowaAkcjaZapisz() {
        for (DcDokumentArch dok : wybrane) {
            //walidacja statusów
            if (!Objects.equals(dok.getDokStatusId().getId(), rejestracja.getObiekt().getRodzajId().getDcDokStatusPocz().getId())) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "błędny status początkowy: " + dok.getDokStatusId().getNazwa() + " dla: " + dok.getSymbolDok(), "błędny status początkowy: " + dok.getDokStatusId().getNazwa() + " dla: " + dok.getSymbolDok());
                UIComponent zapisz = UIComponent.getCurrentComponent(context);
                context.addMessage(zapisz.getClientId(context), message);
                return;
            }
        }
        try {
            // bo inaczej kaskada się sypie
            rejestracja.getObiekt().setDcArchList(new ArrayList<DcDokumentArch>());
            rejestracja.dodajAbst();
            for (DcDokumentArch dok : wybrane) {
                dok.getDokumentyList().add(rejestracja.getObiekt());
                dcC.edit(dok);
            }
            // bo inaczej nie widać
            rejestracja.getObiekt().setDcArchList(wybrane);
            rejestracja.edytujAbst();
            refresh();
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Brakowane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String detale() {
        return "/dcarch/wArchDetale?faces-redirect=true";
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

    public int getListaSize() {
        return listaSize;
    }

    public void setListaSize(int listaSize) {
        this.listaSize = listaSize;
    }

}
