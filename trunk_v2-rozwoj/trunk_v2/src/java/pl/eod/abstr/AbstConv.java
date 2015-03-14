/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.abstr;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class AbstConv <X extends AbstEncja, Y extends AbstKontroler<X>> implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    
    private final Y dcC;
    
    @SuppressWarnings("unchecked")
    public AbstConv(AbstKontroler<X> ak) throws InstantiationException, IllegalAccessException {
        this.dcC = (Y) ak;
    }
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String u;
        try {
            u = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            //ec.printStackTrace();
            return null;
        }
        Object wynik;
        try{
        wynik=dcC.findObiekt(new Long(u).intValue());
        }catch(NumberFormatException nfe){
            //nfe.printStackTrace();
            wynik=null;
        }
        //System.out.println(wynik+"asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        @SuppressWarnings("unchecked")
        X u = (X) value;
        //System.out.println((value != null) ? u.getId().toString() : null);
        return (value != null) ? u.getId().toString() : null;
    }
}
