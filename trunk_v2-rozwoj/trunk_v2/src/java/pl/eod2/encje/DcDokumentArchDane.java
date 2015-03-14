/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Embeddable
public class DcDokumentArchDane implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "arch_data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date archData;
    @Size(max = 256)
    @Column(name = "arch_osoba_zdajaca", length = 256)
    private String archOsobaZdajaca;
    @Size(max = 256)
    @Column(name = "arch_osoba_odpowiadajaca", length = 256)
    private String archOsobaOdpowiadajaca;
    @Size(max = 256)
    @Column(name = "arch_pokoj ", length = 256)
    private String archPokoj;
    @Size(max = 256)
    @Column(name = "arch_regal ", length = 256)
    private String archRegal;
    @Size(max = 256)
    @Column(name = "arch_polka ", length = 256)
    private String archPolka;
    @Size(max = 256)
    @Column(name = "arch_karton ", length = 256)
    private String archKarton;
    @Size(max = 256)
    @Column(name = "arch_teczka ", length = 256)
    private String archTeczka;
    
    @Transient
    private String archDataStr;
    
    public DcDokumentArchDane() {
    }
    
    public Date getArchData() {
        return archData;
    }

    public void setArchData(Date archData) {
        this.archData = archData;
    }

    public String getArchOsobaZdajaca() {
        return archOsobaZdajaca;
    }

    public void setArchOsobaZdajaca(String archOsobaZdajaca) {
        this.archOsobaZdajaca = archOsobaZdajaca;
    }

    public String getArchOsobaOdpowiadajaca() {
        return archOsobaOdpowiadajaca;
    }

    public void setArchOsobaOdpowiadajaca(String archOsobaOdpowiadajaca) {
        this.archOsobaOdpowiadajaca = archOsobaOdpowiadajaca;
    }

    public String getArchPokoj() {
        return archPokoj;
    }

    public void setArchPokoj(String archPokoj) {
        this.archPokoj = archPokoj;
    }

    public String getArchRegal() {
        return archRegal;
    }

    public void setArchRegal(String archRegal) {
        this.archRegal = archRegal;
    }

    public String getArchPolka() {
        return archPolka;
    }

    public void setArchPolka(String archPolka) {
        this.archPolka = archPolka;
    }

    public String getArchKarton() {
        return archKarton;
    }

    public void setArchKarton(String archKarton) {
        this.archKarton = archKarton;
    }

    public String getArchTeczka() {
        return archTeczka;
    }

    public void setArchTeczka(String archTeczka) {
        this.archTeczka = archTeczka;
    }

    public String getArchDataStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (getArchData()== null) {
            return "";
        } else {
            return sdf.format(getArchData());
        }
    }
}
