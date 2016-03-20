package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmMasterGrupaJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "UMasterMg")
@SessionScoped
public class UMasterMg {

    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{UStruktMg}")
    private UStruktMg uStruktMg;
    //private DataModel<UmMasterGrupa> lista = new ListDataModel<UmMasterGrupa>();
    private List<UmMasterGrupa> lista = new ArrayList<>();
    private UmMasterGrupaJpaController dcC;
    private UmMasterGrupa obiekt;
    private String error;

    @PostConstruct
    void init() {
        dcC = new UmMasterGrupaJpaController();
        refresh();
    }

    public void refresh() {
        //lista.setWrappedData(dcC.findUmMasterGrupaEntities());
        lista.clear();
        lista.addAll(dcC.findUmMasterGrupaEntities());
        obiekt = new UmMasterGrupa();
        error = null;
    }
    
    public void refreshBezLista() {
        login.refresh();
        obiekt = new UmMasterGrupa();
        error = null;
    }

    public void dodaj() {
        obiekt.setSpolkaId(login.getZalogowany().getUserId().getSpolkaId());
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

    public void edytuj() throws IllegalOrphanException, NonexistentEntityException, Exception {
        error = dcC.edit(obiekt);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.clear();
            lista.addAll(dcC.findUmMasterGrupaEntities());
        } else {
            uStruktMg.refresh();
            refreshBezLista();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        dcC.destroy(obiekt.getId());
        refresh();
    }

    public void editList(RowEditEvent event) throws NonexistentEntityException, Exception {
        obiekt=(UmMasterGrupa) event.getObject();
        edytuj();
    }

    public String list() {
        refresh();
        return "/um/master";
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<UmMasterGrupa> getLista() {
        return lista;
    }

    /*public DataModel<UmMasterGrupa> getLista() {
    return lista;
    }
    public void setLista(DataModel<UmMasterGrupa> lista) {
    this.lista = lista;
    }*/
    public void setLista(List<UmMasterGrupa> lista) {
        this.lista = lista;
    }

    public UmMasterGrupa getObiekt() {
        return obiekt;
    }

    public void setObiekt(UmMasterGrupa obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UStruktMg getuStruktMg() {
        return uStruktMg;
    }

    public void setuStruktMg(UStruktMg uStruktMg) {
        this.uStruktMg = uStruktMg;
    }

}
