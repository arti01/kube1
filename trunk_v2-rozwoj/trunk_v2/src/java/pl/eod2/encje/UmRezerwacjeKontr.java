/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import pl.eod.abstr.AbstKontroler;
import pl.eod.encje.WnUrlop;

public class UmRezerwacjeKontr extends AbstKontroler<UmRezerwacje> {

    public UmRezerwacjeKontr() {
        super(new UmRezerwacje());
    }

    @Override
    public Map<String, String> create(UmRezerwacje obiekt) {
        EntityManager em = getEntityManager();
        Map<String, String> bledy = new HashMap<>();
        if (obiekt.getDataDo().before(obiekt.getDataOd())) {
            bledy.put("dodaj", "data od większa od daty do");
            return bledy;
        }
        UmUrzadzenie urz=em.find(UmUrzadzenie.class, obiekt.getUrzadzenie().getId());
        for (UmRezerwacje ur : urz.getRezerwacjeList()) {
            if ((obiekt.getDataDo().after(ur.getDataOd()) || obiekt.getDataDo().equals(ur.getDataOd()))
                    && (obiekt.getDataDo().before(ur.getDataDo()) || obiekt.getDataDo().equals(ur.getDataDo()))) {
                bledy.put("dodaj", "data końca zawiera się w istniejącej rezerwacji");
                return bledy;
            }
            if ((obiekt.getDataOd().after(ur.getDataOd()) || obiekt.getDataOd().equals(ur.getDataOd()))
                    && (obiekt.getDataOd().before(ur.getDataDo()) || obiekt.getDataOd().equals(ur.getDataDo()))) {
                bledy.put("dodaj", "data początku zawiera się w istniejącej rezerwacji");
                return bledy;
            }

            if (ur.getDataDo().after(obiekt.getDataOd())
                    && ur.getDataDo().before(obiekt.getDataDo())) {
                bledy.put("dodaj", "rezerwacja w zakresie innej rezerwacji");
                return bledy;
            }
        }
        
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(obiekt);
            urz.getRezerwacjeList().add(obiekt);
            em.merge(urz);
            obiekt.getTworca().getRezerwacjeList().add(obiekt);
            em.merge(obiekt.getTworca());
            em.getTransaction().commit();
        } catch (Exception ex) {
            //ex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }
    
    @Override
    public Map<String, String> edit(UmRezerwacje obiekt) {
        EntityManager em = getEntityManager();
        Map<String, String> bledy = new HashMap<>();
        if (obiekt.getDataDo().before(obiekt.getDataOd())) {
            bledy.put("dodaj", "data od większa od daty do");
            return bledy;
}
        UmUrzadzenie urz=em.find(UmUrzadzenie.class, obiekt.getUrzadzenie().getId());
        for (UmRezerwacje ur : urz.getRezerwacjeList()) {
            if(Objects.equals(ur.getId(), obiekt.getId())){
                continue;
            }
            if ((obiekt.getDataDo().after(ur.getDataOd()) || obiekt.getDataDo().equals(ur.getDataOd()))
                    && (obiekt.getDataDo().before(ur.getDataDo()) || obiekt.getDataDo().equals(ur.getDataDo()))) {
                bledy.put("dodaj", "data końca zawiera się w istniejącej rezerwacji");
                return bledy;
            }
            if ((obiekt.getDataOd().after(ur.getDataOd()) || obiekt.getDataOd().equals(ur.getDataOd()))
                    && (obiekt.getDataOd().before(ur.getDataDo()) || obiekt.getDataOd().equals(ur.getDataDo()))) {
                bledy.put("dodaj", "data początku zawiera się w istniejącej rezerwacji");
                return bledy;
            }

            if (ur.getDataDo().after(obiekt.getDataOd())
                    && ur.getDataDo().before(obiekt.getDataDo())) {
                bledy.put("dodaj", "rezerwacja w zakresie innej rezerwacji");
                return bledy;
}
        }
        if(!bledy.isEmpty()) return bledy;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(obiekt);
            em.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            //logger.log(Level.SEVERE, "blad", ex);
            obiekt.setId(null);
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return bledy;
    }
}
