/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author 103039
 */
@Entity
@Table(name = "DC_PLIK_IMPORT_BIN")
public class DcPlikImportBin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCPLIKIMPORTBIN")
    @SequenceGenerator(name = "SEQDCPLIKIMPORTBIN", sequenceName = "SEQDCPLIKIMPORTBIN")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Lob
    private byte[] plik;
    @OneToOne(mappedBy = "dcPlikImportBin")
    private DcPlikImport dcPlikImport;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DcPlikImport getDcPlikImport() {
        return dcPlikImport;
    }

    public void setDcPlikImport(DcPlikImport dcPlikImport) {
        this.dcPlikImport = dcPlikImport;
    }

    public byte[] getPlik() {
        return plik;
    }

    public void setPlik(byte[] plik) {
        this.plik = plik;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
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
        final DcPlikImportBin other = (DcPlikImportBin) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "pl.eod2.encje.DcPlikImport[ id=" + id + " ]";
    }

}
