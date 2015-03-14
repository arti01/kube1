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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import pl.eod2.encje.UmMasterGrupa;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "spolki", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nazwa"})})
@NamedQueries({
    @NamedQuery(name = "Spolki.findAll", query = "SELECT s FROM Spolki s"),
    @NamedQuery(name = "Spolki.findById", query = "SELECT s FROM Spolki s WHERE s.id = :id"),
    @NamedQuery(name = "Spolki.findByNazwa", query = "SELECT s FROM Spolki s WHERE s.nazwa = :nazwa"),
    @NamedQuery(name = "Spolki.findByOpis", query = "SELECT s FROM Spolki s WHERE s.opis = :opis")})
public class Spolki implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQSPOLKI")
    @SequenceGenerator(name = "SEQSPOLKI", sequenceName = "SEQSPOLKI")
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 255)
    @Column(name = "nazwa", nullable = false, length = 255)
    private String nazwa;
    @Size(max = 255)
    @Column(name = "opis", length = 255)
    private String opis;
    @Basic(optional = false)
    @NotNull
    @NotEmpty
    @Size(min = 1, max = 5)
    @Column(name = "symbol", nullable = false, length = 5)
    private String symbol;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spolkaId", orphanRemoval = true)
    @OrderBy(value = "id DESC")
    private List<Uzytkownik> userList;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "spolkaId", orphanRemoval = false)
    @OrderBy(value = "nazwa ASC")
    private List<UmMasterGrupa> umMasterGrupaList;

    public Spolki() {
    }

    public Spolki(Integer id) {
        this.id = id;
    }

    public Spolki(Integer id, String nazwa) {
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

    public List<Uzytkownik> getUserList() {
        return userList;
    }

    public void setUserList(List<Uzytkownik> userList) {
        this.userList = userList;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<UmMasterGrupa> getUmMasterGrupaList() {
        return umMasterGrupaList;
    }

    public void setUmMasterGrupaList(List<UmMasterGrupa> umMasterGrupaList) {
        this.umMasterGrupaList = umMasterGrupaList;
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
        if (!(object instanceof Spolki)) {
            return false;
        }
        Spolki other = (Spolki) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.Spolki[ id=" + id + " ]";
    }
    
}
