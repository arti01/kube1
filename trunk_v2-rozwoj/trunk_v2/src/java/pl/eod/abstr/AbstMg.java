package pl.eod.abstr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedRej.Rejestracja;

public abstract class AbstMg<X extends AbstEncja, Y extends AbstKontroler<X>> {

    public DataModel<X> lista;
    public List<X> listaPF=new ArrayList<>();
    public final Y dcC;
    public X obiekt;
    public String error;
    private final String link;
    @ManagedProperty(value = "#{RejestracjaRej}")
    public Rejestracja rejestracja;

    @SuppressWarnings({"unchecked", "unchecked"})
    public AbstMg(String s, AbstKontroler<X> ak, X obiekt) throws InstantiationException, IllegalAccessException {
        link = s;
        this.lista = new ListDataModel<>();
        this.dcC = (Y) ak.getClass().newInstance();
        this.obiekt = obiekt;
        lista.setWrappedData(dcC.findEntities());
        listaPF=dcC.findEntities();
        rejestracja = new Rejestracja();
    }

    @SuppressWarnings("unchecked")
    public void newObiekt() throws InstantiationException, IllegalAccessException {
        obiekt = (X) obiekt.getClass().newInstance();
    }

    @SuppressWarnings("unchecked")
    public void refresh() throws InstantiationException, IllegalAccessException {
        lista.setWrappedData(dcC.findEntities());
        listaPF=dcC.findEntities();
        obiekt = (X) obiekt.getClass().newInstance();
        error = null;
        rejestracja.czyscFiltry();
    }

    public Map<String, String> dodaj() throws InstantiationException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        Map<String, String> errorMap = dcC.create(obiekt);
        if (!errorMap.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            //przycisk zapisz/dodaj
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, entry.getValue(), entry.getValue());
                UIComponent input = zapisz.getParent().findComponent(entry.getKey());
                try {
                    context.addMessage(input.getClientId(context), message);
                } catch (NullPointerException e) {
                    context.addMessage(null, message);
                    System.err.println("po migracji na PF wywalic");
                }
            }
        } else {
            refresh();
        }
        return errorMap;
    }

    public Map<String, String> edytuj() throws IllegalOrphanException, NonexistentEntityException, Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> errorMap = dcC.edit(obiekt);
        if (!errorMap.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            //przycisk zapisz/dodaj
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            for (Map.Entry<String, String> entry : errorMap.entrySet()) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, entry.getValue(), entry.getValue());
                UIComponent input = zapisz.getParent().findComponent(entry.getKey());    
                try {
                    context.addMessage(input.getClientId(context), message);
                } catch (NullPointerException e) {
                    context.addMessage(null, message);
                    context.addMessage(zapisz.getClientId(context), message);
                    System.err.println("po migracji na PF wywalic");
                }
                lista.setWrappedData(dcC.findEntities());
            }
        } else {
            refresh();
        }
        return errorMap;
    }

    @SuppressWarnings("unchecked")
    public void usun() throws IllegalOrphanException, NonexistentEntityException, InstantiationException, IllegalAccessException {
        //rodzajGrupa=lista.getRowData();
        dcC.destroy(obiekt);
        refresh();
    }

    public String list() throws InstantiationException, IllegalAccessException {
        refresh();
        return link;
    }

    public DataModel<X> getLista() {
        return lista;
    }

    public void setLista(DataModel<X> lista) {
        this.lista = lista;
    }

    public List<X> getListaPF() {
        return listaPF;
    }

    public void setListaPF(List<X> listaPF) {
        this.listaPF = listaPF;
    }

    public X getObiekt() {
        return obiekt;
    }

    public void setObiekt(X obiekt) {
        this.obiekt = obiekt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Y getDcC() {
        return dcC;
    }

    public Rejestracja getRejestracja() {
        return rejestracja;
    }

    public void setRejestracja(Rejestracja rejestracja) {
        this.rejestracja = rejestracja;
    }

    public void pfMess(Severity sev, String mess, String messDet) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage message = new FacesMessage(sev, mess, messDet);
        context.addMessage(null, message);
    }

}
