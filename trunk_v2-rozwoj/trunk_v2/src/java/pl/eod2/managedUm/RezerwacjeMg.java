package pl.eod2.managedUm;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;

@ManagedBean(name = "RezerwacjeMg")
@SessionScoped
public class RezerwacjeMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> {

    private UmRezerwacjeKontr dcR;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/dcarch/dokumenty", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    public UmRezerwacjeKontr getDcR() {
        return dcR;
    }

    public void setDcR(UmRezerwacjeKontr dcR) {
        this.dcR = dcR;
    }
    
}
