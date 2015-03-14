/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.encje.Uzytkownik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "UM_URZADZENIA")
public class UmUrzadzenie implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUMURZADZENIE")
    @SequenceGenerator(name = "SEQUMURZADZENIE", sequenceName = "SEQUMURZADZENIE")
    private Long id;
    @Size(min = 1, max = 256)
    @Column(name = "nazwa", nullable = false, length = 256, unique = true)
    private String nazwa;
    @Size(min = 0, max = 256)
    @Column(name = "nr_ser", nullable = true, length = 256, unique = false)
    private String nrSer;
    @Size(min = 0, max = 256)
    @Column(name = "nr_inw", nullable = true, length = 256, unique = false)
    private String nrInw;
    @Size(min = 0, max = 256)
    @Column(name = "lokalizacja", nullable = true, length = 256, unique = false)
    private String lokalizacja;
    @Size(min = 0, max = 256)
    @Column(name = "firma_serw", nullable = true, length = 256, unique = false)
    private String firmaSerw;
    @Size(max = 10485760)
    @Lob
    private String notatka;
    @Column(name = "data_wprow")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPrzegl;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private UmGrupa grupa;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Uzytkownik userOdpow;
    @ManyToMany(mappedBy = "urzadzeniaList", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<DcDokument> dokumentyList;
    @Transient
    private boolean dataNizDzis;
    @Transient
    private boolean alertPrzegl;

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

    public String getNrSer() {
        return nrSer;
    }

    public void setNrSer(String nrSer) {
        this.nrSer = nrSer;
    }

    public String getNrInw() {
        return nrInw;
    }

    public void setNrInw(String nrInw) {
        this.nrInw = nrInw;
    }

    public String getLokalizacja() {
        return lokalizacja;
    }

    public void setLokalizacja(String lokalizacja) {
        this.lokalizacja = lokalizacja;
    }

    public String getFirmaSerw() {
        return firmaSerw;
    }

    public void setFirmaSerw(String firmaSerw) {
        this.firmaSerw = firmaSerw;
    }

    public String getNotatka() {
        return notatka;
    }

    public void setNotatka(String notatka) {
        this.notatka = notatka;
    }

    public Date getDataPrzegl() {
        return dataPrzegl;
    }

    public void setDataPrzegl(Date dataPrzegl) {
        this.dataPrzegl = dataPrzegl;
    }

    public UmGrupa getGrupa() {
        return grupa;
    }

    public void setGrupa(UmGrupa grupa) {
        this.grupa = grupa;
    }

    public Uzytkownik getUserOdpow() {
        return userOdpow;
    }

    public void setUserOdpow(Uzytkownik userOdpow) {
        this.userOdpow = userOdpow;
    }

    public boolean isDataNizDzis() {
        dataNizDzis = false;
        if (dataPrzegl == null) {
            return dataNizDzis;
        }
        if (this.dataPrzegl.before(new Date())) {
            dataNizDzis = true;
        }
        return dataNizDzis;
    }

    public List<DcDokument> getDokumentyList() {
        return dokumentyList;
    }

    public void setDokumentyList(List<DcDokument> dokumentyList) {
        this.dokumentyList = dokumentyList;
    }

    public boolean isAlertPrzegl() {
        if (dataPrzegl == null || getGrupa().getCzasAlert() == 0) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dataPrzegl);
        c.add(Calendar.DATE, -(getGrupa().getCzasAlert()));
        return c.getTime().before(new Date());
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
        if (!(object instanceof UmUrzadzenie)) {
            return false;
        }
        UmUrzadzenie other = (UmUrzadzenie) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.UmUrzadzenie[ id=" + id + " ]";
    }

}
