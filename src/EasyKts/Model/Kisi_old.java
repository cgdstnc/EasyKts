/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Model;

import java.awt.image.BufferedImage;

/**
 *
 * @author Administrator
 */
public class Kisi_old {

    private BufferedImage kimlik;
    private BufferedImage surat;
    private Long tc;
    private int suratX,SuratY;
    private Long key;

    public Kisi_old() {
    }

    public BufferedImage getKimlik() {
        return kimlik;
    }

    public void setKimlik(BufferedImage kimlik) {
        this.kimlik = kimlik;
    }

    public BufferedImage getSurat() {
        return surat;
    }

    public void setSurat(BufferedImage surat) {
        this.surat = surat;
    }

    public Long getTc() {
        return tc;
    }

    public void setTc(Long tc) {
        this.tc = tc;
    }

    public int getSuratX() {
        return suratX;
    }

    public void setSuratX(int suratX) {
        this.suratX = suratX;
    }

    public int getSuratY() {
        return SuratY;
    }

    public void setSuratY(int SuratY) {
        this.SuratY = SuratY;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    
}
