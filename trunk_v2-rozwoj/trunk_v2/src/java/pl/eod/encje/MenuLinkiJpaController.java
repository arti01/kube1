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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod.encje.exceptions.NonexistentEntityException;
import pl.eod.encje.exceptions.PreexistingEntityException;

/**
 *
 * @author arti01
 */
public class MenuLinkiJpaController implements Serializable {

    public MenuLinkiJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MenuLinki menuLinki) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(menuLinki);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMenuLinki(menuLinki.getId()) != null) {
                throw new PreexistingEntityException("MenuLinki " + menuLinki + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MenuLinki menuLinki) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            menuLinki = em.merge(menuLinki);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = menuLinki.getId();
                if (findMenuLinki(id) == null) {
                    throw new NonexistentEntityException("The menuLinki with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MenuLinki menuLinki;
            try {
                menuLinki = em.getReference(MenuLinki.class, id);
                menuLinki.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The menuLinki with id " + id + " no longer exists.", enfe);
            }
            em.remove(menuLinki);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MenuLinki> findMenuLinkiEntities() {
        return findMenuLinkiEntities(true, -1, -1);
    }

    public List<MenuLinki> findMenuLinkiEntities(int maxResults, int firstResult) {
        return findMenuLinkiEntities(false, maxResults, firstResult);
    }

    private List<MenuLinki> findMenuLinkiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MenuLinki.class));
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

    public MenuLinki findMenuLinki(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MenuLinki.class, id);
        } finally {
            em.close();
        }
    }

    public int getMenuLinkiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MenuLinki> rt = cq.from(MenuLinki.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
