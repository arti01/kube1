/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

/**
 *
 * @author arti01
 */
public class EmailZalacznik {
    private String nazwa;
    private byte[] plik;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public byte[] getPlik() {
        return plik;
    }

    public void setPlik(byte[] plik) {
        this.plik = plik;
    }
    
    
    
}
