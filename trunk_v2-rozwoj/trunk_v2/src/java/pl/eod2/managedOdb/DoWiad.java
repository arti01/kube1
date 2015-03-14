/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedOdb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "DoWiadOdb")
@SessionScoped
public class DoWiad extends Akcept{
    
     @Override
     public String list() {
        return "/dcodb/doWiadList";
    }
    
    @Override
     public String detale() {
        return "/dcodb/doWiadDetale?faces-redirect=true";
    }
    
}
