package pl.eod2.managedRep;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import pl.eod.encje.ConfigJpaController;

@ManagedBean(name = "RepozytMg")
@SessionScoped
public class RepozytMg {

    private String dir_repo;
    private List<DrzPliki> sourceRoots;
    private TreeNode root;
    DrzPF drR;

    @PostConstruct
    public void init() {
        ConfigJpaController confC = new ConfigJpaController();
        this.dir_repo = confC.findConfigNazwa("dir_repo").getWartosc();
    }

    public String list() {
        sourceRoots = null;
        this.drzewko();
        return "/rep/lista";
    }

    private void drzewko() {
        root = new DefaultTreeNode(this.dir_repo, null);
        drR = new DrzPF(dir_repo, "repozytorium", null);
        createTree(root, drR);
    }

    public synchronized List<DrzPliki> getSourceRoots() {
        if (sourceRoots == null) {
            DrzPliki dp = new DrzPliki("dddddddddddddddddd");
            DrzPliki dp1 = new DrzPliki(dir_repo);
            dp1.getDirectories();
            dp.addDir(dp1);
            sourceRoots = dp.getDirectories();
        }

        return sourceRoots;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    private TreeNode createTree(TreeNode node, DrzPF dp) {
        TreeNode tr = new DefaultTreeNode(dp.getType(), dp, node);
        for (DrzPF dpN : dp.getChidren()) {
            TreeNode tr1 = this.createTree(tr, dpN);
        }
        return tr;
    }
}
