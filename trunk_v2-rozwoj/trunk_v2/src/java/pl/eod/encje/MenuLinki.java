/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "menu_linki")
@NamedQueries({
    @NamedQuery(name = "MenuLinki.findAll", query = "SELECT m FROM MenuLinki m"),
    @NamedQuery(name = "MenuLinki.findById", query = "SELECT m FROM MenuLinki m WHERE m.id = :id"),
    @NamedQuery(name = "MenuLinki.findByNazwa", query = "SELECT m FROM MenuLinki m WHERE m.nazwa = :nazwa"),
    @NamedQuery(name = "MenuLinki.findByLink", query = "SELECT m FROM MenuLinki m WHERE m.link = :link"),
    @NamedQuery(name = "MenuLinki.findByOpis", query = "SELECT m FROM MenuLinki m WHERE m.opis = :opis")})
public class MenuLinki implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "nazwa")
    private String nazwa;
    @Size(max = 255)
    @Column(name = "link")
    private String link;
    @Size(max = 255)
    @Column(name = "opis")
    private String opis;

    public MenuLinki() {
    }

    public MenuLinki(Integer id) {
        this.id = id;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
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
        if (!(object instanceof MenuLinki)) {
            return false;
        }
        MenuLinki other = (MenuLinki) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pl.eod.encje.MenuLinki[ id=" + id + " ]";
    }
    
}
