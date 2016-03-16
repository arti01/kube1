/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import pl.eod.encje.*;
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
@ManagedBean(name = "UmUrzadzenieConv")
@SessionScoped
public class UmUrzadzenieConv implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object object;
    UmUrzadzenieJpaController uC=new UmUrzadzenieJpaController();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String u;
        //System.out.println(value+"asobiekt start");
        try {
            u = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik;
        try{
        wynik=uC.findUmUrzadzenie(new Long(u));
        }catch(NumberFormatException nfe){
            wynik=null;
        }
        return wynik;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        UmUrzadzenie u = (UmUrzadzenie) value;
        return (value != null) ? u.getId().toString() : null;
    }
}
