/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import pl.eod2.encje.Ogloszenia;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "struktura")
@NamedQueries({
    @NamedQuery(name = "Struktura.findAll", query = "SELECT s FROM Struktura s"),
    @NamedQuery(name = "Struktura.findById", query = "SELECT s FROM Struktura s WHERE s.id = :id"),
    @NamedQuery(name = "Struktura.findByStKier", query = "SELECT s FROM Struktura s WHERE s.stKier = :stKier ORDER BY s.userId.fullname"),
    @NamedQuery(name = "Struktura.findBezSzefa", query = "SELECT s FROM Struktura s WHERE s.szefId is null and (s.usuniety<>1 or s.usuniety is null) ORDER BY s.userId.fullname"),
    @NamedQuery(name = "Struktura.findBezSzefaSpolka", query = "SELECT s FROM Struktura s WHERE s.szefId is null and (s.usuniety<>1 or s.usuniety is null) AND s.userId.spolkaId=:spolka ORDER BY s.userId.fullname"),
    @NamedQuery(name = "Struktura.kierownicy", query = "SELECT s FROM Struktura s WHERE s.stKier=1 and (s.usuniety<>1 or s.usuniety is null) AND s.userId.spolkaId=:spolka ORDER BY s.userId.fullname"),
    @NamedQuery(name = "Struktura.kierownicyAll", query = "SELECT s FROM Struktura s WHERE s.stKier=1 and (s.usuniety<>1 or s.usuniety is null) ORDER BY s.userId.fullname")
})
public class Struktura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQSTRUKTURA")
    @SequenceGenerator(name = "SEQSTRUKTURA", sequenceName = "SEQSTRUKTURA")
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "szef_id", referencedColumnName = "id")
    @ManyToOne()
    private Struktura szefId;
    @Column(name = "st_kier", nullable = false)
    private Integer stKier;
    @NotNull
    @Column(name = "przyjmowanie_wnioskow", nullable = false)
    private int przyjmowanieWnioskow;
    @JoinColumn(name = "sec_user_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.ALL})
    private Uzytkownik secUserId;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    private Uzytkownik userId;
    @JoinColumn(name = "dzial_id", referencedColumnName = "id")
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
    private Dzial dzialId;
    @OneToMany(mappedBy = "szefId")
    List<Struktura> bezpPod;
    @Column(name = "usuniety", nullable = true)
    private Integer usuniety;
    @NotNull
    @Column(name = "extraemail", nullable = true)
    private String extraemail;
    @Column(name = "mus_zast", nullable = false)
    private Integer musZast;
    @ManyToMany(mappedBy = "adresaciList", fetch = FetchType.LAZY)
    private List<Ogloszenia> ogloszeniaList;
    @Transient
    List<Struktura> bezpPodzPodwlad;
    @Transient
    List<Struktura> bezpPodBezPodwlad;
    @Transient
    List<Struktura> wszyscyPodwladni;
    @Transient
    List<Struktura> mozliwiSzefowie;
    @Transient
    List<Struktura> bezpPodWidoczni;
    @Transient
    List<Struktura> obceWnioski;
    @Transient
    String[] rolaString;
    @Transient
    boolean sysSdmin;

    public Struktura() {
    }

    public Struktura(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Struktura getSzefId() {
        return szefId;
    }

    public void setSzefId(Struktura szefId) {
        this.szefId = szefId;
    }

    public List<Struktura> getBezpPod() {
        Collections.sort(bezpPod, new Comparator<Struktura>() {
            @Override
            public int compare(Struktura s1, Struktura s2) {
                int wynik;
                try {
                    wynik = s1.getUserId().getFullname().compareToIgnoreCase(s2.getUserId().getFullname());
                } catch (NullPointerException mpe) {
                    wynik = 0;
                }
                return wynik;
            }
        });
        return bezpPod;
    }

    public void setBezpPod(List<Struktura> bezpPod) {
        this.bezpPod = bezpPod;
    }

    public boolean isStKier() {
        if (stKier == null || stKier == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void setStKier(boolean stKier) {
        if (stKier) {
            this.stKier = 1;
        } else {
            this.stKier = 0;
        }
    }

    public Uzytkownik getSecUserId() {
        return secUserId;
    }

    public void setSecUserId(Uzytkownik secUserId) {
        this.secUserId = secUserId;
    }

    public Uzytkownik getUserId() {
        return userId;
    }

    public void setUserId(Uzytkownik userId) {
        this.userId = userId;
    }

    public Dzial getDzialId() {
        return dzialId;
    }

    public void setDzialId(Dzial dzialId) {
        this.dzialId = dzialId;
    }

    public List<Struktura> getBezpPodzPodwlad() {
        List<Struktura> tree = new ArrayList<Struktura>();
        for (Struktura s : getBezpPodWidoczni()) {
            if (s.getBezpPod().size() > 0) {
                tree.add(s);
            }
        }
        return tree;
    }

    public List<Struktura> getBezpPodBezPodwladKier() {
        List<Struktura> bezpPodKier = new ArrayList<Struktura>();
        bezpPod.removeAll(getBezpPodzPodwlad());
        for (Struktura s : bezpPod) {
            if (s.isStKier()) {
                bezpPodKier.add(s);
            }
        }
        return bezpPodKier;
    }

    //podwladni bezposredni bez tych, ktory maja swoich podwladnych
    public List<Struktura> getBezpPodBezPodwlad() {
        bezpPodBezPodwlad = getBezpPodWidoczni();
        bezpPodBezPodwlad.removeAll(getBezpPodzPodwlad());
        return bezpPodBezPodwlad;
    }

    public List<Struktura> getWszyscyPodwladni() {
        wszyscyPodwladni = new ArrayList<Struktura>();
        wszyscyPodwladni.addAll(getBezpPodWidoczni());
        for (Struktura s : bezpPod) {
            wszyscyPodwladni.addAll(s.getWszyscyPodwladni());
        }
        return wszyscyPodwladni;
    }

    public List<Struktura> getMozliwiSzefowie() {
        StrukturaJpaController strukC = new StrukturaJpaController();
        mozliwiSzefowie = strukC.getFindKierownicy(this.userId.getSpolkaId());
        //System.out.println(this.userId);
        //System.out.println(getWszyscyPodwladni());
        mozliwiSzefowie.removeAll(getWszyscyPodwladni());
        mozliwiSzefowie.add(strukC.findGeneryczny());
        mozliwiSzefowie.remove(this);
        return mozliwiSzefowie;
    }

    public String[] getRolaString() {
        String[] strarray = new String[getUserId().getRole().size()];
        //List<String> strList = new ArrayList<String>();
        //for (UserRoles rola : getUserId().getRole()) {
        //  strList.add("=" + rola.getRolename() + "=");
        //}
        //strList.toArray(strarray);
        return strarray;
    }

    public boolean isUsuniety() {
        if (usuniety == null) {
            return false;
        }
        if (usuniety == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setUsuniety(Integer usuniety) {
        this.usuniety = usuniety;
    }

    public List<Struktura> getBezpPodWidoczni() {
        List<Struktura> wynik = new ArrayList<Struktura>();
        for (Struktura s : getBezpPod()) {
            if (!s.isUsuniety()) {
                wynik.add(s);
            }
        }
        return wynik;
    }

    public List<Struktura> getObceWnioski() {
        List<Struktura> wynik = new ArrayList<Struktura>();
        if (isStKier()) {
            wynik.addAll(getBezpPodWidoczni());
        } else {
            wynik.addAll(szefId.getBezpPodWidoczni());
            wynik.add(szefId);
        }
        //wyszukiwanie podwładnych drugiego poziomu, czyli podwaładnych kierowników,
        //którzy mają za szefa szefa tego działu
        Struktura szef;
        if (isStKier()) {
            szef = this;
        } else {
            szef = szefId;
        }

        for (Struktura s : szef.getBezpPod()) {
            if (s.isStKier()) {
                for (Struktura s1 : s.getBezpPodWidoczni()) {
                    if (!s1.isStKier()) {
                        wynik.add(s1);
                    }
                }
            }

        }

        return wynik;
    }

    public boolean isPrzyjmowanieWnioskow() {
        if (przyjmowanieWnioskow == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void setPrzyjmowanieWnioskow(boolean przyjmowanieWnioskow) {
        if (przyjmowanieWnioskow) {
            this.przyjmowanieWnioskow = 0;
        } else {
            this.przyjmowanieWnioskow = 1;
        }
    }

    public String getExtraemail() {
        return extraemail;
    }

    public void setExtraemail(String extraemail) {
        this.extraemail = extraemail;
    }

    public boolean isSysSdmin() {
        if (this.bezpPod.isEmpty() && this.userId.getSpolkaId() == null && this.szefId == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMusZast() {
        boolean wynik;
        if (this.musZast==null||this.musZast != 0) {
            wynik = false;
        } else {
            wynik = true;
        }
        return wynik;
    }

    public void setMusZast(boolean musZast) {
        if (musZast) {
            this.musZast = 0;
        } else {
            this.musZast = 1;
        }
    }

    public List<Ogloszenia> getOgloszeniaList() {
        return ogloszeniaList;
    }

    public void setOgloszeniaList(List<Ogloszenia> ogloszeniaList) {
        this.ogloszeniaList = ogloszeniaList;
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
        if (!(object instanceof Struktura)) {
            return false;
        }
        Struktura other = (Struktura) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.Struktura[ id=" + id + " ]";
    }
}
