/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author arti01
 */
@ManagedBean(name = "StrukturaConv")
@SessionScoped
public class StrukturaConv implements Converter, Serializable {
    private Object object;
    StrukturaJpaController sC=new StrukturaJpaController();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String s;
        try {
            s = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik=null;
        try{
        wynik=sC.findStruktura(new Long(s));
        }catch(NumberFormatException nfe){
            wynik=null;
        }
        return wynik;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Struktura s = (Struktura) value;
        //System.out.println((value != null) ? r.getRola() : null);
        return (value != null) ? s.getId().toString() : null;
    }
}
