/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.managwn;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.richfaces.component.SortOrder;
import pl.eod.encje.WnStatusyJpaController;
import pl.eod.encje.WnUrlop;
import pl.eod.encje.WnUrlopJpaController;
import pl.eod.managed.Login;

@ManagedBean(name = "UrlopAll")
@SessionScoped
public class UrlopAll extends UrlopM {

    private static final long serialVersionUID = 1L;
    WnStatusyJpaController wnStatusyC;
    WnUrlopJpaController wnUrlopC = new WnUrlopJpaController();
    List<WnUrlop> urlopyAll;

    @PostConstruct
    @Override
    public void init() {
        setWnStatusyC(new WnStatusyJpaController());
        setUrlopC(wnUrlopC);
        getSortOrders().put("id", SortOrder.descending);
    }

    @Override
    public String list() {
        if (getLogin().isUrlSel() && !getLogin().isUrlAll()) {
            urlopyAll = wnUrlopC.findWybraneRodzaje(getLogin().getZalogowany().getUserId().getWnRodzaje());
        } else {
            urlopyAll = wnUrlopC.findWnUrlopEntities();
        }

        return "/urlop/urlopyListWszystko";
    }

    public WnStatusyJpaController getWnStatusyC() {
        return wnStatusyC;
    }

    public void setWnStatusyC(WnStatusyJpaController wnStatusyC) {
        this.wnStatusyC = wnStatusyC;
    }

    public List<WnUrlop> getUrlopyAll() {
        return urlopyAll;
    }

    public void setUrlopyAll(List<WnUrlop> urlopyAll) {
        this.urlopyAll = urlopyAll;
    }

}
