/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dzial")
@NamedQueries({
    @NamedQuery(name = "Dzial.findAll", query = "SELECT d FROM Dzial d"),
    @NamedQuery(name = "Dzial.findById", query = "SELECT d FROM Dzial d WHERE d.id = :id"),
    @NamedQuery(name = "Dzial.findByNazwa", query = "SELECT d FROM Dzial d WHERE d.nazwa = :nazwa")})

public class Dzial implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDZIAL")
    @SequenceGenerator(name = "SEQDZIAL", sequenceName = "SEQDZIAL")
    @Column(name = "id")
    private Long id;
    
    @Size(min=0, max=255)
    @Column(name = "nazwa", nullable = false)
    private String nazwa;
    
    @Size(min=0, max=5)
    @Column(name = "symbol", nullable = true)
    private String symbol;
    
    @OneToMany(mappedBy = "dzialId", fetch = FetchType.LAZY)
    private List<Struktura> strukturaList;
    
    @Transient
   private List<Struktura> strukturaListWidoczni; 

    public Dzial() {
    }

    public Dzial(Long id) {
        this.id = id;
    }

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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        System.err.println(symbol+"setter");
        this.symbol = symbol;
    }

    public List<Struktura> getStrukturaList() {
        return strukturaList;
    }

    public void setStrukturaList(List<Struktura> strukturaList) {
        this.strukturaList = strukturaList;
    }

    public List<Struktura> getStrukturaListWidoczni() {
        strukturaListWidoczni=new ArrayList<Struktura>();
        for(Struktura s:getStrukturaList()){
            if(!s.isUsuniety()) strukturaListWidoczni.add(s);
        }
        return strukturaListWidoczni;
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
        if (!(object instanceof Dzial)) {
            return false;
        }
        Dzial other = (Dzial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.Dzial[ id=" + id + " ]";
    }
    
}
