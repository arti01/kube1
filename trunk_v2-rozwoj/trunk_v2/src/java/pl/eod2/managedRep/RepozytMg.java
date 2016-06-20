package pl.eod2.managedRep;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import pl.eod.encje.ConfigJpaController;
import pl.eod.managed.Login;
import pl.eod2.encje.Repozytoria;

@ManagedBean(name = "RepozytMg")
@SessionScoped
public class RepozytMg {

    @ManagedProperty(value = "#{login}")
    private Login login;

    private String dir_repo;
    private TreeNode root;
    private List<TreeNode> inneRoot;
    DrzPF drR;
    String template = "/templates/template_bez_login.xhtml";

    @PostConstruct
    public void init() {
        ConfigJpaController confC = new ConfigJpaController();
        this.dir_repo = confC.findConfigNazwa("dir_repo").getWartosc();
        if(this.dir_repo!=null&&this.dir_repo.length()>0){
            this.drzewko();
        }
    }

    public String list() {
        template = "/templates/templateGPF.xhtml";
        this.drzewko();
        return "/rep/wspolne";
    }

    public String listBezLogin() {
        this.drzewko();
        System.err.println(getTemplate());
        return "../rep/wspolne.xhtml";
    }

    public String inne() {
        inneDrzewka();
        return "/rep/inne";
    }

    private void drzewko() {
        root = new DefaultTreeNode(this.dir_repo, null);
        drR = new DrzPF(dir_repo, "repozytorium", null);
        this.createTree(root, drR);
    }

    private void inneDrzewka() {
        inneRoot = new ArrayList<>();
        List<Repozytoria> repoAll = new ArrayList<>();
        repoAll.addAll(login.getZalogowany().getRepozytoriaList());
        for (Repozytoria rep : login.getZalogowany().getSzefId().getRepozytoriaList()) {
            if (!repoAll.contains(rep)) {
                repoAll.add(rep);
            }
        }
        for (Repozytoria rep : repoAll) {
            DefaultTreeNode rootT = new DefaultTreeNode(rep.getSciezka(), null);
            DrzPF drRT = new DrzPF(rep.getSciezka(), rep.getNazwa(), null);
            this.createTree(rootT, drRT);
            inneRoot.add(rootT);
        }
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

    public List<TreeNode> getInneRoot() {
        return inneRoot;
    }

    public void setInneRoot(List<TreeNode> inneRoot) {
        this.inneRoot = inneRoot;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getTemplate() {
        return template;
    }

    public String getDir_repo() {
        return dir_repo;
    }

}
