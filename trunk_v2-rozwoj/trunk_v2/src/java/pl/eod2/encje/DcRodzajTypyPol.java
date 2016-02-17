/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pl.eod.abstr.AbstEncja;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_rodzaj_typy_pol")
public class DcRodzajTypyPol extends AbstEncja implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDcRodzajTypyPol")
    @SequenceGenerator(name = "SEQDcRodzajTypyPol", sequenceName = "SEQDcRodzajTypyPol")
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer id;
    
    @NotNull
    @Column(nullable = false, length = 256)
    @Size(min = 1, max = 256)
    private String nazwa;
    
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, orphanRemoval = false, mappedBy = "idRodzTypyPol")
    private List<DcRodzajPolaDod> dcRodzajPolaDodList;

    @Override
    public String getNazwa() {
        return nazwa;
    }
    
    

    public List<DcRodzajPolaDod> getDcRodzajPolaDodList() {
        return dcRodzajPolaDodList;
    }

    public void setDcRodzajPolaDodList(List<DcRodzajPolaDod> dcRodzajPolaDodList) {
        this.dcRodzajPolaDodList = dcRodzajPolaDodList;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
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
        final DcRodzajTypyPol other = (DcRodzajTypyPol) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
    
}
