/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedUm;

import pl.eod2.encje.Kalendarz;

/**
 *
 * @author arti01
 */
public class MojKalTab extends Kalendarz{
    private static final long serialVersionUID = 1L;
    String typ;

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }
    
}
