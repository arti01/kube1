package pl.eod2.managedRep;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import pl.eod.encje.ConfigJpaController;

@ManagedBean(name = "RepozytMg")
@ApplicationScoped
public class RepozytMg {

    private String dir_repo;
    private List<DrzPliki> sourceRoots;
    private String plikDownl;
    private String plikDownlName;

    @PostConstruct
    public void init() {
        ConfigJpaController confC = new ConfigJpaController();
        this.dir_repo = confC.findConfigNazwa("dir_repo").getWartosc();
    }

    public String list() {
        sourceRoots = null;
        return "/rep/lista";
    }

    public synchronized List<DrzPliki> getSourceRoots() {
        if (sourceRoots == null) {
            DrzPliki dp=new DrzPliki("dddddddddddddddddd");
            DrzPliki dp1=new DrzPliki(dir_repo);
            dp1.getDirectories();
            dp.addDir(dp1);
            sourceRoots = dp.getDirectories();
        }

        return sourceRoots;
    }

    public String getPlikDownl() {
        return plikDownl;
    }

    public void setPlikDownl(String plikDownl) {
        this.plikDownl = plikDownl;
    }

    public String getPlikDownlName() {
        return plikDownlName;
    }

    public void setPlikDownlName(String plikDownlName) {
        this.plikDownlName = plikDownlName;
    }

}
