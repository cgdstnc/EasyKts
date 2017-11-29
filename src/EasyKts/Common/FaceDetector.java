/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Common;

import EasyKts.Controller.MainFrameController;
import EasyKts.Model.Surat;
import com.sun.media.jfxmedia.logging.Logger;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import org.openimaj.math.geometry.shape.Rectangle;

/**
 *
 * @author Administrator
 */
public class FaceDetector {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(FaceDetector.class.getName());

    private static final HaarCascadeDetector faceDetector = new HaarCascadeDetector();
    private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]{1.0f}, 0.0f);

    private Thread faceDetectionThread;
    private Long faceThreadValid;

    private Camera camera;
    private MainFrameController controller;

    public FaceDetector(Camera camera, MainFrameController controller) {
        this.camera = camera;
        this.controller = controller;
    }

    public void startFaceDetectionThread(long timeOut) {
        faceThreadValid = System.currentTimeMillis();

        if (faceDetectionThread == null || faceDetectionThread.getState() == Thread.State.TERMINATED) {
            faceDetectionThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long valid = faceThreadValid;
                    long start = System.currentTimeMillis();
                    int i = 0;

                    while (valid == faceThreadValid) {//yeni thread başladığında arkadaki threadler dursun
                        LOGGER.log(Level.INFO, Thread.currentThread().getId() + " faceThread is Running.");

                        if ((System.currentTimeMillis() - start) > timeOut) {
                            return;
                        }
                        try {
                            BufferedImage newFrame = camera.getImage();

                            List<DetectedFace> biggestFace = faceDetector.detectFaces(ImageUtilities.createFImage(newFrame));

                            if (!biggestFace.isEmpty()) {
                                DetectedFace face = biggestFace.get(0);
                                for (DetectedFace itr : biggestFace) {
                                    if (itr.getBounds().getHeight() * itr.getBounds().getWidth() > face.getBounds().getHeight() * itr.getBounds().getWidth()) {
                                        face = itr;
                                    }
                                }
                                //DetectedFace face = faces.get(0);
                                Rectangle bounds = face.getBounds();

                                int dx = (int) (0.1 * bounds.width);
                                int dy = (int) (0.2 * bounds.height);
                                int x = (int) bounds.x - dx;
                                int y = (int) bounds.y - dy;
                                int w = (int) bounds.width + 2 * dx;
                                int h = (int) bounds.height + dy;
                                BufferedImage buf = null;

                                try {
                                    buf = newFrame.getSubimage(x - 25, y - 25, w + (25 * 2), h + (25 * 2));
                                } catch (Exception e) {
                                    if (String.valueOf(e.getClass()).equalsIgnoreCase("java.awt.image.RasterFormatException")) {
                                        buf = newFrame.getSubimage(x, y, w, h);
                                    }
                                }

                                notifyController(new Surat(newFrame, buf, x, y));
                                System.out.println("i:" + i);

                                i++;
                                if (i > 2) {
                                    valid=-1;
                                    return;
//                                    break;//ilk koyarken yuzu bulunca hemen cikmasin bulaniklik azalsin
                                }
                            }
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                            return;
                        }
                    }
                }
            });
        }
        if (!faceDetectionThread.isAlive()) {
            faceDetectionThread.start();
        }
    }

    private void notifyController(Surat surat) {
        if (controller != null) {
            controller.setSurat(surat);
        }
    }

    public void stop() {
        faceThreadValid = null;
    }

    public void setCamera(Camera camera) {
        stop();
        this.camera = camera;
    }

}
