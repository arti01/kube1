/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedCfg;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcSymbMer2;
import pl.eod2.encje.DcSymbMer2Kontr;

@ManagedBean(name = "SymbMer2Cfg")
@SessionScoped
public class SymbMer2 extends AbstMg<DcSymbMer2, DcSymbMer2Kontr>{
    public SymbMer2() throws InstantiationException, IllegalAccessException {
        super("/dccfg/symbMer2", new DcSymbMer2Kontr(), new DcSymbMer2());
    }
}