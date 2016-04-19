package pl.eod2.plikiUpload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import pl.eod2.encje.DcPlik;
import pl.eod2.encje.DcPlikJpaController;

@ManagedBean(name = "WyswietlPdf")
@SessionScoped
public class WyswietlPdf {

    private DcPlikJpaController dcPlikC;

    public void paintFota(OutputStream stream, Object object) {
        dcPlikC = new DcPlikJpaController();
        DcPlik plikW = dcPlikC.findDcPlik((int) object);
        try {
            stream.write(plikW.getPlik());
        } catch (IOException ex) {
            ex.printStackTrace();//test 
            Logger.getLogger(WyswietlPdf.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }
    }

    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        dcPlikC = new DcPlikJpaController();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return new DefaultStreamedContent();
        } else {
            // So, browser is requesting the image. Get ID value from actual request param.
            String id = context.getExternalContext().getRequestParameterMap().get("id");
            DcPlik plikW = dcPlikC.findDcPlik(new Long(id).intValue());
            byte[] image = plikW.getPlik();
            return new DefaultStreamedContent(new ByteArrayInputStream(image));
        }
    }
}
