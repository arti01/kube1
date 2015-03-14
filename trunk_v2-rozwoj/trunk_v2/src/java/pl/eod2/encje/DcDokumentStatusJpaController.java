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
import java.util.HashSet;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import pl.eod.abstr.AbstKontroler;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.encje.exceptions.PreexistingEntityException;

@ManagedBean(name = "DcDokumentStatusJpaController")
@SessionScoped
public class DcDokumentStatusJpaController extends AbstKontroler<DcDokumentStatus> implements Serializable {
    private static final long serialVersionUID = 1L;

    public DcDokumentStatusJpaController() {
        super(new DcDokumentStatus());
    }

    public void createSpec(DcDokumentStatus dcDokumentStatus) throws PreexistingEntityException, Exception {
        if (dcDokumentStatus.getDcDokumentList() == null) {
            dcDokumentStatus.setDcDokumentList(new ArrayList<DcDokument>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DcDokument> attachedDcDokumentList = new ArrayList<DcDokument>();
            for (DcDokument dcDokumentListDcDokumentToAttach : dcDokumentStatus.getDcDokumentList()) {
                dcDokumentListDcDokumentToAttach = em.getReference(dcDokumentListDcDokumentToAttach.getClass(), dcDokumentListDcDokumentToAttach.getId());
                attachedDcDokumentList.add(dcDokumentListDcDokumentToAttach);
            }
            dcDokumentStatus.setDcDokumentList(attachedDcDokumentList);
            em.persist(dcDokumentStatus);
            for (DcDokument dcDokumentListDcDokument : dcDokumentStatus.getDcDokumentList()) {
                DcDokumentStatus oldDokStatusIdOfDcDokumentListDcDokument = dcDokumentListDcDokument.getDokStatusId();
                dcDokumentListDcDokument.setDokStatusId(dcDokumentStatus);
                dcDokumentListDcDokument = em.merge(dcDokumentListDcDokument);
                if (oldDokStatusIdOfDcDokumentListDcDokument != null) {
                    oldDokStatusIdOfDcDokumentListDcDokument.getDcDokumentList().remove(dcDokumentListDcDokument);
                    oldDokStatusIdOfDcDokumentListDcDokument = em.merge(oldDokStatusIdOfDcDokumentListDcDokument);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDcDokumentStatus(dcDokumentStatus.getId()) != null) {
                throw new PreexistingEntityException("DcDokumentStatus " + dcDokumentStatus + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editSpec(DcDokumentStatus dcDokumentStatus) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcDokumentStatus persistentDcDokumentStatus = em.find(DcDokumentStatus.class, dcDokumentStatus.getId());
            List<DcDokument> dcDokumentListOld = persistentDcDokumentStatus.getDcDokumentList();
            List<DcDokument> dcDokumentListNew = dcDokumentStatus.getDcDokumentList();
            List<String> illegalOrphanMessages = null;
            for (DcDokument dcDokumentListOldDcDokument : dcDokumentListOld) {
                if (!dcDokumentListNew.contains(dcDokumentListOldDcDokument)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcDokument " + dcDokumentListOldDcDokument + " since its dokStatusId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DcDokument> attachedDcDokumentListNew = new ArrayList<DcDokument>();
            for (DcDokument dcDokumentListNewDcDokumentToAttach : dcDokumentListNew) {
                dcDokumentListNewDcDokumentToAttach = em.getReference(dcDokumentListNewDcDokumentToAttach.getClass(), dcDokumentListNewDcDokumentToAttach.getId());
                attachedDcDokumentListNew.add(dcDokumentListNewDcDokumentToAttach);
            }
            dcDokumentListNew = attachedDcDokumentListNew;
            dcDokumentStatus.setDcDokumentList(dcDokumentListNew);
            dcDokumentStatus = em.merge(dcDokumentStatus);
            for (DcDokument dcDokumentListNewDcDokument : dcDokumentListNew) {
                if (!dcDokumentListOld.contains(dcDokumentListNewDcDokument)) {
                    DcDokumentStatus oldDokStatusIdOfDcDokumentListNewDcDokument = dcDokumentListNewDcDokument.getDokStatusId();
                    dcDokumentListNewDcDokument.setDokStatusId(dcDokumentStatus);
                    dcDokumentListNewDcDokument = em.merge(dcDokumentListNewDcDokument);
                    if (oldDokStatusIdOfDcDokumentListNewDcDokument != null && !oldDokStatusIdOfDcDokumentListNewDcDokument.equals(dcDokumentStatus)) {
                        oldDokStatusIdOfDcDokumentListNewDcDokument.getDcDokumentList().remove(dcDokumentListNewDcDokument);
                        oldDokStatusIdOfDcDokumentListNewDcDokument = em.merge(oldDokStatusIdOfDcDokumentListNewDcDokument);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcDokumentStatus.getId();
                if (findDcDokumentStatus(id) == null) {
                    throw new NonexistentEntityException("The dcDokumentStatus with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroySpec(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcDokumentStatus dcDokumentStatus;
            try {
                dcDokumentStatus = em.getReference(DcDokumentStatus.class, id);
                dcDokumentStatus.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcDokumentStatus with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcDokument> dcDokumentListOrphanCheck = dcDokumentStatus.getDcDokumentList();
            for (DcDokument dcDokumentListOrphanCheckDcDokument : dcDokumentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcDokumentStatus (" + dcDokumentStatus + ") cannot be destroyed since the DcDokument " + dcDokumentListOrphanCheckDcDokument + " in its dcDokumentList field has a non-nullable dokStatusId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcDokumentStatus);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcDokumentStatus> findDcDokumentStatusEntities() {
        return findDcDokumentStatusEntities(true, -1, -1);
    }

    public List<DcDokumentStatus> findDcDokumentStatusEntities(int maxResults, int firstResult) {
        return findDcDokumentStatusEntities(false, maxResults, firstResult);
    }

    private List<DcDokumentStatus> findDcDokumentStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcDokumentStatus.class));
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

    public DcDokumentStatus findDcDokumentStatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcDokumentStatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcDokumentStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcDokumentStatus> rt = cq.from(DcDokumentStatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public List<DcDokumentStatus> getFindDlaArchiwum() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcDokumentStatus.findByTabela");
            q.setParameter("tabela", "archiwum");
            return q.getResultList();
            //return hs;
        } catch (NoResultException ex) {
            //ex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            return null;
        } finally {
            em.close();
        }
    }
    
}
