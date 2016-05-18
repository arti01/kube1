package pl.eod2.managedUm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import pl.eod.abstr.AbstMg;
import pl.eod.encje.Struktura;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod.encje.WnUrlop;
import pl.eod.managed.Login;
import pl.eod2.encje.KalendOsobyDec;
import pl.eod2.encje.KalendOsobyDecController;
import pl.eod2.encje.Kalendarz;
import pl.eod2.encje.KalendarzKontr;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "RezerMojKalMg")
@SessionScoped
public class RezerMojKalMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty(value = "#{login}")
    private Login login;

    private ScheduleModel eventModel;
    private DefaultScheduleEvent event = new DefaultScheduleEvent();
    private int number2 = 3;
    private Uzytkownik uzyt;
    private String typObiekt;
    private Kalendarz obiektKal;
    Date initDate;
    private List<Uzytkownik> usersList;
    private List<Uzytkownik> usersListSelect;
    private final static KalendarzKontr DCCKAL = new KalendarzKontr();
    private final static KalendOsobyDecController KALODEC = new KalendOsobyDecController();
    private final UzytkownikJpaController userC = new UzytkownikJpaController();
    private List<MojKalTab> kalTabela;
    private List<KalendOsobyDec> kalOsDesList;

    public RezerMojKalMg() throws InstantiationException, IllegalAccessException {
        super("/um/rez_moj_kal", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @PostConstruct
    private void init() {
        eventModel = new DefaultScheduleModel();
        uzyt = login.getZalogowany().getUserId();
        try {
            refresh();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(RezerMojKalMg.class.getName()).log(Level.SEVERE, null, ex);
        }
        initDate = Calendar.getInstance().getTime();
        usersList = new ArrayList<>();
        usersListSelect = new ArrayList<>();
        kalTabela = new ArrayList<>();
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
        ustawSched();
    }

    @Override
    public String list() throws InstantiationException, IllegalAccessException {
        uzyt = login.getZalogowany().getUserId();
        ustawSched();
        return super.list();
    }

    public String listTab() throws InstantiationException, IllegalAccessException {
        uzyt = login.getZalogowany().getUserId();
        ustawSched();
        return "/um/rez_kal_tab";
    }

    public String listKalDecyz() throws InstantiationException, IllegalAccessException {
        uzyt = login.getZalogowany().getUserId();
        kalOsDesList=KALODEC.findEntities();
        ustawDlaWielu(kalOsDesList);
        return "/um/kal_decyz";
    }

    public String listObcy() throws InstantiationException, IllegalAccessException {
        ustawSched();
        return super.list();
    }

    public List<Uzytkownik> uzytkownicyAucoComp(String query) {
        query = query.toLowerCase();
        List<Uzytkownik> wynik = new ArrayList<>();
        List<Struktura> wyszuk = new ArrayList<>();
        wyszuk.addAll(login.getZalogowany().getSzefId().getWszyscyPodwladni());
        wyszuk.add(login.getZalogowany().getSzefId());
        for (Struktura st : wyszuk) {
            Uzytkownik u = st.getUserId();
            if (u.getAdrEmail().toLowerCase().contains(query) || u.getFullname().toLowerCase().contains(query)) {
                wynik.add(u);
            }
        }
        return wynik;
    }

    public void ustawDlaWielu(List<KalendOsobyDec> decyzyjni) {
        eventModel.clear();
        for (KalendOsobyDec dec : decyzyjni) {
            for (Kalendarz kal : dec.getUser().getKalendarzList()) {
                if (kal.isPrywatne()) {
                    continue;
                }
                DefaultScheduleEvent ev = new DefaultScheduleEvent(kal.getNazwa(), kal.getDataOd(), kal.getDataDo(), kal);
                ev.setDescription(kal.getOpis());
                ev.setEditable(false);
                ev.setStyleClass(dec.getCssStyl());
                eventModel.addEvent(ev);
            }
            for (Kalendarz kal : dec.getUser().getKalendUczestnikList()) {
                if (kal.isPrywatne()) {
                    continue;
                }
                DefaultScheduleEvent ev = new DefaultScheduleEvent("Uczestnik w " + kal.getNazwa(), kal.getDataOd(), kal.getDataDo(), kal);
                ev.setDescription(kal.getOpis());
                ev.setEditable(false);
                ev.setStyleClass(dec.getCssStyl());
                eventModel.addEvent(ev);
            }
        }
    }

    public void ustawSched() {
        if (!uzyt.equals(login.getZalogowany().getUserId())) {
            userC.refresh(uzyt);
        }
        eventModel.clear();
        kalTabela = new ArrayList<>();
        for (UmRezerwacje rez : uzyt.getRezUczestnikList()) {
            DefaultScheduleEvent ev = new DefaultScheduleEvent(rez.getNazwa() + "\ndla: " + rez.getUrzadzenie().getNazwa(), rez.getDataOd(), rez.getDataDo(), rez);
            ev.setDescription(rez.getOpis());
            ev.setEditable(false);
            MojKalTab mkt = new MojKalTab();
            mkt.setDataOd(rez.getDataOd());
            mkt.setDataDo(rez.getDataDo());
            mkt.setNazwa(rez.getNazwa());
            mkt.setOpis("urządzenie: " + rez.getUrzadzenie().getNazwa() + " " + rez.getOpis());
            mkt.setTworca(rez.getTworca());
            mkt.setTyp("tworca rezerwacji");
            if (!rez.getTworca().equals(login.getZalogowany().getUserId())) {
                mkt.setTyp("uczestnik rezerwacji");
                ev.setStyleClass("rezUczestnik");
            }
            kalTabela.add(mkt);
            eventModel.addEvent(ev);
        }
        for (Kalendarz kal : uzyt.getKalendarzList()) {
            //jesli to obcy kalendarz i zdazenie jest prywatne to nie pokazuj
            if (!uzyt.equals(login.getZalogowany().getUserId()) && kal.isPrywatne()) {
                continue;
            }
            DefaultScheduleEvent ev = new DefaultScheduleEvent(kal.getNazwa(), kal.getDataOd(), kal.getDataDo(), kal);
            ev.setDescription(kal.getOpis());
            MojKalTab mkt = new MojKalTab();
            mkt.setDataOd(kal.getDataOd());
            mkt.setDataDo(kal.getDataDo());
            mkt.setNazwa(kal.getNazwa());
            mkt.setOpis(kal.getOpis());
            mkt.setTworca(kal.getTworca());
            if (uzyt.equals(login.getZalogowany().getUserId())) {
                ev.setEditable(true);
                ev.setStyleClass("calMoj");
                mkt.setTyp("tworca terminu");
            } else {
                ev.setEditable(false);
                ev.setStyleClass("calUczestnik");
                mkt.setTyp("uczestnik terminu");
            }
            if (kal.getTyp() != null && kal.getTyp().equals("urlop")) {
                mkt.setTyp("urlop");
                ev.setStyleClass("calUrlop");
                ev.setEditable(false);
            }
            kalTabela.add(mkt);
            eventModel.addEvent(ev);
        }
        for (Kalendarz kal : uzyt.getKalendUczestnikList()) {
            //jesli to obcy kalendarz i zdazenie jest prywatne to nie pokazuj
            if (!uzyt.equals(login.getZalogowany().getUserId()) && kal.isPrywatne()) {
                continue;
            }
            DefaultScheduleEvent ev = new DefaultScheduleEvent(kal.getNazwa(), kal.getDataOd(), kal.getDataDo(), kal);
            ev.setDescription(kal.getOpis());
            ev.setEditable(false);
            ev.setStyleClass("calUczestnik");
            MojKalTab mkt = new MojKalTab();
            mkt.setDataOd(kal.getDataOd());
            mkt.setDataDo(kal.getDataDo());
            mkt.setNazwa(kal.getNazwa());
            mkt.setOpis(kal.getOpis());
            mkt.setTworca(kal.getTworca());
            mkt.setTyp("uczestnik terminu");
            if (kal.getTyp() != null && kal.getTyp().equals("urlop")) {
                mkt.setTyp("urlop");
                ev.setStyleClass("calUrlop");
            }
            kalTabela.add(mkt);
            eventModel.addEvent(ev);
        }
    }

    public void addEventObj() throws InstantiationException, IllegalAccessException, NonexistentEntityException, Exception {
        if (event.getId() == null) {
            DefaultScheduleEvent newEvent = new DefaultScheduleEvent(obiektKal.getNazwa(), obiektKal.getDataOd(), obiektKal.getDataDo(), obiektKal);
            newEvent.setStyleClass("calMoj");
            newEvent.setDescription(obiektKal.getOpis());
            eventModel.addEvent(newEvent);
            if (!DCCKAL.create(obiektKal).isEmpty()) {
                eventModel.deleteEvent(newEvent);
                return;
            }
        } else {
            Date stOd = obiektKal.getDataOd();
            Date stDo = obiektKal.getDataDo();
            String tyt = obiektKal.getNazwa();
            String desc = obiektKal.getOpis();
            if (!DCCKAL.edit(obiektKal).isEmpty()) {
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
        //obiektKal = (Kalendarz) event.getData();
        DCCKAL.destroy(obiektKal);
        eventModel.deleteEvent(event);
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
        obiektKal = DCCKAL.findObiekt(((Kalendarz) oldEvent.getData()).getId());
        Date stOd = obiektKal.getDataOd();
        Date stDo = obiektKal.getDataDo();
        obiektKal.setDataOd(oldEvent.getStartDate());
        obiektKal.setDataDo(oldEvent.getEndDate());
        if (!DCCKAL.edit(obiektKal).isEmpty()) {
            oldEvent.setStartDate(stOd);
            oldEvent.setEndDate(stDo);
        }
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (DefaultScheduleEvent) selectEvent.getObject();
        if (event.getData().getClass().getName().equals(UmRezerwacje.class.getName())) {
            typObiekt = "rezer";
            obiekt = dcC.findObiekt(((UmRezerwacje) event.getData()).getId());
        } else if (event.getData().getClass().getName().equals(Kalendarz.class.getName())) {
            if (event.isEditable()) {
                typObiekt = "calMoj";
            } else {
                typObiekt = "calUczestnik";
            }
            obiektKal = DCCKAL.findObiekt(((Kalendarz) event.getData()).getId());
            usersListSelect.clear();
            usersListSelect.addAll(usersList);
            usersListSelect.removeAll(obiektKal.getUczestnikList());
        }
    }

    public void onDateSelect(SelectEvent selectEvent) {
        typObiekt = "calMoj";
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
        event.setStyleClass("calMoj");
        obiektKal = new Kalendarz();
        obiektKal.setNazwa("nowy termin");
        obiektKal.setTworca(login.getZalogowany().getUserId());
        Calendar cal = Calendar.getInstance();
        cal.setTime(event.getStartDate());
        cal.add(Calendar.HOUR, 6);
        obiektKal.setDataOd(cal.getTime());
        initDate = event.getStartDate();
        cal.setTime(event.getEndDate());
        cal.add(Calendar.HOUR, 7);
        obiektKal.setDataDo(cal.getTime());
        usersListSelect.clear();
        usersListSelect.addAll(usersList);
    }

    public void onAddUsers(SelectEvent event) {
        Uzytkownik u = (Uzytkownik) event.getObject();
        usersListSelect.remove(u);
    }

    public void onDelUsers(UnselectEvent event) {
        Uzytkownik u = (Uzytkownik) event.getObject();
        usersListSelect.add(u);
    }

    public void onSlideEnd(SlideEndEvent event) {
        number2 = event.getValue();
    }

    public List<Uzytkownik> dostepniList(String cos) {
        List<Uzytkownik> wynik = new ArrayList<>();
        if (obiektKal.getUczestnikList() == null) {
            obiektKal.setUczestnikList(new ArrayList<>());
        }
        for (Uzytkownik u : usersListSelect) {
            if (u.getAdrEmail().toLowerCase().contains(cos.toLowerCase()) || u.getFullname().toLowerCase().contains(cos.toLowerCase())) {
                wynik.add(u);
            }
        }
        wynik.remove(login.getZalogowany().getUserId());
        return wynik;
    }

    public void newObiektKal() {
        obiektKal = new Kalendarz();
    }

    public static void NieobecnoscDodaj(WnUrlop urlop) {
        Kalendarz kal = new Kalendarz();
        kal.setDataOd(urlop.getDataOd());
        kal.setDataDo(urlop.getDataDo());
        kal.setNazwa("Nieobecność");
        kal.setOpis(urlop.getRodzajId().getOpis());
        kal.setTworca(urlop.getUzytkownik());
        kal.setTyp("urlop");
        DCCKAL.edit(kal);
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
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

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public Uzytkownik getUzyt() {
        return uzyt;
    }

    public void setUzyt(Uzytkownik uzyt) {
        this.uzyt = uzyt;
    }

    public String getTypObiekt() {
        return typObiekt;
    }

    public void setTypObiekt(String typObiekt) {
        this.typObiekt = typObiekt;
    }

    public Kalendarz getObiektKal() {
        return obiektKal;
    }

    public void setObiektKal(Kalendarz obiektKal) {
        this.obiektKal = obiektKal;
    }

    public Date getInitDate() {
        return initDate;
    }

    public void setInitDate(Date initDate) {
        this.initDate = initDate;
    }

    public List<Uzytkownik> getUsersListSelect() {
        return usersListSelect;
    }

    public void setUsersListSelect(List<Uzytkownik> usersListSelect) {
        this.usersListSelect = usersListSelect;
    }

    public List<MojKalTab> getKalTabela() {
        return kalTabela;
    }

    public void setKalTabela(List<MojKalTab> kalTabela) {
        this.kalTabela = kalTabela;
    }

    public List<KalendOsobyDec> getKalOsDesList() {
        return kalOsDesList;
    }

}
