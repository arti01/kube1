/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "dc_kontrahenci_email_dod")
public class DcKontahentDodEmail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQDCKONTRAHEMAILDOD")
    @SequenceGenerator(name = "SEQDCKONTRAHEMAILDOD", sequenceName = "SEQDCKONTRAHEMAILDOD")
    private Long id;
    
    @Email
    @Size(max = 255)
    @Column(name = "email", length = 255)
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "kontrahent_id", referencedColumnName = "id", nullable = false)
    private DcKontrahenci kontrahentId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DcKontrahenci getKontrahentId() {
        return kontrahentId;
    }

    public void setKontrahentId(DcKontrahenci kontrahentId) {
        this.kontrahentId = kontrahentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof DcKontahentDodEmail)) {
            return false;
        }
        DcKontahentDodEmail other = (DcKontahentDodEmail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod2.encje.DcKontachentDodEmail[ id=" + id + " ]";
    }
    
}
