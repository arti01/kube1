/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import pl.eod.abstr.AbstKontroler;

/**
 *
 * @author 103039
 */
public class DcTeczkaKontr extends AbstKontroler<DcTeczka> {

    public DcTeczkaKontr() {
        super(new DcTeczka());
    }

    public List<DcTeczka> findDlaStatus(DcDokumentStatus dcStatus) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcTeczka.findDlaStatus");
            q.setParameter("status", dcStatus);
            HashSet hs = new HashSet();
            hs.addAll(q.getResultList());
            ArrayList<DcTeczka> ar =new ArrayList(hs);
            return ar;
            //return hs;
        } catch (NoResultException nex) {
            nex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            return null;
        } finally {
            em.close();
        }
    }
}
