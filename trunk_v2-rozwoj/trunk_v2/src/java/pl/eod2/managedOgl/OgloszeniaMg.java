package pl.eod2.managedOgl;

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
import org.richfaces.event.DropEvent;
import pl.eod.encje.Struktura;
import pl.eod.managed.Login;
import pl.eod2.encje.Ogloszenia;
import pl.eod2.encje.OgloszeniaJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "OgloszeniaMg")
@SessionScoped
public class OgloszeniaMg {

    @ManagedProperty(value = "#{login}")
    private Login login;
    private DataModel<Ogloszenia> lista = new ListDataModel<Ogloszenia>();
    private OgloszeniaJpaController dcC;
    private Ogloszenia obiekt;
    private Struktura struktura;
    private String error;
    private String filtrTytul;
    private String filtrWprow;

    @PostConstruct
    void init() {
        dcC = new OgloszeniaJpaController();
        refresh();
    }

    public void refresh() {
        lista.setWrappedData(dcC.findBySpolka(login.getZalogowany().getUserId().getSpolkaId()));
        obiekt = new Ogloszenia();
        error = null;
    }

    public void dodaj() {
        error = dcC.create(obiekt, login.getZalogowany().getUserId());
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
            lista.setWrappedData(dcC.findBySpolka(login.getZalogowany().getUserId().getSpolkaId()));
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
        return "/ogloszenia/ogloszenia";
    }

    @SuppressWarnings("unchecked")
    public void drop(DropEvent event) throws NonexistentEntityException, Exception {
        List<Struktura> odbiorcyList = new ArrayList<Struktura>();

        if (event.getDragValue().getClass().equals(ArrayList.class)) {
            odbiorcyList.addAll((List<Struktura>) event.getDragValue());
            odbiorcyList.add(odbiorcyList.get(0).getSzefId());
        } else {
            odbiorcyList.add((Struktura) event.getDragValue());
        }
        //struktura=(Struktura) event.getDragValue();
        Ogloszenia ogl = (Ogloszenia) event.getDropValue();
        ogl.getAdresaciList().addAll(odbiorcyList);
        error = dcC.edit(ogl);
    }

    public void usunOdb() throws NonexistentEntityException, Exception {
         //System.err.println(obiekt.getTytul());
        //System.err.println(lista.getRowData().getTytul());
        //System.err.println(struktura.getUserId().getFullname());
        lista.getRowData().getAdresaciList().remove(struktura);
        error = dcC.edit(lista.getRowData());
    }

    public DataModel<Ogloszenia> getLista() {
        return lista;
    }

    public void setLista(DataModel<Ogloszenia> lista) {
        this.lista = lista;
    }

    public Ogloszenia getObiekt() {
        return obiekt;
    }

    public void setObiekt(Ogloszenia obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFiltrTytul() {
        return filtrTytul;
    }

    public void setFiltrTytul(String filtrTytul) {
        this.filtrTytul = filtrTytul;
    }

    public String getFiltrWprow() {
        return filtrWprow;
    }

    public void setFiltrWprow(String filtrWprow) {
        this.filtrWprow = filtrWprow;
    }

    public Login getLogin() {
        login.refresh();
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Struktura getStruktura() {
        return struktura;
    }

    public void setStruktura(Struktura struktura) {
        this.struktura = struktura;
    }

}
