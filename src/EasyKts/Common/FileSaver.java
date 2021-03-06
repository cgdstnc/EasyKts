/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Common;

import EasyKts.System.SettingFunctions;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;

/**
 *
 * @author Administrator
 */
public class FileSaver {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(FileSaver.class.getName());

    private static BufferedImage cloneBufferedImage(BufferedImage img) {
        BufferedImage clone = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = clone.getGraphics();
        g.drawImage(img, 0, 0, null);

        return clone;
    }

    public static void save(BufferedImage img, boolean addWatermark, String fileName) throws IOException {

        File folder = new File(SettingFunctions.getSettings().getOutputPath());
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
        File folder = new File(SettingFunctions.getSettings().getOutputPath());
        folder.mkdirs();
        File f = new File(folder.getAbsolutePath() + "\\" + file);
        FileWriter fileWriter = new FileWriter(f);
        fileWriter.append(text);
        fileWriter.flush();
        fileWriter.close();
    }

    public static void saveJson(Object obj, String file) throws IOException {
        File folder = new File(SettingFunctions.getSettings().getOutputPath());
        folder.mkdirs();
        File f = new File(folder.getAbsolutePath() + "\\" + file);

        String tmp = new Gson().toJson(obj);
        tmp = tmp.replaceAll("\\{", "{\n");
        tmp = tmp.replaceAll("\\}", "}{\n");

        tmp = tmp.replaceAll(",", ",\n");

        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write(tmp);
        fileWriter.flush();
        fileWriter.close();

    }

}
