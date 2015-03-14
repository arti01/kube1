/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

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
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.encje.exceptions.PreexistingEntityException;

/**
 *
 * @author 103039
 */
public class DcAkceptTypKrokuJpaController implements Serializable {

    public DcAkceptTypKrokuJpaController() {
       if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DcAkceptTypKroku dcAkceptTypKroku) throws PreexistingEntityException, Exception {
        if (dcAkceptTypKroku.getDcAkceptKrokiList() == null) {
            dcAkceptTypKroku.setDcAkceptKrokiList(new ArrayList<DcAkceptKroki>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DcAkceptKroki> attachedDcAkceptKrokiList = new ArrayList<DcAkceptKroki>();
            for (DcAkceptKroki dcAkceptKrokiListDcAkceptKrokiToAttach : dcAkceptTypKroku.getDcAkceptKrokiList()) {
                dcAkceptKrokiListDcAkceptKrokiToAttach = em.getReference(dcAkceptKrokiListDcAkceptKrokiToAttach.getClass(), dcAkceptKrokiListDcAkceptKrokiToAttach.getId());
                attachedDcAkceptKrokiList.add(dcAkceptKrokiListDcAkceptKrokiToAttach);
            }
            dcAkceptTypKroku.setDcAkceptKrokiList(attachedDcAkceptKrokiList);
            em.persist(dcAkceptTypKroku);
            for (DcAkceptKroki dcAkceptKrokiListDcAkceptKroki : dcAkceptTypKroku.getDcAkceptKrokiList()) {
                DcAkceptTypKroku oldDcAckeptTypKrokuOfDcAkceptKrokiListDcAkceptKroki = dcAkceptKrokiListDcAkceptKroki.getDcAckeptTypKroku();
                dcAkceptKrokiListDcAkceptKroki.setDcAckeptTypKroku(dcAkceptTypKroku);
                dcAkceptKrokiListDcAkceptKroki = em.merge(dcAkceptKrokiListDcAkceptKroki);
                if (oldDcAckeptTypKrokuOfDcAkceptKrokiListDcAkceptKroki != null) {
                    oldDcAckeptTypKrokuOfDcAkceptKrokiListDcAkceptKroki.getDcAkceptKrokiList().remove(dcAkceptKrokiListDcAkceptKroki);
                    oldDcAckeptTypKrokuOfDcAkceptKrokiListDcAkceptKroki = em.merge(oldDcAckeptTypKrokuOfDcAkceptKrokiListDcAkceptKroki);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDcAkceptTypKroku(dcAkceptTypKroku.getId()) != null) {
                throw new PreexistingEntityException("DcAkceptTypKroku " + dcAkceptTypKroku + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DcAkceptTypKroku dcAkceptTypKroku) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcAkceptTypKroku persistentDcAkceptTypKroku = em.find(DcAkceptTypKroku.class, dcAkceptTypKroku.getId());
            List<DcAkceptKroki> dcAkceptKrokiListOld = persistentDcAkceptTypKroku.getDcAkceptKrokiList();
            List<DcAkceptKroki> dcAkceptKrokiListNew = dcAkceptTypKroku.getDcAkceptKrokiList();
            List<String> illegalOrphanMessages = null;
            for (DcAkceptKroki dcAkceptKrokiListOldDcAkceptKroki : dcAkceptKrokiListOld) {
                if (!dcAkceptKrokiListNew.contains(dcAkceptKrokiListOldDcAkceptKroki)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcAkceptKroki " + dcAkceptKrokiListOldDcAkceptKroki + " since its dcAckeptTypKroku field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DcAkceptKroki> attachedDcAkceptKrokiListNew = new ArrayList<DcAkceptKroki>();
            for (DcAkceptKroki dcAkceptKrokiListNewDcAkceptKrokiToAttach : dcAkceptKrokiListNew) {
                dcAkceptKrokiListNewDcAkceptKrokiToAttach = em.getReference(dcAkceptKrokiListNewDcAkceptKrokiToAttach.getClass(), dcAkceptKrokiListNewDcAkceptKrokiToAttach.getId());
                attachedDcAkceptKrokiListNew.add(dcAkceptKrokiListNewDcAkceptKrokiToAttach);
            }
            dcAkceptKrokiListNew = attachedDcAkceptKrokiListNew;
            dcAkceptTypKroku.setDcAkceptKrokiList(dcAkceptKrokiListNew);
            dcAkceptTypKroku = em.merge(dcAkceptTypKroku);
            for (DcAkceptKroki dcAkceptKrokiListNewDcAkceptKroki : dcAkceptKrokiListNew) {
                if (!dcAkceptKrokiListOld.contains(dcAkceptKrokiListNewDcAkceptKroki)) {
                    DcAkceptTypKroku oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki = dcAkceptKrokiListNewDcAkceptKroki.getDcAckeptTypKroku();
                    dcAkceptKrokiListNewDcAkceptKroki.setDcAckeptTypKroku(dcAkceptTypKroku);
                    dcAkceptKrokiListNewDcAkceptKroki = em.merge(dcAkceptKrokiListNewDcAkceptKroki);
                    if (oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki != null && !oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki.equals(dcAkceptTypKroku)) {
                        oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki.getDcAkceptKrokiList().remove(dcAkceptKrokiListNewDcAkceptKroki);
                        oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki = em.merge(oldDcAckeptTypKrokuOfDcAkceptKrokiListNewDcAkceptKroki);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcAkceptTypKroku.getId();
                if (findDcAkceptTypKroku(id) == null) {
                    throw new NonexistentEntityException("The dcAkceptTypKroku with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcAkceptTypKroku dcAkceptTypKroku;
            try {
                dcAkceptTypKroku = em.getReference(DcAkceptTypKroku.class, id);
                dcAkceptTypKroku.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcAkceptTypKroku with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcAkceptKroki> dcAkceptKrokiListOrphanCheck = dcAkceptTypKroku.getDcAkceptKrokiList();
            for (DcAkceptKroki dcAkceptKrokiListOrphanCheckDcAkceptKroki : dcAkceptKrokiListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcAkceptTypKroku (" + dcAkceptTypKroku + ") cannot be destroyed since the DcAkceptKroki " + dcAkceptKrokiListOrphanCheckDcAkceptKroki + " in its dcAkceptKrokiList field has a non-nullable dcAckeptTypKroku field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcAkceptTypKroku);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcAkceptTypKroku> findDcAkceptTypKrokuEntities() {
        return findDcAkceptTypKrokuEntities(true, -1, -1);
    }

    public List<DcAkceptTypKroku> findDcAkceptTypKrokuEntities(int maxResults, int firstResult) {
        return findDcAkceptTypKrokuEntities(false, maxResults, firstResult);
    }

    private List<DcAkceptTypKroku> findDcAkceptTypKrokuEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcAkceptTypKroku.class));
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

    public DcAkceptTypKroku findDcAkceptTypKroku(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcAkceptTypKroku.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcAkceptTypKrokuCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcAkceptTypKroku> rt = cq.from(DcAkceptTypKroku.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
