
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_kontrahenci")
@NamedQueries({
    @NamedQuery(name = "DcKontrahenci.findAll", query = "SELECT d FROM DcKontrahenci d"),
    @NamedQuery(name = "DcKontrahenci.findById", query = "SELECT d FROM DcKontrahenci d WHERE d.id = :id"),
    @NamedQuery(name = "DcKontrahenci.findByNazwa", query = "SELECT d FROM DcKontrahenci d WHERE d.nazwa = :nazwa"),
    @NamedQuery(name = "DcKontrahenci.findByNipRegon", query = "SELECT d FROM DcKontrahenci d WHERE d.nipRegon = :nipRegon"),
    @NamedQuery(name = "DcKontrahenci.findByOsobaKontak", query = "SELECT d FROM DcKontrahenci d WHERE d.osobaKontak = :osobaKontak"),
    @NamedQuery(name = "DcKontrahenci.findByWww", query = "SELECT d FROM DcKontrahenci d WHERE d.www = :www"),
    @NamedQuery(name = "DcKontrahenci.findByEmail", query = "SELECT d FROM DcKontrahenci d WHERE d.email = :email"),
    @NamedQuery(name = "DcKontrahenci.findByAdres", query = "SELECT d FROM DcKontrahenci d WHERE d.adres = :adres"),
    @NamedQuery(name = "DcKontrahenci.findByInfoDod", query = "SELECT d FROM DcKontrahenci d WHERE d.infoDod = :infoDod")})
public class DcKontrahenci implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCKONTRAH")
    @SequenceGenerator(name = "SEQDCKONTRAH", sequenceName = "SEQDCKONTRAH")
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nazwa", nullable = false, length = 255)
    private String nazwa;
    @Size(max = 50)
    @Column(name = "nip_regon", length = 50)
    private String nipRegon;
    @Size(max = 255)
    @Column(name = "osoba_kontak", length = 255)
    private String osobaKontak;
    @Size(max = 255)
    @Column(name = "www", length = 255)
    private String www;
    @Size(max = 255)
    @Column(name = "tel", length = 255)
    private String tel;
    @Email
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;
    @Size(max = 255)
    @Column(name = "adres", length = 255)
    private String adres;
    @Size(max = 10485760)
    @Lob
    @Column(name = "info_dod")
    private String infoDod;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "kontrahentId", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<DcDokument> dokumentList;

    public DcKontrahenci() {
    }

    public DcKontrahenci(Integer id) {
        this.id = id;
    }

    public DcKontrahenci(Integer id, String nazwa) {
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

    public String getNipRegon() {
        return nipRegon;
    }

    public void setNipRegon(String nipRegon) {
        this.nipRegon = nipRegon;
    }

    public String getOsobaKontak() {
        return osobaKontak;
    }

    public void setOsobaKontak(String osobaKontak) {
        this.osobaKontak = osobaKontak;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getInfoDod() {
        return infoDod;
    }

    public void setInfoDod(String infoDod) {
        this.infoDod = infoDod;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
        if (!(object instanceof DcKontrahenci)) {
            return false;
        }
        DcKontrahenci other = (DcKontrahenci) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcKontrahenci[ id=" + id + " ]";
    }
}
