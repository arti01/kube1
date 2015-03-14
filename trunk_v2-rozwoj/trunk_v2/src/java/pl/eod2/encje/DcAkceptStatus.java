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
@Table(name = "DC_AKCEPT_STATUS")
@NamedQueries({
    @NamedQuery(name = "DcAkceptStatus.findAll", query = "SELECT d FROM DcAkceptStatus d"),
    @NamedQuery(name = "DcAkceptStatus.findById", query = "SELECT d FROM DcAkceptStatus d WHERE d.id = :id"),
    @NamedQuery(name = "DcAkceptStatus.findByNazwa", query = "SELECT d FROM DcAkceptStatus d WHERE d.nazwa = :nazwa")})
public class DcAkceptStatus implements Serializable {
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "akcept", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokumentKrokUzytkownik> dokKrokUserList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "akcept", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokumentKrok> dokKrokList;
    
    public DcAkceptStatus() {
    }

    public DcAkceptStatus(Integer id) {
        this.id = id;
    }

    public DcAkceptStatus(Integer id, String nazwa) {
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

    public List<DcDokumentKrokUzytkownik> getDokKrokUserList() {
        return dokKrokUserList;
    }

    public void setDokKrokUserList(List<DcDokumentKrokUzytkownik> dokKrokUserList) {
        this.dokKrokUserList = dokKrokUserList;
    }

    public List<DcDokumentKrok> getDokKrokList() {
        return dokKrokList;
    }

    public void setDokKrokList(List<DcDokumentKrok> dokKrokList) {
        this.dokKrokList = dokKrokList;
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
        if (!(object instanceof DcAkceptStatus)) {
            return false;
        }
        DcAkceptStatus other = (DcAkceptStatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcAkceptStatus[ id=" + id + " ]";
    }
    
}
