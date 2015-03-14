package pl.eod2.managedRej;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import pl.eod.wydruki.DcDokPocztaList;
import pl.eod.wydruki.PDFHandler;
import pl.eod2.encje.DcDokument;

@ManagedBean(name = "BrakowanieRej")
@SessionScoped
public class Brakowane extends Rejestracja {

    @ManagedProperty(value = "#{RaportyRej}")
    Raporty raporty;
    
    int listaSize = 0;

    public Brakowane() {
        super();
        super.init();
    }

    @PostConstruct
    @Override
    @SuppressWarnings("unchecked")
    public void refreshObiekt() {
        super.refreshObiekt();
        List<DcDokument> l = new ArrayList<>();
        for (DcDokument d : (List<DcDokument>) lista.getWrappedData()) {
            if (d.isAlertBrakowanie()) {
                l.add(d);
            }
        }
        listaSize = l.size();
        lista.setWrappedData(l);
    }

    @Override
    public String list() {
        refreshObiekt();
        return "/dcrej/dokumentyDoBrak";
    }

    public int getListaSize() {
        return listaSize;
    }

    public void setListaSize(int listaSize) {
        this.listaSize = listaSize;
    }

    @SuppressWarnings("unchecked")
    public void druk() {
        //System.err.println("druk");
        DcDokPocztaList pocztaList = new DcDokPocztaList((List<DcDokument>) lista.getWrappedData());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PDFHandler handler = new PDFHandler();
        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(pocztaList);
            ByteArrayOutputStream pdf = handler.createPDFStream(streamSource, raporty.templateFilePath + "rapBrak.xsl", raporty.fopConf);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Raporty getRaporty() {
        return raporty;
    }

    public void setRaporty(Raporty raporty) {
        this.raporty = raporty;
    }
    
}
