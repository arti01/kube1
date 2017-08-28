/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managwn;

import com.google.common.collect.Maps;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.richfaces.component.SortOrder;
import pl.eod.encje.ConfigJpaController;
import pl.eod.encje.KomKolejka;
import pl.eod.encje.KomKolejkaJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.WnHistoria;
import pl.eod.encje.WnRodzajeJpaController;
import pl.eod.encje.WnStatusy;
import pl.eod.encje.WnUrlop;
import pl.eod.encje.WnUrlopDataModel;
import pl.eod.encje.WnUrlopJpaController;
//import pl.eod.encje.WnUrlop_;
import pl.eod.managed.Login;
import pl.eod2.managedUm.RezerMojKalMg;

@ManagedBean(name = "UrlopM")
@SessionScoped
public class UrlopM implements Serializable {

    private static final long serialVersionUID = 1L;
    private WnUrlop urlop;
    private DataModel<WnUrlop> urlopyList = new ListDataModel<>();
    private DataModel<WnUrlop> urlopyAkcept = new ListDataModel<>();
    private final DataModel<WnUrlop> urlopyAkceptHist = new ListDataModel<>();
    private WnUrlopJpaController urlopC;
    private WnRodzajeJpaController rodzajeC;
    private KomKolejkaJpaController KomKolC;
    @ManagedProperty(value = "#{login}")
    private Login login;
    private Locale locale;
    String nameAkceptHistFilter;
    String nameObceFilter;
    String namePodwFilter;
    private final Map<String, String> filterValues = Maps.newHashMap();
    private final Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);
    //private String godzOd;
    //private String godzDo;
    private Date godzOdT;
    private Date godzDoT;
    private Date dataUrlopu;
    
    private boolean centralAccess = true;

    public String list() {
        initUrlop();
        return "/urlop/urlopyList";
    }

    public String listPodwl() {
        initUrlop();
        ArrayList<WnUrlop> akceptL = new ArrayList<>();
        akceptL.addAll(login.getZalogowany().getUserId().getWnUrlopListDoAkceptu());
        for (Struktura s : login.getZalogowany().getUserId().getStrukturaSec()) {
            akceptL.addAll(s.getUserId().getWnUrlopListDoAkceptu());
        }
        urlopyAkcept.setWrappedData(akceptL);
        return "/urlop/urlopyListPodwl";
    }

    public String listPodwlHist() {
        //initUrlop();
        ArrayList<WnUrlop> akceptHist = new ArrayList<>();
        for (WnHistoria wh : login.getZalogowany().getUserId().getWnHistoriaListAkceptant()) {
            if (!akceptHist.contains(wh.getUrlopId())) {
                akceptHist.add(wh.getUrlopId());
            }
        }
        urlopyAkceptHist.setWrappedData(akceptHist);
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
            wnh.setOpisZmiany("Anulowano po zaakceptowaniu");

            urlop.getWnHistoriaList().add(wnh);

            urlopC.createEdit(urlop);

            //wysylanie maila
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (urlop.isExtraemail()) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Informacja o anulowaniu wniosku");
                kk.setTresc("Pracownik " + urlop.getUzytkownik().getFullname() + " anulował wniosek: " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku() + ". Dodatkowe informacje: " + urlop.getInfoDod());
                KomKolC.create(kk);
            }

            if (!login.getZalogowany().getSzefId().getUserId().getAdrEmail().equals("")) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(login.getZalogowany().getSzefId().getUserId().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Informacja o anulowaniu wniosku");
                kk.setTresc("Pracownik " + urlop.getUzytkownik().getFullname() + " anulował wniosek: " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku() + ". Dodatkowe informacje: " + urlop.getInfoDod());
                KomKolC.create(kk);
            }
            info = "Wniosek anulowany";

        } catch (Exception ex) {
            //if(login.getZalogowany().getSzefId()==null) info = "nie można ustawić akceptanta, brak przełożonego";
            //else 
            info = "Coś poszło nie tak...";
            ex.printStackTrace();
        } finally {
            initUrlop();
            listPodwl();//bo musi odswierzyc liste
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
            wnh.setOpisZmiany("Wysłano do akceptacji przełożonego");

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
                    kk.setTemat("Informacja o wniosku");
                    tresc = "Pracownik " + urlop.getUzytkownik().getFullname() + " wnioskuje o: " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku();
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
                    kk.setTemat("Prośba o akceptację wniosku");
                    tresc = "Proszę o akceptację wniosku. " + "Pracownik " + urlop.getUzytkownik().getFullname() + " wnioskuje o: " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku();

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
                info = "Nie można ustawić akceptanta, brak przełożonego";
            } else {
                info = "Coś poszło nie tak...";
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
            RezerMojKalMg.NieobecnoscDodaj(urlop);
            if (!urlop.getUzytkownik().getAdrEmail().equals("")) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek został zaakceptowany");
                kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }

            if (urlop.getPrzyjmujacy() != null) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek został zaakceptowany");
                kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }
            if (urlop.isExtraemail()) {
                KomKolejka kk = new KomKolejka();
                kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
                kk.setStatus(0);
                kk.setIdDokumenu(urlop.getId().intValue());
                kk.setTemat("Wniosek dla: " + urlop.getUzytkownik().getFullname() + " - zaakceptowany");
                kk.setTresc("Wniosek dla " + urlop.getUzytkownik().getFullname() + " o numerze: " + urlop.getNrWniosku() + " został zaakceptowany");
                KomKolC.create(kk);
            }
        }
        initUrlop();
        listPodwl();//bo musi odswierzyc liste
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
            kk.setTemat("Wniosek został odrzucony");
            kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        if (urlop.getPrzyjmujacy() != null) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek został odrzucony");
            kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        if (urlop.isExtraemail()) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek dla " + urlop.getUzytkownik().getFullname() + " został odrzucony");
            kk.setTresc("Wniosek dla " + urlop.getUzytkownik().getFullname() + "  o numerze: " + urlop.getNrWniosku() + " został odrzucony");
            KomKolC.create(kk);
        }

        initUrlop();
        listPodwl();//bo musi odswierzyc liste
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
        urlopC.createEdit(urlop);

        if (!urlop.getUzytkownik().getAdrEmail().equals("")) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek został cofnięty");
            kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }

        if (urlop.getPrzyjmujacy() != null) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getPrzyjmujacy().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek o urlop cofnięty");
            kk.setTresc("Twoj wniosek o numerze " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }

        if (urlop.isExtraemail()) {
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getUzytkownik().getStruktura().getExtraemail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Wniosek dla " + urlop.getUzytkownik().getFullname() + " został cofnięty");
            kk.setTresc("Wniosek dla " + urlop.getUzytkownik().getFullname() + " o numerze: " + urlop.getNrWniosku() + " został cofnięty do poprawy");
            KomKolC.create(kk);
        }

        initUrlop();
        listPodwl();//bo musi odswierzyc liste
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
        message.setSummary("Wniosek usunięty");
        context.addMessage(zapisz.getClientId(context), message);
        initUrlop();
    }

    public void dodaj() throws ParseException {
        Calendar cal = Calendar.getInstance();
        Calendar calOd = Calendar.getInstance();
        Calendar calDo = Calendar.getInstance();
        cal.clear(Calendar.ZONE_OFFSET);
        calDo.clear(Calendar.ZONE_OFFSET);
        if (urlop.getRodzajId().getId() == 40 || urlop.getRodzajId().getId() == 3 || urlop.getRodzajId().getId() == 10 || urlop.getRodzajId().getId() == 11) {
            calOd.setTime(dataUrlopu);
            calDo.setTime(dataUrlopu);
            cal.setTime(godzOdT);
            calOd.add(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            calOd.add(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            cal.setTime(godzDoT);
            calDo.add(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
            calDo.add(Calendar.MINUTE, cal.get(Calendar.MINUTE));
            urlop.setDataOd(calOd.getTime());
            urlop.setDataDo(calDo.getTime());
        } else {
            calDo.setTime(urlop.getDataDo());
            calDo.set(calDo.get(Calendar.YEAR), calDo.get(Calendar.MONTH), calDo.get(Calendar.DATE), 23, 59);
            urlop.setDataDo(calDo.getTime());
        }
        WnStatusy st = new WnStatusy();
        st.setId(new Long(1));
        urlop.setStatusId(st);
        //urlop.setNrWniosku("ddddddddddd");
        urlop.setDataWprowadzenia(new Date());

        String error;
        if (urlop.getId() == null) {
            urlop.setWnHistoriaList(new ArrayList<>());
            WnHistoria wnh = new WnHistoria();
            wnh.setDataZmiany(new Date());
            wnh.setStatusId(st);
            wnh.setZmieniajacy(urlop.getUzytkownik());
            wnh.setUrlopId(urlop);
            wnh.setOpisZmiany("Wprowadzono nowy wniosek");
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
            message.setSummary("Wniosek zapisany");
            message.setSeverity(FacesMessage.SEVERITY_INFO);
        }
        context.addMessage(zapisz.getClientId(context), message);
    }

    public String drukujWs() {
        return "/urlop/printWs.xhtml";
    }

    public String drukujDz() {
        return "/urlop/printDz.xhtml";
    }

    @PostConstruct
    public void init() {
        urlopC = new WnUrlopJpaController();
        rodzajeC = new WnRodzajeJpaController();
        KomKolC = new KomKolejkaJpaController();
        //initUrlop();
        sortOrders.put("id", SortOrder.descending);
    }

    private void initUrlop() {
        //godzOd = "HH:MM";
        //godzDo = "HH:MM";
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        godzOdT = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        godzDoT = cal.getTime();

        dataUrlopu = new Date();
        login.refresh();
        urlop = new WnUrlop();
        urlop.setUzytkownik(login.getZalogowany().getUserId());
        if (!login.getZalogowany().getExtraemail().isEmpty()) {
            urlop.setExtraemail(true);
        }
        urlopyList.setWrappedData(login.getZalogowany().getUserId().getWnUrlopList());
        
       
        ArrayList<Long> dzialDeny = new ArrayList();
        ConfigJpaController confC = new ConfigJpaController();
        String blackList = confC.findConfigNazwa("dzial_blackList").getWartosc();
        if(blackList!=null){
            String[] bList = blackList.trim().split("\\|",-1);
            for(String b : bList)
                dzialDeny.add(Long.valueOf(b));
        
        }
        Long dzialId = login.getZalogowany().getDzialId().getId();
        String dzialNazwa = login.getZalogowany().getDzialId().getNazwa().trim();
       // System.out.println("DZNAME:"+dzialNazwa+"  ID:"+dzialId);
        if(dzialNazwa.indexOf("SD")==-1 && !dzialDeny.contains(dzialId))
            centralAccess=false;
        //login.refresh();
        
       // System.out.println(centralAccess);
    }

    public void changeRodzList() {
        if (urlop.getRodzajId().getId() == 30) {
            urlop.setCzyZaliczka(true);
        }
    }

    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);

        HSSFPalette palette = wb.getCustomPalette();
        short colorIndex = 45;
        palette.setColorAtIndex(colorIndex, (byte) 201, (byte) 221, (byte) 255);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(colorIndex);
        //style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND); 
        style.setFillPattern(HSSFCellStyle.BORDER_MEDIUM);
        style.setFillBackgroundColor(colorIndex);
        //header.setRowStyle(style);
        for (int colNum = 0; colNum < header.getLastCellNum(); colNum++) {
            wb.getSheetAt(0).autoSizeColumn(colNum);
            header.getCell(colNum).setCellStyle(style);
        }
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

    public Date getDataUrlopu() {
        return dataUrlopu;
    }

    public void setDataUrlopu(Date dataUrlopu) {
        this.dataUrlopu = dataUrlopu;
    }

    public Date getGodzOdT() {
        return godzOdT;
    }

    public void setGodzOdT(Date godzOdT) {
        this.godzOdT = godzOdT;
    }

    public Date getGodzDoT() {
        return godzDoT;
    }

    public void setGodzDoT(Date godzDoT) {
        this.godzDoT = godzDoT;
    }

    public boolean isCentralAccess() {
        return centralAccess;
    }

    public void setCentralAccess(boolean centralAccess) {
        this.centralAccess = centralAccess;
    }

    
}
