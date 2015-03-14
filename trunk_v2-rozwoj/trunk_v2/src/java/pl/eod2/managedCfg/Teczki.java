/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedCfg;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcTeczka;
import pl.eod2.encje.DcTeczkaKontr;

@ManagedBean(name = "TeczkiCfg")
@SessionScoped
public class Teczki extends AbstMg<DcTeczka, DcTeczkaKontr>{
    public Teczki() throws InstantiationException, IllegalAccessException {
        super("/dccfg/teczki", new DcTeczkaKontr(), new DcTeczka());
    }
}