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
 * @author arti01
 */
public class DcRodzajJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public DcRodzajJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(DcRodzaj dcRodzaj) {
        EntityManager em = null;
        if ((findDcRodzaj(dcRodzaj.getNazwa())) != null) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcRodzaj);
            em.refresh(em.merge(dcRodzaj.getIdRodzajGrupa()));
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String edit(DcRodzaj dcRodzaj) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        DcRodzaj old = findDcRodzaj(dcRodzaj.getId());
        if (findDcRodzaj(dcRodzaj.getNazwa()) != null && findDcRodzaj(dcRodzaj.getNazwa()).getId() != old.getId()) {
            return "nazwa już istnieje";
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcRodzaj persistentDcRodzaj = em.find(DcRodzaj.class, dcRodzaj.getId());
            DcRodzajGrupa idRodzajGrupaOld = persistentDcRodzaj.getIdRodzajGrupa();
            DcRodzajGrupa idRodzajGrupaNew = dcRodzaj.getIdRodzajGrupa();
            List<DcDokument> dcDokumentListOld = persistentDcRodzaj.getDcDokumentList();
            List<DcDokument> dcDokumentListNew = dcRodzaj.getDcDokumentList();
            List<String> illegalOrphanMessages = null;
            for (DcDokument dcDokumentListOldDcDokument : dcDokumentListOld) {
                if (!dcDokumentListNew.contains(dcDokumentListOldDcDokument)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DcDokument " + dcDokumentListOldDcDokument + " since its rodzajId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idRodzajGrupaNew != null) {
                idRodzajGrupaNew = em.getReference(idRodzajGrupaNew.getClass(), idRodzajGrupaNew.getId());
                dcRodzaj.setIdRodzajGrupa(idRodzajGrupaNew);
            }
            List<DcDokument> attachedDcDokumentListNew = new ArrayList<DcDokument>();
            for (DcDokument dcDokumentListNewDcDokumentToAttach : dcDokumentListNew) {
                dcDokumentListNewDcDokumentToAttach = em.getReference(dcDokumentListNewDcDokumentToAttach.getClass(), dcDokumentListNewDcDokumentToAttach.getId());
                attachedDcDokumentListNew.add(dcDokumentListNewDcDokumentToAttach);
            }
            dcDokumentListNew = attachedDcDokumentListNew;
            dcRodzaj.setDcDokumentList(dcDokumentListNew);
            dcRodzaj = em.merge(dcRodzaj);
            if (idRodzajGrupaOld != null && !idRodzajGrupaOld.equals(idRodzajGrupaNew)) {
                idRodzajGrupaOld.getDcRodzajList().remove(dcRodzaj);
                idRodzajGrupaOld = em.merge(idRodzajGrupaOld);
            }
            if (idRodzajGrupaNew != null && !idRodzajGrupaNew.equals(idRodzajGrupaOld)) {
                idRodzajGrupaNew.getDcRodzajList().add(dcRodzaj);
                idRodzajGrupaNew = em.merge(idRodzajGrupaNew);
            }
            for (DcDokument dcDokumentListNewDcDokument : dcDokumentListNew) {
                if (!dcDokumentListOld.contains(dcDokumentListNewDcDokument)) {
                    DcRodzaj oldRodzajIdOfDcDokumentListNewDcDokument = dcDokumentListNewDcDokument.getRodzajId();
                    dcDokumentListNewDcDokument.setRodzajId(dcRodzaj);
                    dcDokumentListNewDcDokument = em.merge(dcDokumentListNewDcDokument);
                    if (oldRodzajIdOfDcDokumentListNewDcDokument != null && !oldRodzajIdOfDcDokumentListNewDcDokument.equals(dcRodzaj)) {
                        oldRodzajIdOfDcDokumentListNewDcDokument.getDcDokumentList().remove(dcDokumentListNewDcDokument);
                        oldRodzajIdOfDcDokumentListNewDcDokument = em.merge(oldRodzajIdOfDcDokumentListNewDcDokument);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcRodzaj.getId();
                if (findDcRodzaj(id) == null) {
                    throw new NonexistentEntityException("The dcRodzaj with id " + id + " no longer exists.");
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
            DcRodzaj dcRodzaj;
            try {
                dcRodzaj = em.getReference(DcRodzaj.class, id);
                dcRodzaj.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcRodzaj with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DcDokument> dcDokumentListOrphanCheck = dcRodzaj.getDcDokumentList();
            for (DcDokument dcDokumentListOrphanCheckDcDokument : dcDokumentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DcRodzaj (" + dcRodzaj + ") cannot be destroyed since the DcDokument " + dcDokumentListOrphanCheckDcDokument + " in its dcDokumentList field has a non-nullable rodzajId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DcRodzajGrupa idRodzajGrupa = dcRodzaj.getIdRodzajGrupa();
            if (idRodzajGrupa != null) {
                idRodzajGrupa.getDcRodzajList().remove(dcRodzaj);
                idRodzajGrupa = em.merge(idRodzajGrupa);
            }
            em.remove(dcRodzaj);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public String dodajKrok(DcRodzaj rodzaj, DcAkceptKroki krok) {
        String error = null;
        EntityManager em = null;
        krok.setLp(rodzaj.getDcAkceptKroki().size() + 1);
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(krok);
            rodzaj.getDcAkceptKroki().add(krok);
            em.merge(rodzaj);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            error = "blad - zapewnie podwojony członek grupy";
        } finally {
            if (em != null) {
                em.close();
            }
            return error;
        }
    }

    public String editKrok(DcRodzaj rodzaj, DcAkceptKroki krok) {
        EntityManager em = null;
        String error = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(krok);
            em.merge(rodzaj);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            error = "blad - zapewnie podwojony członek grupy";
            return error;
        } finally {
            if (em != null) {
                em.close();
            }
            return error;
        }
    }

    public DcRodzaj upKrok(DcRodzaj rodzaj, DcAkceptKroki krok) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (DcAkceptKroki k : rodzaj.getDcAkceptKroki()) {
                if (k.getLp() == krok.getLp() - 1) {
                    k.setLp(k.getLp() + 1);
                    em.merge(k);
                }
            }
            krok.setLp(krok.getLp() - 1);
            em.merge(krok);
            rodzaj = em.merge(rodzaj);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rodzaj;
    }

    public DcRodzaj downKrok(DcRodzaj rodzaj, DcAkceptKroki krok) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (DcAkceptKroki k : rodzaj.getDcAkceptKroki()) {
                if (k.getLp() == krok.getLp() + 1) {
                    k.setLp(k.getLp() - 1);
                    em.merge(k);
                }
            }
            krok.setLp(krok.getLp() + 1);
            em.merge(krok);
            rodzaj = em.merge(rodzaj);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rodzaj;
    }

    public DcRodzaj usunKrok(DcRodzaj rodzaj, DcAkceptKroki krok) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (DcAkceptKroki k : rodzaj.getDcAkceptKroki()) {
                if (k.getLp() > krok.getLp()) {
                    k.setLp(k.getLp() - 1);
                    em.merge(k);
                }
            }
            rodzaj.getDcAkceptKroki().remove(krok);
            em.remove(em.merge(krok));
            rodzaj = em.merge(rodzaj);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return rodzaj;
    }

    public List<DcRodzaj> findDcRodzajEntitiesAll() {
        return findDcRodzajEntities(true, -1, -1);
    }

    public List<DcRodzaj> findDcRodzajEntities(int maxResults, int firstResult) {
        return findDcRodzajEntities(false, maxResults, firstResult);
    }

    private List<DcRodzaj> findDcRodzajEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq
                    .select(cq.from(DcRodzaj.class));
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

    public DcRodzaj findDcRodzaj(Integer id) {
        EntityManager em = getEntityManager();


        try {
            return em.find(DcRodzaj.class, id);
        } finally {
            em.close();
        }
    }

    public DcRodzaj findDcRodzaj(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcRodzaj.findByNazwa");
            q.setParameter("nazwa", nazwa);
            DcRodzaj u = (DcRodzaj) q.getResultList().get(0);
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
    
    public List<DcRodzaj> findDcRodzajUm() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcRodzaj.findByUm");
            //em.refresh(u.getStruktura());
            return q.getResultList();
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
    
    public List<DcRodzaj> findDcRodzajEntities() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcRodzaj.findAllzKrok");
            //em.refresh(u.getStruktura());
            return q.getResultList();
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
    
    public List<DcRodzaj> findDcRodzajArch() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcRodzaj.findByArch");
            //em.refresh(u.getStruktura());
            return q.getResultList();
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

    public int getDcRodzajCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcRodzaj> rt = cq.from(DcRodzaj.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
