/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import pl.eod.encje.Uzytkownik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_dok_do_wiad_cel")
@NamedQueries({
    @NamedQuery(name = "DcDokDoWiadCel.findAll", query = "SELECT d FROM DcDokDoWiadCel d"),
    @NamedQuery(name = "DcDokDoWiadCel.findById", query = "SELECT d FROM DcDokDoWiadCel d WHERE d.id = :id"),
    @NamedQuery(name = "DcDokDoWiadCel.findByUserid", query = "SELECT d FROM DcDokDoWiadCel d WHERE d.userid = :userid")})
public class DcDokDoWiadCel implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCDOKDOWIADCEL")
    @SequenceGenerator(name = "SEQDCDOKDOWIADCEL", sequenceName = "SEQDCDOKDOWIADCEL")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @JoinColumn(name = "userid", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uzytkownik userid;
    @JoinColumn(name = "id_dok_do_wiad", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokDoWiadomosci idDokDoWiad;

    public DcDokDoWiadCel() {
    }

    public DcDokDoWiadCel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Uzytkownik getUserid() {
        return userid;
    }

    public void setUserid(Uzytkownik userid) {
        this.userid = userid;
    }

    public DcDokDoWiadomosci getIdDokDoWiad() {
        return idDokDoWiad;
    }

    public void setIdDokDoWiad(DcDokDoWiadomosci idDokDoWiad) {
        this.idDokDoWiad = idDokDoWiad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DcDokDoWiadCel)) {
            return false;
        }
        DcDokDoWiadCel other = (DcDokDoWiadCel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcDokDoWiadCel[ id=" + id + " ]";
    }
    
}
