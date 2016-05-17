/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import pl.eod.encje.Uzytkownik;

@Entity
@Table(name = "KALEND_OSOBY_DEC")
@NamedQueries({
    @NamedQuery(name = "KalendOsobyDec.findAll", query = "SELECT d FROM KalendOsobyDec d"),})
public class KalendOsobyDec implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQKALENDOSOBYDEC")
    @SequenceGenerator(name = "SEQKALENDOSOBYDEC", sequenceName = "SEQKALENDOSOBYDEC")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    
    @OneToOne
    private Uzytkownik user;
    
    @NotNull
    @Column(nullable = false, length = 7)
    private String kolor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Uzytkownik getUser() {
        return user;
    }

    public void setUser(Uzytkownik user) {
        this.user = user;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KalendOsobyDec other = (KalendOsobyDec) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    

}
