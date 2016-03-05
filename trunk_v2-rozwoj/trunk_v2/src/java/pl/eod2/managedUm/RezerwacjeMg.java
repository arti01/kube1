package pl.eod2.managedUm;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
import pl.eod.abstr.AbstMg;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;
import pl.eod2.encje.UmUrzadzenie;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RezerwacjeMg")
@ViewScoped
public class RezerwacjeMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{UrzadzeniaMg}")
    private UrzadzeniaMg urzMg;

    private UmRezerwacjeKontr dcR;
    //private List<TreeNode> rootNodes = new ArrayList<>();
    private TreeNode root;
    private TreeNode urzadzenie;
    private ScheduleModel eventModel;
    private ScheduleEvent event = new DefaultScheduleEvent();

    private int wartTest = 20;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @PostConstruct
    private void init() {
        eventModel = new DefaultScheduleModel();
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
            eventModel.addEvent(new DefaultScheduleEvent(rez.getNazwa(), rez.getDataOd(), rez.getDataDo(), rez));
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }

    public void onEventMove(ScheduleEntryMoveEvent selectEvent) throws NonexistentEntityException, Exception{
        ScheduleEvent oldEvent = selectEvent.getScheduleEvent();
        obiekt = dcC.findObiekt(((UmRezerwacje)oldEvent.getData()).getId());
        obiekt.setDataOd(oldEvent.getStartDate());
        obiekt.setDataDo(oldEvent.getEndDate());
        edytuj();
        //FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());    
        //addMessage(message);
    }

    public void addEvent(ActionEvent actionEvent) throws InstantiationException, IllegalAccessException, NonexistentEntityException, Exception {
        if (event.getId() == null) {
            obiekt = new UmRezerwacje();
            obiekt.setNazwa(event.getTitle());
            obiekt.setDataOd(event.getStartDate());
            obiekt.setDataDo(event.getEndDate());
            obiekt.setTworca(login.getZalogowany().getUserId());
            obiekt.setUrzadzenie((UmUrzadzenie) urzadzenie.getData());
            if(!dodaj().isEmpty()){
                return;
            }
            ScheduleEvent newEvent = new DefaultScheduleEvent(event.getTitle(),event.getStartDate(),event.getEndDate(), obiekt);
            eventModel.addEvent(newEvent);
        } else {
            obiekt = (UmRezerwacje) event.getData();
            obiekt.setDataOd(event.getStartDate());
            obiekt.setDataDo(event.getEndDate());
            edytuj();
            eventModel.updateEvent(event);
        }
        event = new DefaultScheduleEvent();
    }

    public UmRezerwacjeKontr getDcR() {
        return dcR;
    }

    public void setDcR(UmRezerwacjeKontr dcR) {
        this.dcR = dcR;
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

    public int getWartTest() {
        return wartTest;
    }

    public void setWartTest(int wartTest) {
        this.wartTest = wartTest;
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

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

}
