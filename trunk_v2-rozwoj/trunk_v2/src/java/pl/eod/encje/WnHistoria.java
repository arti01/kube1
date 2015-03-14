/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "wn_historia")
@NamedQueries({
    @NamedQuery(name = "WnHistoria.findAll", query = "SELECT w FROM WnHistoria w"),
    @NamedQuery(name = "WnHistoria.findById", query = "SELECT w FROM WnHistoria w WHERE w.id = :id"),
    @NamedQuery(name = "WnHistoria.findByDataZmiany", query = "SELECT w FROM WnHistoria w WHERE w.dataZmiany = :dataZmiany")})
public class WnHistoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQWNHIST")
    @SequenceGenerator(name = "SEQWNHIST", sequenceName = "SEQWNHIST")
    private Long id;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_zmiany")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataZmiany;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "opis_zmiany")
    private String opisZmiany;
    
    @JoinColumn(name = "urlop_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private WnUrlop urlopId;
    
    
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private WnStatusy statusId;
    
    @JoinColumn(name = "zmieniajacy", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uzytkownik zmieniajacy;
    
    @JoinColumn(name = "akceptant", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uzytkownik akceptant;
    
    

    public WnHistoria() {
    }

    public WnHistoria(Long id) {
        this.id = id;
    }

    public WnHistoria(Long id, Date dataZmiany) {
        this.id = id;
        this.dataZmiany = dataZmiany;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataZmiany() {
        return dataZmiany;
    }

    public void setDataZmiany(Date dataZmiany) {
        this.dataZmiany = dataZmiany;
    }

    public WnUrlop getUrlopId() {
        return urlopId;
    }

    public void setUrlopId(WnUrlop urlopId) {
        this.urlopId = urlopId;
    }

    public WnStatusy getStatusId() {
        return statusId;
    }

    public void setStatusId(WnStatusy statusId) {
        this.statusId = statusId;
    }

    public Uzytkownik getZmieniajacy() {
        return zmieniajacy;
    }

    public void setZmieniajacy(Uzytkownik zmieniajacy) {
        this.zmieniajacy = zmieniajacy;
    }

    public String getOpisZmiany() {
        return opisZmiany;
    }

    public void setOpisZmiany(String opisZmiany) {
        this.opisZmiany = opisZmiany;
    }

    public Uzytkownik getAkceptant() {
        return akceptant;
    }

    public void setAkceptant(Uzytkownik akceptant) {
        this.akceptant = akceptant;
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
        if (!(object instanceof WnHistoria)) {
            return false;
        }
        WnHistoria other = (WnHistoria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.WnHistoria[ id=" + id + " ]";
    }
    
}
