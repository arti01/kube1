/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;
import pl.eod.encje.Uzytkownik;
import pl.eod2.managedUm.UrzadzeniaMg;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "UM_REZERWACJE")
@NamedQueries({
    @NamedQuery(name = "UmRezerwacje.findAll", query = "SELECT d FROM UmRezerwacje d"),
    @NamedQuery(name = "UmRezerwacje.findById", query = "SELECT d FROM UmRezerwacje d WHERE d.id = :id"),
    @NamedQuery(name = "UmRezerwacje.findByNazwa", query = "SELECT d FROM UmRezerwacje d WHERE d.nazwa = :nazwa")})
public class UmRezerwacje extends AbstEncja implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUMRezerwacje")
    @SequenceGenerator(name = "SEQUMRezerwacje", sequenceName = "SEQUMRezerwacje")
    private Integer id;

    @Size(min = 1, max = 256)
    @Column(name = "nazwa", nullable = false, length = 256, unique = false)
    private String nazwa;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataOd;
    
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataDo;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private UmUrzadzenie urzadzenie;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Uzytkownik tworca;
    
    @ManyToMany()
    private List<Uzytkownik> uczestnikList;

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

    public Date getDataOd() {
        return dataOd;
    }

    public void setDataOd(Date dataOd) {
        this.dataOd = dataOd;
    }

    public Date getDataDo() {
        return dataDo;
    }

    public void setDataDo(Date dataDo) {
        this.dataDo = dataDo;
    }

    public UmUrzadzenie getUrzadzenie() {
        return urzadzenie;
    }

    public void setUrzadzenie(UmUrzadzenie urzadzenie) {
        this.urzadzenie = urzadzenie;
    }

    public Uzytkownik getTworca() {
        return tworca;
    }

    public void setTworca(Uzytkownik tworca) {
        this.tworca = tworca;
    }

    public List<Uzytkownik> getUczestnikList() {
        return uczestnikList;
    }

    public void setUczestnikList(List<Uzytkownik> uczestnikList) {
        this.uczestnikList = uczestnikList;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.nazwa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UmRezerwacje other = (UmRezerwacje) obj;
        if (!Objects.equals(this.nazwa, other.nazwa)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
