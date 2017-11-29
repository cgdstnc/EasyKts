/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Common;

import EasyKts.Controller.MainFrameController;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
public class BarcodeReader {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(BarcodeReader.class.getName());

    private Thread barcodeThread;
    private Long barcodeThreadValid;

    private Camera camera;
    private MainFrameController controller;

    public BarcodeReader(Camera camera, MainFrameController controller) {
        this.camera = camera;
        this.controller = controller;
    }

    public static Result readBarcode(BufferedImage bi) {
        Map<DecodeHintType, Object> tmpHintsMap = new EnumMap<DecodeHintType, Object>(
                DecodeHintType.class);
        tmpHintsMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        tmpHintsMap.put(DecodeHintType.POSSIBLE_FORMATS,
                EnumSet.allOf(BarcodeFormat.class));
        //tmpHintsMap.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE);

        BinaryBitmap binaryBitmap = new BinaryBitmap(
                new HybridBinarizer(
                        new BufferedImageLuminanceSource(bi)));

        try {
            MultiFormatReader mfr = null;
            mfr = new MultiFormatReader();
            Result result = mfr.decode(binaryBitmap, tmpHintsMap);
            System.out.println(result.getText());
            System.out.println(result.getBarcodeFormat());
            return result;
        } catch (Exception e) {
            if (!e.getClass().toString().contains("com.google.zxing.NotFoundException")) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
            return null;
        }
    }

    public void startBarcodeThread(long timeOut) {
        barcodeThreadValid = System.currentTimeMillis();//

        if (barcodeThread == null || barcodeThread.getState() == Thread.State.TERMINATED) {
            barcodeThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long valid = barcodeThreadValid;
                    long start = System.currentTimeMillis();
                    int i = 0;

                    while (valid == barcodeThreadValid) {//yeni thread başladığında arkadaki threadler dursun
                        LOGGER.log(Level.INFO, Thread.currentThread().getId() + " barcodeThread is Running.");
                        if ((System.currentTimeMillis() - start) > timeOut) {
                            return;
                        }
                        try {
                            BufferedImage newFrame = camera.getImage();
                            if (newFrame != null) {
                                Result r = BarcodeReader.readBarcode(newFrame);
                                if (r != null) {
                                    notifyController(r);
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        if (!barcodeThread.isAlive()) {
            // System.out.println("barcodeThread:" + barcodeThread.getState().toString());
            barcodeThread.start();
        }
    }

    public void stop() {
        barcodeThreadValid = null;
    }

    public void setCamera(Camera camera) {
        stop();
        this.camera = camera;
    }

    private void notifyController(Result r) {
        if (controller != null) {
            controller.setBarcodeResult(r);
        }
    }
}
