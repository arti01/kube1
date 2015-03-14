package pl.eod2.managedRej;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.encje.Uzytkownik;
import pl.eod.managed.Login;
import pl.eod2.encje.DcDokDoWiadCel;
import pl.eod2.encje.DcDokDoWiadomosci;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentJpaController;
import pl.eod2.encje.DcDokumentStatus;
import pl.eod2.encje.DcKontrahenci;
import pl.eod.abstr.AbstPlik;
import pl.eod2.encje.DcPlik;
import pl.eod2.encje.DcPlikImport;
import pl.eod2.encje.DcPlikImportJpaController;
import pl.eod2.encje.DcPlikJpaController;
import pl.eod2.encje.DcRodzaj;
import pl.eod2.encje.DcRodzajJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RejestracjaRej")
@SessionScoped
public class Rejestracja {

    DataModel<DcDokument> lista = new ListDataModel<>();
    private DataModel<DcRodzaj> rodzajLista = new ListDataModel<>();
    DcDokumentJpaController dcC;
    private DcPlikJpaController dcPlikC;
    private DcRodzajJpaController dcRodzC;
    DcDokument obiekt;
    private AbstPlik plik;
    private DcPlikImport plikImport;
    private DcPlikImportJpaController plikImpC;
    private String error;
    private Locale locale;
    @ManagedProperty(value = "#{login}")
    private Login login;
    private DcKontrahenci kontrahent;
    private DcDokDoWiadomosci doWiad;
    private DcDokDoWiadCel doWiadCel;
    private Uzytkownik userDoWiad;
    private String filtrdataWprow;
    private String filtrNazwa;
    private String filtrdataDok;
    private String filtrKontrahent;
    private String filtrZrodlo;
    private String filtrStatus;
    private String filtrWprowadzil;
    private String filtrRodzaj;
    private String filtrTeczka;
    private String filtrArchiwisci;
    private String filtrArchLokal;
    private String filtrDaneDod;

    @PostConstruct
    void init() {
        dcC = new DcDokumentJpaController();
        dcPlikC = new DcPlikJpaController();
        dcRodzC = new DcRodzajJpaController();
        kontrahent = new DcKontrahenci();
        userDoWiad = new Uzytkownik();
        doWiad = new DcDokDoWiadomosci();
        plikImpC = new DcPlikImportJpaController();
        //refreshObiekt(); - uwaga - zmiana do testow
    }

    public void refreshObiekt() {
        lista.setWrappedData(dcC.findDcDokumentEntities());
        rodzajLista.setWrappedData(dcRodzC.findDcRodzajEntities());
        obiekt = new DcDokument();
        kontrahent = new DcKontrahenci();
        error = null;
    }

    void refreshBezObiekt() {
        lista.setWrappedData(dcC.findDcDokumentEntities());
        error = null;
    }

    public boolean dodajAbst() throws NonexistentEntityException {
        try {
            error = dcC.create(obiekt, login.getZalogowany());
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            return false;
        } else {
            //System.err.println(obiekt.getDcPlikList());
            if (!obiekt.getDcPlikList().isEmpty()) {
                //System.err.println("usuwanie plikow");
                //System.err.println(plikImport.getId());
                plikImpC.destroy(plikImport.getId());
                plikImport = null;
            }
            return true;
        }
    }

    public void dodaj() throws Exception {
        if (dodajAbst()) {
            plikImport = null;
            refreshObiekt();
        }
    }

    public void edytuj() {
        //System.err.println(obiekt.getDokArchDod());
        if (edytujAbst()) {
            refreshObiekt();
        }
    }

    public void wyslijDoAkceptacji() {
        obiekt = dcC.wyslijDoAkceptacji(obiekt);
        refreshObiekt();
    }

    public boolean edytujAbst() {
        try {
            error = dcC.editZmiana(obiekt);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.err.println(error);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcDokumentEntities());
            return false;
        } else {
            return true;
        }
    }

    public void edytujZdetale() {
        try {
            error = dcC.editZmiana(obiekt);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.err.println(error);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcDokumentEntities());
        } else {
            refreshBezObiekt();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        usunAbst();
        refreshObiekt();
    }

    public void usunAbst() throws IllegalOrphanException, NonexistentEntityException {
        dcC.destroy(obiekt.getId());
    }

    public void anuluj() throws IllegalOrphanException, NonexistentEntityException, Exception {
        DcDokumentStatus ds = new DcDokumentStatus(4);
        obiekt.setDokStatusId(ds);
        try {
            error = dcC.editZmiana(obiekt);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.err.println(error);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcDokumentEntities());
        } else {
            refreshObiekt();
        }
    }

    public void listenerOpis(ValueChangeEvent vce) {
        DcRodzaj rodzaj = (DcRodzaj) vce.getNewValue();
        obiekt.setRodzajId(rodzaj);
        if (!(rodzaj.getSzablon() == null || rodzaj.getSzablon().isEmpty())) {
            obiekt.setOpisDlugi(rodzaj.getSzablon());
        }
    }

    public void usunPlik() throws IllegalOrphanException, NonexistentEntityException {
        dcPlikC.destroy(plik.getId());
        obiekt.getDcPlikList().remove(plik);
        edytujZdetale();
    }

    public String kontrahentList() {
        refreshObiekt();
        return "/dcrej/kontrahenci";
    }

    public String list() {
        refreshObiekt();
        if (kontrahent.getId() != null) {
            obiekt.setKontrahentId(kontrahent);
        }
        kontrahent = new DcKontrahenci();
        if (plikImport != null) {
            DcPlik dcPlik = new DcPlik();
            dcPlik.setNazwa(plikImport.getNazwa());
            dcPlik.setPlik(plikImport.getDcPlikImportBin().getPlik());
            dcPlik.setDataDodania(new Date());
            obiekt.setDcPlikList(new ArrayList<DcPlik>());
            obiekt.getDcPlikList().add(dcPlik);
        }
        return "/dcrej/dokumenty";
    }

    public String detale() {
        //System.err.println(obiekt.getDokArchDod());
        return "/dcrej/dokumentDetale?faces-redirect=true";
    }

    public void dodajDoWiadUser() {
        if (doWiad.getDcDokDoWiadCelList() == null) {
            doWiad.setDcDokDoWiadCelList(new ArrayList<DcDokDoWiadCel>());
        }
        DcDokDoWiadCel cel = new DcDokDoWiadCel();
        //userDoWiad=Uc.findUzytkownik(userDoWiad.getId());
        cel.setUserid(userDoWiad);
        cel.setIdDokDoWiad(doWiad);
        doWiad.getDcDokDoWiadCelList().add(cel);
        //usersLista.remove(user);
        userDoWiad = new Uzytkownik();
        //System.err.println(cel.getId() + "-" + cel.getIdDokDoWiad() + "-" + cel.getUserid());
    }

    public void usunDoWiadUser() {
        doWiad.getDcDokDoWiadCelList().remove(doWiadCel);
    }

    public void dodajDoWiad() throws IllegalOrphanException, NonexistentEntityException, Exception {

        if (obiekt.getDcDokDoWiadList() == null) {
            obiekt.setDcDokDoWiadList(new ArrayList<DcDokDoWiadomosci>());
        }
        doWiad.setWprowadzil(login.getZalogowany().getUserId());
        doWiad.setDataWprow(new Date());
        doWiad.setDokid(obiekt);
        error = dcC.editDoWiad(obiekt, doWiad);
        //System.out.println(obiekt.getDcDokDoWiadList());

        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            //obiekt=dcC.findDcRodzaj(obiekt.getId());
        } else {
            userDoWiad = new Uzytkownik();
            doWiad = new DcDokDoWiadomosci();
        }
    }

    public void czyscFiltry() {
        filtrdataWprow = "";
        filtrNazwa = "";
        filtrdataDok = "";
        filtrKontrahent = "";
        filtrZrodlo = "";
        filtrStatus = "";
        filtrWprowadzil = "";
        filtrRodzaj = "";
        filtrTeczka = "";
        filtrArchiwisci = "";
        filtrArchLokal = "";
        filtrDaneDod = "";
    }

    public DataModel<DcDokument> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcDokument> lista) {
        this.lista = lista;
    }

    public DcDokument getObiekt() {
        return obiekt;
    }

    public void setObiekt(DcDokument obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public DcDokumentJpaController getDcC() {
        return dcC;
    }

    public void setDcC(DcDokumentJpaController dcC) {
        this.dcC = dcC;
    }

    public DcPlikJpaController getDcPlikC() {
        return dcPlikC;
    }

    public void setDcPlikC(DcPlikJpaController dcPlikC) {
        this.dcPlikC = dcPlikC;
    }

    public AbstPlik getPlik() {
        return plik;
    }

    public void setPlik(AbstPlik plik) {
        this.plik = plik;
    }

    public DcKontrahenci getKontrahent() {
        return kontrahent;
    }

    public void setKontrahent(DcKontrahenci kontrahent) {
        this.kontrahent = kontrahent;
    }

    public DcDokDoWiadomosci getDoWiad() {
        return doWiad;
    }

    public void setDoWiad(DcDokDoWiadomosci doWiad) {
        this.doWiad = doWiad;
    }

    public DcDokDoWiadCel getDoWiadCel() {
        return doWiadCel;
    }

    public void setDoWiadCel(DcDokDoWiadCel doWiadCel) {
        this.doWiadCel = doWiadCel;
    }

    public Uzytkownik getUserDoWiad() {
        return userDoWiad;
    }

    public void setUserDoWiad(Uzytkownik userDoWiad) {
        this.userDoWiad = userDoWiad;
    }

    public String getFiltrdataWprow() {
        return filtrdataWprow;
    }

    public void setFiltrdataWprow(String filtrdataWprow) {
        this.filtrdataWprow = filtrdataWprow;
    }

    public String getFiltrNazwa() {
        return filtrNazwa;
    }

    public void setFiltrNazwa(String filtrNazwa) {
        this.filtrNazwa = filtrNazwa;
    }

    public String getFiltrdataDok() {
        return filtrdataDok;
    }

    public void setFiltrdataDok(String filtrdataDok) {
        this.filtrdataDok = filtrdataDok;
    }

    public String getFiltrKontrahent() {
        return filtrKontrahent;
    }

    public void setFiltrKontrahent(String filtrKontrahent) {
        this.filtrKontrahent = filtrKontrahent;
    }

    public String getFiltrZrodlo() {
        return filtrZrodlo;
    }

    public void setFiltrZrodlo(String filtrZrodlo) {
        this.filtrZrodlo = filtrZrodlo;
    }

    public String getFiltrStatus() {
        return filtrStatus;
    }

    public void setFiltrStatus(String filtrStatus) {
        this.filtrStatus = filtrStatus;
    }

    public String getFiltrWprowadzil() {
        return filtrWprowadzil;
    }

    public void setFiltrWprowadzil(String filtrWprowadzil) {
        this.filtrWprowadzil = filtrWprowadzil;
    }

    public String getFiltrRodzaj() {
        return filtrRodzaj;
    }

    public void setFiltrRodzaj(String filtrRodzaj) {
        this.filtrRodzaj = filtrRodzaj;
    }

    public String getFiltrTeczka() {
        return filtrTeczka;
    }

    public void setFiltrTeczka(String filtrTeczka) {
        this.filtrTeczka = filtrTeczka;
    }

    public String getFiltrArchiwisci() {
        return filtrArchiwisci;
    }

    public void setFiltrArchiwisci(String filtrArchiwisci) {
        this.filtrArchiwisci = filtrArchiwisci;
    }

    public String getFiltrArchLokal() {
        return filtrArchLokal;
    }

    public void setFiltrArchLokal(String filtrArchLokal) {
        this.filtrArchLokal = filtrArchLokal;
    }

    public Locale getLocale() {
        locale = new Locale("pl", "PL");
        return locale;
    }

    public DcPlikImport getPlikImport() {
        return plikImport;
    }

    public void setPlikImport(DcPlikImport plikImport) {
        this.plikImport = plikImport;
    }

    public DataModel<DcRodzaj> getRodzajLista() {
        return rodzajLista;
    }

    public void setRodzajLista(DataModel<DcRodzaj> rodzajLista) {
        this.rodzajLista = rodzajLista;
    }

    public String getFiltrDaneDod() {
        return filtrDaneDod;
    }

    public void setFiltrDaneDod(String filtrDaneDod) {
        this.filtrDaneDod = filtrDaneDod;
    }
}
