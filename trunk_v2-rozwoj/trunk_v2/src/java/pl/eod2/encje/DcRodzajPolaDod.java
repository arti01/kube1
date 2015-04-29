/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;
import pl.eod.abstr.AbstPlik;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_rodzaj_pola_dod")
public class DcRodzajPolaDod extends AbstEncja implements Serializable {
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
    
    @ManyToOne()
    private DcRodzaj idRodz;
    
    @ManyToOne()
    private DcRodzajTypyPol idRodzTypyPol;

    public DcRodzaj getIdRodz() {
        return idRodz;
    }

    public void setIdRodz(DcRodzaj idRodz) {
        this.idRodz = idRodz;
    }

    public DcRodzajTypyPol getIdRodzTypyPol() {
        return idRodzTypyPol;
    }

    public void setIdRodzTypyPol(DcRodzajTypyPol idRodzTypyPol) {
        this.idRodzTypyPol = idRodzTypyPol;
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
        final DcRodzajPolaDod other = (DcRodzajPolaDod) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nazwa, other.nazwa)) {
            return false;
        }
        return true;
    }
    
    
}
