/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod2.managedUm;

import java.util.Date;

/**
 *
 * @author arti01
 */
public class RezerChart {
    private Date data;
    private int start;
    private int stop;
    private String tworca;

    public RezerChart(Date data, int start, int stop, String tworca) {
        this.data = data;
        this.start = start;
        this.stop = stop;
        this.tworca = tworca;
    }

    
    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public String getTworca() {
        return tworca;
    }

    public void setTworca(String tworca) {
        this.tworca = tworca;
    }
    
    
}
