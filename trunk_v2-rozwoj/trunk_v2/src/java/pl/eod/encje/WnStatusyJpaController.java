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
public class WnStatusyJpaController implements Serializable {

    public WnStatusyJpaController() {
         if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WnStatusy wnStatusy) throws PreexistingEntityException, Exception {
        if (wnStatusy.getWnHistoriaList() == null) {
            wnStatusy.setWnHistoriaList(new ArrayList<WnHistoria>());
        }
        if (wnStatusy.getWnUrlopList() == null) {
            wnStatusy.setWnUrlopList(new ArrayList<WnUrlop>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<WnHistoria> attachedWnHistoriaList = new ArrayList<WnHistoria>();
            for (WnHistoria wnHistoriaListWnHistoriaToAttach : wnStatusy.getWnHistoriaList()) {
                wnHistoriaListWnHistoriaToAttach = em.getReference(wnHistoriaListWnHistoriaToAttach.getClass(), wnHistoriaListWnHistoriaToAttach.getId());
                attachedWnHistoriaList.add(wnHistoriaListWnHistoriaToAttach);
            }
            wnStatusy.setWnHistoriaList(attachedWnHistoriaList);
            List<WnUrlop> attachedWnUrlopList = new ArrayList<WnUrlop>();
            for (WnUrlop wnUrlopListWnUrlopToAttach : wnStatusy.getWnUrlopList()) {
                wnUrlopListWnUrlopToAttach = em.getReference(wnUrlopListWnUrlopToAttach.getClass(), wnUrlopListWnUrlopToAttach.getId());
                attachedWnUrlopList.add(wnUrlopListWnUrlopToAttach);
            }
            wnStatusy.setWnUrlopList(attachedWnUrlopList);
            em.persist(wnStatusy);
            for (WnHistoria wnHistoriaListWnHistoria : wnStatusy.getWnHistoriaList()) {
                WnStatusy oldStatusIdOfWnHistoriaListWnHistoria = wnHistoriaListWnHistoria.getStatusId();
                wnHistoriaListWnHistoria.setStatusId(wnStatusy);
                wnHistoriaListWnHistoria = em.merge(wnHistoriaListWnHistoria);
                if (oldStatusIdOfWnHistoriaListWnHistoria != null) {
                    oldStatusIdOfWnHistoriaListWnHistoria.getWnHistoriaList().remove(wnHistoriaListWnHistoria);
                    oldStatusIdOfWnHistoriaListWnHistoria = em.merge(oldStatusIdOfWnHistoriaListWnHistoria);
                }
            }
            for (WnUrlop wnUrlopListWnUrlop : wnStatusy.getWnUrlopList()) {
                WnStatusy oldStatusIdOfWnUrlopListWnUrlop = wnUrlopListWnUrlop.getStatusId();
                wnUrlopListWnUrlop.setStatusId(wnStatusy);
                wnUrlopListWnUrlop = em.merge(wnUrlopListWnUrlop);
                if (oldStatusIdOfWnUrlopListWnUrlop != null) {
                    oldStatusIdOfWnUrlopListWnUrlop.getWnUrlopList().remove(wnUrlopListWnUrlop);
                    oldStatusIdOfWnUrlopListWnUrlop = em.merge(oldStatusIdOfWnUrlopListWnUrlop);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWnStatusy(wnStatusy.getId()) != null) {
                throw new PreexistingEntityException("WnStatusy " + wnStatusy + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WnStatusy wnStatusy) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WnStatusy persistentWnStatusy = em.find(WnStatusy.class, wnStatusy.getId());
            List<WnHistoria> wnHistoriaListOld = persistentWnStatusy.getWnHistoriaList();
            List<WnHistoria> wnHistoriaListNew = wnStatusy.getWnHistoriaList();
            List<WnUrlop> wnUrlopListOld = persistentWnStatusy.getWnUrlopList();
            List<WnUrlop> wnUrlopListNew = wnStatusy.getWnUrlopList();
            List<String> illegalOrphanMessages = null;
            for (WnHistoria wnHistoriaListOldWnHistoria : wnHistoriaListOld) {
                if (!wnHistoriaListNew.contains(wnHistoriaListOldWnHistoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WnHistoria " + wnHistoriaListOldWnHistoria + " since its statusId field is not nullable.");
                }
            }
            for (WnUrlop wnUrlopListOldWnUrlop : wnUrlopListOld) {
                if (!wnUrlopListNew.contains(wnUrlopListOldWnUrlop)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain WnUrlop " + wnUrlopListOldWnUrlop + " since its statusId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<WnHistoria> attachedWnHistoriaListNew = new ArrayList<WnHistoria>();
            for (WnHistoria wnHistoriaListNewWnHistoriaToAttach : wnHistoriaListNew) {
                wnHistoriaListNewWnHistoriaToAttach = em.getReference(wnHistoriaListNewWnHistoriaToAttach.getClass(), wnHistoriaListNewWnHistoriaToAttach.getId());
                attachedWnHistoriaListNew.add(wnHistoriaListNewWnHistoriaToAttach);
            }
            wnHistoriaListNew = attachedWnHistoriaListNew;
            wnStatusy.setWnHistoriaList(wnHistoriaListNew);
            List<WnUrlop> attachedWnUrlopListNew = new ArrayList<WnUrlop>();
            for (WnUrlop wnUrlopListNewWnUrlopToAttach : wnUrlopListNew) {
                wnUrlopListNewWnUrlopToAttach = em.getReference(wnUrlopListNewWnUrlopToAttach.getClass(), wnUrlopListNewWnUrlopToAttach.getId());
                attachedWnUrlopListNew.add(wnUrlopListNewWnUrlopToAttach);
            }
            wnUrlopListNew = attachedWnUrlopListNew;
            wnStatusy.setWnUrlopList(wnUrlopListNew);
            wnStatusy = em.merge(wnStatusy);
            for (WnHistoria wnHistoriaListNewWnHistoria : wnHistoriaListNew) {
                if (!wnHistoriaListOld.contains(wnHistoriaListNewWnHistoria)) {
                    WnStatusy oldStatusIdOfWnHistoriaListNewWnHistoria = wnHistoriaListNewWnHistoria.getStatusId();
                    wnHistoriaListNewWnHistoria.setStatusId(wnStatusy);
                    wnHistoriaListNewWnHistoria = em.merge(wnHistoriaListNewWnHistoria);
                    if (oldStatusIdOfWnHistoriaListNewWnHistoria != null && !oldStatusIdOfWnHistoriaListNewWnHistoria.equals(wnStatusy)) {
                        oldStatusIdOfWnHistoriaListNewWnHistoria.getWnHistoriaList().remove(wnHistoriaListNewWnHistoria);
                        oldStatusIdOfWnHistoriaListNewWnHistoria = em.merge(oldStatusIdOfWnHistoriaListNewWnHistoria);
                    }
                }
            }
            for (WnUrlop wnUrlopListNewWnUrlop : wnUrlopListNew) {
                if (!wnUrlopListOld.contains(wnUrlopListNewWnUrlop)) {
                    WnStatusy oldStatusIdOfWnUrlopListNewWnUrlop = wnUrlopListNewWnUrlop.getStatusId();
                    wnUrlopListNewWnUrlop.setStatusId(wnStatusy);
                    wnUrlopListNewWnUrlop = em.merge(wnUrlopListNewWnUrlop);
                    if (oldStatusIdOfWnUrlopListNewWnUrlop != null && !oldStatusIdOfWnUrlopListNewWnUrlop.equals(wnStatusy)) {
                        oldStatusIdOfWnUrlopListNewWnUrlop.getWnUrlopList().remove(wnUrlopListNewWnUrlop);
                        oldStatusIdOfWnUrlopListNewWnUrlop = em.merge(oldStatusIdOfWnUrlopListNewWnUrlop);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = wnStatusy.getId();
                if (findWnStatusy(id) == null) {
                    throw new NonexistentEntityException("The wnStatusy with id " + id + " no longer exists.");
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
            WnStatusy wnStatusy;
            try {
                wnStatusy = em.getReference(WnStatusy.class, id);
                wnStatusy.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The wnStatusy with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<WnHistoria> wnHistoriaListOrphanCheck = wnStatusy.getWnHistoriaList();
            for (WnHistoria wnHistoriaListOrphanCheckWnHistoria : wnHistoriaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WnStatusy (" + wnStatusy + ") cannot be destroyed since the WnHistoria " + wnHistoriaListOrphanCheckWnHistoria + " in its wnHistoriaList field has a non-nullable statusId field.");
            }
            List<WnUrlop> wnUrlopListOrphanCheck = wnStatusy.getWnUrlopList();
            for (WnUrlop wnUrlopListOrphanCheckWnUrlop : wnUrlopListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WnStatusy (" + wnStatusy + ") cannot be destroyed since the WnUrlop " + wnUrlopListOrphanCheckWnUrlop + " in its wnUrlopList field has a non-nullable statusId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(wnStatusy);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WnStatusy> getFindWnStatusyEntities() {
        return findWnStatusyEntities(true, -1, -1);
    }

    public List<WnStatusy> findWnStatusyEntities(int maxResults, int firstResult) {
        return findWnStatusyEntities(false, maxResults, firstResult);
    }

    private List<WnStatusy> findWnStatusyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WnStatusy.class));
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

    public WnStatusy findWnStatusy(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WnStatusy.class, id);
        } finally {
            em.close();
        }
    }

    public int getWnStatusyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WnStatusy> rt = cq.from(WnStatusy.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
