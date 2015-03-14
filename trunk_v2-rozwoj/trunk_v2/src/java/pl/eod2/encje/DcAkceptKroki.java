/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import pl.eod.encje.Uzytkownik;

/**
 *
 * @author 103039
 */
@Entity
@Table(name = "DC_AKCEPT_KROKI")
@NamedQueries({
    @NamedQuery(name = "DcAkceptKroki.findAll", query = "SELECT d FROM DcAkceptKroki d"),
    @NamedQuery(name = "DcAkceptKroki.findById", query = "SELECT d FROM DcAkceptKroki d WHERE d.id = :id"),
    @NamedQuery(name = "DcAkceptKroki.findByRodzajId", query = "SELECT d FROM DcAkceptKroki d WHERE d.rodzajId = :rodzajId")})
public class DcAkceptKroki implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCAKCEPTKROK")
    @SequenceGenerator(name = "SEQDCAKCEPTKROK", sequenceName = "SEQDCAKCEPTKROK")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer lp;
    @JoinColumn(name = "ID_DC_TYP_KROKU", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcAkceptTypKroku dcAckeptTypKroku;
    @JoinColumn(name = "id", referencedColumnName = "id",nullable = false)
    @ManyToMany()
    private List<Uzytkownik> uzytkownikList;
    @JoinColumn(name = "ID_DC_RODZAJ", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcRodzaj rodzajId;
    

    public DcAkceptKroki() {
    }

    public DcAkceptKroki(Integer id) {
        this.id = id;
    }

    public DcAkceptKroki(Integer id, int status, int idDcRodzaj, int idDcTypKroku) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DcRodzaj getRodzajId() {
        return rodzajId;
    }

    public void setRodzajId(DcRodzaj rodzajId) {
        this.rodzajId = rodzajId;
    }
    
    public DcAkceptTypKroku getDcAckeptTypKroku() {
        return dcAckeptTypKroku;
    }

    public void setDcAckeptTypKroku(DcAkceptTypKroku dcAckeptTypKroku) {
        this.dcAckeptTypKroku = dcAckeptTypKroku;
    }

    public List<Uzytkownik> getUzytkownikList() {
        return uzytkownikList;
    }

    public void setUzytkownikList(List<Uzytkownik> uzytkownikList) {
        this.uzytkownikList = uzytkownikList;
    }

    public Integer getLp() {
        return lp;
    }

    public void setLp(Integer lp) {
        this.lp = lp;
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
        if (!(object instanceof DcAkceptKroki)) {
            return false;
        }
        DcAkceptKroki other = (DcAkceptKroki) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcAkceptKroki[ id=" + id + " ]";
    }
    
}
