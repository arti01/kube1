/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentArch;
import pl.eod2.encje.DcDokumentArchDane;
import pl.eod2.encje.DcDokumentJpaController;
import pl.eod2.encje.DcDokumentStatus;
import pl.eod2.encje.DcTeczka;
import pl.eod2.encje.DcTeczkaKontr;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedRej.Rejestracja;

@ManagedBean(name = "PrzeniesArch")
@SessionScoped
public class Przenies {

    private DataModel<DcDokument> listaDoArchiwum = new ListDataModel<>();
    private DcDokumentJpaController dcDokC;
    private DcTeczkaKontr dcTeczC;
    private DcDokumentArchDane dcDokArchDane = new DcDokumentArchDane();
    private List<DcDokument> wybrane = new ArrayList<>();
    private List<DcDokument> doWybrania = new ArrayList<>();
    private List<DcTeczka> doWybraniaTeczki = new ArrayList<>();
    private List<DcTeczka> wybraneTeczki = new ArrayList<>();
    private String typWyboru = "";

    @ManagedProperty(value = "#{RejestracjaRej}")
    private Rejestracja rejestracja;

    @ManagedProperty(value = "#{DokumentyArch}")
    private Dokumenty dokumenty;

    @PostConstruct
    public void refreshObiekt() {
        dcDokC = new DcDokumentJpaController();
        dcTeczC = new DcTeczkaKontr();
        List<DcDokument> l=dcDokC.findStatus(3);
        l.addAll(dcDokC.findStatus(4));
        l.addAll(dcDokC.findStatus(5));
        listaDoArchiwum.setWrappedData(l);
        dcDokArchDane = new DcDokumentArchDane();
        typWyboru = "";
        wybrane.clear();
        //obiekt = new DcDokument();
        //error = null;
    }

    public String list() {
        refreshObiekt();
        return "/dcarch/listDo";
    }

    public void stworzDokZdawOdb() {
        archPojDok();
        rejestracja.refreshObiekt();
        List<DcDokumentArch> wybraneArch = new ArrayList<>();
        if(wybrane.isEmpty()){
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "brak dokumentow", "brak dokumentow");
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            //throw new UnsupportedOperationException("brak dokumentow");
        }
        for (DcDokument dok : wybrane) {
            DcDokumentArch dokArch = new DcDokumentArch(dok);
            dokArch.setDokStatusId(new DcDokumentStatus(6));
            dokArch.setDokArchDane(dcDokArchDane);
            wybraneArch.add(dokArch);
        }
        rejestracja.getObiekt().setDcArchList(wybraneArch);
    }

    public void przeniesDokZdawczoOdb() throws NonexistentEntityException {
        //walidacja, aby dokument byl zdawczo orbiorczy
        if(rejestracja.getObiekt().getRodzajId().getDcDokStatusPocz().getId()!=6){
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "błędny rodzaj - status początkowy różny od: poczekalnia - do akceptu", "błędny rodzaj - status początkowy różny od: poczekalnia - do akceptu");
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            return;
        }
            
        if (rejestracja.dodajAbst()) {
            //usuwanie dokumentow
            for (DcDokument dok : wybrane) {
                dcDokC.destroy(dok);
            }
            refreshObiekt();
        }
    }

    @SuppressWarnings("unchecked")
    public void archPojDok() {
        typWyboru = "pojedyncze";
        wybrane.clear();
        dcDokArchDane = new DcDokumentArchDane();
        for (DcDokument dok : (List<DcDokument>) listaDoArchiwum.getWrappedData()) {
            if (dok.isDoArchZnacznik()) {
                wybrane.add(dok);
            }
        }
        doWybrania = (List<DcDokument>) listaDoArchiwum.getWrappedData();
    }
    
    public void przeniesPoj() {
        dcDokArchDane.setArchData(new Date());
        for (DcDokument dok : wybrane) {
            DcDokumentArch dokArch = new DcDokumentArch(dok);
            dokArch.setDokArchDane(dcDokArchDane);
            dcDokC.przeniesDoArchiwum(dok, dokArch, false);
        }
        refreshObiekt();
    }

    public void archTeczki() {
        typWyboru = "teczki";
        wybraneTeczki.clear();
        doWybraniaTeczki = dcTeczC.findDlaStatus(new DcDokumentStatus(3));
    }

    public void przeniesTeczki() {
        dcDokArchDane.setArchData(new Date());
        for (DcTeczka tc : wybraneTeczki) {
            for (DcDokument dok : tc.getDcDokumentListDoArch()) {
                DcDokumentArch dokArch = new DcDokumentArch(dok);
                dokArch.setDokArchDane(dcDokArchDane);
                dcDokC.przeniesDoArchiwum(dok, dokArch, false);
            }
        }
        refreshObiekt();
    }

    public DataModel<DcDokument> getListaDoArchiwum() {
        return listaDoArchiwum;
    }

    public void setListaDoArchiwum(DataModel<DcDokument> listaDoArchiwum) {
        this.listaDoArchiwum = listaDoArchiwum;
    }

    public DcDokumentArchDane getDcDokArchDane() {
        return dcDokArchDane;
    }

    public void setDcDokArchDane(DcDokumentArchDane dcDokArchDane) {
        this.dcDokArchDane = dcDokArchDane;
    }

    public List<DcDokument> getWybrane() {
        return wybrane;
    }

    public void setWybrane(List<DcDokument> wybrane) {
        this.wybrane = wybrane;
    }

    public List<DcDokument> getDoWybrania() {
        return doWybrania;
    }

    public void setDoWybrania(List<DcDokument> doWybrania) {
        this.doWybrania = doWybrania;
    }

    public String getTypWyboru() {
        return typWyboru;
    }

    public void setTypWyboru(String typWyboru) {
        this.typWyboru = typWyboru;
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

    public Rejestracja getRejestracja() {
        return rejestracja;
    }

    public void setRejestracja(Rejestracja rejestracja) {
        this.rejestracja = rejestracja;
    }

    public Dokumenty getDokumenty() {
        return dokumenty;
    }

    public void setDokumenty(Dokumenty dokumenty) {
        this.dokumenty = dokumenty;
    }

}
