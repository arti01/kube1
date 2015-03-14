/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managwn;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.richfaces.component.SortOrder;
import pl.eod.encje.KomKolejka;
import pl.eod.encje.KomKolejkaJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.WnHistoria;
import pl.eod.encje.WnRodzajeJpaController;
import pl.eod.encje.WnStatusy;
import pl.eod.encje.WnUrlop;
import pl.eod.encje.WnUrlopDataModel;
import pl.eod.encje.WnUrlopJpaController;
import pl.eod.managed.Login;

@ManagedBean(name = "UrlopM")
@SessionScoped
public class UrlopM implements Serializable {

    private static final long serialVersionUID = 1L;
    private WnUrlop urlop;
    private DataModel<WnUrlop> urlopyList = new ListDataModel<WnUrlop>();
    private DataModel<WnUrlop> urlopyAkcept = new ListDataModel<WnUrlop>();
    private DataModel<WnUrlop> urlopyAkceptHist = new ListDataModel<WnUrlop>();
    private WnUrlopJpaController urlopC;
    private WnRodzajeJpaController rodzajeC;
    private KomKolejkaJpaController KomKolC;
    @ManagedProperty(value = "#{login}")
    private Login login;
    private Locale locale;
    String nameAkceptHistFilter;
    String nameObceFilter;
    String namePodwFilter;
    private Map<String, String> filterValues = Maps.newHashMap();
    private Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);

    public String list() {
        initUrlop();
        return "/urlop/urlopyList";
    }

    public String listPodwl() {
        initUrlop();
        return "/urlop/urlopyListPodwl";
    }

    public String listPodwlHist() {
        initUrlop();
        return "/urlop/urlopyListAkceptHist";
    }

    public void anuluj() {
        String info = "";
        try {
            WnStatusy st = new WnStatusy();
            st.setId(new Long(6));
            urlop.setStatusId(st);
            //urlop.setAkceptant(login.getZalogowany().getSzefId().getUserId());

            WnHistoria wnh = new WnHistoria();
            wnh.setDataZmiany(new Date());
            WnStatusy st1 = new WnStatusy();
            st1.setId(new Long(6));
            wnh.setStatusId(st1);
            wnh.setZmieniajacy(urlop.getUzytkownik());
            wnh.setUrlopId(urlop);
            //wnh.setAkceptant(login.getZalogowany().getSzefId().getUserId());
            wnh.setOpisZmiany("anulowano po zaakceptowaniu");

            urlop.getWnHistoriaList().add(wnh);

            urlopC.createEdit(urlop);

            //wysylanie maila
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (urlop.isExtraemail()) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Informacja o anulowaniu wniosku urlopowego");
                kk.setTresc("Pracownik " + urlop.getUzytkownik().getFullname() + " anulował urlop " + urlop.getRodzajId().getOpis() + " wnioskowany w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku() + ". Dodatkowe informacje: " + urlop.getInfoDod());
                KomKolC.create(kk);
            }

            if (!login.getZalogowany().getSzefId().getUserId().getAdrEmail().equals("")) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(login.getZalogowany().getSzefId().getUserId().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Informacja o anulowaniu wniosku urlopowego");
                kk.setTresc("Pracownik " + urlop.getUzytkownik().getFullname() + " anulował urlop " + urlop.getRodzajId().getOpis() + " wnioskowany w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku() + ". Dodatkowe informacje: " + urlop.getInfoDod());
                KomKolC.create(kk);
            }
            info = "Wniosek anulowany";

        } catch (Exception ex) {
            //if(login.getZalogowany().getSzefId()==null) info = "nie można ustawić akceptanta, brak przełożonego";
            //else 
            info = "Coś poszło nie tak";
            ex.printStackTrace();
        } finally {
            initUrlop();
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            FacesMessage message = new FacesMessage();
            message.setSummary(info);
            context.addMessage(zapisz.getClientId(context), message);
        }
    }

    public void wyslij() {
        String info = "";
        String error = null;
        try {
            WnStatusy st = new WnStatusy();
            st.setId(new Long(2));
            urlop.setStatusId(st);
            urlop.setAkceptant(login.getZalogowany().getSzefId().getUserId());

            WnHistoria wnh = new WnHistoria();
            wnh.setDataZmiany(new Date());
            WnStatusy st1 = new WnStatusy();
            st1.setId(new Long(2));
            wnh.setStatusId(st1);
            wnh.setZmieniajacy(urlop.getUzytkownik());
            wnh.setUrlopId(urlop);
            wnh.setAkceptant(login.getZalogowany().getSzefId().getUserId());
            wnh.setOpisZmiany("wysłano do akceptu przełożonemu");

            urlop.getWnHistoriaList().add(wnh);

            error = urlopC.createEdit(urlop);
            String tresc;
            if (error == null) {
                //wysylanie maila
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (urlop.isExtraemail()) {
                    KomKolejka kk = new KomKolejka();
                    kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
                    kk.setStatus(0);
                    kk.setIdDokumenu(urlop.getId().intValue());
                    kk.setTemat("Informacja o wniosku urlopowym");
                    tresc = "Pracownik " + urlop.getUzytkownik().getFullname() + " wnioskuje o urlop " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku();
                    if (!urlop.getInfoDod().isEmpty()) {
                        tresc = tresc + ". Dodatkowe informacje: " + urlop.getInfoDod();
                    }
                    if (urlop.getUzytkownik().getStruktura().getSecUserId() != null) {
                        tresc = tresc + ". Na czas nieobecności pracownika, zastępuje go " + urlop.getUzytkownik().getStruktura().getSecUserId().getFullname() + " (email: " + urlop.getUzytkownik().getStruktura().getSecUserId().getAdrEmail() + ")";
                    }
                    kk.setTresc(tresc);

                    KomKolC.create(kk);
                }

                if (!urlop.getAkceptant().getAdrEmail().equals("")) {
                    KomKolejka kk = new KomKolejka();
                    kk.setAdresList(urlop.getAkceptant().getAdrEmail());
                    kk.setStatus(0);
                    kk.setIdDokumenu(urlop.getId().intValue());
                    kk.setTemat("Prośba o akceptację wniosku urlopowego");
                    tresc = "Proszę o akceptację wniosku urlopowego. " + "Pracownik " + urlop.getUzytkownik().getFullname() + " wnioskuje o urlop " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku();

                    if (!urlop.getInfoDod().isEmpty()) {
                        tresc = tresc + ". Dodatkowe informacje: " + urlop.getInfoDod();
                    }
                    if (urlop.getUzytkownik().getStruktura().getSecUserId() != null) {
                        tresc = tresc + ". Na czas nieobecności pracownika, zastępuje go " + urlop.getUzytkownik().getStruktura().getSecUserId().getFullname() + " (email: " + urlop.getUzytkownik().getStruktura().getSecUserId().getAdrEmail() + ")";
                    }
                    kk.setTresc(tresc);
                    KomKolC.create(kk);
                }
                info = "Wniosek wysłany";
            }
        } catch (Exception ex) {
            if (login.getZalogowany().getSzefId() == null) {
                info = "nie można ustawić akceptanta, brak przełożonego";
            } else {
                info = "Coś poszło nie tak";
            }
            ex.printStackTrace();
        } finally {
            initUrlop();
            FacesContext context = FacesContext.getCurrentInstance();
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            FacesMessage message = new FacesMessage();
            if (error != null) {
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary(error);
            } else {
                message.setSummary(info);
            }
            context.addMessage(zapisz.getClientId(context), message);
        }
    }

    public void akcept() {
        WnStatusy st = new WnStatusy();
        st.setId(new Long(3));
        urlop.setStatusId(st);
        urlop.setAkceptant(null);

        WnHistoria wnh = new WnHistoria();
        wnh.setDataZmiany(new Date());
        WnStatusy st1 = new WnStatusy();
        st1.setId(new Long(3));
        wnh.setStatusId(st1);
        wnh.setZmieniajacy(login.getZalogowany().getUserId());
        wnh.setUrlopId(urlop);
        wnh.setAkceptant(null);
        wnh.setOpisZmiany("Wniosek zaakceptowany");

        urlop.getWnHistoriaList().add(wnh);
        String error;
        error = urlopC.createEdit(urlop);
        if (error == null) {
            if (!urlop.getUzytkownik().getAdrEmail().equals("")) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek o urlop zaakceptowany");
                kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }

            if (urlop.getPrzyjmujacy() != null) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek o urlop zaakceptowany");
                kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }
            if (urlop.isExtraemail()) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " zaakceptowany");
                kk.setTresc("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " nr wniosku: " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }
        }
        initUrlop();
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        FacesMessage message = new FacesMessage();
        if (error != null) {
            message.setSummary(error);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
        } else {
            message.setSummary("Wniosek zaakceptowany");
        }
        context.addMessage(zapisz.getClientId(context), message);
    }

    public void odrzuc() {
        WnStatusy st = new WnStatusy();
        st.setId(new Long(4));
        urlop.setStatusId(st);
        urlop.setAkceptant(null);

        WnHistoria wnh = new WnHistoria();
        wnh.setDataZmiany(new Date());
        WnStatusy st1 = new WnStatusy();
        st1.setId(new Long(4));
        wnh.setStatusId(st1);
        wnh.setZmieniajacy(login.getZalogowany().getUserId());
        wnh.setUrlopId(urlop);
        wnh.setAkceptant(null);
        wnh.setOpisZmiany("Wniosek odrzucony");

        urlop.getWnHistoriaList().add(wnh);

        urlopC.createEdit(urlop);

        if (!urlop.getUzytkownik().getAdrEmail().equals("")) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop odrzucony");
            kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        if (urlop.getPrzyjmujacy() != null) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop odrzucony");
            kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        if (urlop.isExtraemail()) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " odrzucony");
            kk.setTresc("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " nr wniosku: " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        initUrlop();

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        FacesMessage message = new FacesMessage();
        message.setSummary("Wniosek odrzucony");
        context.addMessage(zapisz.getClientId(context), message);

    }

    public void cofnij() {
        WnStatusy st = new WnStatusy();
        st.setId(new Long(5));
        urlop.setStatusId(st);
        urlop.setAkceptant(null);

        WnHistoria wnh = new WnHistoria();
        wnh.setDataZmiany(new Date());
        WnStatusy st1 = new WnStatusy();
        st1.setId(new Long(5));
        wnh.setStatusId(st1);
        wnh.setZmieniajacy(login.getZalogowany().getUserId());
        wnh.setUrlopId(urlop);
        wnh.setAkceptant(null);
        wnh.setOpisZmiany("Wniosek cofnięty do wystawcy");

        urlop.getWnHistoriaList().add(wnh);

        System.err.println(urlopC.createEdit(urlop));

        if (!urlop.getUzytkownik().getAdrEmail().equals("")) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop cofnięty");
            kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }

        if (urlop.getPrzyjmujacy() != null) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop cofnięty");
            kk.setTresc("Twoj wniosek o urlop " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }
        
        if (urlop.isExtraemail()) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " cofnięty");
            kk.setTresc("Wniosek o urlop " + urlop.getUzytkownik().getFullname() + " nr wniosku: " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }

        initUrlop();

        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        FacesMessage message = new FacesMessage();
        message.setSummary("Wniosek cofnięty do wystawcy");
        context.addMessage(zapisz.getClientId(context), message);
    }

    public void usun() {
        urlopC.destroy(urlop);
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        FacesMessage message = new FacesMessage();
        message.setSummary("wniosek usunięty");
        context.addMessage(zapisz.getClientId(context), message);
        initUrlop();
    }

    public void dodaj() {
        WnStatusy st = new WnStatusy();
        st.setId(new Long(1));
        urlop.setStatusId(st);
        //urlop.setNrWniosku("ddddddddddd");
        urlop.setDataWprowadzenia(new Date());

        String error;
        if (urlop.getId() == null) {
            urlop.setWnHistoriaList(new ArrayList<WnHistoria>());
            WnHistoria wnh = new WnHistoria();
            wnh.setDataZmiany(new Date());
            wnh.setStatusId(st);
            wnh.setZmieniajacy(urlop.getUzytkownik());
            wnh.setUrlopId(urlop);
            wnh.setOpisZmiany("wprowadzono nowy wniosek");
            urlop.getWnHistoriaList().add(wnh);
            error = urlopC.createEdit(urlop);
        } else {
            error = urlopC.createEdit(urlop);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent zapisz = UIComponent.getCurrentComponent(context);
        FacesMessage message = new FacesMessage();
        if (error != null) {
            message.setSummary(error);
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
        } else {
            initUrlop();
            message.setSummary("wniosek zapisany");
            message.setSeverity(FacesMessage.SEVERITY_INFO);
        }
        context.addMessage(zapisz.getClientId(context), message);
    }

    @PostConstruct
    public void init() {
        urlopC = new WnUrlopJpaController();
        rodzajeC = new WnRodzajeJpaController();
        KomKolC = new KomKolejkaJpaController();
        initUrlop();
        sortOrders.put("id", SortOrder.descending);
    }

    private void initUrlop() {
        login.refresh();
        urlop = new WnUrlop();
        urlop.setUzytkownik(login.getZalogowany().getUserId());
        if(!login.getZalogowany().getExtraemail().isEmpty()){
            urlop.setExtraemail(true);
        }
        urlopyList.setWrappedData(login.getZalogowany().getUserId().getWnUrlopList());

        ArrayList<WnUrlop> akceptL = new ArrayList<WnUrlop>();
        akceptL.addAll(login.getZalogowany().getUserId().getWnUrlopListDoAkceptu());
        for (Struktura s : login.getZalogowany().getUserId().getStrukturaSec()) {
            akceptL.addAll(s.getUserId().getWnUrlopListDoAkceptu());
        }
        urlopyAkcept.setWrappedData(akceptL);

        ArrayList<WnUrlop> akceptHist = new ArrayList<WnUrlop>();
        for (WnHistoria wh : login.getZalogowany().getUserId().getWnHistoriaListAkceptant()) {
            if (!akceptHist.contains(wh.getUrlopId())) {
                akceptHist.add(wh.getUrlopId());
            }
        }
        urlopyAkceptHist.setWrappedData(akceptHist);
        login.refresh();
    }

    public WnUrlop getUrlop() {
        return urlop;
    }

    public void setUrlop(WnUrlop urlop) {
        this.urlop = urlop;
    }

    public DataModel<WnUrlop> getUrlopyList() {
        return urlopyList;
    }

    public void setUrlopyList(DataModel<WnUrlop> urlopyList) {
        this.urlopyList = urlopyList;
    }

    public WnRodzajeJpaController getRodzajeC() {
        return rodzajeC;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Locale getLocale() {
        locale = new Locale("pl", "PL");
        return locale;
    }

    public DataModel<WnUrlop> getUrlopyAkcept() {
        return urlopyAkcept;
    }

    public void setUrlopyAkcept(DataModel<WnUrlop> urlopyAkcept) {
        this.urlopyAkcept = urlopyAkcept;
    }

    public DataModel<WnUrlop> getUrlopyAkceptHist() {
        return urlopyAkceptHist;
    }

    public String getNameAkceptHistFilter() {
        return nameAkceptHistFilter;
    }

    public void setNameAkceptHistFilter(String nameAkceptHistFilter) {
        this.nameAkceptHistFilter = nameAkceptHistFilter;
    }

    public String getNameObceFilter() {
        return nameObceFilter;
    }

    public void setNameObceFilter(String nameObceFilter) {
        this.nameObceFilter = nameObceFilter;
    }

    public String getNamePodwFilter() {
        return namePodwFilter;
    }

    public void setNamePodwFilter(String namePodwFilter) {
        this.namePodwFilter = namePodwFilter;
    }

    public WnUrlopJpaController getUrlopC() {
        return urlopC;
    }

    public void setUrlopC(WnUrlopJpaController urlopC) {
        this.urlopC = urlopC;
    }

    public Object getDataModel() {
        return new WnUrlopDataModel();
    }

    public Map<String, String> getFilterValues() {
        return filterValues;
    }

    public Map<String, SortOrder> getSortOrders() {
        return sortOrders;
    }
}
