
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstConv;

@ManagedBean(name = "DcDokumentArchConv")
@SessionScoped
public class DcDokumentArchConv extends AbstConv<DcDokumentArch, DcDokumentArchKontr>{
    private static final long serialVersionUID = 1L;

    public DcDokumentArchConv() throws InstantiationException, IllegalAccessException {
        super(new DcDokumentArchKontr());
    }
}
