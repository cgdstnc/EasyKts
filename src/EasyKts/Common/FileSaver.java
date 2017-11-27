/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class FileSaver {

    private static BufferedImage cloneBufferedImage(BufferedImage img) {
        BufferedImage clone = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = clone.getGraphics();
        g.drawImage(img, 0, 0, null);

        return clone;
    }

    public static void save(BufferedImage img, boolean addWatermark, String fileName) throws IOException {
        File folder = new File("KimlikOutput");
        folder.mkdirs();
        File f = new File(folder.getAbsolutePath() + "\\" + fileName);

        if (addWatermark) {
            BufferedImage withWatermark = cloneBufferedImage(img);
            Graphics g = withWatermark.getGraphics();
            g.setColor(new Color(10, 10, 10, 90));
            g.setFont(new Font(g.getFont().getName(), Font.ITALIC, 28));
            for (int i = 0; i < withWatermark.getHeight(); i += 32) {
                g.drawString("Kimlik Tanima Sistemi Ile Elde Edilmistir Kimlik Tanima Sistemi Ile Elde Edilmistir  Kimlik Tanima Sistemi Ile Elde Edilmistir", 10, i);
            }
            ImageIO.write(withWatermark, "JPG", f);
        } else {
            ImageIO.write(img, "JPG", f);
        }
    }

    public static void save(String text, String file) throws IOException {
        File folder = new File("KimlikOutput");
        folder.mkdirs();
        File f = new File(folder.getAbsolutePath() + "\\" + file);
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.append(text);
        fileWriter.flush();
        fileWriter.close();
    }

}
