package pl.eod2.managedUm;

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
import pl.eod2.encje.UmGrupaJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "UGrupaMg")
@SessionScoped
public class UGrupaMg {

    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{UStruktMg}")
    private UStruktMg uStruktMg;
    private DataModel<UmGrupa> lista = new ListDataModel<UmGrupa>();
    private UmGrupaJpaController dcC;
    private UmGrupa obiekt;
    private String error;

    @PostConstruct
    void init() {
        dcC = new UmGrupaJpaController();
        refresh();
    }

    public void refresh() {
        login.refresh();
        lista.setWrappedData(dcC.findUmGrupaEntities());
        obiekt = new UmGrupa();
        error = null;
    }

    public void newObiekt() {
        obiekt = new UmGrupa();
    }

    public void dodaj() throws Exception {
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
            lista.setWrappedData(dcC.findUmGrupaEntities());
        } else {
            uStruktMg.refresh();
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        dcC.destroy(obiekt.getId());
        refresh();
    }

    public String list() {
        refresh();
        return "/um/grupa";
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public DataModel<UmGrupa> getLista() {
        return lista;
    }

    public void setLista(DataModel<UmGrupa> lista) {
        this.lista = lista;
    }

    public UmGrupa getObiekt() {
        return obiekt;
    }

    public void setObiekt(UmGrupa obiekt) {
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
