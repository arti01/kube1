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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author arti01
 */
@Entity(name="DcRodzajGrupa")
@Table(name = "dc_rodzaj_grupa")
@NamedQueries({
    @NamedQuery(name = "DcRodzajGrupa.findAll", query = "SELECT d FROM DcRodzajGrupa d"),
    @NamedQuery(name = "DcRodzajGrupa.findById", query = "SELECT d FROM DcRodzajGrupa d WHERE d.id = :id"),
    @NamedQuery(name = "DcRodzajGrupa.findByNazwa", query = "SELECT d FROM DcRodzajGrupa d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcRodzajGrupa.findByOpis", query = "SELECT d FROM DcRodzajGrupa d WHERE d.opis = :opis")})
public class DcRodzajGrupa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCGRUPARODZAJ")
    @SequenceGenerator(name = "SEQDCGRUPARODZAJ", sequenceName = "SEQDCGRUPARODZAJ")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @NotEmpty
    @Size(min = 3, max = 256)
    @Column(nullable = false, length = 256)
    private String nazwa;
    @Size(max = 10485760)
    @Lob
    private String opis;
    private int urzMed;
    private int archiw;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "idRodzajGrupa", fetch = FetchType.LAZY)
    private List<DcRodzaj> dcRodzajList;

    public DcRodzajGrupa() {
    }

    public DcRodzajGrupa(Integer id) {
        this.id = id;
    }

    public DcRodzajGrupa(Integer id, String nazwa) {
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

    public boolean isUrzMed() {
        return urzMed == 1;
    }

    public void setUrzMed(boolean urzMed) {
        if(urzMed) this.urzMed=1;
        else this.urzMed=0;
    }
    
    public boolean isArchiw() {
        return archiw == 1;
    }

    public void setArchiw(boolean archiw) {
        if(archiw) this.archiw=1;
        else this.archiw=0;
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
        if (!(object instanceof DcRodzajGrupa)) {
            return false;
        }
        DcRodzajGrupa other = (DcRodzajGrupa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcRodzajGrupa[ id=" + id + " ]";
    }
    
}
