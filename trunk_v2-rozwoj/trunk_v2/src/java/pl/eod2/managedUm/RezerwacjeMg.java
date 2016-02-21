package pl.eod2.managedUm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.swing.tree.TreeNode;
import pl.eod.abstr.AbstMg;
import pl.eod.managed.Login;
import pl.eod2.encje.UmGrupa;
import pl.eod2.encje.UmMasterGrupa;
import pl.eod2.encje.UmRezerwacje;
import pl.eod2.encje.UmRezerwacjeKontr;
import pl.eod2.encje.UmUrzadzenie;

@ManagedBean(name = "RezerwacjeMg")
@SessionScoped
public class RezerwacjeMg extends AbstMg<UmRezerwacje, UmRezerwacjeKontr> {

    @ManagedProperty(value = "#{login}")
    private Login login;
    @ManagedProperty(value = "#{UrzadzeniaMg}")
    private UrzadzeniaMg urzMg;

    private UmRezerwacjeKontr dcR;
    private List<TreeNode> rootNodes = new ArrayList<>();
    private UmUrzadzenie urzadzenie;
    private Date dataZKal;
    private List<RezerChart> chartList = new ArrayList<>();

    private int wartTest = 20;

    public RezerwacjeMg() throws InstantiationException, IllegalAccessException {
        super("/um/rezerwacje", new UmRezerwacjeKontr(), new UmRezerwacje());
    }

    @Override
    public void refresh() throws InstantiationException, IllegalAccessException {
        super.refresh();
        if (urzadzenie != null) {
            urzadzenie = urzMg.getDcC().findUmUrzadzenie(urzadzenie.getId());
        }
        login.refresh();
        List<UmMasterGrupa> masterList = login.getZalogowany().getUserId().getSpolkaId().getUmMasterGrupaList();
        rootNodes.clear();
        for (UmMasterGrupa mg : masterList) {
            DrzMaster drMa = new DrzMaster(mg);

            for (UmGrupa gr : mg.getGrupaList()) {
                if (!gr.isRezerwacje()) {
                    continue;
                }
                DrzGrupa drGr = new DrzGrupa(drMa, gr);
                for (UmUrzadzenie uz : gr.getUrzadzenieList()) {
                    DrzUrzad drzU = new DrzUrzad(drGr, uz);
                    drGr.getDrzUrzad().add(drzU);
                }
                drMa.getDrzGrupa().add(drGr);
            }

            if (!drMa.getDrzGrupa().isEmpty()) {
                rootNodes.add(drMa);
            }
        }
    }

    @Override
    public void dodaj() throws InstantiationException, IllegalAccessException {
        obiekt.setUrzadzenie(urzadzenie);
        obiekt.setTworca(login.getZalogowany().getUserId());
        super.dodaj(); //To change body of generated methods, choose Tools | Templates.
        //urzadzenie.getRezerwacjeList().add(obiekt);
    }

    public void calList(ValueChangeEvent event) {
        chartList.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) event.getNewValue());
        //wyszukiwanie rezerwacji
        Calendar calplus = Calendar.getInstance();
        calplus.setTime(calendar.getTime());
        calplus.add(Calendar.DATE, 1);
        for (UmRezerwacje rez : urzadzenie.getRezerwacjeList()) {
            if (rez.getDataOd().after(calplus.getTime())) {
                continue;
            }
            if (rez.getDataOd().before(calendar.getTime()) && rez.getDataDo().before(calendar.getTime())) {
                continue;
            }
            Calendar calStSt=Calendar.getInstance();
            calStSt.setTime(rez.getDataOd());
            double start=calStSt.get(Calendar.HOUR_OF_DAY)+(calStSt.get(Calendar.MINUTE)/60);
            calStSt.setTime(rez.getDataDo());
            double stop=calStSt.get(Calendar.HOUR_OF_DAY)+(calStSt.get(Calendar.MINUTE)/60);
            chartList.add(new RezerChart(calendar.getTime(), start, stop, "tworca"));
        }
    }

    public UmRezerwacjeKontr getDcR() {
        return dcR;
    }

    public void setDcR(UmRezerwacjeKontr dcR) {
        this.dcR = dcR;
    }

    public List<TreeNode> getRootNodes() {
        return rootNodes;
    }

    public void setRootNodes(List<TreeNode> rootNodes) {
        this.rootNodes = rootNodes;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public UmUrzadzenie getUrzadzenie() {
        return urzadzenie;
    }

    public void setUrzadzenie(UmUrzadzenie urzadzenie) {
        this.urzadzenie = urzadzenie;
    }

    public Date getDataZKal() {
        return dataZKal;
    }

    public void setDataZKal(Date dataZKal) {
        this.dataZKal = dataZKal;
    }

    public UrzadzeniaMg getUrzMg() {
        return urzMg;
    }

    public void setUrzMg(UrzadzeniaMg urzMg) {
        this.urzMg = urzMg;
    }

    public int getWartTest() {
        return wartTest;
    }

    public void setWartTest(int wartTest) {
        this.wartTest = wartTest;
    }

    public List<RezerChart> getChartList() {
        return chartList;
    }

    public void setChartList(List<RezerChart> chartList) {
        this.chartList = chartList;
    }
}
