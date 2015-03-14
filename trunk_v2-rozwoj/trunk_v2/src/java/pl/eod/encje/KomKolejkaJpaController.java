/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class KomKolejkaJpaController implements Serializable {

    public KomKolejkaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    
    
    public void create(KomKolejka komKolejka) {
        EntityManager em = null;
        try {
            Date data=new Date();
            komKolejka.setDataInsert(data);
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(komKolejka);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(KomKolejka komKolejka) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            komKolejka = em.merge(komKolejka);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = komKolejka.getId();
                if (findKomKolejka(id) == null) {
                    throw new NonexistentEntityException("The komKolejka with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            KomKolejka komKolejka;
            try {
                komKolejka = em.getReference(KomKolejka.class, id);
                komKolejka.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The komKolejka with id " + id + " no longer exists.", enfe);
            }
            em.remove(komKolejka);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<KomKolejka> findNiewyslane() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery<KomKolejka> cq = cb.createQuery(KomKolejka.class);
            Root<KomKolejka> kk = cq.from(KomKolejka.class);
            cq.select(kk);
            cq.where(cb.equal(kk.get(KomKolejka_.status), 0));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<KomKolejka> findKomKolejkaEntities() {
        return findKomKolejkaEntities(true, -1, -1);
    }

    public List<KomKolejka> findKomKolejkaEntities(int maxResults, int firstResult) {
        return findKomKolejkaEntities(false, maxResults, firstResult);
    }

    private List<KomKolejka> findKomKolejkaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(KomKolejka.class));
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

    public KomKolejka findKomKolejka(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(KomKolejka.class, id);
        } finally {
            em.close();
        }
    }

    public int getKomKolejkaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<KomKolejka> rt = cq.from(KomKolejka.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
