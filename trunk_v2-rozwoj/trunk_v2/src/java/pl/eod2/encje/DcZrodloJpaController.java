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
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author 103039
 */
public class DcZrodloJpaController implements Serializable {
    private static final long serialVersionUID = 1L;

    public DcZrodloJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(DcZrodlo dcZrodlo) {
        EntityManager em = null;
        if ((findDcZrodlo(dcZrodlo.getNazwa())) != null) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcZrodlo);
            em.getTransaction().commit();
        }catch(Exception ex){
            return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
        }

    public String edit(DcZrodlo dcZrodlo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        DcZrodlo old=findDcZrodlo(dcZrodlo.getId());
        if (findDcZrodlo(dcZrodlo.getNazwa()) != null && findDcZrodlo(dcZrodlo.getNazwa()).getId()!=old.getId()) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcZrodlo persistentDcZrodlo = em.find(DcZrodlo.class, dcZrodlo.getId());
            List<DcDokument> dcDokumentListOld = persistentDcZrodlo.getDcDokumentList();
            List<DcDokument> dcDokumentListNew = dcZrodlo.getDcDokumentList();
            List<String> illegalOrphanMessages = null;
            for (DcDokument dcDokumentListOldDcDokument : dcDokumentListOld) {
                if (!dcDokumentListNew.contains(dcDokumentListOldDcDokument)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcDokument " + dcDokumentListOldDcDokument + " since its zrodloId field is not nullable.");
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
            dcZrodlo.setDcDokumentList(dcDokumentListNew);
            dcZrodlo = em.merge(dcZrodlo);
            for (DcDokument dcDokumentListNewDcDokument : dcDokumentListNew) {
                if (!dcDokumentListOld.contains(dcDokumentListNewDcDokument)) {
                    DcZrodlo oldZrodloIdOfDcDokumentListNewDcDokument = dcDokumentListNewDcDokument.getZrodloId();
                    dcDokumentListNewDcDokument.setZrodloId(dcZrodlo);
                    dcDokumentListNewDcDokument = em.merge(dcDokumentListNewDcDokument);
                    if (oldZrodloIdOfDcDokumentListNewDcDokument != null && !oldZrodloIdOfDcDokumentListNewDcDokument.equals(dcZrodlo)) {
                        oldZrodloIdOfDcDokumentListNewDcDokument.getDcDokumentList().remove(dcDokumentListNewDcDokument);
                        oldZrodloIdOfDcDokumentListNewDcDokument = em.merge(oldZrodloIdOfDcDokumentListNewDcDokument);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcZrodlo.getId();
                if (findDcZrodlo(id) == null) {
                    throw new NonexistentEntityException("The dcZrodlo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcZrodlo dcZrodlo;
            try {
                dcZrodlo = em.getReference(DcZrodlo.class, id);
                dcZrodlo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcZrodlo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcDokument> dcDokumentListOrphanCheck = dcZrodlo.getDcDokumentList();
            for (DcDokument dcDokumentListOrphanCheckDcDokument : dcDokumentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcZrodlo (" + dcZrodlo + ") cannot be destroyed since the DcDokument " + dcDokumentListOrphanCheckDcDokument + " in its dcDokumentList field has a non-nullable zrodloId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcZrodlo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcZrodlo> findDcZrodloEntities() {
        return findDcZrodloEntities(true, -1, -1);
    }

    public List<DcZrodlo> findDcZrodloEntities(int maxResults, int firstResult) {
        return findDcZrodloEntities(false, maxResults, firstResult);
    }

    private List<DcZrodlo> findDcZrodloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcZrodlo.class));
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

    public DcZrodlo findDcZrodlo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcZrodlo.class, id);
        } finally {
            em.close();
        }
    }

    public DcZrodlo findDcZrodlo(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcZrodlo.findByNazwa");
            q.setParameter("nazwa", nazwa);
            DcZrodlo u = (DcZrodlo) q.getResultList().get(0);
            //em.refresh(u.getStruktura());
            return u;
        } catch (NoResultException ex) {
            //ex.printStackTrace();
            return null;
        } catch (ArrayIndexOutOfBoundsException exb) {
            //ex.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
    
    public int getDcZrodloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcZrodlo> rt = cq.from(DcZrodlo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
