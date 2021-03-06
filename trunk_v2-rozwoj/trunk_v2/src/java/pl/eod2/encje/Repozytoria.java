/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;
import pl.eod.encje.Struktura;

@Entity
@Table(name = "REPOZYTORIA")
@NamedQueries({
    @NamedQuery(name = "Repozytoria.findAll", query = "SELECT d FROM Repozytoria d"),
    @NamedQuery(name = "Repozytoria.findById", query = "SELECT d FROM Repozytoria d WHERE d.id = :id"),
    @NamedQuery(name = "Repozytoria.findByNazwa", query = "SELECT d FROM Repozytoria d WHERE d.nazwa = :nazwa")})
public class Repozytoria extends AbstEncja implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQRepozytoria")
    @SequenceGenerator(name = "SEQRepozytoria", sequenceName = "SEQRepozytoria")
    private Integer id;

    @Size(min = 1, max = 256)
    @Column(name = "nazwa", nullable = false, length = 256, unique = true)
    private String nazwa;
    
    @Size(min = 1, max = 256)
    @Column(name = "sciezka", nullable = false, length = 256, unique = true)
    private String sciezka;
    
    @ManyToMany(mappedBy = "repozytoriaList", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Struktura> strukturaList;
    
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

    public String getSciezka() {
        return sciezka;
    }

    public void setSciezka(String sciezka) {
        this.sciezka = sciezka;
    }

    public List<Struktura> getStrukturaList() {
        return strukturaList;
    }

    public void setStrukturaList(List<Struktura> strukturaList) {
        this.strukturaList = strukturaList;
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
        final Repozytoria other = (Repozytoria) obj;
        if (!Objects.equals(this.nazwa, other.nazwa)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

}
