/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;

/**
 *
 * @author 103039
 */
@Entity
@Table(name = "DC_DOKUMENT_KROK")
@NamedQueries({
    @NamedQuery(name = "DcDokumentKrok.findAll", query = "SELECT d FROM DcDokumentKrok d"),
    @NamedQuery(name = "DcDokumentKrok.findById", query = "SELECT d FROM DcDokumentKrok d WHERE d.id = :id"),
    @NamedQuery(name = "DcDokumentKrok.findByLp", query = "SELECT d FROM DcDokumentKrok d WHERE d.lp = :lp"),
    @NamedQuery(name = "DcDokumentKrok.findByAkcept", query = "SELECT d FROM DcDokumentKrok d WHERE d.akcept = :akcept")})
public class DcDokumentKrok implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDcDokumentKrok")
    @SequenceGenerator(name = "SEQDcDokumentKrok", sequenceName = "SEQDcDokumentKrok")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LP", nullable = false)
    private int lp;
    @JoinColumn(name = "ID_DOKUMENT", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokument idDok;
    
    @JoinColumn(name = "AKCEPT", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcAkceptStatus akcept;
    @JoinColumn(name = "ID_DC_TYP_KROKU", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcAkceptTypKroku dcAckeptTypKroku;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDokumentKrok", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokumentKrokUzytkownik> dcKrokUzytkownikaList;

    public DcDokumentKrok() {
    }

    public DcDokumentKrok(Integer id) {
        this.id = id;
    }

    public DcDokumentKrok(Integer id, int lp, int idDokument, int akcept, int idDcTypKroku) {
        this.id = id;
        this.lp = lp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }

    public DcDokument getIdDok() {
        return idDok;
    }

    public void setIdDok(DcDokument idDok) {
        this.idDok = idDok;
    }

    public DcAkceptStatus getAkcept() {
        return akcept;
    }

    public void setAkcept(DcAkceptStatus akcept) {
        this.akcept = akcept;
    }
    
    public DcAkceptTypKroku getDcAckeptTypKroku() {
        return dcAckeptTypKroku;
    }

    public void setDcAckeptTypKroku(DcAkceptTypKroku dcAckeptTypKroku) {
        this.dcAckeptTypKroku = dcAckeptTypKroku;
    }

    public List<DcDokumentKrokUzytkownik> getDcKrokUzytkownikaList() {
        return dcKrokUzytkownikaList;
    }

    public void setDcKrokUzytkownikaList(List<DcDokumentKrokUzytkownik> dcKrokUzytkownikaList) {
        this.dcKrokUzytkownikaList = dcKrokUzytkownikaList;
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
        if (!(object instanceof DcDokumentKrok)) {
            return false;
        }
        DcDokumentKrok other = (DcDokumentKrok) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcDokumentKrok[ id=" + id + " ]";
    }
    
}
