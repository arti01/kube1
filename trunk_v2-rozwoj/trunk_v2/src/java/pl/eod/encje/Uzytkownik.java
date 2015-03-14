/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.IdValidation;
import org.eclipse.persistence.annotations.PrimaryKey;
import pl.eod2.encje.DcAkceptKroki;
import pl.eod2.encje.DcDokDoWiadCel;
import pl.eod2.encje.DcDokDoWiadomosci;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentArchDod;
import pl.eod2.encje.DcDokumentKrokUzytkownik;
import pl.eod2.encje.Ogloszenia;
import pl.eod2.encje.UmUrzadzenie;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "uzytkownik")
@NamedQueries({
    @NamedQuery(name = "Uzytkownik.findAll", query = "SELECT u FROM Uzytkownik u"),
    @NamedQuery(name = "Uzytkownik.findById", query = "SELECT u FROM Uzytkownik u WHERE u.id = :id"),
    @NamedQuery(name = "Uzytkownik.findByFullname", query = "SELECT u FROM Uzytkownik u WHERE u.fullname = :fullname"),
    @NamedQuery(name = "Uzytkownik.findByAdrEmail", query = "SELECT u FROM Uzytkownik u WHERE u.adrEmail = :adrEmail"),
    @NamedQuery(name = "Uzytkownik.findByAdrEmailAndFullname", query = "SELECT u FROM Uzytkownik u WHERE u.adrEmail = :adrEmail AND u.fullname = :fullname and (u.struktura.usuniety<>1 or u.struktura.usuniety is null)"),
    @NamedQuery(name = "Uzytkownik.zPrawami", query = "SELECT u FROM Uzytkownik u JOIN u.role r WHERE r.rolename!='eoduser' group by u"),
    @NamedQuery(name = "Uzytkownik.findByExtId", query = "SELECT u FROM Uzytkownik u WHERE u.extId = :extId and (u.struktura.usuniety<>1 or u.struktura.usuniety is null)")})
@PrimaryKey(validation = IdValidation.NULL)
public class Uzytkownik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUSER")
    @SequenceGenerator(name = "SEQUSER", sequenceName = "SEQUSER")
    private Long id;
    @Column(name = "fullname", nullable = false)
    @Size(min = 3, max = 255)
    @NotEmpty
    private String fullname;
    //@NotEmpty
    @Email
    //@UzytkowAdniot(value = true)
    @Column(name = "adr_email")
    private String adrEmail;
    @Column(name = "ext_id", nullable = false)
    private String extId;
    @Transient
    //@OneToOne(mappedBy = "username")
    WnLimity wnLimity;
    @OneToMany(mappedBy = "secUserId", cascade = CascadeType.REFRESH)
    List<Struktura> strukturaSec;
    @OneToOne(mappedBy = "userId", cascade = {CascadeType.ALL})
    Struktura struktura;
    @JoinColumn(name = "haslo_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.ALL)
    private Hasla hasla;
    @JoinColumn(name = "adr_email", referencedColumnName = "username")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<UserRoles> role;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "uzytkownik", orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<WnUrlop> wnUrlopList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "akceptant", fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<WnUrlop> wnUrlopListDoAkceptu;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zmieniajacy", fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<WnHistoria> wnHistoriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "akceptant", fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<WnHistoria> wnHistoriaListAkceptant;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "przyjmujacy", fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<WnUrlop> wnUrlopListPrzyjmujacy;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "userId", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<DcDokument> dcDokumentList;
    @ManyToMany(mappedBy = "uzytkownikList", fetch = FetchType.LAZY)
    private List<DcAkceptKroki> dcAkceptKrokiList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "idUser", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<DcDokumentKrokUzytkownik> dcDokumentKrokUzytkownikList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "wprowadzil", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<DcDokDoWiadomosci> dcDoWiadList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "userid", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<DcDokDoWiadCel> dcDoWiadCelList;
    @Transient
    private List<DcDokDoWiadCel> dcDoWiadCelListFiltr;
    @Transient
    private List<DcDokumentKrokUzytkownik> dcDoAkceptuKrokiList;
    @Transient
    private List<DcDokumentKrokUzytkownik> dcDokumentListHist;
    @JoinColumn(name = "spolka_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Spolki spolkaId;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "wprowadzil", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<Ogloszenia> ogloszeniaList;
    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "userOdpow", orphanRemoval = false, fetch = FetchType.LAZY)
    @OrderBy(value = "id DESC")
    private List<UmUrzadzenie> urzadzenieList;
    
    //@OneToMany(cascade = CascadeType.MERGE, mappedBy = "wydal", orphanRemoval = false, fetch = FetchType.LAZY)
    //@OrderBy(value = "id DESC")
    //private List<DcDokumentArchDod> dokArchDodList;
    
    @Transient
    private List<UmUrzadzenie> urzadzenieAletrPrzeglList;
    @Transient
    boolean eodstru;

    public Uzytkownik() {
        this.extId = "";
    }

    public Uzytkownik(Long id) {
        this.extId = "";
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAdrEmail() {
        return adrEmail;
    }

    public void setAdrEmail(String adrEmail) {
        this.adrEmail = adrEmail;
    }

    public List<Struktura> getStrukturaSec() {
        return strukturaSec;
    }

    public void setStrukturaSec(List<Struktura> strukturaSec) {
        this.strukturaSec = strukturaSec;
    }

    public Struktura getStruktura() {
        return struktura;
    }

    public void setStruktura(Struktura struktura) {
        this.struktura = struktura;
    }

    public Hasla getHasla() {
        return hasla;
    }

    public void setHasla(Hasla hasla) {
        this.hasla = hasla;
    }

    public List<UserRoles> getRole() {
        return role;
    }

    public void setRole(List<UserRoles> role) {
        this.role = role;
    }

    public List<WnUrlop> getWnUrlopList() {
        return wnUrlopList;
    }

    public void setWnUrlopList(List<WnUrlop> wnUrlopList) {
        this.wnUrlopList = wnUrlopList;
    }

    public List<WnHistoria> getWnHistoriaList() {
        return wnHistoriaList;
    }

    public void setWnHistoriaList(List<WnHistoria> wnHistoriaList) {
        this.wnHistoriaList = wnHistoriaList;
    }

    public List<WnUrlop> getWnUrlopListPrzyjmujacy() {
        return wnUrlopListPrzyjmujacy;
    }

    public void setWnUrlopListPrzyjmujacy(List<WnUrlop> wnUrlopListPrzyjmujacy) {
        this.wnUrlopListPrzyjmujacy = wnUrlopListPrzyjmujacy;
    }

    public Spolki getSpolkaId() {
        return spolkaId;
    }

    public void setSpolkaId(Spolki spolkaId) {
        this.spolkaId = spolkaId;
    }

    public List<DcDokDoWiadomosci> getDcDoWiadList() {
        return dcDoWiadList;
    }

    public void setDcDoWiadList(List<DcDokDoWiadomosci> dcDoWiadList) {
        this.dcDoWiadList = dcDoWiadList;
    }

    public List<DcDokDoWiadCel> getDcDoWiadCelList() {
        return dcDoWiadCelList;
    }

    public void setDcDoWiadCelList(List<DcDokDoWiadCel> dcDoWiadCelList) {
        this.dcDoWiadCelList = dcDoWiadCelList;
    }

    public boolean isEodstru() {
        eodstru = false;
        for (UserRoles rola : this.getRole()) {
            if (rola.getRolename().equals("eodstru")) {
                eodstru = true;
            }
        }
        return eodstru;
    }

    public void setEodstru(boolean eodstru) {
        this.eodstru = eodstru;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<WnUrlop> getWnUrlopListDoAkceptu() {
        return wnUrlopListDoAkceptu;
    }

    public void setWnUrlopListDoAkceptu(List<WnUrlop> wnUrlopListDoAkceptu) {
        this.wnUrlopListDoAkceptu = wnUrlopListDoAkceptu;
    }

    public List<WnHistoria> getWnHistoriaListAkceptant() {
        return wnHistoriaListAkceptant;
    }

    public void setWnHistoriaListAkceptant(List<WnHistoria> wnHistoriaListAkceptant) {
        this.wnHistoriaListAkceptant = wnHistoriaListAkceptant;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = (extId == null) ? "" : extId;
    }

    public WnLimity getWnLimity() {
        return wnLimity;
    }

    public void setWnLimity(WnLimity wnLimity) {
        this.wnLimity = wnLimity;
    }

    public List<DcDokument> getDcDokumentList() {
        return dcDokumentList;
    }

    public void setDcDokumentList(List<DcDokument> dcDokumentList) {
        this.dcDokumentList = dcDokumentList;
    }

    public List<DcAkceptKroki> getDcAkceptKrokiList() {
        return dcAkceptKrokiList;
    }

    public void setDcAkceptKrokiList(List<DcAkceptKroki> dcAkceptKrokiList) {
        this.dcAkceptKrokiList = dcAkceptKrokiList;
    }

    public List<DcDokumentKrokUzytkownik> getDcDokumentKrokUzytkownikList() {
        return dcDokumentKrokUzytkownikList;
    }

    public void setDcDokumentKrokUzytkownikList(List<DcDokumentKrokUzytkownik> dcDokumentKrokUzytkownikList) {
        this.dcDokumentKrokUzytkownikList = dcDokumentKrokUzytkownikList;
    }

    public List<DcDokumentKrokUzytkownik> getDcDoAkceptuKrokiList() {
        List<DcDokumentKrokUzytkownik> wynik = new ArrayList<DcDokumentKrokUzytkownik>();
        for (DcDokumentKrokUzytkownik dku : getDcDokumentKrokUzytkownikList()) {
            //krok danego użytkownika musi być bez akceptu
            //krok dokumentu musi być do akceptu lub częsciowa akceptacja
            if (dku.getAkcept().getId() == 2 && (dku.getIdDokumentKrok().getAkcept().getId() == 2 || dku.getIdDokumentKrok().getAkcept().getId() == 3)) {
                if (dku.getIdDokumentKrok().getIdDok().getDokStatusId().getId() != 4) {
                    wynik.add(dku);
                }

            }
        }
        return wynik;
    }

    public List<DcDokument> getDcDokumentListHist() {
        List<DcDokument> wynik = new ArrayList<DcDokument>();
        for (DcDokumentKrokUzytkownik dku : getDcDokumentKrokUzytkownikList()) {
            //krok danego użytkownika musi być bez akceptu
            //krok dokumentu musi być do akceptu lub częsciowa akceptacja
            if (dku.getAkcept().getId() == 4||dku.getAkcept().getId() == 5) {
                if(!wynik.contains(dku.getIdDokumentKrok().getIdDok())){
                    wynik.add(dku.getIdDokumentKrok().getIdDok());
                }
            }
        }
        return wynik;
    }

    public List<DcDokDoWiadCel> getDcDoWiadCelListFiltr() {
        List<DcDokDoWiadCel> wynik = new ArrayList<DcDokDoWiadCel>();
        for (DcDokDoWiadCel dC : this.getDcDoWiadCelList()) {
            if (dC.getIdDokDoWiad().getDokid().getDokStatusId().getId() == 1 || dC.getIdDokDoWiad().getDokid().getDokStatusId().getId() == 4) {
                continue;
            }
            wynik.add(dC);
        }
        return wynik;
    }

    public List<Ogloszenia> getOgloszeniaList() {
        return ogloszeniaList;
    }

    public void setOgloszeniaList(List<Ogloszenia> ogloszeniaList) {
        this.ogloszeniaList = ogloszeniaList;
    }

    public List<UmUrzadzenie> getUrzadzenieList() {
        return urzadzenieList;
    }

    public void setUrzadzenieList(List<UmUrzadzenie> urzadzenieList) {
        this.urzadzenieList = urzadzenieList;
    }

    public List<UmUrzadzenie> getUrzadzenieAletrPrzeglList() {
        urzadzenieAletrPrzeglList=new ArrayList<UmUrzadzenie>();
        for(UmUrzadzenie um:getUrzadzenieList()){
            if(um.isAlertPrzegl()){
                urzadzenieAletrPrzeglList.add(um);
            }
        }
        return urzadzenieAletrPrzeglList;
    }
/*
    public List<DcDokumentArchDod> getDokArchDodList() {
        return dokArchDodList;
    }

    public void setDokArchDodList(List<DcDokumentArchDod> dokArchDodList) {
        this.dokArchDodList = dokArchDodList;
    }
*/
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uzytkownik)) {
            return false;
        }
        Uzytkownik other = (Uzytkownik) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.Uzytkownik[ id=" + id + " ]";
    }
}
