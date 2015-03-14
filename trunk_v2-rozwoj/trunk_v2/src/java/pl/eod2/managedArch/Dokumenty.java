/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentJpaController;
import pl.eod2.encje.DcRodzaj;
import pl.eod2.encje.DcRodzajJpaController;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "DokumentyArch")
@SessionScoped
public class Dokumenty extends AbstMg<DcDokument, DcDokumentJpaController> {

    private DataModel<DcRodzaj> rodzajLista = new ListDataModel<>();
    private DcRodzajJpaController dcR;

    public Dokumenty() throws InstantiationException, IllegalAccessException {
        super("/dcarch/dokumenty", new DcDokumentJpaController(), new DcDokument());
    }

    @PostConstruct
    @Override
    public void refresh() {
        dcR = new DcRodzajJpaController();
        rodzajLista.setWrappedData(dcR.findDcRodzajArch());
        lista.setWrappedData(dcC.findEntitiesDlaArch());
        try {
            obiekt = obiekt.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException ex1) {
            Logger.getLogger(Dokumenty.class.getName()).log(Level.SEVERE, null, ex1);
        }
        error = null;
        rejestracja.setObiekt(null);
        rejestracja.czyscFiltry();
    }

    public String detale() {
        return "/dcarch/dokumentDetale?faces-redirect=true";
    }

     public void wyslijDoAkceptacji() {
        rejestracja.wyslijDoAkceptacji();
        refresh();
    }
    
    @Override
    public void dodaj() throws InstantiationException, IllegalAccessException {
        try {
            if (rejestracja.dodajAbst()) {
                refresh();
                rejestracja.refreshObiekt();
                //urzadzeniaMg.refresh();
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Dokumenty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void edytuj() {
        if (rejestracja.edytujAbst()) {
            rejestracja.refreshObiekt();
            refresh();
        }
    }

    public DataModel<DcRodzaj> getRodzajLista() {
        rodzajLista.setWrappedData(dcR.findDcRodzajArch());
        return rodzajLista;
    }

    public void setRodzajLista(DataModel<DcRodzaj> rodzajLista) {
        this.rodzajLista = rodzajLista;
    }

}
