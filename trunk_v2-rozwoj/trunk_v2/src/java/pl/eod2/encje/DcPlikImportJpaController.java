/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pl.eod.encje.Config;
import pl.eod.encje.ConfigJpaController;
import pl.eod2.encje.exceptions.NonexistentEntityException;

/**
 *
 * @author 103039
 */
public class DcPlikImportJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public DcPlikImportJpaController() {
        if (this.emf == null) {
            this.emf = Persistence.createEntityManagerFactory("eodtPU");
        }
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DcPlikImport dcPlikImport) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dcPlikImport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DcPlikImport dcPlikImport) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dcPlikImport = em.merge(dcPlikImport);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dcPlikImport.getId();
                if (findDcPlikImport(id) == null) {
                    throw new NonexistentEntityException("The dcPlikImport with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DcPlikImport dcPlikImport;
            try {
                dcPlikImport = em.getReference(DcPlikImport.class, id);
                dcPlikImport.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dcPlikImport with id " + id + " no longer exists.", enfe);
            }
            em.remove(dcPlikImport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DcPlikImport> findDcPlikImportEntities() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("DcPlikImport.findAllbezPlik");
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

    public List<DcPlikImport> findDcPlikImportEntities(int maxResults, int firstResult) {
        return findDcPlikImportEntities(false, maxResults, firstResult);
    }

    private List<DcPlikImport> findDcPlikImportEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DcPlikImport.class));
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

    public DcPlikImport findDcPlikImport(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DcPlikImport.class, id);
        } finally {
            em.close();
        }
    }

    public int getDcPlikImportCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DcPlikImport> rt = cq.from(DcPlikImport.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void importFromDir() throws IOException {
        Config cfgDir = new ConfigJpaController().findConfigNazwa("dirImportSkan");
        Config cfgDirBak = new ConfigJpaController().findConfigNazwa("dirImportSkanBak");
        File dir = new File(cfgDir.getWartosc());
        for (File f : dir.listFiles()) {
            if (f.setLastModified(new Date().getTime() - 10000) && f.isFile()) {
                System.err.println(f.getAbsolutePath());
                ByteArrayOutputStream ous = null;
                InputStream ios = null;
                try {
                    byte[] buffer = new byte[4096];
                    ous = new ByteArrayOutputStream();
                    ios = new FileInputStream(f);
                    int read;
                    while ((read = ios.read(buffer)) != -1) {
                        ous.write(buffer, 0, read);
                    }
                } finally {
                    try {
                        if (ous != null) {
                            ous.close();
                        }

                    } catch (IOException e) {
                    }

                    try {
                        if (ios != null) {
                            ios.close();
                        }
                        //przenoszenie pliku
                        f.renameTo(new File(cfgDirBak.getWartosc() + "/" + f.getName()));
                    } catch (IOException e) {
                    }
                }
                DcPlikImport plik = new DcPlikImport();
                plik.setDataDodania(new Date());
                plik.setNazwa(f.getName());
                DcPlikImportBin pim=new DcPlikImportBin();
                pim.setPlik(ous.toByteArray());
                plik.setDcPlikImportBin(pim);
                this.create(plik);
            }
        }

    }
}
