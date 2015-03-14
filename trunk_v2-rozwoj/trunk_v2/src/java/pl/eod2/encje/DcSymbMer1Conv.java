/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstConv;
import pl.eod.abstr.AbstKontroler;

@ManagedBean(name = "DcSymbMer1Conv")
@SessionScoped
public class DcSymbMer1Conv extends AbstConv<DcSymbMer1, DcSymbMer1Kontr>{
    private static final long serialVersionUID = 1L;

    public DcSymbMer1Conv() throws InstantiationException, IllegalAccessException {
        super(new DcSymbMer1Kontr());
    }
}
