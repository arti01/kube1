package pl.eod.encje;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean(name = "UserRolesConv")
@SessionScoped
public class UserRolesConv implements Converter, Serializable {

    private Object object;
    UserRolesJpaController sC = new UserRolesJpaController();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        String s;
        try {
            s = (value != null) ? value : null;
        } catch (NumberFormatException ec) {
            return null;
        }
        Object wynik = null;
        try {
            wynik = sC.findByNazwa(value);
        } catch (NumberFormatException nfe) {
            wynik = null;
        }
        return wynik;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        UserRoles s = (UserRoles) value;
        //System.out.println((value != null) ? r.getRola() : null);
        return (value != null) ? s.getRolename() : null;
    }
}
