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
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.primefaces.event.RowEditEvent;
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
    private List<UmGrupa> lista = new ArrayList<>();
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
        lista.clear();
        lista.addAll(dcC.findUmGrupaEntities());
        obiekt = new UmGrupa();
        error = null;
    }
    
    public void refreshBezLista() {
        login.refresh();
        obiekt = new UmGrupa();
        error = null;
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
            lista.clear();
            lista.addAll(dcC.findUmGrupaEntities());
        } else {
            uStruktMg.refresh();
            refreshBezLista();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        System.err.println(obiekt.getId());
        dcC.destroy(obiekt.getId());
        refresh();
    }

    public String list() {
        refresh();
        return "/um/grupa";
    }
    
    public String raport1() {
        refresh();
        return "/um/raport1";
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<UmGrupa> getLista() {
        return lista;
    }

    public void setLista(List<UmGrupa> lista) {
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
 public void editList(RowEditEvent event) throws NonexistentEntityException, Exception {
        obiekt=(UmGrupa) event.getObject();
        edytuj();
    }
}
