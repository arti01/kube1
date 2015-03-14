/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "wn_limity")
@NamedQueries({
    @NamedQuery(name = "WnLimity.findAll", query = "SELECT w FROM WnLimity w")})
@PrimaryKey(validation = IdValidation.NONE)
public class WnLimity implements Serializable {
    private static final long serialVersionUID = 1L;
    //@Size(max = 255)
    //@Column(name = "username")
    @Id
    @JoinColumn(name = "username", referencedColumnName = "ext_id")
    @OneToOne
    private Uzytkownik username;
    
    @Size(max = 50)
    @Column(name = "ulimit")
    private String ulimit;

    public WnLimity() {
    }

    public Uzytkownik getUsername() {
        return username;
    }

    public void setUsername(Uzytkownik username) {
        this.username = username;
    }

    

    public String getUlimit() {
        return ulimit;
    }

    public void setUlimit(String ulimit) {
        this.ulimit = ulimit;
    }
    
}
