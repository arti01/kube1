/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.encje;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author arti01
 */
public class UserRolesJpaController implements Serializable {
    private static final long serialVersionUID = 1L;

    public UserRolesJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public UserRoles findByNazwa(String nazwa) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<UserRoles> ur = cq.from(UserRoles.class);
            cq.select(ur);
            cq.where(cb.equal(ur.get(UserRoles_.rolename), nazwa));
            Query q = em.createQuery(cq);
            return (UserRoles) q.getSingleResult();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserRoles> findDostepneDoEdycji() {
        String rola1="eodstru";
        String rola2="eodurlop";
        String rola3="eoddok_rej";
        String rola4="eoddok_odb";
        String rola5="eoddok_cfg";
        String rola6="eoddok_arc";
        String rola7="eod_ogl";
        String rola8="eod_um_cfg";
        String rola9="eod_um_sprz";
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<UserRoles> cfg = cq.from(UserRoles.class);
            cq.select(cfg);
            //cq.where(cb.equal(cfg.get(UserRoles_.rolename), rola1));
            cq.where(
                    cb.or(
                    cb.equal(cfg.get(UserRoles_.rolename), rola1), 
                    cb.equal(cfg.get(UserRoles_.rolename), rola2),
                    cb.equal(cfg.get(UserRoles_.rolename), rola3),
                    cb.equal(cfg.get(UserRoles_.rolename), rola4),
                    cb.equal(cfg.get(UserRoles_.rolename), rola5),
                    cb.equal(cfg.get(UserRoles_.rolename), rola6),
                    cb.equal(cfg.get(UserRoles_.rolename), rola7),
                    cb.equal(cfg.get(UserRoles_.rolename), rola8),
                    cb.equal(cfg.get(UserRoles_.rolename), rola9)
                    ));
            @SuppressWarnings("unchecked")
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<UserRoles> findRolesEntities() {
        return findRolesEntities(true, -1, -1);
    }

    public List<UserRoles> findRolesEntities(int maxResults, int firstResult) {
        return findRolesEntities(false, maxResults, firstResult);
    }

    private List<UserRoles> findRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UserRoles.class));
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
}
