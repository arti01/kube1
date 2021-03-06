package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.swing.tree.TreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.richfaces.event.DropEvent;
import pl.eod.managed.Login;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcRodzaj;
import pl.eod2.encje.DcRodzajJpaController;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmUrzadzenie;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedRej.Rejestracja;

@ManagedBean(name = "URejDokMg")
@SessionScoped
public class RejDok {

    private DataModel<DcDokument> lista = new ListDataModel<>();
    private List<DcDokument>listaPF=new ArrayList<>();
    private DataModel<DcRodzaj> rodzajLista = new ListDataModel<>();
    private List<DcRodzaj>rodzajListaPF=new ArrayList<>();
    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{RejestracjaRej}")
    private Rejestracja rejestracja;
    @ManagedProperty(value = "#{UrzadzeniaMg}")
    private UrzadzeniaMg urzadzeniaMg;
    @ManagedProperty(value = "#{RezerwacjeMg}")
    private RezerwacjeMg rezMg;
    private DcRodzajJpaController dcR;
    private List<TreeNode> rootNodesDetDok = new ArrayList<>();
    private UmUrzadzenie urzad;
    DefaultTreeNode node;

    @PostConstruct
    void init() {
    }

    @SuppressWarnings("unchecked")
    void refresh() {
        dcR = new DcRodzajJpaController();
        rodzajLista.setWrappedData(dcR.findDcRodzajUm());
        rodzajListaPF=dcR.findDcRodzajUm();
        List<DcDokument> listD = new ArrayList<>();
        for (DcRodzaj rodz : rodzajListaPF) {
            listD.addAll(rodz.getDcDokumentList());
        }
        lista.setWrappedData(listD);
        listaPF=listD;
        rejestracja.setObiekt(null);
        rezMg.zrobDrzewo(true, null);
    }

    public String list() {
        refresh();
        return "/um/dokumenty";
    }

    public void dodaj() throws Exception {
        if (rejestracja.dodajAbst()) {
            refresh();
            urzadzeniaMg.refresh();
        }
    }

    public void edytuj() {
        if (rejestracja.edytujAbst()) {
            refresh();
        }
    }

    public void usun() throws IllegalOrphanException, NonexistentEntityException {
        rejestracja.usunAbst();
        refresh();
    }

    public String detale() {
        return "/um/dokumentDetale?faces-redirect=true";
    }

    public void nowyDokDlaUrzad() {
        rejestracja.setObiekt(new DcDokument());
        rejestracja.setError(null);
        List<UmUrzadzenie> urzList = new ArrayList<>();
        urzad = urzadzeniaMg.getDcC().findUmUrzadzenie(urzad.getId());
        urzList.add(urzad);
        rejestracja.getObiekt().setUrzadzeniaList(urzList);
        rodzajLista.setWrappedData(urzad.getGrupa().getMasterGrp().getRodzajeDokList());
        rodzajListaPF=urzad.getGrupa().getMasterGrp().getRodzajeDokList();
    }

    public void addUrzDok(){
        if (node.getData() instanceof pl.eod2.encje.UmUrzadzenie) {
            //DrzUrzad drU = (DrzUrzad) node.getData();
            UmUrzadzenie uz = (UmUrzadzenie) node.getData();
            rejestracja.getObiekt().getUrzadzeniaList().add(uz);
        }
        if (node.getData() instanceof pl.eod2.encje.UmGrupa) {
            UmGrupa uGr = (UmGrupa) node.getData();
            rejestracja.getObiekt().getUrzadzeniaList().addAll(uGr.getUrzadzenieList());
        }
        if (node.getData() instanceof pl.eod2.encje.UmMasterGrupa) {
            UmMasterGrupa drM = (UmMasterGrupa) node.getData();
            for (UmGrupa uGr : drM.getGrupaList()) {
                rejestracja.getObiekt().getUrzadzeniaList().addAll(uGr.getUrzadzenieList());
            }
        }
        rejestracja.edytujAbst();
    }
    
    public void drop(DropEvent event) {

        //przypisanie urzadzenia do dokumenty
        NamedNode nn = (NamedNode) event.getDragValue();
        if (nn.getType().equals("urzadz")) {
            DrzUrzad drU = (DrzUrzad) event.getDragValue();
            UmUrzadzenie uz = drU.getObiektDb();
            rejestracja.getObiekt().getUrzadzeniaList().add(uz);
        }
        if (nn.getType().equals("grupa")) {
            DrzGrupa drG = (DrzGrupa) event.getDragValue();
            UmGrupa uGr = drG.getObiektDb();
            rejestracja.getObiekt().getUrzadzeniaList().addAll(uGr.getUrzadzenieList());
        }
        if (nn.getType().equals("master")) {
            DrzMaster drM = (DrzMaster) event.getDragValue();
            for (DrzGrupa drG : drM.getDrzGrupa()) {
                UmGrupa uGr = drG.getObiektDb();
                rejestracja.getObiekt().getUrzadzeniaList().addAll(uGr.getUrzadzenieList());
            }
        }
        rejestracja.edytujAbst();

    }

    public void usunUrzad() {
        rejestracja.getObiekt().getUrzadzeniaList().remove(urzad);
        rejestracja.edytujAbst();
    }

    public DataModel<DcDokument> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcDokument> lista) {
        this.lista = lista;
    }

    public DataModel<DcRodzaj> getRodzajLista() {
        return rodzajLista;
    }

    public void setRodzajLista(DataModel<DcRodzaj> rodzajLista) {
        this.rodzajLista = rodzajLista;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public Rejestracja getRejestracja() {
        return rejestracja;
    }

    public void setRejestracja(Rejestracja rejestracja) {
        this.rejestracja = rejestracja;
    }

    public UmUrzadzenie getUrzad() {
        return urzad;
    }

    public void setUrzad(UmUrzadzenie urzad) {
        this.urzad = urzad;
    }

    public UrzadzeniaMg getUrzadzeniaMg() {
        return urzadzeniaMg;
    }

    public void setUrzadzeniaMg(UrzadzeniaMg urzadzeniaMg) {
        this.urzadzeniaMg = urzadzeniaMg;
    }

    public List<TreeNode> getRootNodesDetDok() {
        try {
            List<UmMasterGrupa> masterList = rejestracja.getObiekt().getRodzajId().getUmMasterGrupaList();
            rootNodesDetDok.clear();
            for (UmMasterGrupa mg : masterList) {
                DrzMaster drMa = new DrzMaster(mg);
                for (UmGrupa gr : mg.getGrupaList()) {
                    DrzGrupa drGr = new DrzGrupa(drMa, gr);
                    gr.getUrzadzenieList().removeAll(rejestracja.getObiekt().getUrzadzeniaList());
                    for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                        DrzUrzad drzU = new DrzUrzad(drGr, uz);
                        drGr.getDrzUrzad().add(drzU);
                    }
                    drMa.getDrzGrupa().add(drGr);
                }
                rootNodesDetDok.add(drMa);
            }
        } catch (NullPointerException ex) {
        }
        return rootNodesDetDok;
    }

    public void setRootNodesDetDok(List<TreeNode> rootNodesDetDok) {
        this.rootNodesDetDok = rootNodesDetDok;
    }

    public RezerwacjeMg getRezMg() {
        rezMg.zrobDrzewo(true, rejestracja.getObiekt().getRodzajId());
        return rezMg;
    }

    public void setRezMg(RezerwacjeMg rezMg) {
        this.rezMg = rezMg;
    }

    public DefaultTreeNode getNode() {
        return node;
    }

    public void setNode(DefaultTreeNode node) {
        this.node = node;
    }

    public List<DcDokument> getListaPF() {
        return listaPF;
    }

    public void setListaPF(List<DcDokument> listaPF) {
        this.listaPF = listaPF;
    }

    public List<DcRodzaj> getRodzajListaPF() {
        return rodzajListaPF;
    }

    public void setRodzajListaPF(List<DcRodzaj> rodzajListaPF) {
        this.rodzajListaPF = rodzajListaPF;
    }

}
