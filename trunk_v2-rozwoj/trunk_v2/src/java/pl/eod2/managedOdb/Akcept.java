package pl.eod2.managedOdb;

import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod.managed.Login;
import pl.eod2.encje.DcDokDoWiadCel;
import pl.eod2.encje.DcDokDoWiadomosci;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentJpaController;
import pl.eod2.encje.DcDokumentKrok;
import pl.eod2.encje.DcDokumentKrokUzytkownik;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "AkceptOdb")
@SessionScoped
public class Akcept {

    private DcDokument obiekt;
    private DcDokumentJpaController dcC;
    @ManagedProperty(value = "#{login}")
    private Login login;
    private String akceptOpis = "";
    private Uzytkownik userDoWiad;
    private DcDokDoWiadomosci doWiad;
    private DcDokDoWiadCel doWiadCel;
    private UzytkownikJpaController Uc;
    private String error;

    @PostConstruct
    void init() {
        dcC = new DcDokumentJpaController();
        Uc=new UzytkownikJpaController();
        refresh();
    }

    public void refresh(){
        userDoWiad=new Uzytkownik();
        doWiad=new DcDokDoWiadomosci();
    }
    
    public String list() {
        return "/dcodb/akcList";
    }

    public String detale() {
        refresh();
        return "/dcodb/akcDetale?faces-redirect=true";
    }

    public String akceptuj() {
        DcDokumentKrokUzytkownik dkuDoZmiany = new DcDokumentKrokUzytkownik();
        for (DcDokumentKrok dk : obiekt.getDcDokKrok()) {
            if (dk.getAkcept().getId() == 2 || dk.getAkcept().getId() == 3) {
                for (DcDokumentKrokUzytkownik dku : dk.getDcKrokUzytkownikaList()) {
                    if (dku.getIdUser() == login.getZalogowany().getUserId()) {
                        dkuDoZmiany = dku;
                    }
                }
            }
        }
        try {
            dkuDoZmiany.setInformacja(akceptOpis);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        dcC.akceptuj(dkuDoZmiany);
        akceptOpis = "";
        return "/dcodb/akcList?faces-redirect=true";
    }

    public String odrzuc() {
        DcDokumentKrokUzytkownik dkuDoZmiany = new DcDokumentKrokUzytkownik();
        for (DcDokumentKrok dk : obiekt.getDcDokKrok()) {
            if (dk.getAkcept().getId() == 2 || dk.getAkcept().getId() == 3) {
                for (DcDokumentKrokUzytkownik dku : dk.getDcKrokUzytkownikaList()) {
                    if (dku.getIdUser() == login.getZalogowany().getUserId()) {
                        dkuDoZmiany = dku;
                    }
                }
            }
        }
        try {
            dkuDoZmiany.setInformacja(akceptOpis);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        dcC.odrzuc(dkuDoZmiany);
        akceptOpis = "";
        return "/dcodb/akcList?faces-redirect=true";
    }
    
    public void dodajDoWiadUser(){
        if (doWiad.getDcDokDoWiadCelList() == null) {
            doWiad.setDcDokDoWiadCelList(new ArrayList<DcDokDoWiadCel>());
        }
        DcDokDoWiadCel cel=new DcDokDoWiadCel();
        //userDoWiad=Uc.findUzytkownik(userDoWiad.getId());
        cel.setUserid(userDoWiad);
        cel.setIdDokDoWiad(doWiad);
        doWiad.getDcDokDoWiadCelList().add(cel);
        //usersLista.remove(user);
        userDoWiad = new Uzytkownik();
        System.err.println(cel.getId()+"-"+cel.getIdDokDoWiad()+"-"+cel.getUserid());
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
        System.out.println(obiekt.getDcDokDoWiadList());

        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            //obiekt=dcC.findDcRodzaj(obiekt.getId());
        } else {
            refresh();
        }
    }
    
    public DcDokument getObiekt() {
        return obiekt;
    }

    public void setObiekt(DcDokument obiekt) {
        this.obiekt = obiekt;
    }

    public Login getLogin() {
        login.refresh();
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getAkceptOpis() {
        return akceptOpis;
    }

    public void setAkceptOpis(String akceptOpis) {
        this.akceptOpis = akceptOpis;
    }

    public Uzytkownik getUserDoWiad() {
        return userDoWiad;
    }

    public void setUserDoWiad(Uzytkownik userDoWiad) {
        this.userDoWiad = userDoWiad;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
}
