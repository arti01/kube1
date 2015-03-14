/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managed;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import pl.eod.encje.DzialJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.StrukturaJpaController;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;

/**
 *
 * @author 103039
 */
@ManagedBean(name = "StruktOsM")
@SessionScoped
public class StruktOsM implements Serializable {

    private static final long serialVersionUID = 1L;
    UzytkownikJpaController userC;
    StrukturaJpaController struktC;
    DzialJpaController dzialC;
    private List<Struktura> srcRoots = new ArrayList<Struktura>();
    @ManagedProperty(value = "#{login}")
    private Login login;
    private boolean rozwin;

    public StruktOsM() {
        this.rozwin = true;
    }

    @PostConstruct
    public void init() {
        userC = new UzytkownikJpaController();
        struktC = new StrukturaJpaController();
        dzialC = new DzialJpaController();
    }

    public String lista() {
        return "/all/strukturaOs";
    }

    public synchronized List<Struktura> getSourceRoots() throws IOException {
        Struktura firma = new Struktura();
        Uzytkownik uFirma = new Uzytkownik();
        List<Struktura> wynik = new ArrayList<Struktura>();
        uFirma.setFullname("Organizacja - wg pracowników");
        firma.setUserId(uFirma);
        srcRoots.clear();
        if (login.isAdmin()) {
            List<Struktura> bezSzefa = struktC.getFindBezSzefa();
            for (Struktura s : bezSzefa) {
                if (!s.isSysSdmin() || login.getZalogowany().isSysSdmin()) {
                    srcRoots.add(s);
                }
            }
            //srcRoots.addAll(bezSzefa);
            firma.setBezpPod(srcRoots);
            wynik.add(firma);
        } else {

            for (Struktura s : struktC.findGeneryczny().getBezpPod()) {
                try {
                    if (s.getUserId().getSpolkaId().equals(login.zalogowany.getUserId().getSpolkaId())) {
                        wynik.add(s);
                    }
                } catch (NullPointerException ex) {
                    System.err.println("Problem w strukturze - istnieje podwładny generycznego(szefa wszystkich szefów), który ma ID_spolki NULL, a nie powinien");
                    ex.printStackTrace();
                }
            }
        }
        return wynik;
    }

    public DzialJpaController getDzialC() {
        return dzialC;
    }

    public StrukturaJpaController getStruktC() {
        return struktC;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public boolean isRozwin() {
        return rozwin;
    }

    public void setRozwin(boolean rozwin) {
        this.rozwin = rozwin;
    }
}