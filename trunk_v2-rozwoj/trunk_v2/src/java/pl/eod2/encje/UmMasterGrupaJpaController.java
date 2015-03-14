/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class UmMasterGrupaJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmMasterGrupaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(UmMasterGrupa umMasterGrupa) {
        if (findUmMasterGrupa(umMasterGrupa.getNazwa()) != null) {
            return "nazwa już istnieje";
        }

        if (umMasterGrupa.getGrupaList() == null) {
            umMasterGrupa.setGrupaList(new ArrayList<UmGrupa>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UmGrupa> attachedGrupaList = new ArrayList<UmGrupa>();
            for (UmGrupa grupaListUmGrupaToAttach : umMasterGrupa.getGrupaList()) {
                grupaListUmGrupaToAttach = em.getReference(grupaListUmGrupaToAttach.getClass(), grupaListUmGrupaToAttach.getId());
                attachedGrupaList.add(grupaListUmGrupaToAttach);
            }
            umMasterGrupa.setGrupaList(attachedGrupaList);
            em.persist(umMasterGrupa);
            for (UmGrupa grupaListUmGrupa : umMasterGrupa.getGrupaList()) {
                UmMasterGrupa oldMasterGrpOfGrupaListUmGrupa = grupaListUmGrupa.getMasterGrp();
                grupaListUmGrupa.setMasterGrp(umMasterGrupa);
                grupaListUmGrupa = em.merge(grupaListUmGrupa);
                if (oldMasterGrpOfGrupaListUmGrupa != null) {
                    oldMasterGrpOfGrupaListUmGrupa.getGrupaList().remove(grupaListUmGrupa);
                    oldMasterGrpOfGrupaListUmGrupa = em.merge(oldMasterGrpOfGrupaListUmGrupa);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String edit(UmMasterGrupa umMasterGrupa) throws NonexistentEntityException, Exception {
        UmMasterGrupa old = findUmMasterGrupa(umMasterGrupa.getId());
        if (findUmMasterGrupa(umMasterGrupa.getNazwa()) != null && !findUmMasterGrupa(umMasterGrupa.getNazwa()).getId().equals(umMasterGrupa.getId())) {
            return "nazwa już istnieje";
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UmMasterGrupa persistentUmMasterGrupa = em.find(UmMasterGrupa.class, umMasterGrupa.getId());
            List<UmGrupa> grupaListOld = persistentUmMasterGrupa.getGrupaList();
            List<UmGrupa> grupaListNew = umMasterGrupa.getGrupaList();
            List<UmGrupa> attachedGrupaListNew = new ArrayList<UmGrupa>();
            for (UmGrupa grupaListNewUmGrupaToAttach : grupaListNew) {
                grupaListNewUmGrupaToAttach = em.getReference(grupaListNewUmGrupaToAttach.getClass(), grupaListNewUmGrupaToAttach.getId());
                attachedGrupaListNew.add(grupaListNewUmGrupaToAttach);
            }
            grupaListNew = attachedGrupaListNew;
            umMasterGrupa.setGrupaList(grupaListNew);
            umMasterGrupa = em.merge(umMasterGrupa);
            for (UmGrupa grupaListOldUmGrupa : grupaListOld) {
                if (!grupaListNew.contains(grupaListOldUmGrupa)) {
                    grupaListOldUmGrupa.setMasterGrp(null);
                    grupaListOldUmGrupa = em.merge(grupaListOldUmGrupa);
                }
            }
            for (UmGrupa grupaListNewUmGrupa : grupaListNew) {
                if (!grupaListOld.contains(grupaListNewUmGrupa)) {
                    UmMasterGrupa oldMasterGrpOfGrupaListNewUmGrupa = grupaListNewUmGrupa.getMasterGrp();
                    grupaListNewUmGrupa.setMasterGrp(umMasterGrupa);
                    grupaListNewUmGrupa = em.merge(grupaListNewUmGrupa);
                    if (oldMasterGrpOfGrupaListNewUmGrupa != null && !oldMasterGrpOfGrupaListNewUmGrupa.equals(umMasterGrupa)) {
                        oldMasterGrpOfGrupaListNewUmGrupa.getGrupaList().remove(grupaListNewUmGrupa);
                        oldMasterGrpOfGrupaListNewUmGrupa = em.merge(oldMasterGrpOfGrupaListNewUmGrupa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = umMasterGrupa.getId();
                if (findUmMasterGrupa(id) == null) {
                    throw new NonexistentEntityException("The umMasterGrupa with id " + id + " no longer exists.");
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UmMasterGrupa umMasterGrupa;
            try {
                umMasterGrupa = em.getReference(UmMasterGrupa.class, id);
                umMasterGrupa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The umMasterGrupa with id " + id + " no longer exists.", enfe);
            }
            List<UmGrupa> grupaList = umMasterGrupa.getGrupaList();
            for (UmGrupa grupaListUmGrupa : grupaList) {
                grupaListUmGrupa.setMasterGrp(null);
                grupaListUmGrupa = em.merge(grupaListUmGrupa);
            }
            em.remove(umMasterGrupa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UmMasterGrupa> findUmMasterGrupaEntities() {
        return findUmMasterGrupaEntities(true, -1, -1);
    }

    public List<UmMasterGrupa> findUmMasterGrupaEntities(int maxResults, int firstResult) {
        return findUmMasterGrupaEntities(false, maxResults, firstResult);
    }

    private List<UmMasterGrupa> findUmMasterGrupaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UmMasterGrupa.class));
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

    public UmMasterGrupa findUmMasterGrupa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UmMasterGrupa.class, id);
        } finally {
            em.close();
        }
    }

    public UmMasterGrupa findUmMasterGrupa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("UmMasterGrupa.findByNazwa");
            q.setParameter("nazwa", nazwa);
            UmMasterGrupa u = (UmMasterGrupa) q.getResultList().get(0);
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

    public int getUmMasterGrupaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UmMasterGrupa> rt = cq.from(UmMasterGrupa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
