/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class UmGrupaJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public UmGrupaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(UmGrupa umGrupa) throws NonexistentEntityException, Exception {
        if(umGrupa.getMasterGrp()==null) return "brak master grupy";
        if (umGrupa.getUrzadzenieList() == null) {
            umGrupa.setUrzadzenieList(new ArrayList<UmUrzadzenie>());
        }
        String blad = null;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UmMasterGrupa masterGrp = umGrupa.getMasterGrp();
            if (masterGrp != null) {
                masterGrp = em.getReference(masterGrp.getClass(), masterGrp.getId());
                umGrupa.setMasterGrp(masterGrp);
            }
            List<UmUrzadzenie> attachedUrzadzenieList = new ArrayList<UmUrzadzenie>();
            for (UmUrzadzenie urzadzenieListUmUrzadzenieToAttach : umGrupa.getUrzadzenieList()) {
                urzadzenieListUmUrzadzenieToAttach = em.getReference(urzadzenieListUmUrzadzenieToAttach.getClass(), urzadzenieListUmUrzadzenieToAttach.getId());
                attachedUrzadzenieList.add(urzadzenieListUmUrzadzenieToAttach);
            }
            umGrupa.setUrzadzenieList(attachedUrzadzenieList);
            em.persist(umGrupa);
            if (masterGrp != null) {
                masterGrp.getGrupaList().add(umGrupa);
                masterGrp = em.merge(masterGrp);
            }
            for (UmUrzadzenie urzadzenieListUmUrzadzenie : umGrupa.getUrzadzenieList()) {
                UmGrupa oldGrupaOfUrzadzenieListUmUrzadzenie = urzadzenieListUmUrzadzenie.getGrupa();
                urzadzenieListUmUrzadzenie.setGrupa(umGrupa);
                urzadzenieListUmUrzadzenie = em.merge(urzadzenieListUmUrzadzenie);
                if (oldGrupaOfUrzadzenieListUmUrzadzenie != null) {
                    oldGrupaOfUrzadzenieListUmUrzadzenie.getUrzadzenieList().remove(urzadzenieListUmUrzadzenie);
                    oldGrupaOfUrzadzenieListUmUrzadzenie = em.merge(oldGrupaOfUrzadzenieListUmUrzadzenie);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = umGrupa.getId();
                if (findUmGrupa(id) == null) {
                    throw new NonexistentEntityException("The umGrupa with id " + id + " no longer exists.");
                }
            }
            umGrupa.setId(null);
            blad="nazwa już istnieje";
            ex.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return blad;
    }

    public String edit(UmGrupa umGrupa) throws NonexistentEntityException, Exception {
        if(umGrupa.getMasterGrp()==null) return "brak master grupy";
        String blad=null;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UmGrupa persistentUmGrupa = em.find(UmGrupa.class, umGrupa.getId());
            UmMasterGrupa masterGrpOld = persistentUmGrupa.getMasterGrp();
            UmMasterGrupa masterGrpNew = umGrupa.getMasterGrp();
            List<UmUrzadzenie> urzadzenieListOld = persistentUmGrupa.getUrzadzenieList();
            List<UmUrzadzenie> urzadzenieListNew = umGrupa.getUrzadzenieList();
            if (masterGrpNew != null) {
                masterGrpNew = em.getReference(masterGrpNew.getClass(), masterGrpNew.getId());
                umGrupa.setMasterGrp(masterGrpNew);
            }
            List<UmUrzadzenie> attachedUrzadzenieListNew = new ArrayList<UmUrzadzenie>();
            for (UmUrzadzenie urzadzenieListNewUmUrzadzenieToAttach : urzadzenieListNew) {
                urzadzenieListNewUmUrzadzenieToAttach = em.getReference(urzadzenieListNewUmUrzadzenieToAttach.getClass(), urzadzenieListNewUmUrzadzenieToAttach.getId());
                attachedUrzadzenieListNew.add(urzadzenieListNewUmUrzadzenieToAttach);
            }
            urzadzenieListNew = attachedUrzadzenieListNew;
            umGrupa.setUrzadzenieList(urzadzenieListNew);
            umGrupa = em.merge(umGrupa);

            /*if (masterGrpOld != null && !masterGrpOld.equals(masterGrpNew)) {
                masterGrpOld.getGrupaList().remove(umGrupa);
                masterGrpOld = em.merge(masterGrpOld);
            }
            if (masterGrpNew != null && !masterGrpNew.equals(masterGrpOld)) {
                masterGrpNew.getGrupaList().add(umGrupa);
                masterGrpNew = em.merge(masterGrpNew);
            }*/
            for (UmUrzadzenie urzadzenieListOldUmUrzadzenie : urzadzenieListOld) {
                if (!urzadzenieListNew.contains(urzadzenieListOldUmUrzadzenie)) {
                    urzadzenieListOldUmUrzadzenie.setGrupa(null);
                    urzadzenieListOldUmUrzadzenie = em.merge(urzadzenieListOldUmUrzadzenie);
                }
            }
            for (UmUrzadzenie urzadzenieListNewUmUrzadzenie : urzadzenieListNew) {
                if (!urzadzenieListOld.contains(urzadzenieListNewUmUrzadzenie)) {
                    UmGrupa oldGrupaOfUrzadzenieListNewUmUrzadzenie = urzadzenieListNewUmUrzadzenie.getGrupa();
                    urzadzenieListNewUmUrzadzenie.setGrupa(umGrupa);
                    urzadzenieListNewUmUrzadzenie = em.merge(urzadzenieListNewUmUrzadzenie);
                    if (oldGrupaOfUrzadzenieListNewUmUrzadzenie != null && !oldGrupaOfUrzadzenieListNewUmUrzadzenie.equals(umGrupa)) {
                        oldGrupaOfUrzadzenieListNewUmUrzadzenie.getUrzadzenieList().remove(urzadzenieListNewUmUrzadzenie);
                        oldGrupaOfUrzadzenieListNewUmUrzadzenie = em.merge(oldGrupaOfUrzadzenieListNewUmUrzadzenie);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = umGrupa.getId();
                if (findUmGrupa(id) == null) {
                    throw new NonexistentEntityException("The umGrupa with id " + id + " no longer exists.");
                }
            }
            ex.printStackTrace();
            blad="nazwa już istnieje";
            //throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return blad;
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UmGrupa umGrupa;
            try {
                umGrupa = em.getReference(UmGrupa.class, id);
                umGrupa.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The umGrupa with id " + id + " no longer exists.", enfe);
            }
            UmMasterGrupa masterGrp = umGrupa.getMasterGrp();
            if (masterGrp != null) {
                masterGrp.getGrupaList().remove(umGrupa);
                masterGrp = em.merge(masterGrp);
            }
            List<UmUrzadzenie> urzadzenieList = umGrupa.getUrzadzenieList();
            for (UmUrzadzenie urzadzenieListUmUrzadzenie : urzadzenieList) {
                urzadzenieListUmUrzadzenie.setGrupa(null);
                urzadzenieListUmUrzadzenie = em.merge(urzadzenieListUmUrzadzenie);
            }
            em.remove(umGrupa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public UmGrupa findUmGrupa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("findUmGrupa.findByNazwa");
            q.setParameter("nazwa", nazwa);
            UmGrupa u = (UmGrupa) q.getResultList().get(0);
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

    public List<UmGrupa> findUmGrupaEntities() {
        return findUmGrupaEntities(true, -1, -1);
    }

    public List<UmGrupa> findUmGrupaEntities(int maxResults, int firstResult) {
        return findUmGrupaEntities(false, maxResults, firstResult);
    }

    private List<UmGrupa> findUmGrupaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UmGrupa.class));
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

    public UmGrupa findUmGrupa(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UmGrupa.class, id);
        } finally {
            em.close();
        }
    }

    public int getUmGrupaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UmGrupa> rt = cq.from(UmGrupa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
