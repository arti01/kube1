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
public class DcTypFlowJpaController implements Serializable {

    public DcTypFlowJpaController() {
       if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DcTypFlow dcTypFlow) throws PreexistingEntityException, Exception {
        if (dcTypFlow.getDcRodzajList() == null) {
            dcTypFlow.setDcRodzajList(new ArrayList<DcRodzaj>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DcRodzaj> attachedDcRodzajList = new ArrayList<DcRodzaj>();
            for (DcRodzaj dcRodzajListDcRodzajToAttach : dcTypFlow.getDcRodzajList()) {
                dcRodzajListDcRodzajToAttach = em.getReference(dcRodzajListDcRodzajToAttach.getClass(), dcRodzajListDcRodzajToAttach.getId());
                attachedDcRodzajList.add(dcRodzajListDcRodzajToAttach);
            }
            dcTypFlow.setDcRodzajList(attachedDcRodzajList);
            em.persist(dcTypFlow);
            for (DcRodzaj dcRodzajListDcRodzaj : dcTypFlow.getDcRodzajList()) {
                DcTypFlow oldIdTypFlowOfDcRodzajListDcRodzaj = dcRodzajListDcRodzaj.getIdTypFlow();
                dcRodzajListDcRodzaj.setIdTypFlow(dcTypFlow);
                dcRodzajListDcRodzaj = em.merge(dcRodzajListDcRodzaj);
                if (oldIdTypFlowOfDcRodzajListDcRodzaj != null) {
                    oldIdTypFlowOfDcRodzajListDcRodzaj.getDcRodzajList().remove(dcRodzajListDcRodzaj);
                    oldIdTypFlowOfDcRodzajListDcRodzaj = em.merge(oldIdTypFlowOfDcRodzajListDcRodzaj);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDcTypFlow(dcTypFlow.getId()) != null) {
                throw new PreexistingEntityException("DcTypFlow " + dcTypFlow + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DcTypFlow dcTypFlow) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcTypFlow persistentDcTypFlow = em.find(DcTypFlow.class, dcTypFlow.getId());
            List<DcRodzaj> dcRodzajListOld = persistentDcTypFlow.getDcRodzajList();
            List<DcRodzaj> dcRodzajListNew = dcTypFlow.getDcRodzajList();
            List<String> illegalOrphanMessages = null;
            for (DcRodzaj dcRodzajListOldDcRodzaj : dcRodzajListOld) {
                if (!dcRodzajListNew.contains(dcRodzajListOldDcRodzaj)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcRodzaj " + dcRodzajListOldDcRodzaj + " since its idTypFlow field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DcRodzaj> attachedDcRodzajListNew = new ArrayList<DcRodzaj>();
            for (DcRodzaj dcRodzajListNewDcRodzajToAttach : dcRodzajListNew) {
                dcRodzajListNewDcRodzajToAttach = em.getReference(dcRodzajListNewDcRodzajToAttach.getClass(), dcRodzajListNewDcRodzajToAttach.getId());
                attachedDcRodzajListNew.add(dcRodzajListNewDcRodzajToAttach);
            }
            dcRodzajListNew = attachedDcRodzajListNew;
            dcTypFlow.setDcRodzajList(dcRodzajListNew);
            dcTypFlow = em.merge(dcTypFlow);
            for (DcRodzaj dcRodzajListNewDcRodzaj : dcRodzajListNew) {
                if (!dcRodzajListOld.contains(dcRodzajListNewDcRodzaj)) {
                    DcTypFlow oldIdTypFlowOfDcRodzajListNewDcRodzaj = dcRodzajListNewDcRodzaj.getIdTypFlow();
                    dcRodzajListNewDcRodzaj.setIdTypFlow(dcTypFlow);
                    dcRodzajListNewDcRodzaj = em.merge(dcRodzajListNewDcRodzaj);
                    if (oldIdTypFlowOfDcRodzajListNewDcRodzaj != null && !oldIdTypFlowOfDcRodzajListNewDcRodzaj.equals(dcTypFlow)) {
                        oldIdTypFlowOfDcRodzajListNewDcRodzaj.getDcRodzajList().remove(dcRodzajListNewDcRodzaj);
                        oldIdTypFlowOfDcRodzajListNewDcRodzaj = em.merge(oldIdTypFlowOfDcRodzajListNewDcRodzaj);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcTypFlow.getId();
                if (findDcTypFlow(id) == null) {
                    throw new NonexistentEntityException("The dcTypFlow with id " + id + " no longer exists.");
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
            DcTypFlow dcTypFlow;
            try {
                dcTypFlow = em.getReference(DcTypFlow.class, id);
                dcTypFlow.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcTypFlow with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcRodzaj> dcRodzajListOrphanCheck = dcTypFlow.getDcRodzajList();
            for (DcRodzaj dcRodzajListOrphanCheckDcRodzaj : dcRodzajListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcTypFlow (" + dcTypFlow + ") cannot be destroyed since the DcRodzaj " + dcRodzajListOrphanCheckDcRodzaj + " in its dcRodzajList field has a non-nullable idTypFlow field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcTypFlow);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcTypFlow> findDcTypFlowEntities() {
        return findDcTypFlowEntities(true, -1, -1);
    }

    public List<DcTypFlow> findDcTypFlowEntities(int maxResults, int firstResult) {
        return findDcTypFlowEntities(false, maxResults, firstResult);
    }

    private List<DcTypFlow> findDcTypFlowEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcTypFlow.class));
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

    public DcTypFlow findDcTypFlow(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcTypFlow.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcTypFlowCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcTypFlow> rt = cq.from(DcTypFlow.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
