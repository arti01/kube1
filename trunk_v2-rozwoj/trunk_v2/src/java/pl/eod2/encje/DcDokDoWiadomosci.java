/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.encje.Uzytkownik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_dok_do_wiadomosci")
@NamedQueries({
    @NamedQuery(name = "DcDokDoWiadomosci.findAll", query = "SELECT d FROM DcDokDoWiadomosci d"),
    @NamedQuery(name = "DcDokDoWiadomosci.findById", query = "SELECT d FROM DcDokDoWiadomosci d WHERE d.id = :id"),
    @NamedQuery(name = "DcDokDoWiadomosci.findByTresc", query = "SELECT d FROM DcDokDoWiadomosci d WHERE d.tresc = :tresc"),
    @NamedQuery(name = "DcDokDoWiadomosci.findByDataWprow", query = "SELECT d FROM DcDokDoWiadomosci d WHERE d.dataWprow = :dataWprow"),
    @NamedQuery(name = "DcDokDoWiadomosci.findByWprowadzil", query = "SELECT d FROM DcDokDoWiadomosci d WHERE d.wprowadzil = :wprowadzil"),
    @NamedQuery(name = "DcDokDoWiadomosci.findByDokid", query = "SELECT d FROM DcDokDoWiadomosci d WHERE d.dokid = :dokid")})
public class DcDokDoWiadomosci implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCDOKDOWIAD")
    @SequenceGenerator(name = "SEQDCDOKDOWIAD", sequenceName = "SEQDCDOKDOWIAD")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Size(max = 255)
    @Column(name = "tresc", length = 255)
    private String tresc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_wprow", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataWprow;
    @JoinColumn(name = "wprowadzil", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uzytkownik wprowadzil;
    
    @JoinColumn(name = "dokid", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokument dokid;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDokDoWiad", fetch = FetchType.LAZY)
    private List<DcDokDoWiadCel> dcDokDoWiadCelList;

    public DcDokDoWiadomosci() {
    }

    public DcDokDoWiadomosci(Integer id) {
        this.id = id;
    }

    public DcDokDoWiadomosci(Integer id, Date dataWprow, int wprowadzil, int dokid) {
        this.id = id;
        this.dataWprow = dataWprow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public Date getDataWprow() {
        return dataWprow;
    }

    public void setDataWprow(Date dataWprow) {
        this.dataWprow = dataWprow;
    }

    public Uzytkownik getWprowadzil() {
        return wprowadzil;
    }

    public void setWprowadzil(Uzytkownik wprowadzil) {
        this.wprowadzil = wprowadzil;
    }

    public DcDokument getDokid() {
        return dokid;
    }

    public void setDokid(DcDokument dokid) {
        this.dokid = dokid;
    }

    public List<DcDokDoWiadCel> getDcDokDoWiadCelList() {
        return dcDokDoWiadCelList;
    }

    public void setDcDokDoWiadCelList(List<DcDokDoWiadCel> dcDokDoWiadCelList) {
        this.dcDokDoWiadCelList = dcDokDoWiadCelList;
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
        if (!(object instanceof DcDokDoWiadomosci)) {
            return false;
        }
        DcDokDoWiadomosci other = (DcDokDoWiadomosci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcDokDoWiadomosci[ id=" + id + " ]";
    }
    
}
