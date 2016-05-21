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
        drzewkoStart();
        return "/all/strukturaOs";
    }

    private void drzewkoStart() {
        Uzytkownik userV = new Uzytkownik();
        root = new DefaultTreeNode("szef", null, null);
        if (login.isAdmin()) {
            userV.setFullname("Organizacja - widok administratora");
            DefaultTreeNode podroot = new DefaultTreeNode("szef", userV, root);
            List<Struktura> bezSzefa = struktC.getFindBezSzefa();
            for (Struktura s : bezSzefa) {
                if (!s.isSysSdmin() || login.getZalogowany().isSysSdmin()) {
                    if (s.isUsuniety()) {
                        continue;
                    }
                    if (s.getBezpPodWidoczni() != null && s.getBezpPodWidoczni().size() != 0) {
                        drzewko(new DefaultTreeNode("szef", s.getUserId(), podroot));
                    } else {
                        new DefaultTreeNode("prac", s.getUserId(), podroot);
                    }
                }
            }
        } else {
            for (Struktura s : struktC.findGeneryczny().getBezpPod()) {
                try {
                    if (s.getUserId().getSpolkaId().equals(login.zalogowany.getUserId().getSpolkaId())) {
                        drzewko(new DefaultTreeNode("szef", s.getUserId(), root));
                    }
                } catch (NullPointerException ex) {
                    System.err.println("Problem w strukturze - istnieje podwładny generycznego(szefa wszystkich szefów), który ma ID_spolki NULL, a nie powinien");
                    ex.printStackTrace();
                }
            }
        }
        //this.createTree(root, drR);
    }

    public void drzewko(DefaultTreeNode nadrz) {
        Uzytkownik us = (Uzytkownik) nadrz.getData();
        Struktura struktura = us.getStruktura();
        for (Struktura s : struktura.getBezpPodWidoczni()) {
            if (s.getBezpPodWidoczni().size() != 0) {
                drzewko(new DefaultTreeNode("szef", s.getUserId(), nadrz));
            } else {
                new DefaultTreeNode("prac", s.getUserId(), nadrz);
            }
        }
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
