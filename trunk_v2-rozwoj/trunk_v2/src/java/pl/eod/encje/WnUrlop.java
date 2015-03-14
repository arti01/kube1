package pl.eod.encje;

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
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "wn_urlop")
@NamedQueries({
    @NamedQuery(name = "WnUrlop.findAll", query = "SELECT w FROM WnUrlop w"),
    @NamedQuery(name = "WnUrlop.findById", query = "SELECT w FROM WnUrlop w WHERE w.id = :id"),
    @NamedQuery(name = "WnUrlop.findByDataOd", query = "SELECT w FROM WnUrlop w WHERE w.dataOd = :dataOd"),
    @NamedQuery(name = "WnUrlop.findByDataDo", query = "SELECT w FROM WnUrlop w WHERE w.dataDo = :dataDo"),
    @NamedQuery(name = "WnUrlop.findByNrWniosku", query = "SELECT w FROM WnUrlop w WHERE w.nrWniosku = :nrWniosku"),
    @NamedQuery(name = "WnUrlop.findDoEskalacji", query = "SELECT w FROM WnUrlop w WHERE w.statusId.id=:statusId"),
    @NamedQuery(name = "WnUrlop.findFiltr", query = "SELECT w FROM WnUrlop w WHERE w.statusId.id=:statusId and w.uzytkownik.fullname like :fullname"),
    @NamedQuery(name = "WnUrlop.findByDataWprowadzenia", query = "SELECT w FROM WnUrlop w WHERE w.dataWprowadzenia = :dataWprowadzenia")})
public class WnUrlop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQWNURLOP")
    @SequenceGenerator(name = "SEQWNURLOP", sequenceName = "SEQWNURLOP")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_od")
    @Temporal(TemporalType.DATE)
    private Date dataOd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_do")
    @Temporal(TemporalType.DATE)
    private Date dataDo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nr_wniosku")
    private String nrWniosku;
    @Basic(optional = false)
    @NotNull
    @Column(name = "data_wprowadzenia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataWprowadzenia;
    @Column(name = "extraemail", nullable = true)
    private Integer extraemail;
    @Column(name = "info_dod", nullable = true)
    private String infoDod;
    
    @OrderBy(value = "id ASC")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "urlopId", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WnHistoria> wnHistoriaList;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private WnStatusy statusId;
    @JoinColumn(name = "rodzaj_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private WnRodzaje rodzajId;
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uzytkownik uzytkownik;
    @JoinColumn(name = "akceptant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uzytkownik akceptant;
    @JoinColumn(name = "przyjmujacy", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Uzytkownik przyjmujacy;
    @Size(max = 255)
    @Column(name = "miejsce")
    private String miejsce;
    @Size(max = 255)
    @Column(name = "cel")
    private String cel;
    @Size(max = 255)
    @Column(name = "srodek_lok")
    private String srodekLok;
    @Transient
    private Date dataOstZmiany;

    public WnUrlop() {
    }

    public WnUrlop(Long id) {
        this.id = id;
    }

    public WnUrlop(Long id, Date dataOd, Date dataDo, String nrWniosku, Date dataWprowadzenia) {
        this.id = id;
        this.dataOd = dataOd;
        this.dataDo = dataDo;
        this.nrWniosku = nrWniosku;
        this.dataWprowadzenia = dataWprowadzenia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataOd() {
        return dataOd;
    }

    public void setDataOd(Date dataOd) {
        this.dataOd = dataOd;
    }

    public Date getDataDo() {
        return dataDo;
    }

    public void setDataDo(Date dataDo) {
        this.dataDo = dataDo;
    }

    public String getNrWniosku() {
        return nrWniosku;
    }

    public void setNrWniosku(String nrWniosku) {
        this.nrWniosku = nrWniosku;
    }

    public Date getDataWprowadzenia() {
        return dataWprowadzenia;
    }

    public void setDataWprowadzenia(Date dataWprowadzenia) {
        this.dataWprowadzenia = dataWprowadzenia;
    }

    public List<WnHistoria> getWnHistoriaList() {
        return wnHistoriaList;
    }

    public void setWnHistoriaList(List<WnHistoria> wnHistoriaList) {
        this.wnHistoriaList = wnHistoriaList;
    }

    public WnStatusy getStatusId() {
        return statusId;
    }

    public void setStatusId(WnStatusy statusId) {
        this.statusId = statusId;
    }

    public WnRodzaje getRodzajId() {
        return rodzajId;
    }

    public void setRodzajId(WnRodzaje rodzajId) {
        this.rodzajId = rodzajId;
    }

    public Uzytkownik getUzytkownik() {
        return uzytkownik;
    }

    public void setUzytkownik(Uzytkownik uzytkownik) {
        this.uzytkownik = uzytkownik;
    }

    public Uzytkownik getAkceptant() {
        return akceptant;
    }

    public void setAkceptant(Uzytkownik akceptant) {
        this.akceptant = akceptant;
    }

    public Date getDataOstZmiany() {
        dataOstZmiany = getWnHistoriaList().get(getWnHistoriaList().size() - 1).getDataZmiany();
        return dataOstZmiany;
    }

    public void setDataOstZmiany(Date dataOstZmiany) {
        this.dataOstZmiany = dataOstZmiany;
    }

    public Uzytkownik getPrzyjmujacy() {
        return przyjmujacy;
    }

    public void setPrzyjmujacy(Uzytkownik przyjmujacy) {
        this.przyjmujacy = przyjmujacy;
    }

    public boolean isExtraemail() {
        System.out.println();
        if (extraemail == null) {
            return false;
        }
        if (extraemail == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setExtraemail(boolean extraemail) {
        if(extraemail) this.extraemail =1;
        else this.extraemail =null;
    }

    public String getInfoDod() {
        return infoDod;
    }

    public void setInfoDod(String infoDod) {
        this.infoDod = infoDod;
    }

    public String getMiejsce() {
        return miejsce;
    }

    public void setMiejsce(String miejsce) {
        this.miejsce = miejsce;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getSrodekLok() {
        return srodekLok;
    }

    public void setSrodekLok(String srodekLok) {
        this.srodekLok = srodekLok;
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
        if (!(object instanceof WnUrlop)) {
            return false;
        }
        WnUrlop other = (WnUrlop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.WnUrlop[ id=" + id + " ]";
    }
}
