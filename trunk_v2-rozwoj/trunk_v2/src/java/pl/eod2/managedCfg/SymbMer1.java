/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedCfg;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcSymbMer1;
import pl.eod2.encje.DcSymbMer1Kontr;

@ManagedBean(name = "SymbMer1Cfg")
@SessionScoped
public class SymbMer1 extends AbstMg<DcSymbMer1, DcSymbMer1Kontr>{
    public SymbMer1() throws InstantiationException, IllegalAccessException {
        super("/dccfg/symbMer1", new DcSymbMer1Kontr(), new DcSymbMer1());
    }
}