/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.encje.Spolki;
/**
 *
 * @author arti01
 */
@Entity
@Table(name = "UM_MASTERGRUPA")
@NamedQueries({
    @NamedQuery(name = "UmMasterGrupa.findAll", query = "SELECT d FROM UmMasterGrupa d"),
    @NamedQuery(name = "UmMasterGrupa.findById", query = "SELECT d FROM UmMasterGrupa d WHERE d.id = :id"),
    @NamedQuery(name = "UmMasterGrupa.findByNazwa", query = "SELECT d FROM UmMasterGrupa d WHERE d.nazwa = :nazwa")})
public class UmMasterGrupa implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUMGRUPA")
    @SequenceGenerator(name = "SEQUMGRUPA", sequenceName = "SEQUMGRUPA")
    private Long id;
    @Size(min = 1, max = 256)
    @Column(name = "nazwa", nullable = false, length = 256, unique = true)
    private String nazwa;
    @Size(max = 10485760)
    @Lob
    private String opis;
    @OrderBy(value = "nazwa ASC")
    @OneToMany(mappedBy = "masterGrp", cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<UmGrupa> grupaList;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Spolki spolkaId;
    @ManyToMany(mappedBy = "umMasterGrupaList", fetch = FetchType.EAGER)
    private List<DcRodzaj> rodzajeDokList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<UmGrupa> getGrupaList() {
        return grupaList;
    }

    public void setGrupaList(List<UmGrupa> grupaList) {
        this.grupaList = grupaList;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Spolki getSpolkaId() {
        return spolkaId;
    }

    public void setSpolkaId(Spolki spolkaId) {
        this.spolkaId = spolkaId;
    }

    public List<DcRodzaj> getRodzajeDokList() {
        return rodzajeDokList;
    }

    public void setRodzajeDokList(List<DcRodzaj> rodzajeDokList) {
        this.rodzajeDokList = rodzajeDokList;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UmMasterGrupa other = (UmMasterGrupa) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
        
}
