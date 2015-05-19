/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_dok_pola_dod")
public class DcDokPolaDod extends AbstEncja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDcRodzajPolaDod")
    @SequenceGenerator(name = "SEQDcRodzajPolaDod", sequenceName = "SEQDcRodzajPolaDod")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    
    @NotNull
    @Column(nullable = false, length = 256)
    @Size(min = 1, max = 256)
    private String nazwa;
    
    @NotNull
    @Min(1)
    private int dlugosc;
    
    private String wartosc;
    
    private String typ;
    
    @ManyToOne()
    private DcDokument dcDok;

    @Override
    public void setNazwa(String nazwa) {
        this.nazwa=nazwa;
    }

    @Override
    public String getNazwa() {
        return nazwa;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(int dlugosc) {
        this.dlugosc = dlugosc;
    }

    public String getWartosc() {
        return wartosc;
    }

    public void setWartosc(String wartosc) {
        this.wartosc = wartosc;
    }

    public DcDokument getDcDok() {
        return dcDok;
    }

    public void setDcDok(DcDokument dcDok) {
        this.dcDok = dcDok;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.nazwa);
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
        final DcDokPolaDod other = (DcDokPolaDod) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nazwa, other.nazwa)) {
            return false;
        }
        return true;
    }

    
    
}
