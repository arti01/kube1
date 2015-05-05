/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstKontroler;

@ManagedBean(name = "RodzajTypyPol")
@SessionScoped
public class DcRodzajTypyPolKontr extends AbstKontroler<DcRodzajTypyPol> {

    public DcRodzajTypyPolKontr() {
        super(new DcRodzajTypyPol());
    }

    
}
