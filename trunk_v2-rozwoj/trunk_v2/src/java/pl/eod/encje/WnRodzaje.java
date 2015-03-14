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
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "wn_rodzaje")
@NamedQueries({
    @NamedQuery(name = "WnRodzaje.findAll", query = "SELECT w FROM WnRodzaje w ORDER BY w.id"),
    @NamedQuery(name = "WnRodzaje.findById", query = "SELECT w FROM WnRodzaje w WHERE w.id = :id"),
    @NamedQuery(name = "WnRodzaje.findByOpis", query = "SELECT w FROM WnRodzaje w WHERE w.opis = :opis")})
public class WnRodzaje implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "opis", unique = true)
    private String opis;
    @Basic(optional = false)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 5)
    @Column(name = "symbol", nullable = false, length = 5)
    private String symbol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rodzajId")
    private List<WnUrlop> wnUrlopList;

    public WnRodzaje() {
    }

    public WnRodzaje(Long id) {
        this.id = id;
    }

    public WnRodzaje(Long id, String opis) {
        this.id = id;
        this.opis = opis;
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

    public List<WnUrlop> getWnUrlopList() {
        return wnUrlopList;
    }

    public void setWnUrlopList(List<WnUrlop> wnUrlopList) {
        this.wnUrlopList = wnUrlopList;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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
        if (!(object instanceof WnRodzaje)) {
            return false;
        }
        WnRodzaje other = (WnRodzaje) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.WnRodzaje[ id=" + id + " ]";
    }
    
}
