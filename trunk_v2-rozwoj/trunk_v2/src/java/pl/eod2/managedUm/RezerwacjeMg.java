package pl.eod2.managedUm;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.TreeNode;
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
    @ManagedProperty(value = "#{UrzadzeniaMg}")
    private UrzadzeniaMg urzMg;

    private UmRezerwacjeKontr dcR;
    //private List<TreeNode> rootNodes = new ArrayList<>();
    private TreeNode root;
    private TreeNode urzadzenie;
    private ScheduleModel eventModel;

    private int wartTest = 20;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @PostConstruct
    private void init(){
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        root = new DefaultTreeNode("urządzenia", null);
        for (UmMasterGrupa mg : masterList) {
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
        eventModel = new DefaultScheduleModel();
    }
    
    @Override
    public void refresh() throws InstantiationException, IllegalAccessException {
        super.refresh();
        login.refresh();
    }
    
    public void ustawSched(){
        UmUrzadzenie urz=(UmUrzadzenie) this.urzadzenie.getData();
        eventModel.clear();
        for(UmRezerwacje rez:urz.getRezerwacjeList()){
            eventModel.addEvent(new DefaultScheduleEvent(rez.getNazwa(), rez.getDataOd(), rez.getDataOd()));
        }
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
    
}
