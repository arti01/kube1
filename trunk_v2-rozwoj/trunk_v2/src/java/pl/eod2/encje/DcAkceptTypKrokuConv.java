/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean(name = "DcAkceptTypKrokuConv")
@SessionScoped
public class DcAkceptTypKrokuConv implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object object;
    DcAkceptTypKrokuJpaController uC=new DcAkceptTypKrokuJpaController();
    
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
        wynik=uC.findDcAkceptTypKroku(new Long(u).intValue());
        }catch(NumberFormatException nfe){
            //nfe.printStackTrace();
            wynik=null;
        }
        //System.out.println(wynik+"asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        DcAkceptTypKroku u = (DcAkceptTypKroku) value;
        //System.out.println((value != null) ? u.getId().toString() : null);
        return (value != null) ? u.getId().toString() : null;
    }
}
