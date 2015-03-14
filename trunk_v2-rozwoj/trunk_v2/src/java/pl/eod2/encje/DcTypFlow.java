/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 103039
 */
@Entity
@Table(name = "DC_TYP_FLOW")
@NamedQueries({
    @NamedQuery(name = "DcTypFlow.findAll", query = "SELECT d FROM DcTypFlow d"),
    @NamedQuery(name = "DcTypFlow.findById", query = "SELECT d FROM DcTypFlow d WHERE d.id = :id"),
    @NamedQuery(name = "DcTypFlow.findByNazwa", query = "SELECT d FROM DcTypFlow d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcTypFlow.findByOpis", query = "SELECT d FROM DcTypFlow d WHERE d.opis = :opis")})
public class DcTypFlow implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Size(max = 255)
    @Column(name = "NAZWA", length = 255)
    private String nazwa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "OPIS", nullable = false, length = 255)
    private String opis;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTypFlow", fetch = FetchType.LAZY)
    private List<DcRodzaj> dcRodzajList;

    public DcTypFlow() {
    }

    public DcTypFlow(Integer id) {
        this.id = id;
    }

    public DcTypFlow(Integer id, String opis) {
        this.id = id;
        this.opis = opis;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<DcRodzaj> getDcRodzajList() {
        return dcRodzajList;
    }

    public void setDcRodzajList(List<DcRodzaj> dcRodzajList) {
        this.dcRodzajList = dcRodzajList;
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
        if (!(object instanceof DcTypFlow)) {
            return false;
        }
        DcTypFlow other = (DcTypFlow) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcTypFlow[ id=" + id + " ]";
    }
    
}
