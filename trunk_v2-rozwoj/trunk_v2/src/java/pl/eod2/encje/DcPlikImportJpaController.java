/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.encje;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import pl.eod.encje.Config;
import pl.eod.encje.ConfigJpaController;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.plikiUpload.Ocr;

/**
 *
 * @author 103039
 */
public class DcPlikImportJpaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Ocr ocr=new Ocr();
    
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

    public void czyscImport() throws NonexistentEntityException {
        Config cfgDir = new ConfigJpaController().findConfigNazwa("dirImportCzasCzyszczenia");
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery<Object> cq = em.getCriteriaBuilder().createQuery();
            Root<DcPlikImport> rt = cq.from(DcPlikImport.class);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -(new Integer(cfgDir.getWartosc())));
            Predicate warDataOd = em.getCriteriaBuilder().lessThan(rt.get(DcPlikImport_.dataDodania), cal.getTime());
            cq.select(rt).where(warDataOd);
            Query q = em.createQuery(cq);
            for (Object pi : q.getResultList()) {
                DcPlikImport pii = (DcPlikImport) pi;
                this.destroy(pii.getId());
            }
        } finally {
            em.close();
        }

    }

    public void importFromDir() {
        Config cfgDir = new ConfigJpaController().findConfigNazwa("dirImportSkan");
        Config cfgDirBak = new ConfigJpaController().findConfigNazwa("dirImportSkanBak");
        File dir = new File(cfgDir.getWartosc());
        for (File f : dir.listFiles()) {
            if (f.setLastModified(new Date().getTime() - 10000) && f.isFile()) {
                //System.err.println(f.getAbsolutePath());
                ByteArrayOutputStream ous = null;
                InputStream ios = null;
                String trescOcr="";
                try {
                    byte[] buffer = new byte[4096];
                    ous = new ByteArrayOutputStream();
                    ios = new FileInputStream(f);
                    int read;
                    while ((read = ios.read(buffer)) != -1) {
                        ous.write(buffer, 0, read);
                    }
                    trescOcr=ocr.oceeruj(f);
                    /*Charset utf8charset = Charset.forName("UTF-8");
                    Charset iso88591charset = Charset.forName("ISO-8859-2");
                    byte[] ocrtmp=trescOcr.getBytes();
                    trescOcr = new String(ocrtmp, iso88591charset);
                    System.err.println("oceeruje automat");
                    System.err.println(trescOcr);
                    //trescOcr = new String(ocrtmp, utf8charset);*/
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DcPlikImportJpaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DcPlikImportJpaController.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (ous != null) {
                            ous.close();
                        }

                        try {
                            if (ios != null) {
                                ios.close();
                            }
                            //przenoszenie pliku
                            if (!f.renameTo(new File(cfgDirBak.getWartosc() + "/" + new Date().getTime() + "_" + f.getName()))) {
                                System.err.println("zapewne brakuje katalogu bak");
                                //throw new IOException();
                            }
                            DcPlikImport plik = new DcPlikImport();
                            plik.setDataDodania(new Date());
                            plik.setNazwa(f.getName());
                            DcPlikImportBin pim = new DcPlikImportBin();
                            pim.setPlik(ous.toByteArray());
                            pim.setTresc(trescOcr);
                            plik.setDcPlikImportBin(pim);
                            this.create(plik);
                            System.err.println("stworzylem dokument dla pliku: "+f.getName());
                        } catch (IOException e) {
                            Logger.getLogger(DcPlikImportJpaController.class.getName()).log(Level.SEVERE, null, e);
                        }
                         catch (NullPointerException e1) {
                            Logger.getLogger(DcPlikImportJpaController.class.getName()).log(Level.SEVERE, null, e1);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(DcPlikImportJpaController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        }
    }
}
