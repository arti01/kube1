/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managed;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import org.richfaces.component.SortOrder;
import pl.eod.encje.Dzial;
import pl.eod.encje.DzialJpaController;
import pl.eod.encje.KomKolejka;
import pl.eod.encje.KomKolejkaJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.StrukturaDataModel;
import pl.eod.encje.StrukturaJpaController;
import pl.eod.encje.UserRoles;
import pl.eod.encje.UserRolesJpaController;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author 103039
 */
@ManagedBean(name = "UsersM")
@SessionScoped
public class UsersM implements Serializable {

    private static final long serialVersionUID = 1L;
    List<Uzytkownik> users = new ArrayList<Uzytkownik>();
    List<UserRoles> roleAll = new ArrayList<UserRoles>();
    List<Dzial> dzialyAll = new ArrayList<Dzial>();
    List<Struktura> kierownicyAll = new ArrayList<Struktura>();
    //DataModel<Struktura> struktury = new ListDataModel<Struktura>();
    UzytkownikJpaController userC;
    Uzytkownik user;
    List<UserRoles> rolesKlon;
    boolean edytuj = false;
    String nameFilter;
    Dzial dzialFilter;
    StrukturaJpaController struktC;
    UserRolesJpaController urC;
    Struktura strukt;
    DzialJpaController dzialC;
    @ManagedProperty(value = "#{login}")
    private Login login;
    boolean sprawdzacUnikEmail;
    private Map<String, String> filterValues = Maps.newHashMap();
    private Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);
    private StrukturaDataModel dataModel;
    private KomKolejkaJpaController KomKolC;
    String error;

    @PostConstruct
    public void init()  {
        userC = new UzytkownikJpaController();
        struktC = new StrukturaJpaController();
        dzialC = new DzialJpaController();
        urC = new UserRolesJpaController();
        login.refresh();
        dataModel = new StrukturaDataModel(login.zalogowany.getUserId().getSpolkaId());
        users = userC.findUzytkownikEntities(login.zalogowany.getUserId().getSpolkaId(), true);
        sortOrders.put("userId.fullname", SortOrder.descending);
        KomKolC = new KomKolejkaJpaController();
    }

    private void initUser() {
        strukt = new Struktura();
        strukt.setPrzyjmowanieWnioskow(false);
        user = new Uzytkownik();
        strukt.setUserId(user);
        Dzial dzial = new Dzial();
        strukt.setDzialId(dzial);
        //struktury = new ListDataModel<Struktura>();
        //struktury.setWrappedData(struktC.findStrukturaWidoczni(login.zalogowany.getUserId().getSpolkaId()));
        roleAll = urC.findDostepneDoEdycji();
        dzialyAll = dzialC.findDzialEntities(login.zalogowany.getUserId().getSpolkaId());
        kierownicyAll = struktC.getFindKierownicy(login.zalogowany.getUserId().getSpolkaId());
        users = userC.findUzytkownikEntities(login.zalogowany.getUserId().getSpolkaId(), true);
        //System.out.println(struktury.getRowCount()+"initUser");
    }

    public String lista() {
        edytuj = false;
        //nameFilter=null;
        //dzialFilter=new Dzial(new Long(0));
        initUser();
        return "/all/usersList";
    }

    public String nowy() {
        edytuj = false;
        nameFilter = null;
        dzialFilter = null;
        initUser();
        return "/all/usersAdd";
    }

    public String edycja() {
        rolesKlon = new CopyOnWriteArrayList<UserRoles>(strukt.getUserId().getRole());
        return "/all/usersEdit";
    }

    public String ustawZastepce() throws IOException {
        strukt = login.getZalogowany();
        rolesKlon = new CopyOnWriteArrayList<UserRoles>(strukt.getUserId().getRole());
        return "/common/ustawZastepce";
    }

    public String ustawZastepceZapisz() throws NonexistentEntityException, Exception {
        boolean czyWyslac=strukt.getSecUserId() != null;
        String adresMail="";
        if(czyWyslac) {
            adresMail=strukt.getSecUserId().getAdrEmail();
        }
        zapisz();
        if (error == null) {
            if (czyWyslac) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(adresMail);
                kk.setStatus(0);
                kk.setIdDokumenu(0);
                kk.setTemat("Informacja o ustanowieniu zastępcy");
                kk.setTresc("Pracownik " + login.getZalogowany().getUserId().getFullname() + " ustanowił Cię zastępcą. Możesz wykonywać w jego imieniu operacje na wnioskach urlopowych.");
                KomKolC.create(kk);
            }
            return "/logowanie/index?faces-redirect=true";
        } else {
            /*FacesMessage message = new FacesMessage(error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);*/
            return "/common/ustawZastepce";
        }
    }

    public String listaFiltr() {
        edytuj = false;
        initUser();
        //System.err.println("c"+nameFilter+"c");
        return "/all/usersList";
    }

    public void dodaj() throws NonexistentEntityException, Exception {
        String error = struktC.create(strukt);
        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
        } else {
            FacesMessage message = new FacesMessage("dodano");
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            initUser();
        }
    }

    public void usun() {
        struktC.zrobNiewidczony(strukt);
        initUser();
        edytuj = false;
    }

    @Deprecated
    public void usunOld() throws NonexistentEntityException, Exception {
        struktC.destroyArti(strukt);
        initUser();
        edytuj = false;
    }

    public String zapisz() throws NonexistentEntityException, Exception {
        //pozostają role nieedytowalne
       /* System.err.println(rolesKlon);
         System.err.println(strukt.getUserId().getRole());
         System.err.println(roleAll);*/


        //obsluga dla readonly roli w formularzu
        if (!strukt.getUserId().getRole().equals(rolesKlon) && rolesKlon != null) {
            rolesKlon.removeAll(roleAll);
            // System.err.println(rolesKlon);
            strukt.getUserId().getRole().addAll(rolesKlon);
        }
        error = struktC.editArti(strukt);
        if (error == null) {
            initUser();
            //error = "Zmiana wykonana";
            return "/all/usersList?faces-redirect=true";
        }
        FacesMessage message = new FacesMessage(error);
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        context.addMessage(zapisz.getClientId(context), message);
        edytuj = true;
        dataModel = new StrukturaDataModel(login.zalogowany.getUserId().getSpolkaId());
        return "/all/usersEdit";
    }

    public void kierListener(ValueChangeEvent e) {
        Boolean kier;
        kier = (Boolean) e.getNewValue();
        try {
            if (kier) {
                strukt.getDzialId().setNazwa("Nowy dział");
            } else {
                strukt.getDzialId().setNazwa(strukt.getSzefId().getDzialId().getNazwa());
            }
        } catch (NullPointerException ex) {
            //ex.printStackTrace();
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
        //strukt.setStKier(true);
    }

    public void changeKierListener(ValueChangeEvent e) throws NonexistentEntityException, Exception {
        Boolean kier;
        kier = (Boolean) e.getNewValue();
        strukt.setStKier(kier);
        String error = null;
        try {
            error = struktC.changeKier(strukt, strukt.getDzialId());
            strukt = struktC.findStruktura(strukt.getId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UsersM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UsersM.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (error != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, error, error);
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent kierownikE = UIComponent.getCurrentComponent(context);
            context.addMessage(kierownikE.getClientId(context), message);
        }
    }

    public void dzialListener(ValueChangeEvent e) throws NullPointerException {
        Struktura str = (Struktura) e.getNewValue();
        //System.out.println(strukt.getDzialId());
        if (!strukt.isStKier()) {
            if (str != null) {
                strukt.getDzialId().setNazwa(str.getDzialId().getNazwa());
                strukt.getDzialId().setSymbol(str.getDzialId().getSymbol());
            } else {
                strukt.getDzialId().setNazwa("");
                strukt.getDzialId().setSymbol("");
            }
        }
    }

    public List<Uzytkownik> getUsers() {
        return users;
    }

    public void setUsers(List<Uzytkownik> users) {
        this.users = users;
    }

    public Uzytkownik getUser() {
        return user;
    }

    public void setUser(Uzytkownik user) {
        this.user = user;
    }

    public boolean isEdytuj() {
        return edytuj;
    }

    public void setEdytuj(boolean edytuj) {
        this.edytuj = edytuj;
    }

    public String getNameFilter() {
        return nameFilter;
    }

    public void setNameFilter(String nameFilter) {
        edytuj = false;
        this.nameFilter = nameFilter;
    }

    public Dzial getDzialFilter() {
        return dzialFilter;
    }

    public void setDzialFilter(Dzial dzialFilter) {
        edytuj = false;
        this.dzialFilter = dzialFilter;
    }

    public DzialJpaController getDzialC() {
        return dzialC;
    }

    public StrukturaJpaController getStruktC() {
        return struktC;
    }

    public Struktura getStrukt() {
        return strukt;
    }

    public void setStrukt(Struktura strukt) {
        this.strukt = strukt;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<UserRoles> getRoleAll() {
        return roleAll;
    }

    public void setRoleAll(List<UserRoles> roleAll) {
        this.roleAll = roleAll;
    }

    public List<Dzial> getDzialyAll() {
        return dzialyAll;
    }

    public void setDzialyAll(List<Dzial> dzialyAll) {
        this.dzialyAll = dzialyAll;
    }

    public List<Struktura> getKierownicyAll() {
        return kierownicyAll;
    }

    public void setKierownicyAll(List<Struktura> kierownicyAll) {
        this.kierownicyAll = kierownicyAll;
    }

    public Object getDataModel() {
        //return new StrukturaDataModel();
        return dataModel;
    }

    public Map<String, String> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Map<String, String> filterValues) {
        this.filterValues = filterValues;
    }

    public Map<String, SortOrder> getSortOrders() {
        return sortOrders;
    }

    public void setSortOrders(Map<String, SortOrder> sortOrders) {
        this.sortOrders = sortOrders;
    }
}