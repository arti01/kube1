/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstPlik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_plik")
@NamedQueries({
    @NamedQuery(name = "DcPlik.findAll", query = "SELECT d FROM DcPlik d"),
    @NamedQuery(name = "DcPlik.findById", query = "SELECT d FROM DcPlik d WHERE d.id = :id"),
    @NamedQuery(name = "DcPlik.findByNazwa", query = "SELECT d FROM DcPlik d WHERE d.nazwa = :nazwa")})
public class DcPlik extends AbstPlik implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCPLIK")
    @SequenceGenerator(name = "SEQDCPLIK", sequenceName = "SEQDCPLIK")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(nullable = false, length = 256)
    private String nazwa;
    @Column(name = "data_dodania")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDodania;
    @Lob
    private byte[] plik;
    @JoinColumn(name = "id_dok", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokument idDok;
    

    public DcPlik() {
    }

    public DcPlik(Integer id) {
        this.id = id;
    }

    public DcPlik(Integer id, String nazwa) {
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

    public byte[] getPlik() {
        return plik;
    }

    public void setPlik(byte[] plik) {
        this.plik = plik;
    }

    public DcDokument getIdDok() {
        return idDok;
    }

    public void setIdDok(DcDokument idDok) {
        this.idDok = idDok;
    }

    public Date getDataDodania() {
        return dataDodania;
    }

    public void setDataDodania(Date dataDodania) {
        this.dataDodania = dataDodania;
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
        if (!(object instanceof DcPlik)) {
            return false;
        }
        DcPlik other = (DcPlik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcPlik[ id=" + id + " ]";
    }
    
}
