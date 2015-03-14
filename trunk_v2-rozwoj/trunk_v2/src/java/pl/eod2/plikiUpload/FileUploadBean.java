package pl.eod2.plikiUpload;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import pl.eod2.encje.DcPlik;
import pl.eod2.encje.exceptions.IllegalOrphanException;
import pl.eod2.encje.exceptions.NonexistentEntityException;
import pl.eod2.managedRej.ImpPlik;
import pl.eod2.managedRej.Rejestracja;

/**
 * @author Ilya Shaikovsky
 *
 */
@ManagedBean
@SessionScoped
public class FileUploadBean implements Serializable {

    private static final long serialVersionUID = 1L;
    //private ArrayList<UploadedImage> files = new ArrayList<UploadedImage>();
    //private int uploadsAvailable = 100;
    //private boolean autoUpload = false;
    //private boolean useFlash = false;
    @ManagedProperty(value = "#{RejestracjaRej}")
    private Rejestracja rejRej;
    @ManagedProperty(value = "#{RejImpPlik}")
    private ImpPlik impPlik;
    /*public int getSize() {
     if (getFiles().size() > 0) {
     return getFiles().size();
     } else {
     return 0;
     }
     }

     public void paint(OutputStream stream, Object object) throws IOException {
     stream.write(getFiles().get((Integer) object).getData());
     }*/

    public void listener(FileUploadEvent event) {
        UploadedFile item = event.getUploadedFile();
        DcPlik dcPlik = new DcPlik();
        //file.setLength(item.getData().length);
        dcPlik.setNazwa(item.getName());
        dcPlik.setPlik(item.getData());
        dcPlik.setIdDok(rejRej.getObiekt());
        dcPlik.setDataDodania(new Date());
        rejRej.getDcPlikC().create(dcPlik);
        rejRej.getObiekt().getDcPlikList().add(dcPlik);
        String error = null;
        try {
            error = rejRej.getDcC().editZmiana(rejRej.getObiekt());
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FileUploadBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        //files.add(file);
        //uploadsAvailable--;
    }

    public void download() {
        final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + rejRej.getPlik().getNazwa() + "\""); // or whatever type you're sending back
        try {
            response.getOutputStream().write(rejRej.getPlik().getPlik()); // from the UploadDetails bean
            response.setContentLength(rejRej.getPlik().getPlik().length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void downloadImport() {
        final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + impPlik.getPlkImp().getNazwa() + "\""); // or whatever type you're sending back
        try {
            response.getOutputStream().write(impPlik.getPlkImp().getDcPlikImportBin().getPlik()); // from the UploadDetails bean
            response.setContentLength(impPlik.getPlkImp().getDcPlikImportBin().getPlik().length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    /*public String clearUploadData() {
     files.clear();
     setUploadsAvailable(5);
     return null;
     }*/
    public long getTimeStamp() {
        return System.currentTimeMillis();
    }

    /*public ArrayList<UploadedImage> getFiles() {
     return files;
     }

     public void setFiles(ArrayList<UploadedImage> files) {
     this.files = files;
     }

     public int getUploadsAvailable() {
     return uploadsAvailable;
     }

     public void setUploadsAvailable(int uploadsAvailable) {
     this.uploadsAvailable = uploadsAvailable;
     }

     public boolean isAutoUpload() {
     return autoUpload;
     }

     public void setAutoUpload(boolean autoUpload) {
     this.autoUpload = autoUpload;
     }

     public boolean isUseFlash() {
     return useFlash;
     }

     public void setUseFlash(boolean useFlash) {
     this.useFlash = useFlash;
     }*/
    public Rejestracja getRejRej() {
        return rejRej;
    }

    public void setRejRej(Rejestracja rejRej) {
        this.rejRej = rejRej;
    }

    public ImpPlik getImpPlik() {
        return impPlik;
    }

    public void setImpPlik(ImpPlik impPlik) {
        this.impPlik = impPlik;
    }
    
}
