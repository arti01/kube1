/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import pl.eod.encje.exceptions.NonexistentEntityException;
import pl.eod.encje.exceptions.PreexistingEntityException;

/**
 *
 * @author arti01
 */
public class DzialJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public DzialJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dzial dzial) throws PreexistingEntityException, Exception {
        if (dzial.getStrukturaList() == null) {
            dzial.setStrukturaList(new ArrayList<Struktura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Struktura> attachedStrukturaList = new ArrayList<Struktura>();
            for (Struktura strukturaListStrukturaToAttach : dzial.getStrukturaList()) {
                strukturaListStrukturaToAttach = em.getReference(strukturaListStrukturaToAttach.getClass(), strukturaListStrukturaToAttach.getId());
                attachedStrukturaList.add(strukturaListStrukturaToAttach);
            }
            dzial.setStrukturaList(attachedStrukturaList);
            em.persist(dzial);
            for (Struktura strukturaListStruktura : dzial.getStrukturaList()) {
                Dzial oldDzialIdOfStrukturaListStruktura = strukturaListStruktura.getDzialId();
                strukturaListStruktura.setDzialId(dzial);
                strukturaListStruktura = em.merge(strukturaListStruktura);
                if (oldDzialIdOfStrukturaListStruktura != null) {
                    oldDzialIdOfStrukturaListStruktura.getStrukturaList().remove(strukturaListStruktura);
                    oldDzialIdOfStrukturaListStruktura = em.merge(oldDzialIdOfStrukturaListStruktura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDzial(dzial.getId()) != null) {
                throw new PreexistingEntityException("Dzial " + dzial + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dzial dzial) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dzial persistentDzial = em.find(Dzial.class, dzial.getId());
            List<Struktura> strukturaListOld = persistentDzial.getStrukturaList();
            List<Struktura> strukturaListNew = dzial.getStrukturaList();
            List<Struktura> attachedStrukturaListNew = new ArrayList<Struktura>();
            for (Struktura strukturaListNewStrukturaToAttach : strukturaListNew) {
                strukturaListNewStrukturaToAttach = em.getReference(strukturaListNewStrukturaToAttach.getClass(), strukturaListNewStrukturaToAttach.getId());
                attachedStrukturaListNew.add(strukturaListNewStrukturaToAttach);
            }
            strukturaListNew = attachedStrukturaListNew;
            dzial.setStrukturaList(strukturaListNew);
            dzial = em.merge(dzial);
            for (Struktura strukturaListOldStruktura : strukturaListOld) {
                if (!strukturaListNew.contains(strukturaListOldStruktura)) {
                    strukturaListOldStruktura.setDzialId(null);
                    strukturaListOldStruktura = em.merge(strukturaListOldStruktura);
                }
            }
            for (Struktura strukturaListNewStruktura : strukturaListNew) {
                if (!strukturaListOld.contains(strukturaListNewStruktura)) {
                    Dzial oldDzialIdOfStrukturaListNewStruktura = strukturaListNewStruktura.getDzialId();
                    strukturaListNewStruktura.setDzialId(dzial);
                    strukturaListNewStruktura = em.merge(strukturaListNewStruktura);
                    if (oldDzialIdOfStrukturaListNewStruktura != null && !oldDzialIdOfStrukturaListNewStruktura.equals(dzial)) {
                        oldDzialIdOfStrukturaListNewStruktura.getStrukturaList().remove(strukturaListNewStruktura);
                        oldDzialIdOfStrukturaListNewStruktura = em.merge(oldDzialIdOfStrukturaListNewStruktura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = dzial.getId();
                if (findDzial(id) == null) {
                    throw new NonexistentEntityException("The dzial with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Dzial dzial) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            em.remove(em.merge(dzial));
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dzial> getFindDzialEntities() {
        return findDzialEntities(true, -1, -1);
    }

    public List<Dzial> findDzialEntities(Spolki spolka) {
         EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery();
            Root<Dzial> dzial = cq.from(Dzial.class);
            Join<Dzial, Struktura> struktura=dzial.join(Dzial_.strukturaList);
            Join<Struktura, Uzytkownik> userzy=struktura.join(Struktura_.userId);
            cq.select(dzial);
            Predicate nadrz = cb.and(cb.equal(userzy.get(Uzytkownik_.spolkaId), spolka),
                    cb.isNotNull(userzy.get(Uzytkownik_.spolkaId)));
            //dla adminow
            if(spolka!=null) cq.where(nadrz);
            cq.distinct(true);
            Query q = em.createQuery(cq);
            //System.err.println(q.getResultList());
            return (List<Dzial>) q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Dzial> findDzialEntities() {
        return findDzialEntities(true, -1, -1);
    }

    public List<Dzial> findDzialEntities(int maxResults, int firstResult) {
        return findDzialEntities(false, maxResults, firstResult);
    }

    private List<Dzial> findDzialEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Dzial> dzial = cq.from(Dzial.class);
            cq.select(dzial);
            cq.orderBy(cb.asc(dzial.get(Dzial_.nazwa)));
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

    public Dzial findDzial(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dzial.class, id);
        } finally {
            em.close();
        }
    }

    public int getDzialCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dzial> rt = cq.from(Dzial.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Dzial findDzialByNazwa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Dzial.findByNazwa");
            q.setParameter("nazwa", nazwa);
            Dzial u = (Dzial) q.getSingleResult();
            em.refresh(u);
            return u;
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
}
