/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedArch;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod.abstr.AbstMg;
import pl.eod2.encje.DcDokumentArch;
import pl.eod2.encje.DcDokumentArchKontr;

@ManagedBean(name = "RaportyArch")
@SessionScoped
public class Raporty extends AbstMg<DcDokumentArch, DcDokumentArchKontr> {

    public Raporty() throws InstantiationException, IllegalAccessException {
        super("/dcarch/listW", new DcDokumentArchKontr(), new DcDokumentArch());
    }

    public String nieZwrocone() {
        lista.setWrappedData(dcC.findEntitiesWydane(true));
        return "/dcarch/rapNiezwrocone";
    }
    
    public String wydWgOsoby() {
        lista.setWrappedData(dcC.findEntitiesWydane(false));
        return "/dcarch/rapWydWgOs";
    }
}
