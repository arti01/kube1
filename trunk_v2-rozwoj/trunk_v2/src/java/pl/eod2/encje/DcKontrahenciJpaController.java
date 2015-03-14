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
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class DcKontrahenciJpaController implements Serializable {
    private static final long serialVersionUID = 1L;

    public DcKontrahenciJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(DcKontrahenci dcKontrahenci) {
       EntityManager em = null;
        if ((findDcKontrahenci(dcKontrahenci.getNazwa())) != null) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcKontrahenci);
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

    public String edit(DcKontrahenci dcKontrahenci) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        DcKontrahenci old=findDcKontrahenci(dcKontrahenci.getId());
        if (findDcKontrahenci(dcKontrahenci.getNazwa()) != null && findDcKontrahenci(dcKontrahenci.getNazwa()).getId()!=old.getId()) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dcKontrahenci = em.merge(dcKontrahenci);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcKontrahenci.getId();
                if (findDcKontrahenci(id) == null) {
                    throw new NonexistentEntityException("The dcKontrahenci with id " + id + " no longer exists.");
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

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcKontrahenci dcKontrahenci;
            try {
                dcKontrahenci = em.getReference(DcKontrahenci.class, id);
                dcKontrahenci.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcKontrahenci with id " + id + " no longer exists.", enfe);
            }
            em.remove(dcKontrahenci);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcKontrahenci> findEntities() {
        return findDcKontrahenciEntities(true, -1, -1);
    }

    public List<DcKontrahenci> findDcKontrahenciEntities(int maxResults, int firstResult) {
        return findDcKontrahenciEntities(false, maxResults, firstResult);
    }

    private List<DcKontrahenci> findDcKontrahenciEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcKontrahenci.class));
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

    public DcKontrahenci findDcKontrahenci(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcKontrahenci.findByNazwa");
            q.setParameter("nazwa", nazwa);
            DcKontrahenci u = (DcKontrahenci) q.getResultList().get(0);
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
    
    public DcKontrahenci findDcKontrahenci(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcKontrahenci.class, id);
        } finally {
            em.close();
        }
    }
    
    public DcKontrahenci findConfigNazwa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb=em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DcKontrahenci> cfg = cq.from(DcKontrahenci.class);
            cq.select(cfg);
            cq.where(cb.equal(cfg.get(DcKontrahenci_.nazwa), nazwa));
            Query q = em.createQuery(cq);
            return (DcKontrahenci)q.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getDcKontrahenciCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcKontrahenci> rt = cq.from(DcKontrahenci.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
