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

@ManagedBean(name = "UmMasterGrpConv")
@SessionScoped
public class UmMasterGrpConv implements Converter, Serializable {
    private static final long serialVersionUID = 1L;
    
    private Object object;
    UmMasterGrupaJpaController uC=new UmMasterGrupaJpaController();
    
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
        wynik=uC.findUmMasterGrupa(new Long(u));
        }catch(NumberFormatException nfe){
            //nfe.printStackTrace();
            wynik=null;
        }
        //System.out.println(wynik+"asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        UmMasterGrupa u = (UmMasterGrupa) value;
        //System.out.println((value != null) ? u.getId().toString() : null);
        return (value != null) ? u.getId().toString() : null;
    }
}
