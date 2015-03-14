package pl.eod2.managedCfg;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod2.encje.DcRodzajGrupa;
import pl.eod2.encje.DcRodzajGrupaJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RodzajeGrupyCfg")
@SessionScoped
public class RodzajeGrupy {

    private DataModel<DcRodzajGrupa> lista = new ListDataModel<DcRodzajGrupa>();
    private DcRodzajGrupaJpaController dcRodzajGrupaC;
    private DcRodzajGrupa rodzajGrupa;
    private String error;

    @PostConstruct
    void init() {
        dcRodzajGrupaC = new DcRodzajGrupaJpaController();
        refresh();
    }

    public void refresh() {
        lista.setWrappedData(dcRodzajGrupaC.findDcRodzajGrupaEntities());
        rodzajGrupa = new DcRodzajGrupa();
        error = null;
    }

    public void dodaj() {
        error = dcRodzajGrupaC.create(rodzajGrupa);
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
        error = dcRodzajGrupaC.edit(rodzajGrupa);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcRodzajGrupaC .findDcRodzajGrupaEntities());
        } else {
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        //rodzajGrupa=lista.getRowData();
        dcRodzajGrupaC.destroy(rodzajGrupa.getId());
        refresh();
    }

    public void test() {
        System.err.println("test" + lista.getRowData().getNazwa());
    }

    public String list() {
        refresh();
        return "/dccfg/rodzajegrupy";
    }

    public DataModel<DcRodzajGrupa> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcRodzajGrupa> lista) {
        this.lista = lista;
    }

    public DcRodzajGrupa getRodzajGrupa() {
        return rodzajGrupa;
    }

    public void setRodzajGrupa(DcRodzajGrupa rodzajGrupa) {
        this.rodzajGrupa = rodzajGrupa;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }    
}
