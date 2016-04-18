package pl.eod2.managedRej;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import pl.eod.cron4j.EmailMoj;
import pl.eod.cron4j.EmailOdbior;
import pl.eod.cron4j.EmailZalacznik;
import pl.eod2.encje.DcDokPolaDod;
import pl.eod2.encje.DcPlik;
import pl.eod2.encje.DcPlikImport;
import pl.eod2.encje.DcPlikImportJpaController;
import pl.eod2.encje.DcPlikJpaController;
import pl.eod2.encje.DcRodzaj;
import pl.eod2.encje.DcRodzajJpaController;
import pl.eod2.encje.DcRodzajPolaDod;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedCfg.Kontrahenci;

@ManagedBean(name = "RejestracjaRej")
@SessionScoped
public class Rejestracja {

    DataModel<DcDokument> lista = new ListDataModel<>();
    List<DcDokument> listaPF = new ArrayList<>();
    private DataModel<DcRodzaj> rodzajLista = new ListDataModel<>();
    private List<DcRodzaj> rodzajListaPF = new ArrayList<>();
    public DcDokumentJpaController dcC;
    public DcPlikJpaController dcPlikC;
    private DcRodzajJpaController dcRodzC;
    DcDokument obiekt;
    private AbstPlik plik;
    private DcPlikImport plikImport;
    private DcPlikImportJpaController plikImpC;
    private String error;
    private Locale locale;
    @ManagedProperty(value = "#{login}")
    private Login login;

    @ManagedProperty(value = "#{RejImpPlik}")
    private ImpPlik impPlik;

    @ManagedProperty(value = "#{EmailOdbior}")
    EmailOdbior emailOdb;

    @ManagedProperty(value = "#{KontrahenciCfg}")
    Kontrahenci kontrahCfg;

    private EmailMoj email;

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
    }

    public void refreshObiekt() {
        lista.setWrappedData(dcC.findDcDokumentEntities());
        listaPF = dcC.findDcDokumentEntities();
        rodzajLista.setWrappedData(dcRodzC.findDcRodzajEntities());
        rodzajListaPF = dcRodzC.findDcRodzajEntities();
        obiekt = new DcDokument();
        error = null;
    }

    public void newObiekt() {
        obiekt = new DcDokument();
    }

    void refreshBezObiekt() {
        lista.setWrappedData(dcC.findDcDokumentEntities());
        listaPF = dcC.findDcDokumentEntities();
        error = null;
    }

    public boolean dodajAbst() throws NonexistentEntityException {
        UIComponent input = null;
        try {
            List<DcDokPolaDod> pola = new ArrayList<>();
            for (DcDokPolaDod pole : obiekt.getDcDokPolaDodList()) {
                if (pole.getTyp().equals("data") && !pole.getWartosc().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    Date warData = sdf.parse(pole.getWartosc());
                    sdf.applyPattern("yyyy-MM-dd");
                    pole.setWartosc(sdf.format(warData));
                }
                pole.setDcDok(obiekt);
                pola.add(pole);
            }
            obiekt.setDcDokPolaDodList(pola);
            error = dcC.create(obiekt, login.getZalogowany());
        } catch (NullPointerException nex) {
            error = "zapewne brakuje rodzaju dokumentu";
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz1 = UIComponent.getCurrentComponent(context);
            input = zapisz1.getParent().findComponent("rodzajD");
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            if (input != null) {
                context.addMessage(input.getClientId(context), message);
            }
            return false;
        } else {
            if (!obiekt.getDcPlikList().isEmpty()) {
                if (plikImport != null) {
                    plikImpC.destroy(plikImport.getId());
                    plikImport = null;
                } else {
                    for (DcPlikImport pi : impPlik.getLista()) {
                        if (pi.isWybrany()) {
                            plikImpC.destroy(pi.getId());
                        }
                    }
                }

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

    public void edytuj() throws ParseException {
        if (edytujAbst()) {
            refreshObiekt();
        }
    }

    public void wyslijDoAkceptacji() {
        obiekt = dcC.wyslijDoAkceptacji(obiekt);
        refreshObiekt();
    }

    public boolean edytujAbst() {
        List<DcDokPolaDod> pola = new ArrayList<>();
        for (DcDokPolaDod pole : obiekt.getDcDokPolaDodList()) {
            if (pole.getTyp().equals("data") && !pole.getWartosc().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    Date warData = sdf.parse(pole.getWartosc());
                    sdf.applyPattern("yyyy-MM-dd");
                    pole.setWartosc(sdf.format(warData));
                } catch (ParseException ex) {
                    Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pole.setDcDok(obiekt);
            pola.add(pole);
        }
        obiekt.setDcDokPolaDodList(pola);
        try {
            error = dcC.editZmiana(obiekt);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcDokumentEntities());
            listaPF = dcC.findDcDokumentEntities();
            return false;
        } else {
            return true;
        }
    }

    public void edytujZdetale() {
        List<DcDokPolaDod> pola = new ArrayList<>();
        for (DcDokPolaDod pole : obiekt.getDcDokPolaDodList()) {
            if (pole.getTyp().equals("data") && !pole.getWartosc().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                    Date warData = sdf.parse(pole.getWartosc());
                    sdf.applyPattern("yyyy-MM-dd");
                    pole.setWartosc(sdf.format(warData));
                } catch (ParseException ex) {
                    Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            pole.setDcDok(obiekt);
            pola.add(pole);
        }
        obiekt.setDcDokPolaDodList(pola);
        try {
            error = dcC.editZmiana(obiekt);
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rejestracja.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcDokumentEntities());
            listaPF = dcC.findDcDokumentEntities();
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

        //pola dodatkowe
        obiekt.setDcDokPolaDodList(new ArrayList<DcDokPolaDod>());
        if (obiekt.getRodzajId().getDcRodzajPolaDodList() != null) {
            for (DcRodzajPolaDod poleRodzaj : obiekt.getRodzajId().getDcRodzajPolaDodList()) {
                DcDokPolaDod poleDodDok = new DcDokPolaDod();
                poleDodDok.setNazwa(poleRodzaj.getNazwa());
                poleDodDok.setDlugosc(poleRodzaj.getDlugosc());
                poleDodDok.setTyp(poleRodzaj.getIdRodzTypyPol().getNazwa());
                poleDodDok.setWartosc("");
                obiekt.getDcDokPolaDodList().add(poleDodDok);
            }
        }

    }

    public void usunPlik() throws IllegalOrphanException, NonexistentEntityException {
        dcPlikC.destroy(plik.getId());
        obiekt.getDcPlikList().remove(plik);
        edytujZdetale();
    }

    public String kontrahentList() {
        refreshObiekt();
        kontrahent = new DcKontrahenci();
        return "/dcrej/kontrahenci";
    }

    public String list() {
        refreshObiekt();
        if (kontrahent.getId() != null) {
            obiekt.setKontrahentId(kontrahent);
        }
        kontrahent = new DcKontrahenci();
        //obsluga jednego pliku importu
        if (plikImport != null) {
            DcPlik dcPlik = new DcPlik();
            dcPlik.setNazwa(plikImport.getNazwa());
            dcPlik.setPlik(plikImport.getDcPlikImportBin().getPlik());
            dcPlik.setTresc(plikImport.getDcPlikImportBin().getTresc());
            dcPlik.setDataDodania(new Date());
            obiekt.setDcPlikList(new ArrayList<>());
            obiekt.getDcPlikList().add(dcPlik);
            plikImport = null;
        }
        return "/dcrej/dokumenty";
    }

    public String importWielu() {
        refreshObiekt();
        kontrahent = new DcKontrahenci();
        obiekt.setDcPlikList(new ArrayList<>());
        for (DcPlikImport pi : impPlik.getLista()) {
            if (pi.isWybrany()) {
                DcPlik dcPlik = new DcPlik();
                dcPlik.setNazwa(pi.getNazwa());
                dcPlik.setPlik(pi.getDcPlikImportBin().getPlik());
                dcPlik.setTresc(pi.getDcPlikImportBin().getTresc());
                dcPlik.setDataDodania(new Date());
                obiekt.getDcPlikList().add(dcPlik);
            }
        }
        if (obiekt.getDcPlikList().isEmpty()) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "brak wybranych plików", "brak wybranych plików");
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            return "/dcrej/pliki";
        }
        return "/dcrej/dokumenty";
    }

    public String importEmail() {
        refreshObiekt();
        emailOdb.getEmaile().remove(email);
        email.setStworzony(true);
        emailOdb.getEmaile().add(email);
        kontrahent = new DcKontrahenci();
        obiekt.setDcPlikList(new ArrayList<DcPlik>());
        for (EmailZalacznik ez : email.getZalaczniki()) {
            DcPlik dcPlik = new DcPlik();
            dcPlik.setNazwa(ez.getNazwa());
            dcPlik.setPlik(ez.getPlik());
            dcPlik.setDataDodania(new Date());
            obiekt.getDcPlikList().add(dcPlik);
        }
        obiekt.setNazwa(email.getTemat());
        obiekt.setOpis(email.getNadawca());
        obiekt.setOpisDlugi(email.getTresc());

        //szukanie kontrahenta
        for (DcKontrahenci k : kontrahCfg.getLista()) {
            for (String em : k.getEmaileAll()) {
                if (email.getNadawca().contains(em)) {
                    obiekt.setKontrahentId(k);
                    break;
                }
            }
        }

        return "/dcrej/dokumenty";
    }

    public String detale() {
        return "/dcrej/dokumentDetale?faces-redirect=true";
    }

    public void dodajDoWiadUser() {
        if (doWiad.getDcDokDoWiadCelList() == null) {
            doWiad.setDcDokDoWiadCelList(new ArrayList<>());
        }
        DcDokDoWiadCel cel = new DcDokDoWiadCel();
        //userDoWiad=Uc.findUzytkownik(userDoWiad.getId());
        cel.setUserid(userDoWiad);
        cel.setIdDokDoWiad(doWiad);
        doWiad.getDcDokDoWiadCelList().add(cel);
        //usersLista.remove(user);
        userDoWiad = new Uzytkownik();
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

    public List<DcDokument> getListaPF() {
        return listaPF;
    }

    public void setListaPF(List<DcDokument> listaPF) {
        this.listaPF = listaPF;
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

    public List<DcRodzaj> getRodzajListaPF() {
        return rodzajListaPF;
    }

    public void setRodzajListaPF(List<DcRodzaj> rodzajListaPF) {
        this.rodzajListaPF = rodzajListaPF;
    }

    public String getFiltrDaneDod() {
        return filtrDaneDod;
    }

    public void setFiltrDaneDod(String filtrDaneDod) {
        this.filtrDaneDod = filtrDaneDod;
    }

    public ImpPlik getImpPlik() {
        return impPlik;
    }

    public void setImpPlik(ImpPlik impPlik) {
        this.impPlik = impPlik;
    }

    public EmailMoj getEmail() {
        return email;
    }

    public void setEmail(EmailMoj email) {
        this.email = email;
    }

    public EmailOdbior getEmailOdb() {
        return emailOdb;
    }

    public void setEmailOdb(EmailOdbior emailOdb) {
        this.emailOdb = emailOdb;
    }

    public Kontrahenci getKontrahCfg() {
        return kontrahCfg;
    }

    public void setKontrahCfg(Kontrahenci kontrahCfg) {
        this.kontrahCfg = kontrahCfg;
    }

}
