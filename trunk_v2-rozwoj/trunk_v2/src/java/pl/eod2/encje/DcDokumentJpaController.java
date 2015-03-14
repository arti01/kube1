/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.eod.abstr.AbstKontroler;
import pl.eod.encje.Struktura;
import pl.eod.encje.Uzytkownik;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;

public class DcDokumentJpaController extends AbstKontroler<DcDokument> implements Serializable {

    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getAnonymousLogger();

    public DcDokumentJpaController() {
        super(new DcDokument());
    }

    private DcDokument createKroki(DcDokument dcDokument) {
        dcDokument.setDcDokKrok(new ArrayList<DcDokumentKrok>());
        EntityManager em = null;
        try {
            em = getEntityManager();
            DcAkceptStatus aS1 = new DcAkceptStatusJpaController().findDcAkceptStatus(1);
            DcAkceptStatus aS2 = new DcAkceptStatusJpaController().findDcAkceptStatus(2);
            for (DcAkceptKroki aKrok : dcDokument.getRodzajId().getDcAkceptKroki()) {
                DcDokumentKrok krok = new DcDokumentKrok();
                krok.setAkcept(aS1);
                krok.setDcAckeptTypKroku(aKrok.getDcAckeptTypKroku());
                krok.setIdDok(dcDokument);
                krok.setLp(aKrok.getLp());
                krok.setDcKrokUzytkownikaList(new ArrayList<DcDokumentKrokUzytkownik>());
                for (Uzytkownik u : aKrok.getUzytkownikList()) {
                    DcDokumentKrokUzytkownik krokUser = new DcDokumentKrokUzytkownik();
                    krokUser.setAkcept(aS2);
                    krokUser.setIdDokumentKrok(krok);
                    krokUser.setIdUser(u);
                    krok.getDcKrokUzytkownikaList().add(krokUser);
                    em.persist(krokUser);
                }
                em.persist(krok);
                dcDokument.getDcDokKrok().add(krok);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dcDokument;
    }

    public String create(DcDokument dcDokument, Struktura struktura) throws NonexistentEntityException, Exception {
        dcDokument.setUserId(struktura.getUserId());

        List<DcPlik> pliki = dcDokument.getDcPlikList();
        dcDokument.setDcPlikList(new ArrayList<DcPlik>());
        if (dcDokument.getKontrahentId() == null) {
            DcKontrahenciJpaController dcKonC = new DcKontrahenciJpaController();
            DcKontrahenci dokKon = dcKonC.findDcKontrahenci(1);
            dcDokument.setKontrahentId(dokKon);
        }

        DcDokumentStatusJpaController dcDokStatC = new DcDokumentStatusJpaController();
        DcDokumentStatus dokSt = dcDokStatC.findDcDokumentStatus(1);
        dcDokument.setDokStatusId(dokSt);
        if (dcDokument.getDcPlikList() == null) {
            dcDokument.setDcPlikList(new ArrayList<DcPlik>());
        }
        dcDokument.setDataWprow(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        dcDokument.setSymbolSpDzialRok(dcDokument.getUserId().getSpolkaId().getSymbol() + "/" + dcDokument.getUserId().getStruktura().getDzialId().getSymbol() + "/" + sdf.format(dcDokument.getDataWprow()));
        dcDokument.setDokStatusId(new DcDokumentStatus(1));
        dcDokument.setSymbolNrKol(this.findMaxNrKol(dcDokument));

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dcDokument = this.createKroki(dcDokument);
            em.persist(dcDokument);
            em.getTransaction().commit();
            em.refresh(dcDokument);
        } catch (Exception ex) {
            dcDokument.setDcPlikList(pliki);//bo gineły
            logger.log(Level.SEVERE, "blad", ex);
            return "blad - zapewne nie wypełniony rodzaj";
        } finally {
            if (em != null) {
                em.close();
            }
        }

        if (pliki != null) {
            for (DcPlik plik : pliki) {
                plik.setIdDok(dcDokument);
                dcDokument.getDcPlikList().add(plik);
            }
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.merge(dcDokument);
                em.getTransaction().commit();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "blad", ex);
                return "blad";
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }

        return null;
    }

    public DcDokument wyslijDoAkceptacji(DcDokument dcDokument) {
        EntityManager em = null;
        try {
            DcAkceptStatus aS2 = new DcAkceptStatusJpaController().findDcAkceptStatus(2);
            DcDokumentStatus dS2 = new DcDokumentStatusJpaController().findDcDokumentStatus(2);
            em = getEntityManager();
            em.getTransaction().begin();
            dcDokument.setDokStatusId(dS2);
            for (DcDokumentKrok dk : dcDokument.getDcDokKrok()) {
                if (dk.getLp() == 1) {
                    dk.setAkcept(aS2);
                }
            }
            em.merge(dcDokument);
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            //return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dcDokument;
    }

    public DcDokument przeniesDoArchiwum(DcDokument dcDokument, DcDokumentArch docArch, boolean poczekalnia) {
        EntityManager em;
        DcDokumentStatus dS;
        if (poczekalnia) {
            dS = new DcDokumentStatusJpaController().findDcDokumentStatus(6);
        } else {
            dS = new DcDokumentStatusJpaController().findDcDokumentStatus(8);
        }
        em = getEntityManager();
        try {

            docArch.setDokStatusId(dS);
            DcDokumentArchKontr dcArchKontr = new DcDokumentArchKontr();

            em.getTransaction().begin();
            em.remove(em.merge(dcDokument));
            em.getTransaction().commit();

            em.getTransaction().begin();
            dcArchKontr.create(docArch);
            em.getTransaction().commit();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            em.getTransaction().rollback();
            //return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dcDokument;
    }

    public DcDokument akceptuj(DcDokumentKrokUzytkownik dku) {
        EntityManager em = null;
        DcDokument dok = dku.getIdDokumentKrok().getIdDok();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcAkceptStatus aS4 = new DcAkceptStatusJpaController().findDcAkceptStatus(4);
            DcAkceptStatus aS3 = new DcAkceptStatusJpaController().findDcAkceptStatus(3);
            DcAkceptStatus aS2 = new DcAkceptStatusJpaController().findDcAkceptStatus(2);
            //zmiana statusu akceptacji usera na zaakceptowany
            dku.setAkcept(aS4);
            dku.setDataAkcept(new Date());
            em.merge(dku);
            //jesli wymaga akceptacji tylko jednego to zmiana statusu kroku na zaakceptowany
            if (dku.getIdDokumentKrok().getDcAckeptTypKroku().getId() == 2) {
                dku.getIdDokumentKrok().setAkcept(aS4);
                //jesli wymaga akceptacji wszystkich, to sprawdzenie, czy wszyscy inni zaakceptowali
                //jesli tak, zmiana na zaakceptowany, jesli nie, to na czesciowo zaakceptowany
            } else {
                boolean czyInni = true;
                for (DcDokumentKrokUzytkownik dkui : dku.getIdDokumentKrok().getDcKrokUzytkownikaList()) {
                    if (dkui.getAkcept().getId() == 2) {
                        czyInni = false;
                    }
                }
                if (czyInni) {
                    dku.getIdDokumentKrok().setAkcept(aS4);
                } else {
                    dku.getIdDokumentKrok().setAkcept(aS3);
                }
            }
            em.merge(dku.getIdDokumentKrok());
            //wyszukiwanie kolejnego kroku i zmiana statusu, jeśli ten krok jest zaakceptowany
            DcDokumentKrok dkNext = null;
            if (dku.getIdDokumentKrok().getAkcept().getId() == 4) {
                for (DcDokumentKrok dk2 : dku.getIdDokumentKrok().getIdDok().getDcDokKrok()) {
                    if (dk2.getLp() == dku.getIdDokumentKrok().getLp() + 1) {
                        dkNext = dk2;
                    }
                }
            }
            //jesli nie ma kolejnego kroku a biezacy jest zaakceptowany zmien status dokumentu 
            if (dkNext == null && dku.getIdDokumentKrok().getAkcept().getId() == 4) {
                dku.getIdDokumentKrok().getIdDok().setDokStatusId(new DcDokumentStatus(3));
                //obsluga dprzypisanych dokumentow z archiwum
                if ((dku.getIdDokumentKrok().getIdDok().getDcArchList() != null) && (!dku.getIdDokumentKrok().getIdDok().getDcArchList().isEmpty())) {
                    List<DcDokumentArch> lista = new ArrayList();
                    DcDokumentStatus dsKoncowy = dku.getIdDokumentKrok().getIdDok().getRodzajId().getDcDokStatusKonc();
                    for (DcDokumentArch da : dku.getIdDokumentKrok().getIdDok().getDcArchList()) {
                        da.setDokStatusId(dsKoncowy);
                        lista.add(da);
                    }
                    dku.getIdDokumentKrok().getIdDok().getDcArchList().clear();
                    dku.getIdDokumentKrok().getIdDok().getDcArchList().addAll(lista);
                }
                em.merge(dku.getIdDokumentKrok().getIdDok());
            } //jesli jest kolejny, to zmien  mu status z poczatkowy na brak akceptu
            else if (dkNext != null) {
                dkNext.setAkcept(aS2);
                em.merge(dkNext);
            }
            //a jesli nie ma kolejnego, a obecny nie jest zaakceptowany to nic nie rob ;)
            em.getTransaction().commit();
            dok = new DcDokumentJpaController().findDcDokument(dok.getId());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            //return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dok;
    }

    public DcDokument odrzuc(DcDokumentKrokUzytkownik dku) {
        EntityManager em = null;
        DcDokument dok = dku.getIdDokumentKrok().getIdDok();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcAkceptStatus aS5 = new DcAkceptStatusJpaController().findDcAkceptStatus(5);
            DcAkceptStatus aS4 = new DcAkceptStatusJpaController().findDcAkceptStatus(4);
            DcAkceptStatus aS3 = new DcAkceptStatusJpaController().findDcAkceptStatus(3);
            DcAkceptStatus aS2 = new DcAkceptStatusJpaController().findDcAkceptStatus(2);
            //zmiana statusu akceptacji usera na zaakceptowany
            dku.setAkcept(aS5);
            dku.setDataAkcept(new Date());
            em.merge(dku);
            //bezwzgledna zmiana statusu
            dku.getIdDokumentKrok().setAkcept(aS5);
            em.merge(dku.getIdDokumentKrok());

            //bezwzgledna zmiana statusu dokumentu
            dku.getIdDokumentKrok().getIdDok().setDokStatusId(new DcDokumentStatus(5));
            em.merge(dku.getIdDokumentKrok().getIdDok());
            em.getTransaction().commit();

            dok = new DcDokumentJpaController().findDcDokument(dok.getId());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            //return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return dok;
    }

    public String editDoWiad(DcDokument dcDokument, DcDokDoWiadomosci doWiad) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(doWiad);
            dcDokument.getDcDokDoWiadList().add(doWiad);
            em.merge(dcDokument);
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "blad", ex);
            //return "blad";
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public String editZmiana(DcDokument dcDokument) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;

        if (dcDokument.getKontrahentId() == null) {
            DcKontrahenciJpaController dcKonC = new DcKontrahenciJpaController();
            DcKontrahenci dokKon = dcKonC.findDcKontrahenci(1);
            dcDokument.setKontrahentId(dokKon);
        }

        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcDokument persistentDcDokument = em.find(DcDokument.class, dcDokument.getId());
            Uzytkownik userIdOld = persistentDcDokument.getUserId();
            Uzytkownik userIdNew = dcDokument.getUserId();
            DcZrodlo zrodloIdOld = persistentDcDokument.getZrodloId();
            DcZrodlo zrodloIdNew = dcDokument.getZrodloId();
            DcRodzaj rodzajIdOld = persistentDcDokument.getRodzajId();
            DcRodzaj rodzajIdNew = dcDokument.getRodzajId();
            DcTeczka projektIdOld = persistentDcDokument.getTeczkaId();
            DcTeczka projektIdNew = dcDokument.getTeczkaId();
            List<DcPlik> dcPlikListOld = persistentDcDokument.getDcPlikList();
            List<DcPlik> dcPlikListNew = dcDokument.getDcPlikList();
            List<String> illegalOrphanMessages = null;
            for (DcPlik dcPlikListOldDcPlik : dcPlikListOld) {
                if (!dcPlikListNew.contains(dcPlikListOldDcPlik)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<>();
                    }
                    illegalOrphanMessages.add("You must retain DcPlik " + dcPlikListOldDcPlik + " since its idDok field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIdNew != null) {
                userIdNew = em.getReference(userIdNew.getClass(), userIdNew.getId());
                dcDokument.setUserId(userIdNew);
            }
            if (zrodloIdNew != null) {
                zrodloIdNew = em.getReference(zrodloIdNew.getClass(), zrodloIdNew.getId());
                dcDokument.setZrodloId(zrodloIdNew);
            }
            if (rodzajIdNew != null) {
                rodzajIdNew = em.getReference(rodzajIdNew.getClass(), rodzajIdNew.getId());
                dcDokument.setRodzajId(rodzajIdNew);
            }
            if (projektIdNew != null) {
                projektIdNew = em.getReference(projektIdNew.getClass(), projektIdNew.getId());
                dcDokument.setTeczkaId(projektIdNew);
            }
            List<DcPlik> attachedDcPlikListNew = new ArrayList<>();
            for (DcPlik dcPlikListNewDcPlikToAttach : dcPlikListNew) {
                dcPlikListNewDcPlikToAttach = em.getReference(dcPlikListNewDcPlikToAttach.getClass(), dcPlikListNewDcPlikToAttach.getId());
                attachedDcPlikListNew.add(dcPlikListNewDcPlikToAttach);
            }

            //urzadzenia - USUWANIE duplikatow
            HashSet<UmUrzadzenie> hs = new HashSet<>();
            hs.addAll(dcDokument.getUrzadzeniaList());
            dcDokument.getUrzadzeniaList().clear();
            dcDokument.getUrzadzeniaList().addAll(hs);

            dcPlikListNew = attachedDcPlikListNew;
            dcDokument.setDcPlikList(dcPlikListNew);

            dcDokument = this.createKroki(dcDokument);

            dcDokument = em.merge(dcDokument);

            if (userIdOld != null && !userIdOld.equals(userIdNew)) {
                userIdOld.getDcDokumentList().remove(dcDokument);
                userIdOld = em.merge(userIdOld);
            }
            if (userIdNew != null && !userIdNew.equals(userIdOld)) {
                userIdNew.getDcDokumentList().add(dcDokument);
                userIdNew = em.merge(userIdNew);
            }
            if (zrodloIdOld != null && !zrodloIdOld.equals(zrodloIdNew)) {
                zrodloIdOld.getDcDokumentList().remove(dcDokument);
                zrodloIdOld = em.merge(zrodloIdOld);
            }
            if (zrodloIdNew != null && !zrodloIdNew.equals(zrodloIdOld)) {
                zrodloIdNew.getDcDokumentList().add(dcDokument);
                zrodloIdNew = em.merge(zrodloIdNew);
            }
            if (rodzajIdOld != null && !rodzajIdOld.equals(rodzajIdNew)) {
                rodzajIdOld.getDcDokumentList().remove(dcDokument);
                rodzajIdOld = em.merge(rodzajIdOld);
            }
            if (rodzajIdNew != null && !rodzajIdNew.equals(rodzajIdOld)) {
                rodzajIdNew.getDcDokumentList().add(dcDokument);
                rodzajIdNew = em.merge(rodzajIdNew);
            }
            if (projektIdOld != null && !projektIdOld.equals(projektIdNew)) {
                projektIdOld.getDcDokumentList().remove(dcDokument);
                projektIdOld = em.merge(projektIdOld);
            }
            if (projektIdNew != null && !projektIdNew.equals(projektIdOld)) {
                projektIdNew.getDcDokumentList().add(dcDokument);
                projektIdNew = em.merge(projektIdNew);
            }
            for (DcPlik dcPlikListNewDcPlik : dcPlikListNew) {
                if (!dcPlikListOld.contains(dcPlikListNewDcPlik)) {
                    DcDokument oldIdDokOfDcPlikListNewDcPlik = dcPlikListNewDcPlik.getIdDok();
                    dcPlikListNewDcPlik.setIdDok(dcDokument);
                    dcPlikListNewDcPlik = em.merge(dcPlikListNewDcPlik);
                    if (oldIdDokOfDcPlikListNewDcPlik != null && !oldIdDokOfDcPlikListNewDcPlik.equals(dcDokument)) {
                        oldIdDokOfDcPlikListNewDcPlik.getDcPlikList().remove(dcPlikListNewDcPlik);
                        oldIdDokOfDcPlikListNewDcPlik = em.merge(oldIdDokOfDcPlikListNewDcPlik);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcDokument.getId();
                if (findDcDokument(id) == null) {
                    throw new NonexistentEntityException("The dcDokument with id " + id + " no longer exists.");
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
            DcDokument dcDokument;
            try {
                dcDokument = em.getReference(DcDokument.class, id);
                dcDokument.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcDokument with id " + id + " no longer exists.", enfe);
            }

            Uzytkownik userId = dcDokument.getUserId();
            if (userId != null) {
                userId.getDcDokumentList().remove(dcDokument);
                userId = em.merge(userId);
            }
            DcZrodlo zrodloId = dcDokument.getZrodloId();
            if (zrodloId != null) {
                zrodloId.getDcDokumentList().remove(dcDokument);
                zrodloId = em.merge(zrodloId);
            }
            DcRodzaj rodzajId = dcDokument.getRodzajId();
            if (rodzajId != null) {
                rodzajId.getDcDokumentList().remove(dcDokument);
                rodzajId = em.merge(rodzajId);
            }
            DcTeczka projektId = dcDokument.getTeczkaId();
            if (projektId != null) {
                projektId.getDcDokumentList().remove(dcDokument);
                projektId = em.merge(projektId);
            }
            em.remove(dcDokument);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcDokument> findDcDokumentEntities() {
        return findDcDokumentEntities(true, -1, -1);
    }

    public List<DcDokument> findDcDokumentEntities(int maxResults, int firstResult) {
        return findDcDokumentEntities(false, maxResults, firstResult);
    }

    @SuppressWarnings("unchecked")
    private List<DcDokument> findDcDokumentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcDokument.class));
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

    public DcDokument findDcDokument(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcDokument.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcDokumentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
            Root<DcDokument> rt = cq.from(DcDokument.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<DcDokument> findRaport(DcRodzajGrupa rodzGrupa, Date dataRejOd, Date dataRejDo, DcZrodlo zrodlo) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<DcDokument> cq = cb.createQuery(DcDokument.class);
            Root<DcDokument> dokument = cq.from(DcDokument.class);
            cq.select(dokument);
            Predicate warunek;
            Predicate warRodzaj;
            Predicate warZrodlo;
            Predicate warDataOd;
            Predicate warDataDo;

            DcDokumentStatus status = new DcDokumentStatus(3);
            Expression<DcDokumentStatus> statusE = dokument.get(DcDokument_.dokStatusId);
            Predicate warStatus = cb.equal(statusE, status);
            warunek = warStatus;

            if (rodzGrupa != null && rodzGrupa.getDcRodzajList() != null && !rodzGrupa.getDcRodzajList().isEmpty()) {
                Expression<DcRodzaj> rodzaj = dokument.get(DcDokument_.rodzajId);
                warRodzaj = rodzaj.in(rodzGrupa.getDcRodzajList());
                warunek = cb.and(warRodzaj, warunek);
            } else {
                return null;
            }
            if (dataRejOd != null) {
                Expression<Date> dataRej = dokument.get(DcDokument_.dataWprow);
                warDataOd = cb.greaterThanOrEqualTo(dataRej, dataRejOd);
                warunek = cb.and(warDataOd, warunek);
            }
            if (dataRejDo != null) {
                Calendar c = Calendar.getInstance();
                c.setTime(dataRejDo);
                c.add(Calendar.DATE, 1);
                dataRejDo = c.getTime();
                Expression<Date> dataRej = dokument.get(DcDokument_.dataWprow);
                warDataDo = cb.lessThanOrEqualTo(dataRej, dataRejDo);
                warunek = cb.and(warDataDo, warunek);
            }
            if (zrodlo != null) {
                Expression<DcZrodlo> zrodloE = dokument.get(DcDokument_.zrodloId);
                warZrodlo = cb.equal(zrodloE, zrodlo);
                warunek = cb.and(warZrodlo, warunek);
            }
            //Predicate warunek = cb.equal(dokument.get(DcDokument_.nazwa), example.getNazwa());
            cq.where(warunek);
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public int findMaxNrKol(DcDokument dokument) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcDokument.findMaxNrKol");
            q.setParameter("symbolSpDzialRok", dokument.getSymbolSpDzialRok());
            int u = (Integer) q.getResultList().get(0);

            Query qArch = em.createNamedQuery("DcDokumentArch.findMaxNrKol");
            qArch.setParameter("symbolSpDzialRok", dokument.getSymbolSpDzialRok());
            int uArch = (Integer) qArch.getResultList().get(0);
            if (u > uArch) {
                return u + 1;
            } else {
                return uArch + 1;
            }

        } catch (NoResultException | ArrayIndexOutOfBoundsException | NullPointerException ex) {
            logger.log(Level.SEVERE, "blad", ex);
            return 1;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<DcDokument> findByExample(DcDokument dokument, Date dataRejOd, Date dataRejDo, Date dataDokOd, Date dataDokDo) throws ParseException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery();
            Root<DcDokument> cfg = cq.from(DcDokument.class);
            cq.select(cfg);
            Predicate nazwa = cb.like(cb.lower(cfg.get(DcDokument_.nazwa)), "%" + dokument.getNazwa().toLowerCase() + "%");
            Predicate opis = cb.like(cb.lower(cfg.get(DcDokument_.opis)), "%" + dokument.getOpis().toLowerCase() + "%");
            Predicate opisDlugi = cb.like(cb.lower(cfg.get(DcDokument_.opisDlugi)), "%" + dokument.getOpis().toLowerCase() + "%");
            Predicate rodzaj = cb.equal(cfg.get(DcDokument_.rodzajId), dokument.getRodzajId());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            
            if(dataRejOd==null){
                dataRejOd=sdf.parse("1900-01-01");
            }
            if(dataRejDo==null){
                dataRejDo=sdf.parse("2100-01-01");
            }
            
            if(dataDokOd==null&&dataDokDo!=null){
                dataDokOd=sdf.parse("1900-01-01");
            }
            if(dataDokDo==null&&dataDokOd!=null){
                dataDokDo=sdf.parse("2100-01-01");
            }
            
            Predicate pdata = cb.between(cfg.get(DcDokument_.dataWprow), dataRejOd, dataRejDo);
            Predicate ddata = cb.between(cfg.get(DcDokument_.dataDok), dataDokOd, dataDokDo);
            
            List<Predicate> warunek=new ArrayList<>();
            warunek.add(cb.or(nazwa));
            warunek.add(cb.or(opis, opisDlugi));
            warunek.add(cb.and(pdata));
            if(dataDokOd!=null&&dataDokDo!=null){
                warunek.add(cb.and(ddata));
            }
            
            if (dokument.getRodzajId() != null) {
                warunek.add(cb.and(rodzaj));
            }

            cq.where(warunek.toArray(new Predicate[warunek.size()]));

            Query q = em.createQuery(cq);

            List<DcDokument> wynik;
            wynik = (List<DcDokument>) q.getResultList();
            return wynik;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<DcDokument> findStatus(int statusId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcDokument.findByStatus");
            q.setParameter("statusId", statusId);
            return q.getResultList();
        } catch (NoResultException ex) {
            logger.log(Level.SEVERE, "blad", ex);
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<DcDokument> findEntitiesDlaArch() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcDokument.findDlaArch");
            return q.getResultList();
        } catch (NoResultException ex) {
            logger.log(Level.SEVERE, "blad", ex);
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }
}
