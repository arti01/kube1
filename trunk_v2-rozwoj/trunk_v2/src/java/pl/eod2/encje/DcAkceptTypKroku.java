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
@Table(name = "DC_AKCEPT_TYP_KROKU")
@NamedQueries({
    @NamedQuery(name = "DcAkceptTypKroku.findAll", query = "SELECT d FROM DcAkceptTypKroku d"),
    @NamedQuery(name = "DcAkceptTypKroku.findById", query = "SELECT d FROM DcAkceptTypKroku d WHERE d.id = :id"),
    @NamedQuery(name = "DcAkceptTypKroku.findByNazwa", query = "SELECT d FROM DcAkceptTypKroku d WHERE d.nazwa = :nazwa")})
public class DcAkceptTypKroku implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAZWA", nullable = false, length = 255)
    private String nazwa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dcAckeptTypKroku", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<DcAkceptKroki> dcAkceptKrokiList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dcAckeptTypKroku", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<DcDokumentKrok> dcDocKrokiList;

    public DcAkceptTypKroku() {
    }

    public DcAkceptTypKroku(Integer id) {
        this.id = id;
    }

    public DcAkceptTypKroku(Integer id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
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

    public List<DcAkceptKroki> getDcAkceptKrokiList() {
        return dcAkceptKrokiList;
    }

    public void setDcAkceptKrokiList(List<DcAkceptKroki> dcAkceptKrokiList) {
        this.dcAkceptKrokiList = dcAkceptKrokiList;
    }

    public List<DcDokumentKrok> getDcDocKrokiList() {
        return dcDocKrokiList;
    }

    public void setDcDocKrokiList(List<DcDokumentKrok> dcDocKrokiList) {
        this.dcDocKrokiList = dcDocKrokiList;
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
        if (!(object instanceof DcAkceptTypKroku)) {
            return false;
        }
        DcAkceptTypKroku other = (DcAkceptTypKroku) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcAkceptTypKroku[ id=" + id + " ]";
    }
    
}
