package pl.eod.encje;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;

/**
 *
 * @author arti01
 */
public class WnUrlopJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    public WnUrlopJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public String createEdit(WnUrlop wnUrlop) {
        if (wnUrlop.getDataDo().before(wnUrlop.getDataOd())) {
            return "Data końca nie może być przed datą początku";
        }
        if (wnUrlop.getUzytkownik().getStruktura().isMusZast() && wnUrlop.getUzytkownik().getStruktura().getSecUserId() == null) {
            return "Przed wystawieniem wniosku konieczny jest ustawiony zastępca";
        }

        if (wnUrlop.getWnHistoriaList() == null) {
            wnUrlop.setWnHistoriaList(new ArrayList<WnHistoria>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();

            //sprawdzenie, czy urlop się nie pokrywa z już wysłanym lub zaakceptowanym            
            Uzytkownik u = em.find(Uzytkownik.class, wnUrlop.getUzytkownik().getId());
            if (wnUrlop.getStatusId().getId() == 1 || wnUrlop.getStatusId().getId() == 2) {
                for (WnUrlop ur : u.getWnUrlopList()) {
                    if (ur.getStatusId().getId() != 2 && ur.getStatusId().getId() != 3) {
                        continue;
                    }
                    if (ur.getId().equals( wnUrlop.getId())) {
                        continue;
                    }

                    if ((wnUrlop.getDataDo().after(ur.getDataOd()) || wnUrlop.getDataDo().equals(ur.getDataOd()))
                            && (wnUrlop.getDataDo().before(ur.getDataDo()) || wnUrlop.getDataDo().equals(ur.getDataDo()))) {
                        return "data końca zawiera się w już wysłanym wniosku";
                    }
                    if ((wnUrlop.getDataOd().after(ur.getDataOd()) || wnUrlop.getDataOd().equals(ur.getDataOd()))
                            && (wnUrlop.getDataOd().before(ur.getDataDo()) || wnUrlop.getDataOd().equals(ur.getDataDo()))) {
                        return "data początku zawiera się w już wysłanym wniosku";
                    }

                    if (ur.getDataDo().after(wnUrlop.getDataOd())
                            && ur.getDataDo().before(wnUrlop.getDataDo())) {
                        return "już wysłany wniosek leży w tym zakresie";
                    }

                }
            }

            u.getWnUrlopList().add(0, wnUrlop);
            em.getTransaction().begin();
            if (wnUrlop.getId() == null) {
                em.persist(wnUrlop);
            }
            em.merge(u);
            //em.getTransaction().commit();
            //nadawanie numeru wniosku;
            String nrWniosku = wnUrlop.getUzytkownik().getSpolkaId().getSymbol().toUpperCase();
            nrWniosku = nrWniosku + "/" + wnUrlop.getRodzajId().getSymbol().toUpperCase();
            //nrWniosku = nrWniosku + "/" + (getWnUrlopCountRokBiezacy() + 1) + "/";
            nrWniosku = nrWniosku + "/" + (getWnUrlopCountMiesiacBiezacy() + 1) + "/";
            SimpleDateFormat sdfr = new SimpleDateFormat("yyyy");
            SimpleDateFormat sdfm = new SimpleDateFormat("MM");
            nrWniosku = nrWniosku + sdfm.format(new Date()) + "/" + sdfr.format(new Date());
            if (wnUrlop.getStatusId().getId() == 1) {
                wnUrlop.setNrWniosku(nrWniosku);
            }
            //em.getTransaction().begin();
            em.merge(wnUrlop);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String destroy(WnUrlop wnUrlop) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Uzytkownik u = em.find(Uzytkownik.class, wnUrlop.getUzytkownik().getId());
            //System.err.println(u.getWnUrlopList());
            u.getWnUrlopList().remove(wnUrlop);
            //System.err.println(u.getWnUrlopList());
            em.getTransaction().begin();
            em.merge(u);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void eskaluj(WnUrlop urlop) {
        //System.err.println(urlop.getId());

        try {
            urlop.setAkceptant(urlop.getAkceptant().getStruktura().getSzefId().getUserId());
        } catch (NullPointerException np) {
            System.err.println("Podczas eskalacji nie można ustawić akceptanta dla wniosku o id " + urlop.getId());
            return;
        }
        //System.err.println("proba eskalacji");
        WnHistoria wnh = new WnHistoria();
        wnh.setStatusId(urlop.getStatusId());
        wnh.setDataZmiany(new Date());
        wnh.setZmieniajacy(null);
        wnh.setUrlopId(urlop);
        wnh.setAkceptant(urlop.getAkceptant());
        wnh.setOpisZmiany("Wniosek eskalowany automatycznie");
        urlop.getWnHistoriaList().add(wnh);
        String error = createEdit(urlop);
        if (error != null) {
            System.err.println(createEdit(urlop));
        } else {
            KomKolejkaJpaController KomKolC = new KomKolejkaJpaController();
            KomKolejka kk = new KomKolejka();
            kk.setAdresList(urlop.getAkceptant().getAdrEmail());
            kk.setStatus(0);
            kk.setIdDokumenu(urlop.getId().intValue());
            kk.setTemat("Prośba o akceptację wniosku urlopowego - eskalacja");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            kk.setTresc("Proszę o akceptację wniosku urlopowego, który nie został zaakceptowany przez bezpośredniego przełożonego. " + "Pracownik " + urlop.getUzytkownik().getFullname() + " wnioskuje o urlop " + urlop.getRodzajId().getOpis() + " w dniach od:" + sdf.format(urlop.getDataOd()) + " do:" + sdf.format(urlop.getDataDo()) + ". Numer wniosku: " + urlop.getNrWniosku() + ". Dodatkowe informacje: " + urlop.getInfoDod());
            KomKolC.create(kk);
        }
    }

    public void eskalujCron() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CET"), new Locale("pl", "PL"));
        ConfigJpaController cfgC = new ConfigJpaController();
        Config cfg = cfgC.findConfigNazwa("eskalujPoMinutach");
        //System.out.println(cfg.getWartosc());
        cal.add(Calendar.MINUTE, -(new Long(cfg.getWartosc()).intValue()));
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("WnUrlop.findDoEskalacji");
            q.setParameter("statusId", 2);
            for (WnUrlop u : (List<WnUrlop>) q.getResultList()) {
                //System.out.println(cal.getTime());
                //System.out.println(u.getDataOstZmiany());
                //System.out.println(u.getDataOstZmiany().before(cal.getTime()));
                if (u.getDataOstZmiany().before(cal.getTime())) {
                    eskaluj(u);
                }
            }
        } finally {
            em.close();
        }
    }

    public List<WnUrlop> findWnUrlopEntities() {
        return findWnUrlopEntities(true, -1, -1);
    }

    public List<WnUrlop> findWnUrlopEntities(int maxResults, int firstResult) {
        return findWnUrlopEntities(false, maxResults, firstResult);
    }

    private List<WnUrlop> findWnUrlopEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder queryBuilder = em.getCriteriaBuilder();
            CriteriaQuery<WnUrlop> queryDefinition = queryBuilder.createQuery(WnUrlop.class);
            Root<WnUrlop> urlop = queryDefinition.from(WnUrlop.class);
            Order o;
            o = queryBuilder.desc(urlop.get("id"));
            queryDefinition.select(urlop).orderBy(o);
            CriteriaQuery cq = queryBuilder.createQuery();
            Query q = em.createQuery(queryDefinition);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public WnUrlop findWnUrlop(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WnUrlop.class, id);
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public int getWnUrlopCountRokBiezacy() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<WnUrlop> rt = cq.from(WnUrlop.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            cq.where(cb.greaterThan(rt.get(WnUrlop_.dataWprowadzenia), cal.getTime()));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public int getWnUrlopCountMiesiacBiezacy() {
        Calendar cal = Calendar.getInstance();
        //cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<WnUrlop> rt = cq.from(WnUrlop.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            cq.where(cb.greaterThan(rt.get(WnUrlop_.dataWprowadzenia), cal.getTime()));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public int getWnUrlopCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WnUrlop> rt = cq.from(WnUrlop.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}