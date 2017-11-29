/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Controller;

import EasyKts.Model.Surat;
import EasyKts.System.SettingFunctions;
import EasyKts.System.projectEnum;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Cagdas
 */
public class CustomWebcamPanel extends WebcamPanel { // implements MouseListener, MouseMotionListener bu olay yemiyor.

    private MainFrameController controller;
    private Webcam webcam;

    private boolean crop = false;
    private Integer x0;
    private Integer y0;
    private Integer x1;
    private Integer y1;

    public CustomWebcamPanel(Webcam webcam) {
        super(webcam);
    }

    public CustomWebcamPanel(Webcam webcam, boolean start) {
        super(webcam, start);
    }

    public CustomWebcamPanel(Webcam webcam, boolean start, MainFrameController controller) {
        super(webcam, start);
        this.controller = controller;
        this.webcam = webcam;
    }

    public CustomWebcamPanel(Webcam webcam, Dimension size, boolean start) {
        super(webcam, size, start);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (crop) {
            if (SettingFunctions.getSettings().getMode().equals(projectEnum.mode.BARCODE)) {
                return;
            }
            g.setColor(Color.red);
            Graphics2D g2 = (Graphics2D) g;
            float thickness = 2.5f;
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRect(Math.min(x0, x1), Math.min(y0, y1), Math.abs((x0 - x1)), Math.abs((y0 - y1)));
        }
    }

    public BufferedImage getScreenShot() {
        boolean cropBeforeState = crop;
        crop = false;//screenshotta kırmızı çerçeve olmasın;

        Dimension size = getSize();
        BufferedImage screenShot = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = screenShot.createGraphics();
        paint(g2);

        crop = cropBeforeState;
        return screenShot;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        System.out.println(this.toString());
        x0 = e.getX();
        y0 = e.getY();
        x1 = e.getX();
        y1 = e.getY();
        crop = true;
    }

    public void mouseReleased(MouseEvent e) {
        BufferedImage screenShot = getScreenShot();
        BufferedImage cameraImage = webcam.getImage();

        Surat surat = new Surat(cameraImage,
                screenShot.getSubimage(Math.min(x0, x1),
                        Math.min(y0, y1), Math.abs((x1 - x0)),
                        Math.abs((y1 - y0))), Math.min(x0, x1),
                Math.min(y0, y1));
        
        controller.setSurat(surat);
        crop = false;
        repaint();
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {
        x1 = e.getX();
        y1 = e.getY();
        System.out.println(this.toString());
        repaint();
    }

    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public String toString() {
        return "CustomWebcamPanel{" + "crop=" + crop + ", x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + '}';
    }

}
