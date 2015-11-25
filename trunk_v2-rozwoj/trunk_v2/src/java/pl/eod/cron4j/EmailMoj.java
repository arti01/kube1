/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author arti01
 */
public class EmailMoj {
    private String[] idMsg;
    private String temat;
    private String nadawca;
    private String tresc;
    private Date data;
    private List<EmailZalacznik> zalaczniki;
    private boolean stworzony;

    public EmailMoj() {
        this.zalaczniki=new ArrayList<>();
        this.stworzony=false;
    }

    public String getTemat() {
        return temat;
    }

    public void setTemat(String temat) {
        this.temat = temat;
    }

    public String getNadawca() {
        return nadawca;
    }

    public void setNadawca(String nadawca) {
        this.nadawca = nadawca;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTresc() {
        return tresc;
    }

    public void setTresc(String tresc) {
        this.tresc = tresc;
    }

    public List<EmailZalacznik> getZalaczniki() {
        return zalaczniki;
    }

    public void setZalaczniki(List<EmailZalacznik> zalaczniki) {
        this.zalaczniki = zalaczniki;
    }

    public String[] getIdMsg() {
        return idMsg;
    }

    public void setIdMsg(String[] idMsg) {
        this.idMsg = idMsg;
    }

    public boolean isStworzony() {
        return stworzony;
    }

    public void setStworzony(boolean stworzony) {
        this.stworzony = stworzony;
    }
    
}
