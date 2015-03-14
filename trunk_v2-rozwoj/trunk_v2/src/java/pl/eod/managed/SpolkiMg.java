package pl.eod.managed;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.encje.Spolki;
import pl.eod.encje.SpolkiJpaController;
import pl.eod.encje.exceptions.IllegalOrphanException;
import pl.eod.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "SpolkiMg")
@SessionScoped
public class SpolkiMg {

    private DataModel<Spolki> lista = new ListDataModel<>();
    private SpolkiJpaController dcC;
    private pl.eod.encje.Spolki obiekt;
    private String error;

    @PostConstruct
    void init() {
        dcC = new SpolkiJpaController();
        refresh();
    }

    void refresh() {
        lista.setWrappedData(dcC.findSpolkiEntities());
        obiekt = new Spolki();
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

    public void edytuj() throws IllegalOrphanException, NonexistentEntityException, Exception {
        error=dcC.edit(obiekt);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findSpolkiEntities());
        } else {
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        //rodzajGrupa=lista.getRowData();
        dcC.destroy(obiekt.getId());
        refresh();
    }

    public String list() {
        refresh();
        return "/admin/spolki";
    }

    public DataModel<Spolki> getLista() {
        return lista;
    }

    public void setLista(DataModel<Spolki> lista) {
        this.lista = lista;
    }

    public Spolki getObiekt() {
        return obiekt;
    }

    public void setObiekt(Spolki obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }    
}

