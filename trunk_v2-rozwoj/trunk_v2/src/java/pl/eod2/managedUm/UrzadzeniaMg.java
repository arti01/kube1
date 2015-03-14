package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmUrzadzenie;
import pl.eod2.encje.UmUrzadzenieJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "UrzadzeniaMg")
@SessionScoped
public class UrzadzeniaMg {

    @ManagedProperty(value = "#{login}")
    private Login login;
    private DataModel<UmUrzadzenie> lista = new ListDataModel<UmUrzadzenie>();
    private List<UmGrupa> grupyList = new ArrayList<UmGrupa>();
    private UmUrzadzenieJpaController dcC;
    private UmUrzadzenie obiekt;
    private String error;
    private String filtrNazwa;
    private String filtrNrSer;
    private String filtrNrInw;
    private String filtrLokal;
    private String filtrOsobOdp;
    private String filtrFirmaSerw;
    private String filtrGrupa;

    @PostConstruct
    void init() {
        dcC = new UmUrzadzenieJpaController();
        refresh();
    }

    public void refresh() {
        login.refresh();
        lista.setWrappedData(dcC.findUmUrzadzenieEntities());
        obiekt = new UmUrzadzenie();
        grupyList.clear();
        for (UmMasterGrupa mg : login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList()) {
            grupyList.addAll(mg.getGrupaList());
        }
        error = null;
    }

    public void newObiekt() {
        obiekt = new UmUrzadzenie();
    }
    
    public void dodaj() {
        Map<String, String> errorMap = dcC.create(obiekt);
        if (!errorMap.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            //przycisk zapisz/dodaj
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, entry.getValue(), entry.getValue());
                UIComponent input = zapisz.getParent().findComponent(entry.getKey());
                context.addMessage(input.getClientId(context), message);
            }
            lista.setWrappedData(dcC.findUmUrzadzenieEntities());
            /*error = dcC.create(obiekt);
             if (error != null) {
             FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
             FacesContext context = FacesContext.getCurrentInstance();

             UIComponent zapisz = UIComponent.getCurrentComponent(context);
             System.err.println(zapisz.getParent().getClientId());
             UIComponent osoba = zapisz.getParent().findComponent("userOdpowD");
             System.err.println(osoba.getClientId());
             context.addMessage(zapisz.getClientId(context), message);
             context.addMessage(osoba.getClientId(context), message);*/
        } else {
            refresh();
        }
    }

    public void edytuj() throws IllegalOrphanException, NonexistentEntityException, Exception {
        Map<String, String> errorMap = dcC.edit(obiekt);
        if (!errorMap.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            //przycisk zapisz/dodaj
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, entry.getValue(), entry.getValue());
                UIComponent input = zapisz.getParent().findComponent(entry.getKey());
                context.addMessage(input.getClientId(context), message);
            }
        } else {
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        dcC.destroy(obiekt.getId());
        refresh();
    }

    public String list() {
        refresh();
        return "/um/urzadzenie";
    }

    public Login getLogin() {
        return login;
    }

    public void clearFiltr() {
        filtrNazwa = null;
        filtrNrSer = null;
        filtrNrInw = null;
        filtrLokal = null;
        filtrOsobOdp = null;
        filtrFirmaSerw = null;
        filtrGrupa = null;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public DataModel<UmUrzadzenie> getLista() {
        return lista;
    }

    public void setLista(DataModel<UmUrzadzenie> lista) {
        this.lista = lista;
    }

    public UmUrzadzenie getObiekt() {
        return obiekt;
    }

    public void setObiekt(UmUrzadzenie obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<UmGrupa> getGrupyList() {
        return grupyList;
    }

    public void setGrupyList(List<UmGrupa> grupyList) {
        this.grupyList = grupyList;
    }

    public String getFiltrNazwa() {
        return filtrNazwa;
    }

    public void setFiltrNazwa(String filtrNazwa) {
        this.filtrNazwa = filtrNazwa;
    }

    public String getFiltrNrSer() {
        return filtrNrSer;
    }

    public void setFiltrNrSer(String filtrNrSer) {
        this.filtrNrSer = filtrNrSer;
    }

    public String getFiltrNrInw() {
        return filtrNrInw;
    }

    public void setFiltrNrInw(String filtrNrInw) {
        this.filtrNrInw = filtrNrInw;
    }

    public String getFiltrLokal() {
        return filtrLokal;
    }

    public void setFiltrLokal(String filtrLokal) {
        this.filtrLokal = filtrLokal;
    }

    public String getFiltrOsobOdp() {
        return filtrOsobOdp;
    }

    public void setFiltrOsobOdp(String filtrOsobOdp) {
        this.filtrOsobOdp = filtrOsobOdp;
    }

    public String getFiltrFirmaSerw() {
        return filtrFirmaSerw;
    }

    public void setFiltrFirmaSerw(String filtrFirmaSerw) {
        this.filtrFirmaSerw = filtrFirmaSerw;
    }

    public String getFiltrGrupa() {
        return filtrGrupa;
    }

    public void setFiltrGrupa(String filtrGrupa) {
        this.filtrGrupa = filtrGrupa;
    }

    public UmUrzadzenieJpaController getDcC() {
        return dcC;
    }

    }
