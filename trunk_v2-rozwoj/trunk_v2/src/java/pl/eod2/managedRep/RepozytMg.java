package pl.eod2.managedRep;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import pl.eod.encje.ConfigJpaController;

@ManagedBean(name = "RepozytMg")
@ApplicationScoped
public class RepozytMg {

    private String dir_repo;
    private List<DrzPliki> sourceRoots = new ArrayList<>();

    @PostConstruct
    public void init() {
        ConfigJpaController confC = new ConfigJpaController();
        this.dir_repo = confC.findConfigNazwa("dir_repo").getWartosc();
    }

    public String list() {
        return "/rep/lista";
    }

    public synchronized List<DrzPliki> getSourceRoots() {
        if (sourceRoots == null) {
            sourceRoots = new DrzPliki(dir_repo).getDirectories();
        }

        return sourceRoots;
    }

}
