/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.Date;
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
    
}
