/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import pl.eod.abstr.AbstKontroler;

@ManagedBean
@SessionScoped
public class KalendarzKontr extends AbstKontroler<Kalendarz> {

    public KalendarzKontr() {
        super(new Kalendarz());
    }

    @Override
    public Map<String, String> create(Kalendarz obiekt) {
        EntityManager em = getEntityManager();
        Map<String, String> bledy = new HashMap<>();
        if (obiekt.getDataDo().before(obiekt.getDataOd())) {
            bledy.put("dodaj", "data od większa od daty do");
            return bledy;
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(obiekt);
            em.merge(obiekt.getTworca());
            em.getTransaction().commit();
        } catch (Exception ex) {
            //ex.printStackTrace();
            LOG.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }

    @Override
    public Map<String, String> edit(Kalendarz obiekt) {
        EntityManager em = getEntityManager();
        Map<String, String> bledy = new HashMap<>();
        if (obiekt.getDataDo().before(obiekt.getDataOd())) {
            bledy.put("dodaj", "data od większa od daty do");
            return bledy;
        }
        if (!bledy.isEmpty()) {
            return bledy;
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(obiekt);
            em.getTransaction().commit();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }
    private static final Logger LOG = Logger.getLogger(KalendarzKontr.class.getName());
}
