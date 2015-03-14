/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.eod.encje.exceptions.NonexistentEntityException;
import pl.eod.encje.exceptions.mojWyjatek;
import pl.eod.managed.Login;

/**
 *
 * @author 103039
 */
public class StrukturaJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public StrukturaJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public List<Struktura> getFindKierownicy(Spolki spolka) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query;
            //dla adminow
            if (spolka != null) {
                query = em.createNamedQuery("Struktura.kierownicy");
                query.setParameter("spolka", spolka);
            } else {
                query = em.createNamedQuery("Struktura.kierownicyAll");
            }
            //LOG.log(Level.OFF, query.getResultList().toString()+"logger");
            List<Struktura> wynik = query.getResultList();
            return wynik;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @SuppressWarnings("empty-statement")
    public String create(Struktura struktura) throws Exception {
        
        //sprawdzenie licencji
        UzytkownikJpaController uzytC = new UzytkownikJpaController();
        //jeśli jest rola (eoduser jest dodawane pozniej)
        if(struktura.getUserId().getRole().size()>0){
            //jesli już jest rowna liczba licencji
            if(uzytC.iluZprawami() >= eodt.lib.NewClass.LICZ){
                return "Przekroczenie licencji, aktualna liczba użytkowników to: " +uzytC.iluZprawami()+ " - zmiana niewykonana";
            }
        }
        
        ConfigJpaController confC = new ConfigJpaController();
        boolean sprawdzacUnikEmail = (confC.findConfigNazwa("email_unikalny").getWartosc().equals("0"));

        if (struktura.getUserId().getExtId() != null) {
            if (struktura.getUserId().getExtId().equals("")) {
                struktura.getUserId().setExtId(null);
            }
        }
        String defPass = confC.findConfigNazwa("domysle_haslo").getWartosc();
        Hasla h = new Hasla();
        //System.out.println("--"+defPass+"====");
        if (confC.findConfigNazwa("realm_szyfrowanie").getWartosc().equals("md5")) {
            h.setPass(Login.md5(defPass));
        } else {
            h.setPass(defPass);
        }
        struktura.getUserId().setHasla(h);

        UserRolesJpaController urC = new UserRolesJpaController();
        struktura.getUserId().getRole().add(urC.findByNazwa("eoduser"));

        EntityManager em = null;
        try {
            if (!struktura.isStKier()) {
                if (struktura.getSzefId() == null) {
                    return "pracownik musi mieć przełożonego";
                }
                struktura.setDzialId(struktura.getSzefId().getDzialId());
            }
            UzytkownikJpaController uC = new UzytkownikJpaController();
            if ((uC.findStruktura(struktura.getUserId().getAdrEmail()) != null) && sprawdzacUnikEmail) {
                if (!struktura.getUserId().getAdrEmail().equals("")) {
                    return "email już istnieje";
                }
            }

            if (struktura.getSzefId() != null) {
                struktura.getUserId().setSpolkaId(struktura.getSzefId().getUserId().getSpolkaId());
            }

            DzialJpaController dC = new DzialJpaController();
            if (dC.findDzialByNazwa(struktura.getDzialId().getNazwa()) != null && struktura.isStKier()) {
                //System.err.println("valid tutaj 1");
                return "dział już istnieje";
            }
            em = getEntityManager();
            //System.out.println(struktura.getDzialId());
            em.getTransaction().begin();
            //System.err.println(struktura.getUserId().getRole());
            em.merge(struktura);
            em.getTransaction().commit();
            if (struktura.getSzefId() != null) {
                em.refresh(em.find(struktura.getClass(), struktura.getSzefId().getId()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new mojWyjatek(ex.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Struktura findGeneryczny() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Struktura> user = cq.from(Struktura.class);
            cq.select(user);
            Predicate nadrz = cb.isNull(user.get(Struktura_.szefId));
            cq.where(nadrz);
            Query q = em.createQuery(cq);
            List<Struktura> wynik = q.getResultList();
            if (wynik.isEmpty()) {
                return null;
            } else {
                for (Struktura s : wynik) {
                    if (s.bezpPod.size() > 0) {
                        return s;
                    }
                }
                return null;
            }
        } finally {
            em.close();
        }
    }

    public void zrobNiewidczony(Struktura struktura) {
        EntityManager em = null;
        em = getEntityManager();
        Dzial dOld = em.find(Dzial.class, struktura.getDzialId().getId());
        Struktura sOld;
        sOld = null;
        if (struktura.getSzefId() != null) {
            sOld = em.find(Struktura.class, struktura.getSzefId().getId());
        }
        struktura.setUsuniety(1);
        struktura.getUserId().setRole(null);
        struktura.getUserId().setAdrEmail(struktura.getId() + "usuniety" + struktura.getUserId().getAdrEmail());
        if (struktura.isStKier()) {
            Dzial dzial = struktura.getDzialId();
            struktura.setDzialId(null);
            struktura.setSzefId(null);
            em.getTransaction().begin();
            em.merge(struktura);
            em.remove(em.merge(dzial));
            em.getTransaction().commit();
        } else {
            em = getEntityManager();
            em.getTransaction().begin();
            struktura.setSzefId(null);
            struktura.setDzialId(null);
            em.merge(struktura);
            em.getTransaction().commit();
            em.refresh(em.merge(dOld));
        }
        if (sOld != null) {
            em.refresh(em.merge(sOld));
        }
    }

    public String changeKier(Struktura struktura, Dzial dzialOld) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        if (struktura.isStKier()) {
            Dzial d = new Dzial();
            d.setNazwa("Nowy dział");
            struktura.setDzialId(d);
        } else {
            if (struktura.getSzefId() != null) {
                struktura.setDzialId(struktura.getSzefId().getDzialId());
            } else {
                return "Brak przełożonego - wybierz właściwego";
            }
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(struktura);
            em.getTransaction().commit();
            if (!struktura.isStKier()) {
                DzialJpaController dzialC = new DzialJpaController();
                dzialC.destroy(dzialOld);
                System.out.println(dzialOld.getNazwa() + "usuwanie dzialu" + struktura.getDzialId().getNazwa());
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String editArti(Struktura struktura) throws NonexistentEntityException, Exception, NullPointerException {
        ConfigJpaController confC = new ConfigJpaController();
        
        //sprawdzenie licencji
        UzytkownikJpaController uzytC = new UzytkownikJpaController();
        //jeśli więcej niż jedna rola (eoduser)
        if(struktura.getUserId().getRole().size()>1){
            //jesli już jest rowna liczba licencji
            if(uzytC.iluZprawami() >= eodt.lib.NewClass.LICZ){
                return "Przekroczenie licencji, aktualna liczba użytkowników to: " +uzytC.iluZprawami()+ " - zmiana niewykonana";
            }
        }
        
        boolean sprawdzacUnikEmail = (confC.findConfigNazwa("email_unikalny").getWartosc().equals("0"));
        if (struktura.getUserId().getExtId() != null) {
            if (struktura.getUserId().getExtId().equals("")) {
                struktura.getUserId().setExtId(null);
            }
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            Struktura oldStruktura = em.find(struktura.getClass(), struktura.getId());
            Struktura oldSecUser = null;
            //kontrola czy zastępca nie ma ustawionego usera jako zastępcy - zapętlenie
            try {
                Uzytkownik secUser = em.find(Uzytkownik.class, struktura.getSecUserId().getId());
                if (secUser.getStruktura().getSecUserId().getId().equals(struktura.getUserId().getId())) {
                    return "Wprowadzony zastępca ma jako osobę zastępującą ustawionego " + secUser.getStruktura().getSecUserId().getFullname()
                            + " (email: " + secUser.getStruktura().getSecUserId().getAdrEmail() + ") - zmiana niemożliwa";
                }
            } catch (NullPointerException ex) {
            }
            Dzial oldDzial = oldStruktura.getDzialId();
            boolean oldKier = oldStruktura.isStKier();
            if (oldStruktura.getSecUserId() != null) {
                oldSecUser = oldStruktura.getSecUserId().getStruktura();
            }
            Long idOldSzef = null;
            if (oldStruktura.getSzefId() != null) {
                idOldSzef = oldStruktura.getSzefId().getId();
            }
            if (!struktura.isStKier()) {
                if (struktura.getSzefId() != null) {
                    struktura.setDzialId(struktura.getSzefId().getDzialId());
                } else {
                    return "Brak przełożonego - ustaw i zapisz";
                }
            }

            if (!struktura.getUserId().getAdrEmail().equals(oldStruktura.getUserId().getAdrEmail())) {
                if (!struktura.getUserId().getAdrEmail().equals("")) {
                    UzytkownikJpaController uC = new UzytkownikJpaController();
                    if (uC.findStruktura(struktura.getUserId().getAdrEmail()) != null && sprawdzacUnikEmail) {
                        return "email już istnieje";
                    }
                }
            }

            if (struktura.isStKier() == true) {
                if (!struktura.getDzialId().getNazwa().equals(oldStruktura.getDzialId().getNazwa())) {
                    DzialJpaController dC = new DzialJpaController();
                    if (dC.findDzialByNazwa(struktura.getDzialId().getNazwa()) != null) {
                        return "dział już istnieje";
                    }
                }
            }
            
            em.getTransaction().begin();
            System.err.println(struktura.getDzialId().getNazwa());
            em.merge(struktura);
            /*if (struktura.isStKier() == true && oldStruktura.isStKier() == false) {
             em.merge(struktura.getDzialId());   
             }*/
            em.getTransaction().commit();

            if (struktura.getSzefId() != null) {
                em.refresh(em.find(struktura.getClass(), struktura.getSzefId().getId()));
            }
            if (idOldSzef != null) {
                em.refresh(em.find(struktura.getClass(), idOldSzef));
            }

            if (struktura.getSecUserId() != null) {
                em.refresh(em.find(struktura.getClass(), struktura.getSecUserId().getStruktura().getId()));
            }
            if (oldSecUser != null) {
                em.refresh(em.find(struktura.getClass(), oldSecUser.getId()));
            }

            //System.out.println(struktura.isStKier()+"--------"+oldKier+oldDzial.getNazwa());

            if (!struktura.isStKier() && oldKier) {
                DzialJpaController dzialC = new DzialJpaController();
                dzialC.destroy(oldDzial);
            }

            em.refresh(em.merge(struktura));
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void destroyArti(Struktura struktura) throws NonexistentEntityException {
        EntityManager em = null;
        if (struktura.bezpPod.size() > 0) {
            return;
        }
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            if (struktura.isStKier()) {
                em.remove(em.merge(struktura));
            } else {
                struktura.setDzialId(null);
                em.remove(em.merge(struktura));
            }
            if (struktura.getSzefId() != null) {
            }
            System.out.println(struktura.getId());
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Struktura> getFindBezSzefa() {
        EntityManager em = getEntityManager();
        try {
            Query q;
            List<Struktura> wynik;
            q = em.createNamedQuery("Struktura.findBezSzefa");
            q.setHint("eclipselink.refresh", true);
            wynik = q.getResultList();
            return wynik;
        } finally {
            em.close();
        }
    }

    public List<Struktura> findStrukturaWidoczni(Spolki spolka) {
        List<Struktura> wynik = new ArrayList<Struktura>();
        for (Struktura s : findStrukturaEntities(spolka)) {
            if (!s.isUsuniety()) {
                wynik.add(s);
            }
        }
        return wynik;
    }

    public List<Struktura> findStrukturaEntities() {
        return findStrukturaEntities(true, -1, -1);
    }

    public List<Struktura> findStrukturaEntities(Spolki spolka) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery();
            Root<Struktura> struktura = cq.from(Struktura.class);
            Join<Struktura, Uzytkownik> userzy = struktura.join(Struktura_.userId);
            cq.select(struktura);
            Predicate nadrz = cb.and(cb.equal(userzy.get(Uzytkownik_.spolkaId), spolka),
                    cb.isNotNull(userzy.get(Uzytkownik_.spolkaId)));

            //dla adminow
            if (spolka != null) {
                cq.where(nadrz);
            }
            Query q = em.createQuery(cq);
            //System.err.println(q.getResultList());
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Struktura> getFindStrukturaEntities() {
        return findStrukturaEntities(true, -1, -1);
    }

    public List<Struktura> findStrukturaEntities(int maxResults, int firstResult) {
        return findStrukturaEntities(false, maxResults, firstResult);
    }

    private List<Struktura> findStrukturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Struktura.class));
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

    public Struktura findStruktura(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Struktura.class, id);
        } finally {
            em.close();
        }
    }

    public int getStrukturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Struktura> rt = cq.from(Struktura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
