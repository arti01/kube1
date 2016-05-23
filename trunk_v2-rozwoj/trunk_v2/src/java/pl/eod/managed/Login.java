package pl.eod.managed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.eod.encje.ConfigJpaController;
import pl.eod.encje.MenuLinki;
import pl.eod.encje.MenuLinkiJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.StrukturaJpaController;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod.encje.WnLimity;
import pl.eod.encje.exceptions.NonexistentEntityException;

@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {

    private static final long serialVersionUID = 1L;
    Struktura zalogowany = null;
    StrukturaJpaController strukC;
    String username;
    String password;
    String template = "../templates/templateRF2PF.xhtml";
    String licencjaDla;
    boolean urlop;
    boolean urlAll;
    boolean struktura;
    boolean admin;
    boolean kierownik;
    boolean dcRej;
    boolean dcOdb;
    boolean dcCfg;
    boolean dcArc;
    boolean ogl;
    boolean umCfg;
    boolean umSprz;
    boolean umDoc;
    boolean umRez;
    boolean kalDec;
    String typLogowania;
    List<MenuLinki> menuLinki;
    MenuLinkiJpaController menuLinkiC;
    boolean bladLicencj = false;

    boolean menuStrukturaExp = false;
    boolean menuUrlopExp = false;
    boolean menuDcCfgExp = false;
    boolean menuDcRejExp = false;
    boolean menuDcOdbExp = false;
    boolean menuDcArcExp = false;
    boolean menuDcArcRap = false;
    boolean menuOglExp = false;
    boolean menuUmCfg = false;
    boolean menuUmSprze = false;
    boolean menuRezerExp = false;

    @PostConstruct
    public void init() {
        ConfigJpaController confC = new ConfigJpaController();
        typLogowania = confC.findConfigNazwa("realm_szyfrowanie").getWartosc();
        menuLinkiC = new MenuLinkiJpaController();
        menuLinki = menuLinkiC.findMenuLinkiEntities();

        //licencje
        /*String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));        
        String eodtljar=absolutePath+"/../../../../lib/eodtl.jar";*/
        java.net.URL fileURL = eodt.lib.NewClass.class.getProtectionDomain().getCodeSource().getLocation();
        String eodtljar = fileURL.getFile();
        //System.err.println(eodtljar);

        String md5 = "";
        String klucz = confC.findConfigNazwa("kluczLicencji").getWartosc();
        try {
            FileInputStream fis = new FileInputStream(new File(eodtljar));
            md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!md5.equalsIgnoreCase(klucz)) {
            zalogowany = null;
            this.setBladLicencj(true);
            System.err.println(klucz + "licencja");
            template = "../templates/template_login.xhtml";
        }

        UzytkownikJpaController uzytC = new UzytkownikJpaController();
        if (uzytC.iluZprawami() > eodt.lib.NewClass.LICZ) {
            zalogowany = null;
            this.setBladLicencj(true);
            System.err.println(uzytC.iluZprawami() + "licencja" + eodt.lib.NewClass.LICZ);
            template = "../templates/template_login.xhtml";
        }
    }

    public String wyloguj() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("NAZWA:>" + cookie.getName() + "<");
            //if((cookie.getName( )).indexOf("JSESSIONIDSSO") == 0 ){

            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        request.getSession().invalidate();
        return "../index.html?faces-redirect=true";
    }

    public String zmienHaslo() throws NonexistentEntityException, Exception {
        String error = null;
        if (!zalogowany.getUserId().getHasla().getPass().equals(password)) {
            error = "rózne hasła";
        } else {
            ConfigJpaController confC = new ConfigJpaController();
            if (confC.findConfigNazwa("realm_szyfrowanie").getWartosc().equals("md5")) {
                zalogowany.getUserId().getHasla().setPass(Login.md5(zalogowany.getUserId().getHasla().getPass()));
            }
            strukC = new StrukturaJpaController();
            strukC.editArti(zalogowany);
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (error != null) {
            FacesMessage message = new FacesMessage(error);
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            request.getSession().invalidate();
            FacesMessage message = new FacesMessage("zmiana wykonana");
            UIComponent zapisz = UIComponent.getCurrentComponent(context);
            context.addMessage(zapisz.getClientId(context), message);
            //System.err.println("tutaj");
            return "../index.html";
        }
    }

    public void loginTest() {
        System.err.println("ssssseeeeeeeeeeee");
    }

    public String loginNew() {
        System.err.println("ssssssssssssssssssssssssssssssss");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.login(this.username, this.password);
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Login failed."));
            return "error";
        }
        return "/all/index";
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UzytkownikJpaController uzytC = new UzytkownikJpaController();

        zalogowany = uzytC.findStruktura(request.getUserPrincipal().getName());
        return "/all/index";
    }

    public String stronaIndex() {
        refresh();
        return "../logowanie/index.xhtml";
    }

    public void setZalogowany(Struktura user) {
        zalogowany = user;
    }

    public void refresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        UzytkownikJpaController uzytC = new UzytkownikJpaController();
        //System.out.println(uzytC.iluZprawami() + "prawa");

        try {
            zalogowany = uzytC.findStruktura(request.getUserPrincipal().getName());
        } catch (NullPointerException np) {
            System.err.println("nie zalogowany");
        }

        //obsluga zewnetrzne id
        if (zalogowany == null) {
            zalogowany = uzytC.findStrukturaExtid(request.getUserPrincipal().getName());
        }

        try {
            if (zalogowany.isUsuniety()) {
                this.wyloguj();
                template = "../templates/template_login.xhtml";
                //return null;
            }
        } catch (NullPointerException ex) {
            System.err.println("brak użytkownika w bazie - user zewnętrzny");
        }
        //wyszukiwanie limitu
        WnLimity limit = uzytC.findLimit(zalogowany.getUserId());
        zalogowany.getUserId().setWnLimity(limit);

    }

    public void menuStrukturaExpList(ActionEvent event) {
        menuStrukturaExp = !menuStrukturaExp;
        menuUrlopExp = false;
        menuDcRejExp = false;
        menuDcCfgExp = false;
        menuDcOdbExp = false;
        menuDcArcExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuUrlopExpList(ActionEvent event) {
        menuUrlopExp = !menuUrlopExp;
        menuStrukturaExp = false;
        menuDcRejExp = false;
        menuDcCfgExp = false;
        menuDcOdbExp = false;
        menuDcArcExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuDcCfgExpList(ActionEvent event) {
        menuDcCfgExp = !menuDcCfgExp;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuDcArcExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuDcRejExpList(ActionEvent event) {
        menuDcRejExp = !menuDcRejExp;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcOdbExp = false;
        menuDcArcExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuDcOdbExpList(ActionEvent event) {
        menuDcOdbExp = !menuDcOdbExp;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcArcExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuDcArcExpList(ActionEvent event) {
        menuDcArcExp = !menuDcArcExp;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuDcArcRapList(ActionEvent event) {
        menuDcArcRap = !menuDcArcRap;
        menuDcArcExp = false;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuOglExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuRezerExp = false;
    }

    public void menuOglExpList(ActionEvent event) {
        menuOglExp = !menuOglExp;
        menuDcArcExp = false;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuUmCfg = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuUmCfgExpList(ActionEvent event) {
        menuUmCfg = !menuUmCfg;
        menuOglExp = false;
        menuDcArcExp = false;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuUmSprze = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuUmSprzExpList(ActionEvent event) {
        menuUmSprze = !menuUmSprze;
        menuOglExp = false;
        menuDcArcExp = false;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuUmCfg = false;
        menuDcArcRap = false;
        menuRezerExp = false;
    }

    public void menuRezerExpList(ActionEvent event) {
        menuRezerExp = !menuRezerExp;
        menuUmSprze = false;
        menuOglExp = false;
        menuDcArcExp = false;
        menuStrukturaExp = false;
        menuUrlopExp = false;
        menuDcCfgExp = false;
        menuDcRejExp = false;
        menuDcOdbExp = false;
        menuUmCfg = false;
        menuDcArcRap = false;
    }

    public Struktura getZalogowany() {
        if (zalogowany == null) {
            refresh();
        }
        return zalogowany;
    }

    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());
            //Converts message digest value in base 16 (hex)
            md5 = new BigInteger(1, digest.digest()).toString(16);
            while (md5.length() < 32) {
                md5 = "0" + md5;
            }
            //new BigInteger
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        System.err.println(username);
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        System.err.println(password);
        this.password = password;
    }

    public boolean isUrlop() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        urlop = request.isUserInRole("eodurlop");
        return urlop;
    }

    public boolean isStruktura() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        struktura = request.isUserInRole("eodstru");
        return struktura;
    }

    public boolean isDcRej() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        dcRej = request.isUserInRole("eoddok_rej");
        return dcRej;
    }

    public boolean isDcOdb() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        dcOdb = request.isUserInRole("eoddok_odb");
        return dcOdb;
    }

    public boolean isDcCfg() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        dcCfg = request.isUserInRole("eoddok_cfg");
        return dcCfg;
    }

    public boolean isDcArc() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        dcArc = request.isUserInRole("eoddok_arc");
        return dcArc;
    }

    public boolean isOgl() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        ogl = request.isUserInRole("eod_ogl");
        return ogl;
    }

    public boolean isAdmin() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        admin = request.isUserInRole("eodadm");
        return admin;
    }

    public boolean isUmCfg() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_um_cfg");
    }

    public boolean isUmSprz() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_um_sprz");
    }

    public boolean isUmDoc() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_um_doc");
    }

    public boolean isUmRez() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_um_rez");
    }

    public boolean isKalDec() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_kal_dec");
    }

    public boolean isUrlAll() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        return request.isUserInRole("eod_url_all");
    }

    public void setUrlAll(boolean urlAll) {
        this.urlAll = urlAll;
    }

    public boolean isKierownik() {
        try {
            return getZalogowany().isStKier();
        } catch (Exception ex) {
            return false;
        }

    }

    public String getTypLogowania() {
        return typLogowania;
    }

    public void setTypLogowania(String typLogowania) {
        this.typLogowania = typLogowania;
    }

    public List<MenuLinki> getMenuLinki() {
        return menuLinki;
    }

    public boolean isMenuStrukturaExp() {
        return menuStrukturaExp;
    }

    public void setMenuStrukturaExp(boolean menuStrukturaExp) {
        this.menuStrukturaExp = menuStrukturaExp;
    }

    public boolean isMenuUrlopExp() {
        return menuUrlopExp;
    }

    public void setMenuUrlopExp(boolean menuUrlopExp) {
        this.menuUrlopExp = menuUrlopExp;
    }

    public boolean isMenuDcCfgExp() {
        return menuDcCfgExp;
    }

    public void setMenuDcCfgExp(boolean menuDcCfgExp) {
        this.menuDcCfgExp = menuDcCfgExp;
    }

    public boolean isMenuDcRejExp() {
        return menuDcRejExp;
    }

    public void setMenuDcRejExp(boolean menuDcRejExp) {
        this.menuDcRejExp = menuDcRejExp;
    }

    public boolean isMenuDcOdbExp() {
        return menuDcOdbExp;
    }

    public void setMenuDcOdbExp(boolean menuDcOdbExp) {
        this.menuDcOdbExp = menuDcOdbExp;
    }

    public boolean isMenuDcArcExp() {
        return menuDcArcExp;
    }

    public void setMenuDcArcExp(boolean menuDcArcExp) {
        this.menuDcArcExp = menuDcArcExp;
    }

    public boolean isMenuOglExp() {
        return menuOglExp;
    }

    public void setMenuOglExp(boolean menuOglExp) {
        this.menuOglExp = menuOglExp;
    }

    public boolean isMenuRezerExp() {
        return menuRezerExp;
    }

    public void setMenuRezerExp(boolean menuRezerExp) {
        this.menuRezerExp = menuRezerExp;
    }

    public void setUmCfg(boolean umCfg) {
        this.umCfg = umCfg;
    }

    public void setUmSprz(boolean umSprz) {
        this.umSprz = umSprz;
    }

    public boolean isMenuUmCfg() {
        return menuUmCfg;
    }

    public void setMenuUmCfg(boolean menuUmCfg) {
        this.menuUmCfg = menuUmCfg;
    }

    public boolean isMenuUmSprze() {
        return menuUmSprze;
    }

    public void setMenuUmSprze(boolean menuUmSprze) {
        this.menuUmSprze = menuUmSprze;
    }

    public boolean isMenuDcArcRap() {
        return menuDcArcRap;
    }

    public void setMenuDcArcRap(boolean menuDcArcRap) {
        this.menuDcArcRap = menuDcArcRap;
    }

    public boolean isBladLicencj() {
        return bladLicencj;
    }

    public void setBladLicencj(boolean bladLicencj) {
        this.bladLicencj = bladLicencj;
    }

    public String htmlLogout() throws IOException {
        wyloguj();
        return "../index.html";
    }

    public String htmlZmianaHasla() {
        return "/logowanie/zmianaHasla.xhtml?faces-redirect=true";
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getLicencjaDla() {
        licencjaDla = eodt.lib.NewClass.FIRMA;
        return licencjaDla;
    }

}
