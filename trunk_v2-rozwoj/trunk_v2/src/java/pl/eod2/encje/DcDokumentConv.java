
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstConv;

@ManagedBean(name = "DcDokumentConv")
@SessionScoped
public class DcDokumentConv extends AbstConv<DcDokument, DcDokumentJpaController>{
    private static final long serialVersionUID = 1L;

    public DcDokumentConv() throws InstantiationException, IllegalAccessException {
        super(new DcDokumentJpaController());
    }
}
