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
@ManagedBean(name = "DzialConv")
@SessionScoped
public class DzialConv implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    private Object object;
    DzialJpaController dC=new DzialJpaController();
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String dzial;
        try {
            dzial = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik=null;
        try{
        wynik=dC.findDzial(new Long(dzial));
        }catch(NumberFormatException nfe){
            wynik=null;
        }
        return wynik;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Dzial r = (Dzial) value;
        //System.out.println((value != null) ? r.getRola() : null);
        return (value != null) ? r.getId().toString() : null;
    }
}
