/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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

@Entity
@Table(name = "dc_arch_zajetosc")
@NamedQueries({
    @NamedQuery(name = "DcArchZajetosc.findAll", query = "SELECT d FROM DcArchZajetosc d"),
    @NamedQuery(name = "DcArchZajetosc.findById", query = "SELECT d FROM DcArchZajetosc d WHERE d.id = :id"),
    @NamedQuery(name = "DcArchZajetosc.findByNazwa", query = "SELECT d FROM DcArchZajetosc d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcArchZajetosc.findByOpis", query = "SELECT d FROM DcArchZajetosc d WHERE d.opis = :opis")})
public class DcArchZajetosc extends AbstEncja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCARCHZAJ")
    @SequenceGenerator(name = "SEQDCARCHZAJ", sequenceName = "SEQDCARCHZAJ")
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
    private double razemMetrow;
    private double zajetychMetrow;
    @Transient
    private double wolnychMetrow;


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

    public double getRazemMetrow() {
        return razemMetrow;
    }

    public void setRazemMetrow(double razemMetrow) {
        this.razemMetrow = razemMetrow;
    }

    public double getZajetychMetrow() {
        return zajetychMetrow;
    }

    public void setZajetychMetrow(double zajetychMetrow) {
        this.zajetychMetrow = zajetychMetrow;
    }

    public double getWolnychMetrow() {
        return razemMetrow-zajetychMetrow;
    }

    @Override
    public String toString() {
        return "DcArchZajetosc{" + "id=" + id + ", nazwa=" + nazwa + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
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
        final DcArchZajetosc other = (DcArchZajetosc) obj;
        return Objects.equals(this.id, other.id);
    }
    
}
