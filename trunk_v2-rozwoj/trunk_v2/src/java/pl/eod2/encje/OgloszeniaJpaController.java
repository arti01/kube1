/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod.encje.Spolki;
import pl.eod.encje.Struktura;
import pl.eod.encje.Uzytkownik;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author arti01
 */
public class OgloszeniaJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public OgloszeniaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String create(Ogloszenia ogloszenia, Uzytkownik uzytkownik) {
        ogloszenia.setWprowadzil(uzytkownik);
        ogloszenia.setDataWprow(new Date());
        if (ogloszenia.getAdresaciList() == null) {
            ogloszenia.setAdresaciList(new ArrayList<Struktura>());
        }
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(ogloszenia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String edit(Ogloszenia ogloszenia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ogloszenia persistentOgloszenia = em.find(Ogloszenia.class, ogloszenia.getId());
            Uzytkownik wprowadzilOld = persistentOgloszenia.getWprowadzil();
            Uzytkownik wprowadzilNew = ogloszenia.getWprowadzil();
            List<Struktura> adresaciListOld = persistentOgloszenia.getAdresaciList();
            List<Struktura> adresaciListNew = ogloszenia.getAdresaciList();
            if (wprowadzilNew != null) {
                wprowadzilNew = em.getReference(wprowadzilNew.getClass(), wprowadzilNew.getId());
                ogloszenia.setWprowadzil(wprowadzilNew);
            }
            List<Struktura> attachedAdresaciListNew = new ArrayList<Struktura>();
            for (Struktura adresaciListNewStrukturaToAttach : adresaciListNew) {
                adresaciListNewStrukturaToAttach = em.getReference(adresaciListNewStrukturaToAttach.getClass(), adresaciListNewStrukturaToAttach.getId());
                if (!attachedAdresaciListNew.contains(adresaciListNewStrukturaToAttach)) {
                    attachedAdresaciListNew.add(adresaciListNewStrukturaToAttach);
                }
            }
            adresaciListNew = attachedAdresaciListNew;
            ogloszenia.setAdresaciList(adresaciListNew);
            ogloszenia = em.merge(ogloszenia);
            if (wprowadzilOld != null && !wprowadzilOld.equals(wprowadzilNew)) {
                wprowadzilOld.getOgloszeniaList().remove(ogloszenia);
                wprowadzilOld = em.merge(wprowadzilOld);
            }
            if (wprowadzilNew != null && !wprowadzilNew.equals(wprowadzilOld)) {
                wprowadzilNew.getOgloszeniaList().add(ogloszenia);
                wprowadzilNew = em.merge(wprowadzilNew);
            }
            for (Struktura adresaciListOldStruktura : adresaciListOld) {
                if (!adresaciListNew.contains(adresaciListOldStruktura)) {
                    adresaciListOldStruktura.getOgloszeniaList().remove(ogloszenia);
                    adresaciListOldStruktura = em.merge(adresaciListOldStruktura);
                }
            }
            for (Struktura adresaciListNewStruktura : adresaciListNew) {
                if (!adresaciListOld.contains(adresaciListNewStruktura)) {
                    adresaciListNewStruktura.getOgloszeniaList().add(ogloszenia);
                    adresaciListNewStruktura = em.merge(adresaciListNewStruktura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ogloszenia.getId();
                if (findOgloszenia(id) == null) {
                    throw new NonexistentEntityException("The ogloszenia with id " + id + " no longer exists.");
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
            Ogloszenia ogloszenia;
            try {
                ogloszenia = em.getReference(Ogloszenia.class, id);
                ogloszenia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ogloszenia with id " + id + " no longer exists.", enfe);
            }
            Uzytkownik wprowadzil = ogloszenia.getWprowadzil();
            if (wprowadzil != null) {
                wprowadzil.getOgloszeniaList().remove(ogloszenia);
                wprowadzil = em.merge(wprowadzil);
            }
            List<Struktura> adresaciList = ogloszenia.getAdresaciList();
            for (Struktura adresaciListStruktura : adresaciList) {
                adresaciListStruktura.getOgloszeniaList().remove(ogloszenia);
                adresaciListStruktura = em.merge(adresaciListStruktura);
            }
            em.remove(ogloszenia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ogloszenia> findOgloszeniaEntities() {
        return findOgloszeniaEntities(true, -1, -1);
    }

    public List<Ogloszenia> findOgloszeniaEntities(int maxResults, int firstResult) {
        return findOgloszeniaEntities(false, maxResults, firstResult);
    }

    private List<Ogloszenia> findOgloszeniaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ogloszenia.class));
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

    public Ogloszenia findOgloszenia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ogloszenia.class, id);
        } finally {
            em.close();
        }
    }

    public int getOgloszeniaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ogloszenia> rt = cq.from(Ogloszenia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Ogloszenia> findBySpolka(Spolki spolka) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Ogloszenia.findBySpolka");
            q.setParameter("spolkaId", spolka);
            //em.refresh(u.getStruktura());
            return q.getResultList();
        } catch (NoResultException ex) {
            //ex.printStackTrace();
            return null;
        } catch(NullPointerException exnp){
            return null;
        } finally {
            em.close();
        }
    }
    
}
