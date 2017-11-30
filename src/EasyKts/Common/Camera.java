/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Common;

import EasyKts.Controller.MainFrameController;
import EasyKts.Controller.CustomWebcamPanel;
import EasyKts.System.SettingFunctions;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;
import com.github.sarxos.webcam.WebcamPanel;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

/**
 *
 * @author Administrator
 */
public class Camera implements WebcamMotionListener {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Camera.class.getName());

    private Webcam webcam = null;
    private CustomWebcamPanel webcamPanel = null;
    private WebcamMotionDetector motionDetector;

    private MainFrameController controller;

    public Camera(MainFrameController controller) {
        if (Webcam.getWebcams().size() < 1) {
            JOptionPane.showMessageDialog(null, "Hiçbir kamera bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return;
        }
        this.controller = controller;
    }

    public String[] getWebcamNames() {
        String[] names = new String[Webcam.getWebcams().size()];
        for (int i = 0; i < Webcam.getWebcams().size(); i++) {
            names[i] = Webcam.getWebcams().get(i).getName();
        }
        return names;
    }

    private Dimension getMaxSize(Webcam webcam) {
        Dimension[] dims = webcam.getViewSizes();
        Dimension max = dims[0];
        for (Dimension dim : dims) {
            if (dim.height > max.height) {
                max = dim;
            }
        }

        return max;
    }

    public int changeCam(int index) {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        webcam = Webcam.getWebcams().get(index);
        Dimension dim = getMaxSize(webcam);
        webcam.setViewSize(dim);
        webcamPanel = new CustomWebcamPanel(webcam, false,controller);
        webcamPanel.setFPSDisplayed(false);

        if (webcam.isOpen()) {
            webcam.close();
        }

        int i = 0;
        do {
            if (webcam.getLock().isLocked()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return 0;
                }
            } else {
                break;
            }
        } while (i++ < 3);

        try {
            webcam.open();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Kamera Başka Bir Uygulama Tarafından Kullanılmakta!", "Hata", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }

        webcamPanel.start();
        return index;
    }

    public int changeCam(String cameraName) {
        String[] cams = getWebcamNames();
        for (int i = 0; i < cams.length; i++) {
            String cam = cams[i];
            if (cam.equalsIgnoreCase(cameraName)) {
                return changeCam(i);
            }
        }
        return changeCam(0);
    }

    public void startWebcamMotionDetector() throws Throwable {
        if (webcam != null) {
            if (motionDetector != null) {
                motionDetector.stop();
            }
            motionDetector = new WebcamMotionDetector(webcam);
            motionDetector.setInterval(500);
            motionDetector.setPixelThreshold(90);
            motionDetector.addMotionListener(this);
            motionDetector.start();
        }
    }

    public synchronized BufferedImage getImage() {
        return webcam.getImage();
    }

    public CustomWebcamPanel getWebcamPanel() {
        return webcamPanel;
    }

    @Override
    public void motionDetected(WebcamMotionEvent wme) {
        LOGGER.log(Level.INFO,"Hareket algilandi.",wme);
        
        controller.setLastMotion(System.currentTimeMillis());
        
        if (SettingFunctions.getSettings().getResetBarcodeOnMotion()) {
            controller.resetBarcode(true);
        }
        if (SettingFunctions.getSettings().getResetFaceOnMotion()) {
            controller.resetFace(true);
        }
//        controller.reset();
    }
}
