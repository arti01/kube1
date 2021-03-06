package pl.eod2.managedCfg;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.encje.Uzytkownik;
import pl.eod2.encje.DcKontahentDodEmail;
import pl.eod2.encje.DcKontrahenci;
import pl.eod2.encje.DcKontrahenciJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "KontrahenciCfg")
@SessionScoped
public class Kontrahenci {

    private DcKontrahenciJpaController dcC;
    private DataModel<DcKontrahenci> lista;
    private List<DcKontrahenci> listaPF=new ArrayList<>();
    private DcKontrahenci obiekt;
    private String error;
    private String link;
    private String filtrNazwa;
    private String filtrNip;
    private String filtrAdres;
    private DcKontahentDodEmail emailDod;

    @PostConstruct
    void init() {
        this.lista = new ListDataModel<>();
        this.dcC = new DcKontrahenciJpaController();
        this.link = "/dccfg/kontrahenci";
        this.filtrNazwa="";
        this.filtrNip="";
        this.filtrAdres="";
        refresh();
    }

    void refresh() {
        lista.setWrappedData(dcC.findEntities());
        listaPF=dcC.findEntities();
        obiekt = new DcKontrahenci();
        emailDod=new DcKontahentDodEmail();
        obiekt.setDodEmailList(new ArrayList<>());
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
        error = dcC.edit(obiekt);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            lista.setWrappedData(dcC.findEntities());
        } else {
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        //rodzajGrupa=lista.getRowData();
        dcC.destroy(obiekt.getId());
        refresh();
    }
    
    public void dodajEmail(){
        emailDod.setKontrahentId(obiekt);
        obiekt.getDodEmailList().add(emailDod);
        emailDod=new DcKontahentDodEmail();
    }
    
    public void usunEmail(){
        obiekt.getDodEmailList().remove(emailDod);
        emailDod=new DcKontahentDodEmail();
    }

    public List<DcKontrahenci> autoCompKontrah(String query) {
        query = query.toLowerCase();
        List<DcKontrahenci> wynik = new ArrayList<>();
        for (DcKontrahenci u : listaPF) {
            if (u.getNazwa().toLowerCase().contains(query)) {
                wynik.add(u);
            }
        }
        return wynik;
    }
    
    public String list() {
        refresh();
        return link;
    }

    public List<DcKontrahenci> getListaPF() {
        return listaPF;
    }

    public void setListaPF(List<DcKontrahenci> listaPF) {
        this.listaPF = listaPF;
    }

    public DataModel<DcKontrahenci> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcKontrahenci> lista) {
        this.lista = lista;
    }

    public DcKontrahenci getObiekt() {
        return obiekt;
    }

    public void setObiekt(DcKontrahenci obiekt) {
        this.obiekt = obiekt;
    }

    public DcKontahentDodEmail getEmailDod() {
        return emailDod;
    }

    public void setEmailDod(DcKontahentDodEmail emailDod) {
        this.emailDod = emailDod;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFiltrNazwa() {
        return filtrNazwa;
    }

    public void setFiltrNazwa(String filtrNazwa) {
        this.filtrNazwa = filtrNazwa;
    }

    public String getFiltrNip() {
        return filtrNip;
    }

    public void setFiltrNip(String filtrNip) {
        this.filtrNip = filtrNip;
    }

    public String getFiltrAdres() {
        return filtrAdres;
    }

    public void setFiltrAdres(String filtrAdres) {
        this.filtrAdres = filtrAdres;
    }
    
}
