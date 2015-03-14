/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRej;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpServletResponse;
import pl.eod.wydruki.DcDokPocztaList;
import pl.eod.wydruki.PDFHandler;
import pl.eod2.encje.DcDokument;
import pl.eod2.encje.DcDokumentJpaController;
import pl.eod2.encje.DcRodzajGrupa;
import pl.eod2.encje.DcZrodlo;

@ManagedBean(name = "RaportyRej")
@SessionScoped
public class Raporty {

    private DcDokumentJpaController dcC;
    private DataModel<DcDokument> lista = new ListDataModel<>();
    private DcRodzajGrupa filtrRodzGrupa;
    private Date filtrDataRejOd;
    private Date filtrDataRejDo;
    private DcZrodlo filtrZrodlo;
    String templateFilePath;
    String fopConf;
    boolean pokazDruk = false;

    @PostConstruct
    void init() {
        dcC = new DcDokumentJpaController();
        String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        absolutePath = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        templateFilePath = absolutePath + "/../../../../../resources/wydruki/";
        fopConf = absolutePath + "/../../../fop-xconf.xml";
    }

    public String raportPocz() {
        return "/dcrej/rapPocztowy";
    }

    public void pokazRap() {
        lista.setWrappedData(dcC.findRaport(filtrRodzGrupa, filtrDataRejOd, filtrDataRejDo, filtrZrodlo));
        if (lista.getRowCount() > 0) {
            pokazDruk = true;
        } else {
            pokazDruk = false;
        }
    }

    @SuppressWarnings("unchecked")
    public void druk() {
        DcDokPocztaList pocztaList = new DcDokPocztaList((List<DcDokument>) lista.getWrappedData());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            pocztaList.setDataOd(sdf.format(filtrDataRejOd));
            pocztaList.setDataDo(sdf.format(filtrDataRejDo));
        } catch (java.lang.NullPointerException npe) {
        }
        PDFHandler handler = new PDFHandler();
        try {
            ByteArrayOutputStream streamSource = handler.getXMLSource(pocztaList);
            ByteArrayOutputStream pdf = handler.createPDFStream(streamSource, templateFilePath + "rapPocztowy.xsl", fopConf);
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

    public DataModel<DcDokument> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcDokument> lista) {
        this.lista = lista;
    }

    public void setFiltrRodzGrupa(DcRodzajGrupa filtrRodzGrupa) {
        this.filtrRodzGrupa = filtrRodzGrupa;
    }

    public DcRodzajGrupa getFiltrRodzGrupa() {
        return filtrRodzGrupa;
    }

    public Date getFiltrDataRejOd() {
        return filtrDataRejOd;
    }

    public void setFiltrDataRejOd(Date filtrDataRejOd) {
        this.filtrDataRejOd = filtrDataRejOd;
    }

    public Date getFiltrDataRejDo() {
        return filtrDataRejDo;
    }

    public void setFiltrDataRejDo(Date filtrDataRejDo) {
        this.filtrDataRejDo = filtrDataRejDo;
    }

    public DcZrodlo getFiltrZrodlo() {
        return filtrZrodlo;
    }

    public void setFiltrZrodlo(DcZrodlo filtrZrodlo) {
        this.filtrZrodlo = filtrZrodlo;
    }

    public boolean isPokazDruk() {
        return pokazDruk;
    }

    public void setPokazDruk(boolean pokazDruk) {
        this.pokazDruk = pokazDruk;
    }

}
