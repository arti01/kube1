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
 * @author arti01
 */
public class DcAkceptStatusJpaController implements Serializable {

    public DcAkceptStatusJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DcAkceptStatus dcAkceptStatus) throws PreexistingEntityException, Exception {
        if (dcAkceptStatus.getDokKrokUserList() == null) {
            dcAkceptStatus.setDokKrokUserList(new ArrayList<DcDokumentKrokUzytkownik>());
        }
        if (dcAkceptStatus.getDokKrokList() == null) {
            dcAkceptStatus.setDokKrokList(new ArrayList<DcDokumentKrok>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DcDokumentKrokUzytkownik> attachedDokKrokUserList = new ArrayList<DcDokumentKrokUzytkownik>();
            for (DcDokumentKrokUzytkownik dokKrokUserListDcDokumentKrokUzytkownikToAttach : dcAkceptStatus.getDokKrokUserList()) {
                dokKrokUserListDcDokumentKrokUzytkownikToAttach = em.getReference(dokKrokUserListDcDokumentKrokUzytkownikToAttach.getClass(), dokKrokUserListDcDokumentKrokUzytkownikToAttach.getId());
                attachedDokKrokUserList.add(dokKrokUserListDcDokumentKrokUzytkownikToAttach);
            }
            dcAkceptStatus.setDokKrokUserList(attachedDokKrokUserList);
            List<DcDokumentKrok> attachedDokKrokList = new ArrayList<DcDokumentKrok>();
            for (DcDokumentKrok dokKrokListDcDokumentKrokToAttach : dcAkceptStatus.getDokKrokList()) {
                dokKrokListDcDokumentKrokToAttach = em.getReference(dokKrokListDcDokumentKrokToAttach.getClass(), dokKrokListDcDokumentKrokToAttach.getId());
                attachedDokKrokList.add(dokKrokListDcDokumentKrokToAttach);
            }
            dcAkceptStatus.setDokKrokList(attachedDokKrokList);
            em.persist(dcAkceptStatus);
            for (DcDokumentKrokUzytkownik dokKrokUserListDcDokumentKrokUzytkownik : dcAkceptStatus.getDokKrokUserList()) {
                DcAkceptStatus oldAkceptOfDokKrokUserListDcDokumentKrokUzytkownik = dokKrokUserListDcDokumentKrokUzytkownik.getAkcept();
                dokKrokUserListDcDokumentKrokUzytkownik.setAkcept(dcAkceptStatus);
                dokKrokUserListDcDokumentKrokUzytkownik = em.merge(dokKrokUserListDcDokumentKrokUzytkownik);
                if (oldAkceptOfDokKrokUserListDcDokumentKrokUzytkownik != null) {
                    oldAkceptOfDokKrokUserListDcDokumentKrokUzytkownik.getDokKrokUserList().remove(dokKrokUserListDcDokumentKrokUzytkownik);
                    oldAkceptOfDokKrokUserListDcDokumentKrokUzytkownik = em.merge(oldAkceptOfDokKrokUserListDcDokumentKrokUzytkownik);
                }
            }
            for (DcDokumentKrok dokKrokListDcDokumentKrok : dcAkceptStatus.getDokKrokList()) {
                DcAkceptStatus oldAkceptOfDokKrokListDcDokumentKrok = dokKrokListDcDokumentKrok.getAkcept();
                dokKrokListDcDokumentKrok.setAkcept(dcAkceptStatus);
                dokKrokListDcDokumentKrok = em.merge(dokKrokListDcDokumentKrok);
                if (oldAkceptOfDokKrokListDcDokumentKrok != null) {
                    oldAkceptOfDokKrokListDcDokumentKrok.getDokKrokList().remove(dokKrokListDcDokumentKrok);
                    oldAkceptOfDokKrokListDcDokumentKrok = em.merge(oldAkceptOfDokKrokListDcDokumentKrok);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDcAkceptStatus(dcAkceptStatus.getId()) != null) {
                throw new PreexistingEntityException("DcAkceptStatus " + dcAkceptStatus + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DcAkceptStatus dcAkceptStatus) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcAkceptStatus persistentDcAkceptStatus = em.find(DcAkceptStatus.class, dcAkceptStatus.getId());
            List<DcDokumentKrokUzytkownik> dokKrokUserListOld = persistentDcAkceptStatus.getDokKrokUserList();
            List<DcDokumentKrokUzytkownik> dokKrokUserListNew = dcAkceptStatus.getDokKrokUserList();
            List<DcDokumentKrok> dokKrokListOld = persistentDcAkceptStatus.getDokKrokList();
            List<DcDokumentKrok> dokKrokListNew = dcAkceptStatus.getDokKrokList();
            List<String> illegalOrphanMessages = null;
            for (DcDokumentKrokUzytkownik dokKrokUserListOldDcDokumentKrokUzytkownik : dokKrokUserListOld) {
                if (!dokKrokUserListNew.contains(dokKrokUserListOldDcDokumentKrokUzytkownik)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcDokumentKrokUzytkownik " + dokKrokUserListOldDcDokumentKrokUzytkownik + " since its akcept field is not nullable.");
                }
            }
            for (DcDokumentKrok dokKrokListOldDcDokumentKrok : dokKrokListOld) {
                if (!dokKrokListNew.contains(dokKrokListOldDcDokumentKrok)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcDokumentKrok " + dokKrokListOldDcDokumentKrok + " since its akcept field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<DcDokumentKrokUzytkownik> attachedDokKrokUserListNew = new ArrayList<DcDokumentKrokUzytkownik>();
            for (DcDokumentKrokUzytkownik dokKrokUserListNewDcDokumentKrokUzytkownikToAttach : dokKrokUserListNew) {
                dokKrokUserListNewDcDokumentKrokUzytkownikToAttach = em.getReference(dokKrokUserListNewDcDokumentKrokUzytkownikToAttach.getClass(), dokKrokUserListNewDcDokumentKrokUzytkownikToAttach.getId());
                attachedDokKrokUserListNew.add(dokKrokUserListNewDcDokumentKrokUzytkownikToAttach);
            }
            dokKrokUserListNew = attachedDokKrokUserListNew;
            dcAkceptStatus.setDokKrokUserList(dokKrokUserListNew);
            List<DcDokumentKrok> attachedDokKrokListNew = new ArrayList<DcDokumentKrok>();
            for (DcDokumentKrok dokKrokListNewDcDokumentKrokToAttach : dokKrokListNew) {
                dokKrokListNewDcDokumentKrokToAttach = em.getReference(dokKrokListNewDcDokumentKrokToAttach.getClass(), dokKrokListNewDcDokumentKrokToAttach.getId());
                attachedDokKrokListNew.add(dokKrokListNewDcDokumentKrokToAttach);
            }
            dokKrokListNew = attachedDokKrokListNew;
            dcAkceptStatus.setDokKrokList(dokKrokListNew);
            dcAkceptStatus = em.merge(dcAkceptStatus);
            for (DcDokumentKrokUzytkownik dokKrokUserListNewDcDokumentKrokUzytkownik : dokKrokUserListNew) {
                if (!dokKrokUserListOld.contains(dokKrokUserListNewDcDokumentKrokUzytkownik)) {
                    DcAkceptStatus oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik = dokKrokUserListNewDcDokumentKrokUzytkownik.getAkcept();
                    dokKrokUserListNewDcDokumentKrokUzytkownik.setAkcept(dcAkceptStatus);
                    dokKrokUserListNewDcDokumentKrokUzytkownik = em.merge(dokKrokUserListNewDcDokumentKrokUzytkownik);
                    if (oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik != null && !oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik.equals(dcAkceptStatus)) {
                        oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik.getDokKrokUserList().remove(dokKrokUserListNewDcDokumentKrokUzytkownik);
                        oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik = em.merge(oldAkceptOfDokKrokUserListNewDcDokumentKrokUzytkownik);
                    }
                }
            }
            for (DcDokumentKrok dokKrokListNewDcDokumentKrok : dokKrokListNew) {
                if (!dokKrokListOld.contains(dokKrokListNewDcDokumentKrok)) {
                    DcAkceptStatus oldAkceptOfDokKrokListNewDcDokumentKrok = dokKrokListNewDcDokumentKrok.getAkcept();
                    dokKrokListNewDcDokumentKrok.setAkcept(dcAkceptStatus);
                    dokKrokListNewDcDokumentKrok = em.merge(dokKrokListNewDcDokumentKrok);
                    if (oldAkceptOfDokKrokListNewDcDokumentKrok != null && !oldAkceptOfDokKrokListNewDcDokumentKrok.equals(dcAkceptStatus)) {
                        oldAkceptOfDokKrokListNewDcDokumentKrok.getDokKrokList().remove(dokKrokListNewDcDokumentKrok);
                        oldAkceptOfDokKrokListNewDcDokumentKrok = em.merge(oldAkceptOfDokKrokListNewDcDokumentKrok);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcAkceptStatus.getId();
                if (findDcAkceptStatus(id) == null) {
                    throw new NonexistentEntityException("The dcAkceptStatus with id " + id + " no longer exists.");
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
            DcAkceptStatus dcAkceptStatus;
            try {
                dcAkceptStatus = em.getReference(DcAkceptStatus.class, id);
                dcAkceptStatus.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcAkceptStatus with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcDokumentKrokUzytkownik> dokKrokUserListOrphanCheck = dcAkceptStatus.getDokKrokUserList();
            for (DcDokumentKrokUzytkownik dokKrokUserListOrphanCheckDcDokumentKrokUzytkownik : dokKrokUserListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcAkceptStatus (" + dcAkceptStatus + ") cannot be destroyed since the DcDokumentKrokUzytkownik " + dokKrokUserListOrphanCheckDcDokumentKrokUzytkownik + " in its dokKrokUserList field has a non-nullable akcept field.");
            }
            List<DcDokumentKrok> dokKrokListOrphanCheck = dcAkceptStatus.getDokKrokList();
            for (DcDokumentKrok dokKrokListOrphanCheckDcDokumentKrok : dokKrokListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcAkceptStatus (" + dcAkceptStatus + ") cannot be destroyed since the DcDokumentKrok " + dokKrokListOrphanCheckDcDokumentKrok + " in its dokKrokList field has a non-nullable akcept field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcAkceptStatus);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcAkceptStatus> findDcAkceptStatusEntities() {
        return findDcAkceptStatusEntities(true, -1, -1);
    }

    public List<DcAkceptStatus> findDcAkceptStatusEntities(int maxResults, int firstResult) {
        return findDcAkceptStatusEntities(false, maxResults, firstResult);
    }

    private List<DcAkceptStatus> findDcAkceptStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcAkceptStatus.class));
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

    public DcAkceptStatus findDcAkceptStatus(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcAkceptStatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcAkceptStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcAkceptStatus> rt = cq.from(DcAkceptStatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
