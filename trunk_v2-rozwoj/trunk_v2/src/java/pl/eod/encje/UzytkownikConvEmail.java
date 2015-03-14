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

@ManagedBean(name = "UzytkownikConvEmail")
@SessionScoped
public class UzytkownikConvEmail implements Converter, Serializable {
    private static final long serialVersionUID = 1L;

    private Object object;
    UzytkownikJpaController uC = new UzytkownikJpaController();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String u;
        Object wynik = null;
        wynik = uC.findUzytkownikByEmail(value);
        //System.out.println(wynik + "asobiekt");
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Uzytkownik u = (Uzytkownik) value;
        //System.out.println((value != null) ? "asstringEmail"+u.getId().toString() : "asstringEmail"+null);
        return (value != null) ? u.getAdrEmail() : null;
    }
}
