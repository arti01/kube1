package pl.eod2.managedOdb;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import pl.eod.wydruki.DcDokPoczta;
import pl.eod.wydruki.DcDokPocztaList;
import pl.eod.wydruki.PDFHandler;
import pl.eod2.encje.DcDokument;

@ManagedBean(name = "HistOdb")
@SessionScoped
public class Historia extends Akcept {

    @Override
    public String list() {
        return "/dcodb/histList";
    }

    @Override
    public String detale() {
        return "/dcodb/histDetale?faces-redirect=true";
    }

    public void druk() {
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        String templateFilePath = absolutePath + "/../../../../../resources/wydruki/";
        String fopConf = absolutePath + "/../../../fop-xconf.xml";
        
        System.err.println(getObiekt());

        DcDokPoczta dcDokPoczta = new DcDokPoczta(getObiekt());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*try {
            akceptListDruk.setDataOd(sdf.format(filtrDataRejOd));
            akceptListDruk.setDataDo(sdf.format(filtrDataRejDo));
        } catch (java.lang.NullPointerException npe) {
        }*/

        PDFHandler handler = new PDFHandler();
        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(dcDokPoczta);
            ByteArrayOutputStream pdf = handler.createPDFStream(streamSource, templateFilePath + "historiaAkcept.xsl", fopConf);
            //System.err.println(pdf);
            final HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setHeader("Content-Disposition", "attachment;filename=\"" + "wydruk.pdf" + "\""); // or whatever type you're sending back
            try {
                response.getOutputStream().write(pdf.toByteArray()); // from the UploadDetails bean
                response.setContentLength(pdf.toByteArray().length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
