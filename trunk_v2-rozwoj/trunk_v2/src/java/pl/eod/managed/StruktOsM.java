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
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import pl.eod.encje.DzialJpaController;
import pl.eod.encje.Struktura;
import pl.eod.encje.StrukturaJpaController;
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod2.managedRep.DrzPF;

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
    private final List<Struktura> srcRoots = new ArrayList<>();
    @ManagedProperty(value = "#{login}")
    private Login login;
    private boolean rozwin;
    private TreeNode root;

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
        drzewko();
        return "/all/strukturaOs";
    }
    
    private void drzewko() {
        root = new DefaultTreeNode("Organizacja - wg pracowników", null);
        this.createTree(root, drR);
    }

    public void createTree(){
        
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
                //System.err.println(s.getUserId().getAdrEmail());
                //System.err.println(s.getUserId().getFullname());
                //System.err.println(s.getUserId());
                //System.err.println(s.getUserId().getSpolkaId());
                //System.err.println(login.zalogowany.getUserId().getSpolkaId());
                /*
                jesli po zalogowaniu na admina widać drzewko, a po zalogowaniu na usera nie, to moze to śwoadczyć, że bezpośredni
                podwladny generycznego (prezes) nie ma ustawionego lub ma zle id spolki
                */
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

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
    
    
}