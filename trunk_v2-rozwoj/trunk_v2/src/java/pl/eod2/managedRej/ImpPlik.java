/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedRej;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.eod2.encje.DcPlikImport;
import pl.eod2.encje.DcPlikImportJpaController;

@ManagedBean(name = "RejImpPlik")
@SessionScoped
public class ImpPlik {

    DcPlikImportJpaController dcpiC;
    private DataModel<DcPlikImport> lista = new ListDataModel<>();
    DcPlikImport plkImp;
    String paginator="10";

    @PostConstruct
    void init() {
        dcpiC = new DcPlikImportJpaController();
        //lista.setWrappedData(dcpiC.findDcPlikImportEntities());
    }

    public String list() {
        lista.setWrappedData(dcpiC.findDcPlikImportEntities());
        return "/dcrej/pliki";
    }
    
    public void zaznaczWszystkie(){
        for(DcPlikImport pi:lista){
            pi.setWybrany(true);
        }
    }

    public DataModel<DcPlikImport> getLista() {
        return lista;
    }

    public void setLista(DataModel<DcPlikImport> lista) {
        this.lista = lista;
    }
    
    public DcPlikImport getPlkImp() {
        return plkImp;
    }

    public void setPlkImp(DcPlikImport plkImp) {
        this.plkImp = plkImp;
    }

    public String getPaginator() {
        return paginator;
    }

    public void setPaginator(String paginator) {
        this.paginator = paginator;
    }
    
}
