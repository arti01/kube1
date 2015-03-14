
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstConv;

@ManagedBean(name = "DcSymbMer2Conv")
@SessionScoped
public class DcSymbMer2Conv extends AbstConv<DcSymbMer2, DcSymbMer2Kontr>{
    private static final long serialVersionUID = 1L;

    public DcSymbMer2Conv() throws InstantiationException, IllegalAccessException {
        super(new DcSymbMer2Kontr());
    }
}
