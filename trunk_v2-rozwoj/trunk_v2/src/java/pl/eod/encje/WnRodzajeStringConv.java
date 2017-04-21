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

@ManagedBean(name = "WnRodzajeStringConv")
@SessionScoped
public class WnRodzajeStringConv implements Converter, Serializable {

    private static final long serialVersionUID = 1L;
    
    private Object object;
    WnRodzajeJpaController uC=new WnRodzajeJpaController();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String u;
        try {
            u = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik=null;
        try{
        wynik=uC.findWnRodzaje(new Long(u));
        }catch(NumberFormatException nfe){
            wynik=null;
        }
        //System.out.println(wynik+"asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        String u;
        try{
            u = (String) value;
        }catch (ClassCastException e){
            u=null;
        }
        //System.out.println((value != null) ? u.getId().toString() : null);
        return (value != null) ? u : null;
    }
}
