/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcArchZajetosc;
import pl.eod2.encje.DcArchZajetoscKontr;

@ManagedBean(name = "ZajetoscArch")
@SessionScoped
public class ZajetoscArch extends AbstMg<DcArchZajetosc, DcArchZajetoscKontr> {


    public ZajetoscArch() throws InstantiationException, IllegalAccessException {
        super("/dcarch/listZajetoscArch", new DcArchZajetoscKontr(), new DcArchZajetosc());
        super.refresh();
    }

    
}
