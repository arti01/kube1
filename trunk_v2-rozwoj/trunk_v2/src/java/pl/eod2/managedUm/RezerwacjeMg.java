package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.swing.tree.TreeNode;
import pl.eod.abstr.AbstMg;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;
import pl.eod2.encje.UmUrzadzenie;

@ManagedBean(name = "RezerwacjeMg")
@SessionScoped
public class RezerwacjeMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> {

    @ManagedProperty(value = "#{login}")
    private Login login;
    private UmRezerwacjeKontr dcR;
    private List<TreeNode> rootNodes = new ArrayList<>();
    private UmUrzadzenie urzadzenie;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @Override
    public void refresh() {
        login.refresh();
        urzadzenie=null;
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        rootNodes.clear();
        for (UmMasterGrupa mg : masterList) {
            DrzMaster drMa = new DrzMaster(mg);

            for (UmGrupa gr : mg.getGrupaList()) {
                if (!gr.isRezerwacje()) {
                    continue;
                }
                DrzGrupa drGr = new DrzGrupa(drMa, gr);
                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    DrzUrzad drzU = new DrzUrzad(drGr, uz);
                    drGr.getDrzUrzad().add(drzU);
                }
                drMa.getDrzGrupa().add(drGr);
            }

            if (!drMa.getDrzGrupa().isEmpty()) {
                rootNodes.add(drMa);
            }
        }
    }

    public UmRezerwacjeKontr getDcR() {
        return dcR;
    }

    public void setDcR(UmRezerwacjeKontr dcR) {
        this.dcR = dcR;
    }

    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public void setRootNodes(List<TreeNode> rootNodes) {
        this.rootNodes = rootNodes;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public UmUrzadzenie getUrzadzenie() {
        return urzadzenie;
    }

    public void setUrzadzenie(UmUrzadzenie urzadzenie) {
        this.urzadzenie = urzadzenie;
    }

}
