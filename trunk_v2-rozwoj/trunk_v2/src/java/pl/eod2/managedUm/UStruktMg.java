package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.swing.tree.TreeNode;
import org.primefaces.event.TreeDragDropEvent;
import org.primefaces.model.DefaultTreeNode;
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
    private Login login;
    private UmMasterGrupaJpaController dcC;
    private UmGrupaJpaController dcG;
    private UmUrzadzenieJpaController dcU;
    private List<TreeNode> rootNodes = new ArrayList<>();
    private org.primefaces.model.TreeNode root;
    private org.primefaces.model.TreeNode selectedNode;

    @PostConstruct
    void init() {
        dcC = new UmMasterGrupaJpaController();
        dcG = new UmGrupaJpaController();
        dcU = new UmUrzadzenieJpaController();
        //refresh();
        zrobDrzewo();
    }

    public void refresh() {
        login.refresh();
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        rootNodes.clear();
        for (UmMasterGrupa mg : masterList) {
            DrzMaster drMa = new DrzMaster(mg);

            for (UmGrupa gr : mg.getGrupaList()) {
                DrzGrupa drGr = new DrzGrupa(drMa, gr);

                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    DrzUrzad drzU = new DrzUrzad(drGr, uz);
                    drGr.getDrzUrzad().add(drzU);
                }
                drMa.getDrzGrupa().add(drGr);
            }
            rootNodes.add(drMa);
        }
        zrobDrzewo();
    }

    public void zrobDrzewo() {
        root = new DefaultTreeNode("urządzenia", null);
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        for (UmMasterGrupa mg : masterList) {
            org.primefaces.model.TreeNode mtr = new DefaultTreeNode("grupa", mg, root);
            for (UmGrupa gr : mg.getGrupaList()) {
                org.primefaces.model.TreeNode gtr = new DefaultTreeNode("grupa", gr, mtr);
                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    org.primefaces.model.TreeNode utr = new DefaultTreeNode("urzadzenie", uz, gtr);
                }
            }
        }
    }

    public String list() {
        refresh();
        return "/um/struktura";
    }

    @SuppressWarnings("unchecked")
    public void drop(DropEvent event) {

        //przeniesienie urządzenia
        if (event.getDragValue().getClass().equals(pl.eod2.managedUm.DrzUrzad.class)) {
            DrzUrzad drU = (DrzUrzad) event.getDragValue();
            UmUrzadzenie uz = drU.getObiektDb();

            DrzGrupa drG = (DrzGrupa) event.getDropValue();
            UmGrupa gr = drG.getObiektDb();

            uz.setGrupa(gr);
            try {
                dcU.edit(uz);
            } catch (Exception ex) {
                Logger.getLogger(UStruktMg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //przeniesienie grupy
        if (event.getDragValue().getClass().equals(pl.eod2.managedUm.DrzGrupa.class)) {
            DrzGrupa drU = (DrzGrupa) event.getDragValue();
            UmGrupa uz = drU.getObiektDb();

            DrzMaster drG = (DrzMaster) event.getDropValue();
            UmMasterGrupa gr = drG.getObiektDb();
            uz.setMasterGrp(gr);
            try {
                dcG.edit(uz);
            } catch (Exception ex) {
                Logger.getLogger(UStruktMg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        refresh();
    }

    public void onDragDrop(TreeDragDropEvent event) {
        org.primefaces.model.TreeNode dragNode = event.getDragNode();
        org.primefaces.model.TreeNode dropNode = event.getDropNode();
        System.err.println("Dragged " + dragNode.getData()+ "Dropped on " + dropNode.getData() + " at " );
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Dragged " + dragNode.getData(), "Dropped on " + dropNode.getData() + " at " );
        FacesContext.getCurrentInstance().addMessage(null, message);
        /*if (dropNode.getType().equals(VALID_TYPE)) {
            //Update the underlying data structure here
        } else {
            //Display a warning to the user if required
        }*/
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

    public org.primefaces.model.TreeNode getRoot() {
        return root;
    }

    public void setRoot(org.primefaces.model.TreeNode root) {
        this.root = root;
    }

    public org.primefaces.model.TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(org.primefaces.model.TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

}
