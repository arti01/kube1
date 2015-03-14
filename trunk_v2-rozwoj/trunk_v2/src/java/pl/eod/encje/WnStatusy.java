/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "wn_statusy")
@NamedQueries({
    @NamedQuery(name = "WnStatusy.findAll", query = "SELECT w FROM WnStatusy w"),
    @NamedQuery(name = "WnStatusy.findById", query = "SELECT w FROM WnStatusy w WHERE w.id = :id"),
    @NamedQuery(name = "WnStatusy.findByOpis", query = "SELECT w FROM WnStatusy w WHERE w.opis = :opis"),
    @NamedQuery(name = "WnStatusy.findBySkrot", query = "SELECT w FROM WnStatusy w WHERE w.skrot = :skrot"),
    @NamedQuery(name = "WnStatusy.findByKolor", query = "SELECT w FROM WnStatusy w WHERE w.kolor = :kolor")})
public class WnStatusy implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "opis", unique = true)
    private String opis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "skrot")
    private String skrot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "kolor")
    private String kolor;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusId")
    private List<WnHistoria> wnHistoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statusId")
    private List<WnUrlop> wnUrlopList;

    public WnStatusy() {
    }

    public WnStatusy(Long id) {
        this.id = id;
    }

    public WnStatusy(Long id, String opis, String skrot, String kolor) {
        this.id = id;
        this.opis = opis;
        this.skrot = skrot;
        this.kolor = kolor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSkrot() {
        return skrot;
    }

    public void setSkrot(String skrot) {
        this.skrot = skrot;
    }

    public String getKolor() {
        return kolor;
    }

    public void setKolor(String kolor) {
        this.kolor = kolor;
    }

    public List<WnHistoria> getWnHistoriaList() {
        return wnHistoriaList;
    }

    public void setWnHistoriaList(List<WnHistoria> wnHistoriaList) {
        this.wnHistoriaList = wnHistoriaList;
    }

    public List<WnUrlop> getWnUrlopList() {
        return wnUrlopList;
    }

    public void setWnUrlopList(List<WnUrlop> wnUrlopList) {
        this.wnUrlopList = wnUrlopList;
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
        if (!(object instanceof WnStatusy)) {
            return false;
        }
        WnStatusy other = (WnStatusy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.WnStatusy[ id=" + id + " ]";
    }
    
}
