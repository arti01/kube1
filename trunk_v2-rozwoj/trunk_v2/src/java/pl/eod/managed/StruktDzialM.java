/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import pl.eod.encje.Dzial;
import pl.eod.encje.DzialJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.StrukturaJpaController;
import pl.eod.encje.UzytkownikJpaController;

/**
 *
 * @author arti01
 */
@ManagedBean(name = "StruktDzialM")
@SessionScoped
public class StruktDzialM implements Serializable {

    private static final long serialVersionUID = 1L;
    UzytkownikJpaController userC;
    StrukturaJpaController struktC;
    DzialJpaController dzialC;
    private List<Struktura> srcRoots = new ArrayList<Struktura>();
    @ManagedProperty(value = "#{login}")
    private Login login;

    @PostConstruct
    public void init() {
        userC = new UzytkownikJpaController();
        struktC = new StrukturaJpaController();
        dzialC = new DzialJpaController();
    }

    public String lista() {
        return "/all/strukturaDzial";
    }

    public synchronized List<Struktura> getSourceRoots() {
        Struktura firma = new Struktura();
        Dzial uDzial = new Dzial();
        List<Struktura> wynik = new ArrayList<>();
        uDzial.setNazwa("Organizacja - wg działów");
        firma.setDzialId(uDzial);
        srcRoots.clear();
        if (login.isAdmin()) {
            srcRoots.addAll(struktC.getFindBezSzefa());
            firma.setBezpPod(srcRoots);
            wynik.add(firma);
        }
        else{
            for(Struktura s:struktC.findGeneryczny().getBezpPod()){
              if(s.getUserId().getSpolkaId().equals(login.zalogowany.getUserId().getSpolkaId())){
                  wynik.add(s);
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
}