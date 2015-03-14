/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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
@Table(name = "dc_dokument")
@NamedQueries({
    @NamedQuery(name = "DcDokument.findAll", query = "SELECT d FROM DcDokument d"),
    @NamedQuery(name = "DcDokument.findById", query = "SELECT d FROM DcDokument d WHERE d.id = :id"),
    @NamedQuery(name = "DcDokument.findByNazwa", query = "SELECT d FROM DcDokument d WHERE d.nazwa = :tytul"),
    @NamedQuery(name = "DcDokument.findByOpis", query = "SELECT d FROM DcDokument d WHERE d.opis = :opis"),
    @NamedQuery(name = "DcDokument.findByDataWprow", query = "SELECT d FROM DcDokument d WHERE d.dataWprow = :dataWprow"),
    @NamedQuery(name = "DcDokument.findByDataDok", query = "SELECT d FROM DcDokument d WHERE d.dataDok = :dataDok"),
    @NamedQuery(name = "DcDokument.findByStatus", query = "SELECT d FROM DcDokument d WHERE d.dokStatusId.id = :statusId"),
    @NamedQuery(name = "DcDokument.findDlaArch", query = "SELECT d FROM DcDokument d WHERE d.rodzajId.idRodzajGrupa.archiw =1"),
    @NamedQuery(name = "DcDokument.findMaxNrKol", query = "SELECT max(d.symbolNrKol) FROM DcDokument d WHERE d.symbolSpDzialRok=:symbolSpDzialRok")
})
public class DcDokument extends AbstEncja implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCDOKUMENT")
    @SequenceGenerator(name = "SEQDCDOKUMENT", sequenceName = "SEQDCDOKUMENT")
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDok", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcPlik> dcPlikList;
    @JoinColumn(name = "dokstatusid_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcDokumentStatus dokStatusId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDok", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokumentKrok> dcDokKrok;
    @JoinColumn(name = "kontrahent_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcKontrahenci kontrahentId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dokid", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DcDokDoWiadomosci> dcDokDoWiadList;
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = true)
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<UmUrzadzenie> urzadzeniaList;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    private List<DcDokumentArch> dcArchList;
    @Transient
    private String dataWprowStr;
    @Transient
    private String dataDokStr;
    @Transient
    private String procentWykonania;
    @Transient
    private boolean alertAkceptacja;
    @Transient
    private boolean alertBrakowanie;
    @Transient
    private String symbolDok;
    @Transient
    private boolean doArchZnacznik;
    
    @Embedded
    //pamietać, aby w tej klasie coś dopisać (nawet pusty string w jakims polu), bo wali nulami
    private DcDokumentArchDod dokArchDod;
    
    public DcDokument() {
        this.dokArchDod=new DcDokumentArchDod();
    }

    public DcDokument(Integer id) {
        this.id = id;
        this.dokArchDod=new DcDokumentArchDod();
    }

    public DcDokument(Integer id, String tytul, Date dataWprow, int userId) {
        this.id = id;
        this.nazwa = tytul;
        this.dataWprow = dataWprow;
        this.dokArchDod=new DcDokumentArchDod();
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
        if(dataDok==null){
            dataDok=new Date();
        }
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

    public List<DcPlik> getDcPlikList() {
        return dcPlikList;
    }

    public void setDcPlikList(List<DcPlik> dcPlikList) {
        this.dcPlikList = dcPlikList;
    }

    public DcDokumentStatus getDokStatusId() {
        return dokStatusId;
    }

    public void setDokStatusId(DcDokumentStatus dokStatusId) {
        this.dokStatusId = dokStatusId;
    }

    public List<DcDokumentKrok> getDcDokKrok() {
        return dcDokKrok;
    }

    public void setDcDokKrok(List<DcDokumentKrok> dcDokKrok) {
        this.dcDokKrok = dcDokKrok;
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

    public List<DcDokDoWiadomosci> getDcDokDoWiadList() {
        return dcDokDoWiadList;
    }

    public void setDcDokDoWiadList(List<DcDokDoWiadomosci> dcDokDoWiadList) {
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

    public String getProcentWykonania() {
        if(getDokStatusId().getId()==3){
            return "100";
        }
        int krokiAll=getDcDokKrok().size();
        int krokiZaakceptowane=0;
        if(krokiAll==0) return "brak";
        for(DcDokumentKrok krok: getDcDokKrok()){
            if(krok.getAkcept().getId()==4){
                krokiZaakceptowane++;
            }
        }
        return (krokiZaakceptowane*100)/krokiAll+"";
    }

    public boolean isAlertAkceptacja() {
        if(this.getDokStatusId().getId()!=2) return false;
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -getRodzajId().getLimitCzaasuAkceptacji());
        return getDataWprow().before(c.getTime());
    }

    public void setAlertAkceptacja(boolean alertAkceptacja) {
        this.alertAkceptacja = alertAkceptacja;
    }

    public String getSymbolDok() {
        try{
        symbolDok=this.symbolNrKol+"/"+this.symbolSpDzialRok+"/"+this.getRodzajId().getSymbol();
        }catch (NullPointerException ex){
            return null;
        }
        return symbolDok;
    }

    public void setSymbolDok(String symbolDok) {
        this.symbolDok = symbolDok;
    }

    public int getSymbolNrKol() {
        return symbolNrKol;
    }

    public void setSymbolNrKol(int symbolNrKol) {
        this.symbolNrKol = symbolNrKol;
    }

    public List<UmUrzadzenie> getUrzadzeniaList() {
        return urzadzeniaList;
    }

    public void setUrzadzeniaList(List<UmUrzadzenie> urzadzeniaList) {
        this.urzadzeniaList = urzadzeniaList;
    }

    public boolean isDoArchZnacznik() {
        return doArchZnacznik;
    }

    public void setDoArchZnacznik(boolean doArchZnacznik) {
        this.doArchZnacznik = doArchZnacznik;
    }

    public List<DcDokumentArch> getDcArchList() {
        return dcArchList;
    }

    public void setDcArchList(List<DcDokumentArch> dcArchList) {
        this.dcArchList = dcArchList;
    }

    public boolean isAlertBrakowanie() {
        alertBrakowanie = false;
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(dataDok);
        cal1.add(Calendar.MONTH, this.getRodzajId().getSymbMer1Id().getCzas());
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(dataDok);
        cal2.add(Calendar.MONTH, this.getRodzajId().getSymbMer2Id().getCzas());
        if(cal1.before(Calendar.getInstance())||cal2.before(Calendar.getInstance())){
            return true;
        }
        return alertBrakowanie;
    }

    public DcDokumentArchDod getDokArchDod() {
        return dokArchDod;
    }

    public void setDokArchDod(DcDokumentArchDod dokArchDod) {
        this.dokArchDod = dokArchDod;
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
        if (!(object instanceof DcDokument)) {
            return false;
        }
        DcDokument other = (DcDokument) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcDokument[ id=" + id + " ]";
    }
}
