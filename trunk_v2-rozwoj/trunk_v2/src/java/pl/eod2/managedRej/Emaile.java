/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRej;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import pl.eod.cron4j.EmailMoj;
import pl.eod.cron4j.EmailOdbior;

@ManagedBean(name = "RejEmaile")
@SessionScoped
public class Emaile {

    private DataModel<EmailMoj> lista = new ListDataModel<>();
    @ManagedProperty(value = "#{EmailOdbior}")
    EmailOdbior emailOdb;


    public String list() throws MessagingException, NoSuchProviderException, IOException {
        lista.setWrappedData(emailOdb.getEmaile());
        return "/dcrej/emaile";
    }

    public DataModel<EmailMoj> getLista() {
        return lista;
    }

    public void setLista(DataModel<EmailMoj> lista) {
        this.lista = lista;
    }

    public EmailOdbior getEmailOdb() {
        return emailOdb;
    }

    public void setEmailOdb(EmailOdbior emailOdb) {
        this.emailOdb = emailOdb;
    }

}
