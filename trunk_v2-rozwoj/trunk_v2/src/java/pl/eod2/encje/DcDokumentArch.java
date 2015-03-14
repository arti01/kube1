/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;
import pl.eod.encje.Uzytkownik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_dokument_arch")
@NamedQueries({
    @NamedQuery(name = "DcDokumentArch.findAll", query = "SELECT d FROM DcDokumentArch d"),
    @NamedQuery(name = "DcDokumentArch.findById", query = "SELECT d FROM DcDokumentArch d WHERE d.id = :id"),
    @NamedQuery(name = "DcDokumentArch.findByNazwa", query = "SELECT d FROM DcDokumentArch d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcDokumentArch.findByStatus", query = "SELECT d FROM DcDokumentArch d WHERE d.dokStatusId.id = :statusId"),
    @NamedQuery(name = "DcDokumentArch.findMaxNrKol", query = "SELECT max(d.symbolNrKol) FROM DcDokumentArch d WHERE d.symbolSpDzialRok=:symbolSpDzialRok")
})
public class DcDokumentArch extends AbstEncja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(nullable = false, length = 256)
    private String nazwa;
    @Size(max = 10485760)
    @Lob
    private String opis;
    @Size(max = 10485760)
    @Lob
    @Column(name = "opis_dlugi")
    private String opisDlugi;
    @Size(max = 50)
    @Column(name = "symbol_sp_dzial_rok")
    private String symbolSpDzialRok;
    @Column(name = "symbol_nr_kol")
    private int symbolNrKol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_wprow", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataWprow;
    @Column(name = "data_dok")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDok;
    @Embedded
    private DcDokumentArchDane dokArchDane;
    

    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Uzytkownik userId;
    @JoinColumn(name = "zrodlo_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcZrodlo zrodloId;
    @JoinColumn(name = "rodzaj_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private DcRodzaj rodzajId;
    @JoinColumn(name = "teczka_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcTeczka teczkaId;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "idDok")
    private List<DcPlikArch> dcPlikList;
    
    @JoinColumn(name = "dokstatusid_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokumentStatus dokStatusId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "idDok")
    private List<DcDokumentKrokArch> dcDokKrok;

    @JoinColumn(name = "kontrahent_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcKontrahenci kontrahentId;

    @JoinColumn(name = "id", referencedColumnName = "id", nullable = true)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<UmUrzadzenie> urzadzeniaList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dokid", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokDoWiadomosciArch> dcDokDoWiadList;
    
    @ManyToMany(mappedBy = "dcArchList", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<DcDokument> dokumentyList;
    
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = true)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    private List<DcDokumentArch> dcArchList;
    
    @ManyToMany(mappedBy = "dcArchList")
    private List<DcDokumentArch> dcArchListOld;
    
    @Transient
    private String dataWprowStr;
    @Transient
    private String dataDokStr;
    @Transient
    private String symbolDok;
    @Transient
    private boolean wyborZnacznik;
    @Transient
    private boolean alertBrakowanie;
    
    @Transient//specjalny dokument do wyswietlania w raportach
    private DcDokument dokWyszuk;

    public DcDokumentArch() {
    }

    public DcDokumentArch(DcDokument dc) {
        this.id = dc.getId();
        this.dataDok = dc.getDataDok();
        this.dataWprow = dc.getDataWprow();
        DcDokumentArchDane dad=new DcDokumentArchDane();
        dad.setArchData(new Date());
        this.dokArchDane=dad;
        
        this.dcDokKrok=new ArrayList<>();
        for (DcDokumentKrok kr : dc.getDcDokKrok()) {
            DcDokumentKrokArch ka=new DcDokumentKrokArch(kr);
            ka.setIdDok(this);
            this.dcDokKrok.add(ka);
        }
        
        this.dcPlikList=new ArrayList<>();
        for (DcPlik plik : dc.getDcPlikList()) {
            DcPlikArch pa =new DcPlikArch(plik);
            pa.setIdDok(this);
            this.dcPlikList.add(pa);
        }
        
        this.dcDokDoWiadList=new ArrayList<>();
        for (DcDokDoWiadomosci doWiad : dc.getDcDokDoWiadList()) {
            DcDokDoWiadomosciArch dwa =new DcDokDoWiadomosciArch(doWiad);
            dwa.setDokid(this);
            this.dcDokDoWiadList.add(dwa);
        }
        
        this.dcArchList=dc.getDcArchList();
        
        this.dokStatusId = dc.getDokStatusId();
        this.kontrahentId = dc.getKontrahentId();
        this.nazwa = dc.getNazwa();
        this.opis = dc.getOpis();
        this.opisDlugi = dc.getOpisDlugi();
        this.rodzajId = dc.getRodzajId();
        this.symbolNrKol = dc.getSymbolNrKol();
        this.symbolSpDzialRok = dc.getSymbolSpDzialRok();
        this.teczkaId = dc.getTeczkaId();
        this.urzadzeniaList = dc.getUrzadzeniaList();
        this.userId = dc.getUserId();
        this.zrodloId = dc.getZrodloId();
    }

    public DcDokumentArch(Integer id) {
        this.id = id;
    }

    public DcDokumentArch(Integer id, String tytul, Date dataWprow, int userId) {
        this.id = id;
        this.nazwa = tytul;
        this.dataWprow = dataWprow;
    }

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

    public Date getDataWprow() {
        return dataWprow;
    }

    public void setDataWprow(Date dataWprow) {
        this.dataWprow = dataWprow;
    }

    public Date getDataDok() {
        return dataDok;
    }

    public void setDataDok(Date dataDok) {
        this.dataDok = dataDok;
    }

    public Uzytkownik getUserId() {
        return userId;
    }

    public void setUserId(Uzytkownik userId) {
        this.userId = userId;
    }

    public DcZrodlo getZrodloId() {
        return zrodloId;
    }

    public void setZrodloId(DcZrodlo zrodloId) {
        this.zrodloId = zrodloId;
    }

    public DcRodzaj getRodzajId() {
        return rodzajId;
    }

    public void setRodzajId(DcRodzaj rodzajId) {
        this.rodzajId = rodzajId;
    }

    public DcTeczka getTeczkaId() {
        return teczkaId;
    }

    public void setTeczkaId(DcTeczka teczkaId) {
        this.teczkaId = teczkaId;
    }

    public List<DcPlikArch> getDcPlikList() {
        return dcPlikList;
    }

    public void setDcPlikList(List<DcPlikArch> dcPlikList) {
        this.dcPlikList = dcPlikList;
    }

    public List<DcDokumentKrokArch> getDcDokKrok() {
        return dcDokKrok;
    }

    public void setDcDokKrok(List<DcDokumentKrokArch> dcDokKrok) {
        this.dcDokKrok = dcDokKrok;
    }

    public DcDokumentStatus getDokStatusId() {
        return dokStatusId;
    }

    public void setDokStatusId(DcDokumentStatus dokStatusId) {
        this.dokStatusId = dokStatusId;
    }

    public String getOpisDlugi() {
        return opisDlugi;
    }

    public String getSymbolSpDzialRok() {
        return symbolSpDzialRok;
    }

    public void setSymbolSpDzialRok(String symbolSpDzialRok) {
        this.symbolSpDzialRok = symbolSpDzialRok;
    }

    public void setOpisDlugi(String opisDlugi) {
        this.opisDlugi = opisDlugi;
    }

    public DcKontrahenci getKontrahentId() {
        return kontrahentId;
    }

    public void setKontrahentId(DcKontrahenci kontrahentId) {
        this.kontrahentId = kontrahentId;
    }

    public List<DcDokDoWiadomosciArch> getDcDokDoWiadList() {
        return dcDokDoWiadList;
    }

    public void setDcDokDoWiadList(List<DcDokDoWiadomosciArch> dcDokDoWiadList) {
        this.dcDokDoWiadList = dcDokDoWiadList;
    }
    
    public String getDataWprowStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(getDataWprow());
    }

    public String getDataDokStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (getDataDok() == null) {
            return "";
        } else {
            return sdf.format(getDataDok());
        }
    }

    public boolean isAlertAkceptacja() {
        if (this.getDokStatusId().getId() != 2) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -getRodzajId().getLimitCzaasuAkceptacji());
        return getDataWprow().before(c.getTime());
    }

    public String getSymbolDok() {
        try {
            symbolDok = this.symbolNrKol + "/" + this.symbolSpDzialRok + "/" + this.getRodzajId().getSymbol();
        } catch (NullPointerException ex) {
            return null;
        }
        return symbolDok;
    }

    public int getSymbolNrKol() {
        return symbolNrKol;
    }

    public void setSymbolNrKol(int symbolNrKol) {
        this.symbolNrKol = symbolNrKol;
    }

    public DcDokumentArchDane getDokArchDane() {
        return dokArchDane;
    }

    public void setDokArchDane(DcDokumentArchDane dokArchDane) {
        this.dokArchDane = dokArchDane;
    }

    public List<UmUrzadzenie> getUrzadzeniaList() {
        return urzadzeniaList;
    }

    public void setUrzadzeniaList(List<UmUrzadzenie> urzadzeniaList) {
        this.urzadzeniaList = urzadzeniaList;
    }

    public boolean isWyborZnacznik() {
        return wyborZnacznik;
    }

    public void setWyborZnacznik(boolean wyborZnacznik) {
        this.wyborZnacznik = wyborZnacznik;
    }

    public List<DcDokument> getDokumentyList() {
        return dokumentyList;
    }

    public void setDokumentyList(List<DcDokument> dokumentyList) {
        this.dokumentyList = dokumentyList;
    }

    public boolean isAlertBrakowanie() {
        alertBrakowanie = false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dataDok);
        cal1.add(Calendar.MONTH, getRodzajId().getSymbMer1Id().getCzas());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dataDok);
        cal2.add(Calendar.MONTH, getRodzajId().getSymbMer2Id().getCzas());
        if(cal1.before(Calendar.getInstance())||cal2.before(Calendar.getInstance())){
            return true;
        }
        return alertBrakowanie;
    }

    public List<DcDokumentArch> getDcArchListOld() {
        return dcArchListOld;
    }

    public void setDcArchListOld(List<DcDokumentArch> dcArchListOld) {
        this.dcArchListOld = dcArchListOld;
    }

    public List<DcDokumentArch> getDcArchList() {
        return dcArchList;
    }

    public void setDcArchList(List<DcDokumentArch> dcArchList) {
        this.dcArchList = dcArchList;
    }

    public DcDokument getDokWyszuk() {
        return dokWyszuk;
    }

    public void setDokWyszuk(DcDokument dokWyszuk) {
        this.dokWyszuk = dokWyszuk;
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
        if (!(object instanceof DcDokumentArch)) {
            return false;
        }
        DcDokumentArch other = (DcDokumentArch) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcDokument[ id=" + id + " ]";
    }
}
