package pl.eod2.managedUm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
import pl.eod.abstr.AbstMg;
import pl.eod.encje.Uzytkownik;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;
import pl.eod2.encje.UmUrzadzenie;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RezerwacjeMg")
@SessionScoped
public class RezerwacjeMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{UrzadzeniaMg}")
    private UrzadzeniaMg urzMg;

    //private List<TreeNode> rootNodes = new ArrayList<>();
    private TreeNode root;
    private TreeNode urzadzenie;
    private ScheduleModel eventModel;
    private DefaultScheduleEvent event = new DefaultScheduleEvent();
    private List<Uzytkownik> usersList;
    private List<Uzytkownik> usersListSelect;
    private List<UmUrzadzenie> urzAll;
    private String viewKal;
    private Date initDate;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @PostConstruct
    private void init() {
        initDate = Calendar.getInstance().getTime();
        eventModel = new DefaultScheduleModel();
        usersList = new ArrayList<>();
        usersListSelect = new ArrayList<>();
        zrobDrzewo(false);
        for (Uzytkownik u : login.getZalogowany().getUserId().getSpolkaId().getUserList()) {
            if (!u.getStruktura().isUsuniety()) {
                usersList.add(u);
            }
        }
        usersListSelect.addAll(usersList);
    }

    @Override
    public void refresh() throws InstantiationException, IllegalAccessException {
        super.refresh();
        login.refresh();
        zrobDrzewo(false);
    }

    public void zrobDrzewo(boolean all) {
        urzAll = new ArrayList<>();
        root = new DefaultTreeNode("urządzenia", null);
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        for (UmMasterGrupa mg : masterList) {
            if (!mg.isGrZrezerwacja()&&!all) {
                continue;
            }
            TreeNode mtr = new DefaultTreeNode("grupa", mg, root);
            for (UmGrupa gr : mg.getGrupaList()) {
                if (!gr.isRezerwacje()&&!all) {
                    continue;
                }
                TreeNode gtr = new DefaultTreeNode("grupa", gr, mtr);
                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    TreeNode utr = new DefaultTreeNode("urzadzenie", uz, gtr);
                    urzAll.add(uz);
                }
            }
        }
    }

    public void ustawSched() {
        if (!this.urzadzenie.getType().equals("urzadzenie")) {
            return;
        }
        UmUrzadzenie urz = (UmUrzadzenie) this.urzadzenie.getData();
        urz = urzMg.getDcC().findUmUrzadzenie(urz.getId());
        eventModel.clear();
        for (UmRezerwacje rez : urz.getRezerwacjeList()) {
            DefaultScheduleEvent ev = new DefaultScheduleEvent(rez.getNazwa(), rez.getDataOd(), rez.getDataDo(), rez);
            //Może edytować osoba z prawami rezerwacji, tworca lub zastępca            
            ev.setEditable(rez.getTworca().equals(login.isUmRez()) || rez.getTworca().equals(login.getZalogowany().getUserId())
                    || rez.getTworca().equals(login.getZalogowany().getSecUserId())
            );
            ev.setDescription(rez.getOpis());
            if (!ev.isEditable()) {
                ev.setStyleClass("rezUczestnik");
            }
            eventModel.addEvent(ev);
        }
    }

    public void ustawSchedCompl(SelectEvent event) {
        UmUrzadzenie urz = (UmUrzadzenie) event.getObject();
        urz = urzMg.getDcC().findUmUrzadzenie(urz.getId());
        urzadzenie = new DefaultTreeNode("urzadzenie", urz, null);
        eventModel.clear();
        for (UmRezerwacje rez : urz.getRezerwacjeList()) {
            DefaultScheduleEvent ev = new DefaultScheduleEvent(rez.getNazwa(), rez.getDataOd(), rez.getDataDo(), rez);
            //Może edytować osoba z prawami rezerwacji, tworca lub zastępca
            ev.setEditable(rez.getTworca().equals(login.isUmRez()) || rez.getTworca().equals(login.getZalogowany().getUserId())
                    || rez.getTworca().equals(login.getZalogowany().getSecUserId()));
            eventModel.addEvent(ev);
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        //sprawdzenie czy może dodać do urządzenia rezerwacje
        UmUrzadzenie urz = (UmUrzadzenie) urzadzenie.getData();
        if (login.isUmRez() || urz.getUserOdpow().equals(login.getZalogowany().getUserId())
                || urz.getUserOdpow().equals(login.getZalogowany().getSecUserId())) {
            event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
            obiekt = new UmRezerwacje();
            obiekt.setNazwa("nowa rezerwacja");
            Calendar cal = Calendar.getInstance();
            cal.setTime(event.getStartDate());
            cal.add(Calendar.HOUR, 6);
            obiekt.setDataOd(cal.getTime());
            initDate = event.getStartDate();
            cal.setTime(event.getEndDate());
            cal.add(Calendar.HOUR, 7);
            obiekt.setDataDo(cal.getTime());
            usersListSelect.clear();
            usersListSelect.addAll(usersList);
        } else {
            pfMess(FacesMessage.SEVERITY_WARN, "brak praw do zasobu", "brak praw do zasobu");
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        obiekt = dcC.findObiekt(((UmRezerwacje) event.getData()).getId());
        usersListSelect.clear();
        usersListSelect.addAll(usersList);
        usersListSelect.removeAll(obiekt.getUczestnikList());
    }

    public void onEventMove(ScheduleEntryMoveEvent selectEvent) throws NonexistentEntityException, Exception {
        DefaultScheduleEvent oldEvent = (DefaultScheduleEvent) selectEvent.getScheduleEvent();
        ruchEvent(oldEvent);
    }

    public void onEventResize(ScheduleEntryResizeEvent selectEvent) throws NonexistentEntityException, Exception {
        DefaultScheduleEvent oldEvent = (DefaultScheduleEvent) selectEvent.getScheduleEvent();
        ruchEvent(oldEvent);
    }

    private void ruchEvent(DefaultScheduleEvent oldEvent) throws NonexistentEntityException, Exception {
        obiekt = dcC.findObiekt(((UmRezerwacje) oldEvent.getData()).getId());
        Date stOd = obiekt.getDataOd();
        Date stDo = obiekt.getDataDo();
        obiekt.setDataOd(oldEvent.getStartDate());
        obiekt.setDataDo(oldEvent.getEndDate());
        if (!edytuj().isEmpty()) {
            oldEvent.setStartDate(stOd);
            oldEvent.setEndDate(stDo);
        }
    }

    public void addEventObj() throws InstantiationException, IllegalAccessException, NonexistentEntityException, Exception {
        if (event.getId() == null) {
            obiekt.setTworca(login.getZalogowany().getUserId());
            obiekt.setUrzadzenie((UmUrzadzenie) urzadzenie.getData());
            /*Date stOd = obiekt.getDataOd();
            Date stDo = obiekt.getDataDo();*/
            DefaultScheduleEvent newEvent = new DefaultScheduleEvent(obiekt.getNazwa(), obiekt.getDataOd(), obiekt.getDataDo(), obiekt);
            newEvent.setDescription(obiekt.getOpis());
            eventModel.addEvent(newEvent);
            if (!dodaj().isEmpty()) {
                eventModel.deleteEvent(newEvent);
                return;
            }
        } else {
            Date stOd = obiekt.getDataOd();
            Date stDo = obiekt.getDataDo();
            String tyt = obiekt.getNazwa();
            String desc = obiekt.getOpis();
            if (!edytuj().isEmpty()) {
                return;
            }
            event.setTitle(tyt);
            event.setStartDate(stOd);
            event.setEndDate(stDo);
            event.setDescription(desc);
            eventModel.updateEvent(event);
        }
        event = new DefaultScheduleEvent();
    }

    public void delEvent(ActionEvent actionEvent) throws IllegalOrphanException, NonexistentEntityException, InstantiationException, IllegalAccessException {
        obiekt = (UmRezerwacje) event.getData();
        usun();
        refresh();
        eventModel.deleteEvent(event);
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public TreeNode getUrzadzenie() {
        return urzadzenie;
    }

    public void setUrzadzenie(TreeNode urzadzenie) {
        this.urzadzenie = urzadzenie;
    }

    public UrzadzeniaMg getUrzMg() {
        return urzMg;
    }

    public void setUrzMg(UrzadzeniaMg urzMg) {
        this.urzMg = urzMg;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    public DefaultScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(DefaultScheduleEvent event) {
        this.event = event;
    }

    public List<Uzytkownik> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Uzytkownik> usersList) {
        this.usersList = usersList;
    }

    public List<Uzytkownik> dostepniList(String cos) {
        List<Uzytkownik> wynik = new ArrayList<>();
        if (obiekt.getUczestnikList() == null) {
            obiekt.setUczestnikList(new ArrayList<>());
        }
        //usersList.removeAll(obiekt.getUczestnikList());
        for (Uzytkownik u : usersListSelect) {
            if (u.getAdrEmail().toLowerCase().contains(cos.toLowerCase()) || u.getFullname().toLowerCase().contains(cos.toLowerCase())) {
                wynik.add(u);
            }
        }
        return wynik;
    }

    public List<UmUrzadzenie> urzAllList(String cos) {
        List<UmUrzadzenie> wynik = new ArrayList<>();

        for (UmUrzadzenie urz : urzAll) {
            if (urz.getNazwa().toLowerCase().contains(cos.toLowerCase())
                    || urz.getGrupa().getNazwa().toLowerCase().contains(cos.toLowerCase())
                    || urz.getGrupa().getMasterGrp().getNazwa().toLowerCase().contains(cos.toLowerCase())) {
                wynik.add(urz);
            }
        }
        return wynik;
    }

    public void onAddUsers(SelectEvent event) {
        Uzytkownik u = (Uzytkownik) event.getObject();
        usersListSelect.remove(u);
    }

    public void onDelUsers(UnselectEvent event) {
        Uzytkownik u = (Uzytkownik) event.getObject();
        usersListSelect.add(u);
    }

    public List<Uzytkownik> uzytkownicyAucoComp(String query) {
        query = query.toLowerCase();
        List<Uzytkownik> wynik = new ArrayList<>();
        for (Uzytkownik u : usersList) {
            if (u.getAdrEmail().toLowerCase().contains(query) || u.getFullname().toLowerCase().contains(query)) {
                wynik.add(u);
            }
        }
        return wynik;
    }

    public String getViewKal() {
        if (viewKal == null) {
            viewKal = "month";
        }
        return viewKal;
    }

    public void setViewKal(String viewKal) {
        this.viewKal = viewKal;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

}
