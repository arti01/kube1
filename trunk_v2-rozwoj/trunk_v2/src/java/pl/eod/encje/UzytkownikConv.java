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
@ManagedBean(name = "UzytkownikConv")
@SessionScoped
public class UzytkownikConv implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object object;
    UzytkownikJpaController uC=new UzytkownikJpaController();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String u;
        //System.out.println(value+"asobiekt start");
        try {
            u = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik=null;
        try{
        wynik=uC.findUzytkownik(new Long(u));
        }catch(NumberFormatException nfe){
            wynik=null;
        }
        return wynik;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Uzytkownik u = (Uzytkownik) value;
        return (value != null) ? u.getId().toString() : null;
    }
}
