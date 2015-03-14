package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.swing.tree.TreeNode;
import org.richfaces.event.DropEvent;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmGrupaJpaController;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmMasterGrupaJpaController;
import pl.eod2.encje.UmUrzadzenie;
import pl.eod2.encje.UmUrzadzenieJpaController;

@ManagedBean(name = "UStruktMg")
@SessionScoped
public class UStruktMg {

    @ManagedProperty(value = "#{login}")
    private Login login;;
    private UmMasterGrupaJpaController dcC;
    private UmGrupaJpaController dcG;
    private UmUrzadzenieJpaController dcU;
    private List<TreeNode> rootNodes = new ArrayList<TreeNode>();

    @PostConstruct
    void init() {
        dcC = new UmMasterGrupaJpaController();
        dcG = new UmGrupaJpaController();
        dcU = new UmUrzadzenieJpaController();
        refresh();
    }

    public void refresh() {
        login.refresh();
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        rootNodes.clear();
        for (UmMasterGrupa mg : masterList) {
            DrzMaster drMa = new DrzMaster(mg);
            
            for (UmGrupa gr : mg.getGrupaList()) {
                DrzGrupa drGr = new DrzGrupa(drMa, gr);
            
                for(UmUrzadzenie uz:gr.getUrzadzenieList()){
                    DrzUrzad drzU=new DrzUrzad(drGr, uz);
                    drGr.getDrzUrzad().add(drzU);
                }
                drMa.getDrzGrupa().add(drGr);
            }
            rootNodes.add(drMa);
        }
        //srcRoots.get(0).getGrupaList();
        //srcRoots.get(0).getGrupaList().get(0).getUrzadzenieList();
    }

    public String list() {
        refresh();
        return "/um/struktura";
    }

    @SuppressWarnings("unchecked")
    public void drop(DropEvent event) {

         //przeniesienie urządzenia
         if(event.getDragValue().getClass().equals(pl.eod2.managedUm.DrzUrzad.class)){
             DrzUrzad drU=(DrzUrzad) event.getDragValue();
             UmUrzadzenie uz=drU.getObiektDb();
             
             DrzGrupa drG=(DrzGrupa) event.getDropValue();
             UmGrupa gr=drG.getObiektDb();
             
             uz.setGrupa(gr);
             try {
                 dcU.edit(uz);
             } catch (Exception ex) {
                 Logger.getLogger(UStruktMg.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         //przeniesienie grupy
         if(event.getDragValue().getClass().equals(pl.eod2.managedUm.DrzGrupa.class)){
             DrzGrupa drU=(DrzGrupa) event.getDragValue();
             UmGrupa uz=drU.getObiektDb();
             
             DrzMaster drG=(DrzMaster) event.getDropValue();
             UmMasterGrupa gr=drG.getObiektDb();
             uz.setMasterGrp(gr);
             try {
                 dcG.edit(uz);
             } catch (Exception ex) {
                 Logger.getLogger(UStruktMg.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         
         refresh();
    }
        
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public void setRootNodes(List<TreeNode> rootNodes) {
        this.rootNodes = rootNodes;
    }

}
