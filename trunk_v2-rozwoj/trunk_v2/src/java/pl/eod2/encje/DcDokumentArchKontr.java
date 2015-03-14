/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.eod.abstr.AbstKontroler;
import static pl.eod2.encje.DcDokumentJpaController.logger;

public class DcDokumentArchKontr extends AbstKontroler<DcDokumentArch> {

    public DcDokumentArchKontr() {
        super(new DcDokumentArch());
    }

    public List<DcDokumentArch> findStatus(int statusId) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcDokumentArch.findByStatus");
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
    public List<DcDokumentArch> findByExample(DcDokumentArch dokument, Date dataRejOd, Date dataRejDo, Date dataDokOd, Date dataDokDo) throws ParseException {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Object> cq = cb.createQuery();
            Root<DcDokumentArch> cfg = cq.from(DcDokumentArch.class);
            cq.select(cfg);
            Predicate nazwa = cb.like(cb.lower(cfg.get(DcDokumentArch_.nazwa)), "%" + dokument.getNazwa().toLowerCase() + "%");
            Predicate opis = cb.like(cb.lower(cfg.get(DcDokumentArch_.opis)), "%" + dokument.getOpis().toLowerCase() + "%");
            Predicate opisDlugi = cb.like(cb.lower(cfg.get(DcDokumentArch_.opisDlugi)), "%" + dokument.getOpis().toLowerCase() + "%");
            Predicate rodzaj = cb.equal(cfg.get(DcDokumentArch_.rodzajId), dokument.getRodzajId());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if (dataRejOd == null) {
                dataRejOd = sdf.parse("1900-01-01");
            }
            if (dataRejDo == null) {
                dataRejDo = sdf.parse("2100-01-01");
            }

            if (dataDokOd == null && dataDokDo != null) {
                dataDokOd = sdf.parse("1900-01-01");
            }
            if (dataDokDo == null && dataDokOd != null) {
                dataDokDo = sdf.parse("2100-01-01");
            }

            Predicate pdata = cb.between(cfg.get(DcDokumentArch_.dataWprow), dataRejOd, dataRejDo);
            Predicate ddata = cb.between(cfg.get(DcDokumentArch_.dataDok), dataDokOd, dataDokDo);

            List<Predicate> warunek = new ArrayList<>();
            warunek.add(cb.or(nazwa));
            warunek.add(cb.or(opis, opisDlugi));
            warunek.add(cb.and(pdata));
            if (dataDokOd != null && dataDokDo != null) {
                warunek.add(cb.and(ddata));
            }

            if (dokument.getRodzajId() != null) {
                warunek.add(cb.and(rodzaj));
            }

            cq.where(warunek.toArray(new Predicate[warunek.size()]));

            Query q = em.createQuery(cq);

            List<DcDokumentArch> wynik;
            wynik = (List<DcDokumentArch>) q.getResultList();
            return wynik;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings({"unchecked"})
    public List<DcDokumentArch> findEntitiesWydane(boolean niezwrocone) {
        EntityManager em = getEntityManager();
        List<DcDokumentArch> wynik = new ArrayList<>();
        try {
            Query q = em.createNamedQuery("DcDokumentArch.findByStatus");
            q.setParameter("statusId", 9);
            //wyszukiwanie ostatnich dokumentow ktore zmieniają status na wydany
            for (DcDokumentArch da : (List<DcDokumentArch>) q.getResultList()) {
                for (DcDokument d : da.getDokumentyList()) {
                    if (d.getRodzajId().getDcDokStatusKonc().getId() == 9) {
                        if (da.getDokWyszuk() == null) {
                            da.setDokWyszuk(d);
                        }
                        if (d.getDataWprow().after(da.getDokWyszuk().getDataWprow())) {
                            da.setDokWyszuk(d);
                        }
                    }
                }
                if (da.getDokWyszuk().getDokArchDod().getDataPlanZwrot().before(new Date())||!niezwrocone) {
                    wynik.add(da);
                }
            }
            return wynik;
        } catch (NoResultException ex) {
            logger.log(Level.SEVERE, "blad", ex);
            return Collections.EMPTY_LIST;
        } finally {
            em.close();
        }
    }
}
