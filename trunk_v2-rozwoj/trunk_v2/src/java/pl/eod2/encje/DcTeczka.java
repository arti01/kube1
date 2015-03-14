/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_teczka")
@NamedQueries({
    @NamedQuery(name = "DcTeczka.findAll", query = "SELECT d FROM DcTeczka d"),
    @NamedQuery(name = "DcTeczka.findById", query = "SELECT d FROM DcTeczka d WHERE d.id = :id"),
    @NamedQuery(name = "DcTeczka.findByNazwa", query = "SELECT d FROM DcTeczka d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcTeczka.findDlaStatus", query = "SELECT d.teczkaId from DcDokument d where d.dokStatusId=:status"),
    @NamedQuery(name = "DcTeczka.findByOpis", query = "SELECT d FROM DcTeczka d WHERE d.opis = :opis")})
public class DcTeczka extends AbstEncja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCTECZKA")
    @SequenceGenerator(name = "SEQDCTECZKA", sequenceName = "SEQDCTECZKA")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(nullable = false, length = 256, unique = true)
    private String nazwa;
    @Size(max = 10485760)
    @Lob
    private String opis;
    @OneToMany(mappedBy = "teczkaId", fetch = FetchType.LAZY)
    private List<DcDokument> dcDokumentList;
    @OneToMany(mappedBy = "teczkaId", fetch = FetchType.LAZY)
    private List<DcDokumentArch> dcDokumentArchList;
    @Transient
    private List<DcDokument> dcDokumentListDoArch;
    @Transient
    private List<DcDokumentArch> dcDokumentListPoczDoArch;

    public DcTeczka() {
    }

    public DcTeczka(Integer id) {
        this.id = id;
    }

    public DcTeczka(Integer id, String nazwa) {
        this.id = id;
        this.nazwa = nazwa;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getNazwa() {
        return nazwa;
    }

    @Override
    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public List<DcDokument> getDcDokumentList() {
        return dcDokumentList;
    }

    public void setDcDokumentList(List<DcDokument> dcDokumentList) {
        this.dcDokumentList = dcDokumentList;
    }

    public List<DcDokument> getDcDokumentListDoArch() {
        List<DcDokument> wynik = new ArrayList<>();
        for (DcDokument dok : this.dcDokumentList) {
            if (dok.getDokStatusId().getId() == 3) {
                wynik.add(dok);
            }
        }
        return wynik;
    }

    public List<DcDokumentArch> getDcDokumentListPoczDoArch() {
        List<DcDokumentArch> wynik = new ArrayList<>();
        for (DcDokumentArch dok : this.dcDokumentArchList) {
            if (dok.getDokStatusId().getId() == 7) {
                wynik.add(dok);
            }
        }
        return wynik;
    }

    public List<DcDokumentArch> getDcDokumentArchList() {
        return dcDokumentArchList;
    }

    public void setDcDokumentArchList(List<DcDokumentArch> dcDokumentArchList) {
        this.dcDokumentArchList = dcDokumentArchList;
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
        if (!(object instanceof DcTeczka)) {
            return false;
        }
        DcTeczka other = (DcTeczka) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcTeczka[ id=" + id + " ]";
    }

}
