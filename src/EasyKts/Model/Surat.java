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
public class Surat {
    private BufferedImage source;
    private BufferedImage surat;
    private int x,y;

    public Surat(BufferedImage source, BufferedImage surat, int x, int y) {
        this.source = source;
        this.surat = surat;
        this.x = x;
        this.y = y;
    }

    public BufferedImage getSource() {
        return source;
    }

    public BufferedImage getSurat() {
        return surat;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
