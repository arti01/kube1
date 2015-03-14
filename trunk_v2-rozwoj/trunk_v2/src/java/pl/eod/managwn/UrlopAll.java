/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managwn;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.richfaces.component.SortOrder;
import pl.eod.encje.WnStatusyJpaController;
import pl.eod.encje.WnUrlopJpaController;

@ManagedBean(name = "UrlopAll")
@SessionScoped
public class UrlopAll extends UrlopM {
    private static final long serialVersionUID = 1L;

    WnStatusyJpaController wnStatusyC;
    

    @PostConstruct
    @Override
    public void init() {
        setWnStatusyC(new WnStatusyJpaController());
        setUrlopC(new WnUrlopJpaController());
        getSortOrders().put("id", SortOrder.descending);
    }

    @Override
    public String list() {
        return "/urlop/urlopyListWszystko";
    }

    public WnStatusyJpaController getWnStatusyC() {
        return wnStatusyC;
    }

    public void setWnStatusyC(WnStatusyJpaController wnStatusyC) {
        this.wnStatusyC = wnStatusyC;
    }
}
