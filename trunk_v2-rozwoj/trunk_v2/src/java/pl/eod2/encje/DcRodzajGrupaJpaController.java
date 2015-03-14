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
import pl.eod.encje.Uzytkownik;
import pl.eod.encje.UzytkownikJpaController;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class DcRodzajGrupaJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public DcRodzajGrupaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(DcRodzajGrupa dcRodzajGrupa) {
        EntityManager em = null;
        DcRodzajGrupaJpaController uC = new DcRodzajGrupaJpaController();
        if ((uC.findDcRodzajGrupa(dcRodzajGrupa.getNazwa())) != null) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcRodzajGrupa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String edit(DcRodzajGrupa dcRodzajGrupa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        DcRodzajGrupa old=findDcRodzajGrupa(dcRodzajGrupa.getId());
        if (findDcRodzajGrupa(dcRodzajGrupa.getNazwa()) != null && findDcRodzajGrupa(dcRodzajGrupa.getNazwa()).getId()!=old.getId()) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcRodzajGrupa persistentDcRodzajGrupa = em.find(DcRodzajGrupa.class, dcRodzajGrupa.getId());
            List<DcRodzaj> dcRodzajListOld = persistentDcRodzajGrupa.getDcRodzajList();
            List<DcRodzaj> dcRodzajListNew = dcRodzajGrupa.getDcRodzajList();
            List<String> illegalOrphanMessages = null;
            for (DcRodzaj dcRodzajListOldDcRodzaj : dcRodzajListOld) {
                if (!dcRodzajListNew.contains(dcRodzajListOldDcRodzaj)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcRodzaj " + dcRodzajListOldDcRodzaj + " since its idRodzajGrupa field is not nullable.");
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
            dcRodzajGrupa.setDcRodzajList(dcRodzajListNew);
            dcRodzajGrupa = em.merge(dcRodzajGrupa);
            for (DcRodzaj dcRodzajListNewDcRodzaj : dcRodzajListNew) {
                if (!dcRodzajListOld.contains(dcRodzajListNewDcRodzaj)) {
                    DcRodzajGrupa oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj = dcRodzajListNewDcRodzaj.getIdRodzajGrupa();
                    dcRodzajListNewDcRodzaj.setIdRodzajGrupa(dcRodzajGrupa);
                    dcRodzajListNewDcRodzaj = em.merge(dcRodzajListNewDcRodzaj);
                    if (oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj != null && !oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj.equals(dcRodzajGrupa)) {
                        oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj.getDcRodzajList().remove(dcRodzajListNewDcRodzaj);
                        oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj = em.merge(oldIdRodzajGrupaOfDcRodzajListNewDcRodzaj);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcRodzajGrupa.getId();
                if (findDcRodzajGrupa(id) == null) {
                    throw new NonexistentEntityException("The dcRodzajGrupa with id " + id + " no longer exists.");
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
            DcRodzajGrupa dcRodzajGrupa;
            try {
                dcRodzajGrupa = em.getReference(DcRodzajGrupa.class, id);
                dcRodzajGrupa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcRodzajGrupa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcRodzaj> dcRodzajListOrphanCheck = dcRodzajGrupa.getDcRodzajList();
            for (DcRodzaj dcRodzajListOrphanCheckDcRodzaj : dcRodzajListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcRodzajGrupa (" + dcRodzajGrupa + ") cannot be destroyed since the DcRodzaj " + dcRodzajListOrphanCheckDcRodzaj + " in its dcRodzajList field has a non-nullable idRodzajGrupa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dcRodzajGrupa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcRodzajGrupa> findDcRodzajGrupaEntities() {
        return findDcRodzajGrupaEntities(true, -1, -1);
    }

    public List<DcRodzajGrupa> findDcRodzajGrupaEntities(int maxResults, int firstResult) {
        return findDcRodzajGrupaEntities(false, maxResults, firstResult);
    }

    private List<DcRodzajGrupa> findDcRodzajGrupaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcRodzajGrupa.class));
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

    public DcRodzajGrupa findDcRodzajGrupa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcRodzajGrupa.class, id);
        } finally {
            em.close();
        }
    }

    public DcRodzajGrupa findDcRodzajGrupa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcRodzajGrupa.findByNazwa");
            q.setParameter("nazwa", nazwa);
            DcRodzajGrupa u = (DcRodzajGrupa) q.getResultList().get(0);
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

    public int getDcRodzajGrupaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcRodzajGrupa> rt = cq.from(DcRodzajGrupa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
