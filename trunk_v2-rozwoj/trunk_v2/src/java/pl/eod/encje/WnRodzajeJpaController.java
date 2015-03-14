/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import pl.eod.encje.exceptions.IllegalOrphanException;
import pl.eod.encje.exceptions.NonexistentEntityException;
import pl.eod.encje.exceptions.PreexistingEntityException;

/**
 *
 * @author arti01
 */
public class WnRodzajeJpaController implements Serializable {

    public WnRodzajeJpaController() {
         if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WnRodzaje wnRodzaje) throws PreexistingEntityException, Exception {
        if (wnRodzaje.getWnUrlopList() == null) {
            wnRodzaje.setWnUrlopList(new ArrayList<WnUrlop>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<WnUrlop> attachedWnUrlopList = new ArrayList<WnUrlop>();
            for (WnUrlop wnUrlopListWnUrlopToAttach : wnRodzaje.getWnUrlopList()) {
                wnUrlopListWnUrlopToAttach = em.getReference(wnUrlopListWnUrlopToAttach.getClass(), wnUrlopListWnUrlopToAttach.getId());
                attachedWnUrlopList.add(wnUrlopListWnUrlopToAttach);
            }
            wnRodzaje.setWnUrlopList(attachedWnUrlopList);
            em.persist(wnRodzaje);
            for (WnUrlop wnUrlopListWnUrlop : wnRodzaje.getWnUrlopList()) {
                WnRodzaje oldRodzajIdOfWnUrlopListWnUrlop = wnUrlopListWnUrlop.getRodzajId();
                wnUrlopListWnUrlop.setRodzajId(wnRodzaje);
                wnUrlopListWnUrlop = em.merge(wnUrlopListWnUrlop);
                if (oldRodzajIdOfWnUrlopListWnUrlop != null) {
                    oldRodzajIdOfWnUrlopListWnUrlop.getWnUrlopList().remove(wnUrlopListWnUrlop);
                    oldRodzajIdOfWnUrlopListWnUrlop = em.merge(oldRodzajIdOfWnUrlopListWnUrlop);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWnRodzaje(wnRodzaje.getId()) != null) {
                throw new PreexistingEntityException("WnRodzaje " + wnRodzaje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WnRodzaje wnRodzaje) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WnRodzaje persistentWnRodzaje = em.find(WnRodzaje.class, wnRodzaje.getId());
            List<WnUrlop> wnUrlopListOld = persistentWnRodzaje.getWnUrlopList();
            List<WnUrlop> wnUrlopListNew = wnRodzaje.getWnUrlopList();
            List<String> illegalOrphanMessages = null;
            for (WnUrlop wnUrlopListOldWnUrlop : wnUrlopListOld) {
                if (!wnUrlopListNew.contains(wnUrlopListOldWnUrlop)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WnUrlop " + wnUrlopListOldWnUrlop + " since its rodzajId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<WnUrlop> attachedWnUrlopListNew = new ArrayList<WnUrlop>();
            for (WnUrlop wnUrlopListNewWnUrlopToAttach : wnUrlopListNew) {
                wnUrlopListNewWnUrlopToAttach = em.getReference(wnUrlopListNewWnUrlopToAttach.getClass(), wnUrlopListNewWnUrlopToAttach.getId());
                attachedWnUrlopListNew.add(wnUrlopListNewWnUrlopToAttach);
            }
            wnUrlopListNew = attachedWnUrlopListNew;
            wnRodzaje.setWnUrlopList(wnUrlopListNew);
            wnRodzaje = em.merge(wnRodzaje);
            for (WnUrlop wnUrlopListNewWnUrlop : wnUrlopListNew) {
                if (!wnUrlopListOld.contains(wnUrlopListNewWnUrlop)) {
                    WnRodzaje oldRodzajIdOfWnUrlopListNewWnUrlop = wnUrlopListNewWnUrlop.getRodzajId();
                    wnUrlopListNewWnUrlop.setRodzajId(wnRodzaje);
                    wnUrlopListNewWnUrlop = em.merge(wnUrlopListNewWnUrlop);
                    if (oldRodzajIdOfWnUrlopListNewWnUrlop != null && !oldRodzajIdOfWnUrlopListNewWnUrlop.equals(wnRodzaje)) {
                        oldRodzajIdOfWnUrlopListNewWnUrlop.getWnUrlopList().remove(wnUrlopListNewWnUrlop);
                        oldRodzajIdOfWnUrlopListNewWnUrlop = em.merge(oldRodzajIdOfWnUrlopListNewWnUrlop);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = wnRodzaje.getId();
                if (findWnRodzaje(id) == null) {
                    throw new NonexistentEntityException("The wnRodzaje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WnRodzaje wnRodzaje;
            try {
                wnRodzaje = em.getReference(WnRodzaje.class, id);
                wnRodzaje.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The wnRodzaje with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<WnUrlop> wnUrlopListOrphanCheck = wnRodzaje.getWnUrlopList();
            for (WnUrlop wnUrlopListOrphanCheckWnUrlop : wnUrlopListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WnRodzaje (" + wnRodzaje + ") cannot be destroyed since the WnUrlop " + wnUrlopListOrphanCheckWnUrlop + " in its wnUrlopList field has a non-nullable rodzajId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(wnRodzaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WnRodzaje> getFindWnRodzajeEntities() {
        return findWnRodzajeEntities(true, -1, -1);
    }

    public List<WnRodzaje> findWnRodzajeEntities(int maxResults, int firstResult) {
        return findWnRodzajeEntities(false, maxResults, firstResult);
    }

    private List<WnRodzaje> findWnRodzajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WnRodzaje.class)).orderBy(em.getCriteriaBuilder().asc(cq.from(WnRodzaje.class).get("id")));
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

    public WnRodzaje findWnRodzaje(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WnRodzaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getWnRodzajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WnRodzaje> rt = cq.from(WnRodzaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
