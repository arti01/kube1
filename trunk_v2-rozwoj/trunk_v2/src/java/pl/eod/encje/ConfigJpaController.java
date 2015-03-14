/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
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
import pl.eod.encje.exceptions.PreexistingEntityException;

/**
 *
 * @author arti01
 */
public class ConfigJpaController implements Serializable {
    private static final long serialVersionUID = 1L;

    public ConfigJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Config config) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(config);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConfig(config.getId()) != null) {
                throw new PreexistingEntityException("Config " + config + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Config config) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            config = em.merge(config);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = config.getId();
                if (findConfig(id) == null) {
                    throw new NonexistentEntityException("The config with id " + id + " no longer exists.");
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
            Config config;
            try {
                config = em.getReference(Config.class, id);
                config.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The config with id " + id + " no longer exists.", enfe);
            }
            em.remove(config);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Config> findConfigEntities() {
        return findConfigEntities(true, -1, -1);
    }

    public List<Config> findConfigEntities(int maxResults, int firstResult) {
        return findConfigEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    public Config findConfigNazwa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Config> cfg = cq.from(Config.class);
            cq.select(cfg);
            cq.where(cb.equal(cfg.get(Config_.nazwa), nazwa));
            Query q = em.createQuery(cq);
            return (Config)q.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    private List<Config> findConfigEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Config.class));
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

    public Config findConfig(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Config.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Config> rt = cq.from(Config.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
