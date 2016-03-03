/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.abstr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

/**
 *
 * @author 103039
 * @param <X>
 */
public abstract class AbstKontroler<X extends AbstEncja> {

    private final X type;
    static final Logger logger = Logger. getAnonymousLogger();

    public AbstKontroler(X type) {
        this.type = type;
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<X> findEntities() {
        return findEntities(true, -1, -1);
    }

    public List<X> getFindEntities() {
        return findEntities();
    }
    
    public X findEntities(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery(this.type.getClass().getSimpleName() + ".findByNazwa");
            q.setParameter("nazwa", nazwa);
            @SuppressWarnings("unchecked")
            X u = (X) q.getResultList().get(0);
            return u;
        } catch (NoResultException | ArrayIndexOutOfBoundsException ex) {
            //ex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            return null;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public X findObiekt(X obiekt) {
        EntityManager em = getEntityManager();
        try {
            return (X) em.find(type.getClass(), obiekt.getId());
        } finally {
            em.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public X findObiekt(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return (X) em.find(type.getClass(), id);
        } finally {
            em.close();
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<X> findEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(type.getClass()));
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

    public Map<String, String> create(X obiekt) {
        Map<String, String> bledy = new HashMap<>();
        EntityManager em = null;
        if (findEntities(obiekt.getNazwa()) != null) {
            bledy.put("nazwaD", "nazwa już istnieje");
        }
        //if(!bledy.isEmpty()) return bledy;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(obiekt);
            em.getTransaction().commit();
        } catch (Exception ex) {
            //ex.printStackTrace();
            logger.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }

    public Map<String, String> edit(X obiekt) {
        X oldObiekt=findObiekt(obiekt);
        Map<String, String> bledy = new HashMap<>();
        EntityManager em = null;
        if ((findEntities(obiekt.getNazwa()) != null)&&(!obiekt.getNazwa().equals(oldObiekt.getNazwa()))) {
            bledy.put("nazwaD", "nazwa już istnieje");
        }
        if(!bledy.isEmpty()) return bledy;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(obiekt);
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }
    
    public void destroy(X obiekt) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(obiekt));
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
