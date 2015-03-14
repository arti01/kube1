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
@Table(name = "dc_dok_do_wiad_cel_arch")
public class DcDokDoWiadCelArch implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCDOKDOWIADCELARCH")
    @SequenceGenerator(name = "SEQDCDOKDOWIADCELARCH", sequenceName = "SEQDCDOKDOWIADCELARCH")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @JoinColumn(name = "userid", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uzytkownik userid;
    @JoinColumn(name = "id_dok_do_wiad", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokDoWiadomosciArch idDokDoWiad;

    public DcDokDoWiadCelArch() {
    }
    
    public DcDokDoWiadCelArch(DcDokDoWiadCel dwc) {
        this.userid=dwc.getUserid();
    }

    public DcDokDoWiadCelArch(Integer id) {
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

    public DcDokDoWiadomosciArch getIdDokDoWiad() {
        return idDokDoWiad;
    }

    public void setIdDokDoWiad(DcDokDoWiadomosciArch idDokDoWiad) {
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
        if (!(object instanceof DcDokDoWiadCelArch)) {
            return false;
        }
        DcDokDoWiadCelArch other = (DcDokDoWiadCelArch) object;
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
