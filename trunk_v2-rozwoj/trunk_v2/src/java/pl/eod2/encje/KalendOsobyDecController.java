/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class KalendOsobyDecController implements Serializable {
    private static final long serialVersionUID = 1L;

    public KalendOsobyDecController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    public List<KalendOsobyDec> findEntities() {
        return findKalendOsobyDecEntities(true, -1, -1);
    }

    public List<KalendOsobyDec> findKalendOsobyDecEntities(int maxResults, int firstResult) {
        return findKalendOsobyDecEntities(false, maxResults, firstResult);
    }

    private List<KalendOsobyDec> findKalendOsobyDecEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KalendOsobyDec.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    
    public KalendOsobyDec findKalendOsobyDec(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KalendOsobyDec.class, id);
        } finally {
            em.close();
        }
    }
    
}
