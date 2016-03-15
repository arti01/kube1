package pl.eod2.managedUm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
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

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @PostConstruct
    private void init() {
        eventModel = new DefaultScheduleModel();
        usersList = new ArrayList<>();
        for (Uzytkownik u : login.getZalogowany().getUserId().getSpolkaId().getUserList()) {
            if (!u.getStruktura().isUsuniety()) {
                usersList.add(u);
            }
        }
        /*login.getZalogowany().getUserId().getSpolkaId().getUserList().stream().filter((u) -> (!u.getStruktura().isUsuniety())).forEach((u) -> {
            usersList.add(u);
        });*/
        zrobDrzewo();
    }

    @Override
    public void refresh() throws InstantiationException, IllegalAccessException {
        super.refresh();
        login.refresh();
        zrobDrzewo();
    }

    public void zrobDrzewo() {
        root = new DefaultTreeNode("urządzenia", null);
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        for (UmMasterGrupa mg : masterList) {
            if (!mg.isGrZrezerwacja()) {
                continue;
            }
            TreeNode mtr = new DefaultTreeNode("grupa", mg, root);
            for (UmGrupa gr : mg.getGrupaList()) {
                if (!gr.isRezerwacje()) {
                    continue;
                }
                TreeNode gtr = new DefaultTreeNode("grupa", gr, mtr);
                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    TreeNode utr = new DefaultTreeNode("urzadzenie", uz, gtr);
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
            ev.setEditable(rez.getTworca().equals(login.getZalogowany().getUserId()));
            eventModel.addEvent(ev);
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        obiekt = new UmRezerwacje();
        obiekt.setNazwa("nowa rezerwacja");
        obiekt.setDataOd(event.getStartDate());
        obiekt.setDataDo(event.getEndDate());
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        obiekt = obiekt = dcC.findObiekt(((UmRezerwacje) event.getData()).getId());
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
            Date stOd = obiekt.getDataOd();
            Date stDo = obiekt.getDataDo();
            if (!dodaj().isEmpty()) {
                return;
            }
            DefaultScheduleEvent newEvent = new DefaultScheduleEvent(obiekt.getNazwa(), stOd, stDo, obiekt);
            eventModel.addEvent(newEvent);
        } else {
            Date stOd = obiekt.getDataOd();
            Date stDo = obiekt.getDataDo();
            String tyt = obiekt.getNazwa();
            if (!edytuj().isEmpty()) {
                return;
            }
            event.setTitle(tyt);
            event.setStartDate(stOd);
            event.setEndDate(stDo);
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
        System.err.println(usersList);
        if (obiekt.getUczestnikList() == null) {
            obiekt.setUczestnikList(new ArrayList<>());
        }
        System.err.println(obiekt.getUczestnikList());
        //System.err.println(obiekt.getUczestnikList());
        usersList.removeAll(obiekt.getUczestnikList());
        System.err.println(usersList);
        for (Uzytkownik u : usersList) {
            if (u.getAdrEmail().toLowerCase().contains(cos.toLowerCase()) || u.getFullname().toLowerCase().contains(cos.toLowerCase())) {
                wynik.add(u);
            }
        }
        return wynik;
    }
    public void onChangeUsers(ValueChangeEvent event){
        event.getNewValue();
    }
}
