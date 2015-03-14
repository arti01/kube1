package pl.eod.encje;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean(name = "WnStatusyConv")
@SessionScoped
public class WnStatusyConv implements Converter, Serializable {
    
    private Object object;
    WnStatusyJpaController uC=new WnStatusyJpaController();
    
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
        wynik=uC.findWnStatusy(new Long(u));
        }catch(NumberFormatException nfe){
            //nfe.printStackTrace();
            wynik=null;
        }
        //System.out.println(wynik+"asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        WnStatusy u = (WnStatusy) value;
        //System.out.println((value != null) ? u.getId().toString() : null);
        return (value != null) ? u.getId().toString() : null;
    }
}
