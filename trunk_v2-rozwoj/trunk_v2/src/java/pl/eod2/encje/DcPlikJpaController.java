/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class DcPlikJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public DcPlikJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DcPlik dcPlik) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcPlik);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DcPlik dcPlik) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcPlik persistentDcPlik = em.find(DcPlik.class, dcPlik.getId());
            DcDokument idDokOld = persistentDcPlik.getIdDok();
            DcDokument idDokNew = dcPlik.getIdDok();
            if (idDokNew != null) {
                idDokNew = em.getReference(idDokNew.getClass(), idDokNew.getId());
                dcPlik.setIdDok(idDokNew);
            }
            dcPlik = em.merge(dcPlik);
            if (idDokOld != null && !idDokOld.equals(idDokNew)) {
                idDokOld.getDcPlikList().remove(dcPlik);
                idDokOld = em.merge(idDokOld);
            }
            if (idDokNew != null && !idDokNew.equals(idDokOld)) {
                idDokNew.getDcPlikList().add(dcPlik);
                idDokNew = em.merge(idDokNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcPlik.getId();
                if (findDcPlik(id) == null) {
                    throw new NonexistentEntityException("The dcPlik with id " + id + " no longer exists.");
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
            DcPlik dcPlik;
            try {
                dcPlik = em.getReference(DcPlik.class, id);
                dcPlik.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcPlik with id " + id + " no longer exists.", enfe);
            }
            DcDokument idDok = dcPlik.getIdDok();
            if (idDok != null) {
                idDok.getDcPlikList().remove(dcPlik);
                idDok = em.merge(idDok);
            }
            em.remove(dcPlik);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcPlik> findDcPlikEntities() {
        return findDcPlikEntities(true, -1, -1);
    }

    public List<DcPlik> findDcPlikEntities(int maxResults, int firstResult) {
        return findDcPlikEntities(false, maxResults, firstResult);
    }

    private List<DcPlik> findDcPlikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcPlik.class));
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

    public DcPlik findDcPlik(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcPlik.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcPlikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcPlik> rt = cq.from(DcPlik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
