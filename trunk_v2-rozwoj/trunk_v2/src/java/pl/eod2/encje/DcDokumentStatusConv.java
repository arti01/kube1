
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstConv;

@ManagedBean(name = "DcDokumentStatusConv")
@SessionScoped
public class DcDokumentStatusConv extends AbstConv<DcDokumentStatus, DcDokumentStatusJpaController>{
    private static final long serialVersionUID = 1L;

    public DcDokumentStatusConv() throws InstantiationException, IllegalAccessException {
        super(new DcDokumentStatusJpaController());
    }

}
