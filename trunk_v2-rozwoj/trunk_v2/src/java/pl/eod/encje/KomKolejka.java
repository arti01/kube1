/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "kom_kolejka")
@NamedQueries({
    @NamedQuery(name = "KomKolejka.findAll", query = "SELECT k FROM KomKolejka k")})
public class KomKolejka implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQKOMKOL")
    @SequenceGenerator(name = "SEQKOMKOL", sequenceName = "SEQKOMKOL")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "adres_list")
    private String adresList;
    @Size(max = 2147483647)
    @Column(name = "tresc", length = 1000)
    private String tresc;
    @Column(name = "status")
    private Integer status;
    @Size(max = 255)
    @Column(name = "temat")
    private String temat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_insert")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsert;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_wysylk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataWysylk;
    @Column(name = "id_dokumentu")
    private int idDokumenu;
    
    public KomKolejka() {
    }

    public KomKolejka(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdresList() {
        return adresList;
    }

    public void setAdresList(String adresList) {
        this.adresList = adresList;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTemat() {
        return temat;
    }

    public void setTemat(String temat) {
        this.temat = temat;
    }

    public Date getDataInsert() {
        return dataInsert;
    }

    public void setDataInsert(Date dataInsert) {
        this.dataInsert = dataInsert;
    }

    public Date getDataWysylk() {
        return dataWysylk;
    }

    public void setDataWysylk(Date dataWysylk) {
        this.dataWysylk = dataWysylk;
    }

    public int getIdDokumenu() {
        return idDokumenu;
    }

    public void setIdDokumenu(int idDokumenu) {
        this.idDokumenu = idDokumenu;
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
        if (!(object instanceof KomKolejka)) {
            return false;
        }
        KomKolejka other = (KomKolejka) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.KomKolejka[ id=" + id + " ]";
    }
    
}
