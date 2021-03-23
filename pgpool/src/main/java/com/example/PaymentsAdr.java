/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author arti01
 */
@Entity
@Table(name = "payments_adr", catalog = "testy", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PaymentsAdr.findAll", query = "SELECT p FROM PaymentsAdr p"),
    @NamedQuery(name = "PaymentsAdr.findById", query = "SELECT p FROM PaymentsAdr p WHERE p.id = :id"),
    @NamedQuery(name = "PaymentsAdr.findByIdAgenta", query = "SELECT p FROM PaymentsAdr p WHERE p.idAgenta = :idAgenta"),
    @NamedQuery(name = "PaymentsAdr.findByTytul", query = "SELECT p FROM PaymentsAdr p WHERE p.tytul = :tytul"),
    @NamedQuery(name = "PaymentsAdr.findByColumn1", query = "SELECT p FROM PaymentsAdr p WHERE p.column1 = :column1")})
public class PaymentsAdr implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "id_agenta", nullable = false)
    private int idAgenta;
    @Column()
    private String tytul;
    @Temporal(TemporalType.TIMESTAMP)
    private Date column1;

    public PaymentsAdr() {
    }

    public PaymentsAdr(Integer id) {
        this.id = id;
    }

    public PaymentsAdr(Integer id, int idAgenta) {
        this.id = id;
        this.idAgenta = idAgenta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getIdAgenta() {
        return idAgenta;
    }

    public void setIdAgenta(int idAgenta) {
        this.idAgenta = idAgenta;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public Date getColumn1() {
        return column1;
    }

    public void setColumn1(Date column1) {
        this.column1 = column1;
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
        if (!(object instanceof PaymentsAdr)) {
            return false;
        }
        PaymentsAdr other = (PaymentsAdr) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.example.PaymentsAdr[ id=" + id + " ]";
    }

}
