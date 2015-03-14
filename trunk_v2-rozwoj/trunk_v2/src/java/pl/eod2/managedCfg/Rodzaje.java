/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedCfg;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod.managed.Login;
import pl.eod2.encje.DcAkceptKroki;
import pl.eod2.encje.DcAkceptTypKroku;
import pl.eod2.encje.DcAkceptTypKrokuJpaController;
import pl.eod2.encje.DcRodzaj;
import pl.eod2.encje.DcRodzajJpaController;
import pl.eod2.encje.DcTypFlow;
import pl.eod2.encje.DcTypFlowJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RodzajeCfg")
@SessionScoped
public class Rodzaje {

    private DataModel<DcRodzaj> lista = new ListDataModel<>();
    private DcRodzajJpaController dcC;
    private DcRodzaj obiekt;
    private String error;
    @ManagedProperty(value = "#{RodzajeGrupyCfg}")
    private RodzajeGrupy rodzajeGrupy;
    @ManagedProperty(value = "#{login}")
    private Login login;
    private List<DcTypFlow> typFlowLista = new ArrayList<>();
    private List<DcAkceptTypKroku> typKrokuLista = new ArrayList<>();
    private List<Uzytkownik> usersLista = new ArrayList<>();
    private DcTypFlowJpaController dcTypFlowC;
    private DcAkceptTypKrokuJpaController dcTypKrokuC;
    private UzytkownikJpaController uC;
    private DcAkceptKroki akcKrok;
    private Uzytkownik user;

    @PostConstruct
    void init() {
        dcC = new DcRodzajJpaController();
        dcTypFlowC = new DcTypFlowJpaController();
        dcTypKrokuC = new DcAkceptTypKrokuJpaController();
        uC = new UzytkownikJpaController();
        refresh();
    }

    void refresh() {
        lista.setWrappedData(dcC.findDcRodzajEntitiesAll());
        typFlowLista = dcTypFlowC.findDcTypFlowEntities();
        typKrokuLista = dcTypKrokuC.findDcAkceptTypKrokuEntities();
        usersLista = login.getZalogowany().getUserId().getSpolkaId().getUserList();
        obiekt = new DcRodzaj();
        akcKrok = new DcAkceptKroki();
        error = null;
    }

    public void dodaj() {
        error = dcC.create(obiekt);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
        } else {
            refresh();
        }
    }

    public void edytuj() {
        try {
            //System.err.println(obiekt.getUmMasterGrupaList());
            error = dcC.edit(obiekt);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(Rodzaje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Rodzaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.err.println(obiekt.getUmMasterGrupaList());
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findDcRodzajEntities());
        } else {
            refresh();
            rodzajeGrupy.refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        //rodzajGrupa=lista.getRowData();
        dcC.destroy(obiekt.getId());
        refresh();
        rodzajeGrupy.refresh();
    }

    public void dodajKrok() throws IllegalOrphanException, NonexistentEntityException {
        akcKrok.setRodzajId(obiekt);
        error = dcC.dodajKrok(obiekt, akcKrok);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            obiekt=dcC.findDcRodzaj(obiekt.getId());
        } else {
            akcKrok = new DcAkceptKroki();
            usersLista = uC.findUzytkownikEntities();
        }
    }

    public void editKrok() throws IllegalOrphanException, NonexistentEntityException {
        error = dcC.editKrok(obiekt, akcKrok);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            obiekt=dcC.findDcRodzaj(obiekt.getId());
        } else {
            akcKrok = new DcAkceptKroki();
            usersLista = uC.findUzytkownikEntities();
        }
    }

    public void usunKrok() throws IllegalOrphanException, NonexistentEntityException, Exception {
        obiekt = dcC.usunKrok(obiekt, akcKrok);
        akcKrok = new DcAkceptKroki();
    }

    public void editKrokPrepare() {
        usersLista.removeAll(akcKrok.getUzytkownikList());
    }

    public void dodajUser() {
        if (akcKrok.getUzytkownikList() == null) {
            akcKrok.setUzytkownikList(new ArrayList<Uzytkownik>());
        }
        akcKrok.getUzytkownikList().add(user);
        usersLista.remove(user);
        user = new Uzytkownik();
    }

    public void usunUser() {
        //System.err.println(user);
        akcKrok.getUzytkownikList().remove(user);
        usersLista.add(user);
        user = new Uzytkownik();
    }

    public void upKrok() {
        obiekt = dcC.upKrok(obiekt, akcKrok);
    }

    public void downKrok() {
        obiekt = dcC.downKrok(obiekt, akcKrok);
    }

    public void test() {
        System.err.println("test" + lista.getRowData().getNazwa());
    }

    public String list() {
        refresh();
        return "/dccfg/rodzaje";
    }

    public String krokiLista() {

        return "/dccfg/rodzajKroki?faces-redirect=true";
    }

    public DataModel<DcRodzaj> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcRodzaj> lista) {
        this.lista = lista;
    }

    public DcRodzaj getObiekt() {
        return obiekt;
    }

    public void setObiekt(DcRodzaj obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public RodzajeGrupy getRodzajeGrupy() {
        return rodzajeGrupy;
    }

    public void setRodzajeGrupy(RodzajeGrupy rodzajeGrupy) {
        this.rodzajeGrupy = rodzajeGrupy;
    }

    public List<DcTypFlow> getTypFlowLista() {
        return typFlowLista;
    }

    public void setTypFlowLista(List<DcTypFlow> typFlowLista) {
        this.typFlowLista = typFlowLista;
    }

    public DcAkceptKroki getAkcKrok() {
        return akcKrok;
    }

    public void setAkcKrok(DcAkceptKroki akcKrok) {
        this.akcKrok = akcKrok;
    }

    public List<DcAkceptTypKroku> getTypKrokuLista() {
        return typKrokuLista;
    }

    public void setTypKrokuLista(List<DcAkceptTypKroku> typKrokuLista) {
        this.typKrokuLista = typKrokuLista;
    }

    public Uzytkownik getUser() {
        return user;
    }

    public void setUser(Uzytkownik user) {
        this.user = user;
    }

    public List<Uzytkownik> getUsersLista() {
        return usersLista;
    }

    public void setUsersLista(List<Uzytkownik> usersLista) {
        this.usersLista = usersLista;
    }

    public UzytkownikJpaController getuC() {
        return uC;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }    
}
