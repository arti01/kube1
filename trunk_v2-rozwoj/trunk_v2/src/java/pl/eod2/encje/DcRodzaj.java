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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity(name = "DcRodzaj")
@Table(name = "dc_rodzaj")
@NamedQueries({
    @NamedQuery(name = "opisBind.findAll", query = "SELECT d FROM DcRodzaj d"),
    @NamedQuery(name = "DcRodzaj.findAllzKrok", query = "SELECT d FROM DcRodzaj d WHERE SIZE(d.dcAkceptKroki)>0"),
    @NamedQuery(name = "DcRodzaj.findById", query = "SELECT d FROM DcRodzaj d WHERE d.id = :id"),
    @NamedQuery(name = "DcRodzaj.findByNazwa", query = "SELECT d FROM DcRodzaj d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcRodzaj.findByUm", query = "SELECT d FROM DcRodzaj d WHERE d.idRodzajGrupa.urzMed=1 and SIZE(d.dcAkceptKroki)>0"),
    @NamedQuery(name = "DcRodzaj.findByArch", query = "SELECT d FROM DcRodzaj d WHERE d.idRodzajGrupa.archiw=1 and SIZE(d.dcAkceptKroki)>0"),
    @NamedQuery(name = "DcRodzaj.findByOpis", query = "SELECT d FROM DcRodzaj d WHERE d.opis = :opis")})
public class DcRodzaj implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCRODZAJ")
    @SequenceGenerator(name = "SEQDCRODZAJ", sequenceName = "SEQDCRODZAJ")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(nullable = false, length = 256)
    private String nazwa;
    @Size(max = 5)
    @Column(nullable = false, length = 5)
    private String symbol;
    @Size(max = 10)
    @Column(nullable = true, length = 10)
    private String symbolRwa;
    @Size(max = 10485760)
    @Lob
    private String opis;
    @Size(max = 10485760)
    @Lob
    private String szablon;
    @Column(name = "limit_czasu_akceptacji")
    private int limitCzaasuAkceptacji;
    @JoinColumn(name = "id_rodzaj_grupa", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcRodzajGrupa idRodzajGrupa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rodzajId", fetch = FetchType.LAZY)
    private List<DcDokument> dcDokumentList;
    @JoinColumn(name = "id_typ_flow", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcTypFlow idTypFlow;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rodzajId", fetch = FetchType.LAZY)
    private List<DcAkceptKroki> dcAkceptKroki;
    @JoinColumn(name = "id", referencedColumnName = "id")
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH},fetch = FetchType.LAZY)
    private List<UmMasterGrupa> umMasterGrupaList;
    @JoinColumn(name = "id_dc_status_pocz", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private DcDokumentStatus dcDokStatusPocz;
    @JoinColumn(name = "id_dc_status_konc", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private DcDokumentStatus dcDokStatusKonc;
    @JoinColumn(name = "id_symb_mer1", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcSymbMer1 symbMer1Id;
    @JoinColumn(name = "id_symb_mer2", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private DcSymbMer2 symbMer2Id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rodzajId", fetch = FetchType.LAZY)
    private List<DcDokumentArch> dcDokumentArchList;

    public DcRodzaj() {
    }

    public DcRodzaj(Integer id) {
        this.id = id;
    }

    public DcRodzaj(Integer id, String nazwa) {
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOpis() {
        return opis;
    }

    public int getLimitCzaasuAkceptacji() {
        return limitCzaasuAkceptacji;
    }

    public void setLimitCzaasuAkceptacji(int limitCzaasuAkceptacji) {
        this.limitCzaasuAkceptacji = limitCzaasuAkceptacji;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public DcRodzajGrupa getIdRodzajGrupa() {
        return idRodzajGrupa;
    }

    public void setIdRodzajGrupa(DcRodzajGrupa idRodzajGrupa) {
        this.idRodzajGrupa = idRodzajGrupa;
    }

    public List<DcDokument> getDcDokumentList() {
        return dcDokumentList;
    }

    public void setDcDokumentList(List<DcDokument> dcDokumentList) {
        this.dcDokumentList = dcDokumentList;
    }

    public DcTypFlow getIdTypFlow() {
        return idTypFlow;
    }

    public void setIdTypFlow(DcTypFlow idTypFlow) {
        this.idTypFlow = idTypFlow;
    }

    public List<DcAkceptKroki> getDcAkceptKroki() {
        return dcAkceptKroki;
    }

    public void setDcAkceptKroki(List<DcAkceptKroki> dcAkceptKroki) {
        this.dcAkceptKroki = dcAkceptKroki;
    }
    
    public String getSzablon() {
        return szablon;
    }

    public void setSzablon(String szablon) {
        if(szablon==null||szablon.isEmpty()){
            szablon=" ";
        }
        this.szablon = szablon;
    }

    public List<UmMasterGrupa> getUmMasterGrupaList() {
        return umMasterGrupaList;
    }

    public void setUmMasterGrupaList(List<UmMasterGrupa> umMasterGrupaList) {
        this.umMasterGrupaList = umMasterGrupaList;
    }

    public String getSymbolRwa() {
        return symbolRwa;
    }

    public void setSymbolRwa(String symbolRwa) {
        this.symbolRwa = symbolRwa;
    }

    public DcSymbMer1 getSymbMer1Id() {
        return symbMer1Id;
    }

    public void setSymbMer1Id(DcSymbMer1 symbMer1Id) {
        this.symbMer1Id = symbMer1Id;
    }

    public DcSymbMer2 getSymbMer2Id() {
        return symbMer2Id;
    }

    public void setSymbMer2Id(DcSymbMer2 symbMer2Id) {
        this.symbMer2Id = symbMer2Id;
    }

    public DcDokumentStatus getDcDokStatusPocz() {
        return dcDokStatusPocz;
    }

    public void setDcDokStatusPocz(DcDokumentStatus dcDokStatusPocz) {
        this.dcDokStatusPocz = dcDokStatusPocz;
    }

    public DcDokumentStatus getDcDokStatusKonc() {
        return dcDokStatusKonc;
    }

    public void setDcDokStatusKonc(DcDokumentStatus dcDokStatusKonc) {
        this.dcDokStatusKonc = dcDokStatusKonc;
    }

    public List<DcDokumentArch> getDcDokumentArchList() {
        return dcDokumentArchList;
    }

    public void setDcDokumentArchList(List<DcDokumentArch> dcDokumentArchList) {
        this.dcDokumentArchList = dcDokumentArchList;
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
        if (!(object instanceof DcRodzaj)) {
            return false;
        }
        DcRodzaj other = (DcRodzaj) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcRodzaj[ id=" + id + " ]";
    }
    
}
