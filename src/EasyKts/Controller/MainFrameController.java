/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Controller;

import EasyKts.Common.BarcodeReader;
import EasyKts.Common.Camera;
import EasyKts.Common.FaceDetector;
import EasyKts.Common.FileSaver;
import EasyKts.Model.Settings;
import EasyKts.Model.Surat;
import EasyKts.System.SettingFunctions;
import EasyKts.System.projectEnum;
import EasyKts.View.MainFrame;
import com.google.zxing.Result;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class MainFrameController {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(MainFrameController.class.getName());

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private MainFrame frame;

    private Camera camera;
    private FaceDetector faceDetector;
    private BarcodeReader barcodeReader;

    private boolean initState = true;

    private Surat currentSurat;
    private Result barcodeResult;

    public MainFrameController() {
        MainFrame view = new MainFrame();
        view.setLocationRelativeTo(null);
        view.setSize((int) screenSize.width / 3, (int) (screenSize.height / 5) * 4);
        view.setVisible(true);

        this.frame = view;

        camera = new Camera(this);
        projectEnum.mode mode = SettingFunctions.getSettings().getMode();
        switch (mode) {
            case BARCODE:
                barcodeReader = new BarcodeReader(camera, this);
                frame.jpFaceMainContainer.setVisible(false);
                break;
            case FACE:
                faceDetector = new FaceDetector(camera, this);
                frame.jpBarcodeContainer.setVisible(false);
                break;
            case BOTH:
                faceDetector = new FaceDetector(camera, this);
                barcodeReader = new BarcodeReader(camera, this);
                break;
            default:
                faceDetector = new FaceDetector(camera, this);
                barcodeReader = new BarcodeReader(camera, this);
                break;
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(camera.getWebcamNames());
        frame.jcbCams.setModel(model);

        frame.setLocationRelativeTo(null);
        frame.setTitle("Kimlik Tanıma Sistemi");
        frame.jfSaveOnay.setTitle("Kimlik Tanıma Sistemi");

        try {
            ImageIcon img = new ImageIcon("resource/logo.png");
            frame.setIconImage(img.getImage());
            frame.jfSaveOnay.setIconImage(img.getImage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        //INIT CAMERA AND RUN THREADS
        new Thread(new Runnable() {
            @Override
            public void run() {
                int changedCamIndex = camera.changeCam(SettingFunctions.getSettings().getDefaultCamera());
                frame.jcbCams.setSelectedIndex(changedCamIndex);
                resetFace(true);
                resetBarcode(true);
            }
        }).start();

        addActionListeners();
    }

    private void addActionListeners() {
        //Camera ComboBox
        frame.jcbCams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                camera.changeCam(frame.jcbCams.getSelectedIndex());
                Settings s = SettingFunctions.getSettings();
                s.setDefaultCamera(frame.jcbCams.getSelectedItem().toString());
                SettingFunctions.setSettings(s);

                if (faceDetector != null) {
                    faceDetector.setCamera(camera);
                }
                if (barcodeReader != null) {
                    barcodeReader.setCamera(camera);
                }

                frame.jpCamera.removeAll();
                BoxLayout boxLayout0 = new BoxLayout(frame.jpCamera, BoxLayout.PAGE_AXIS);
                frame.jpCamera.setLayout(boxLayout0);
                frame.jpCamera.add(camera.getWebcamPanel());
                frame.jpCamera.revalidate();
                frame.jpCamera.repaint();

                try {
                    camera.startWebcamMotionDetector();
                } catch (Throwable ex) {
                    LOGGER.log(Level.SEVERE, ex.toString(), ex);
                }
            }
        });

        //Kimlik bölümündeki reset
        frame.jbKimlikReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFace(true);
            }
        });

        //Barcode yeniden ara
        frame.jbBarcodeReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBarcode(true);
            }
        });

        //Reset All
        frame.jbResetAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFace(true);
                resetBarcode(true);
            }
        });

        //Save All
        frame.jbSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SettingFunctions.getSettings().getKullaniciOnayIste()) {
                    if (faceDetector != null) {
                        faceDetector.stop();
                    }
                    if (barcodeReader != null) {
                        barcodeReader.stop();
                    }

                    frame.jfSaveOnay.setSize((int) screenSize.width / 2, 165);
                    frame.jfSaveOnay.setLocationRelativeTo(null);
                    frame.jfSaveOnay.setVisible(true);
                    frame.jcbKaydetOnay.setSelected(false);
                    frame.jbSaveFinal.setEnabled(false);
                } else {
                    saveAll();
                }

            }
        });

        //Kullanıcı kontrol ettim CheckBox'ı
        frame.jcbKaydetOnay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jbSaveFinal.setEnabled(frame.jcbKaydetOnay.isSelected());
            }
        });

        //SaveAndExit
        frame.jbSaveFinal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAll();
            }
        });

        //ResetAll
        frame.jbStartOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jfSaveOnay.setVisible(false);
                resetFace(true);
                resetBarcode(true);
            }
        });

        //Panel Mouse Listener
        frame.jpCamera.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                camera.getWebcamPanel().mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (faceDetector != null) {
                    if (initState) {
                        faceDetector.stop();
                        resetFace(false);
                    }
                }
                camera.getWebcamPanel().mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (faceDetector != null) {
                    faceDetector.stop();
                    resetFace(false);
                    camera.getWebcamPanel().mouseReleased(e);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                camera.getWebcamPanel().mouseEntered(e);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                camera.getWebcamPanel().mouseExited(e);
            }
        });

        frame.jpCamera.addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                camera.getWebcamPanel().mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                camera.getWebcamPanel().mouseMoved(e);
            }
        });

    }

    public void saveAll() {
        if (faceDetector != null) {
            faceDetector.stop();
        }
        if (barcodeReader != null) {
            barcodeReader.stop();
        }
        boolean success = true;
        if (currentSurat != null) {
            try {
                FileSaver.save(currentSurat.getSource(), true, "kimlik.jpg");
                FileSaver.save(currentSurat.getSurat(), false, "surat.jpg");
            } catch (Exception ex) {
                success = false;
                JOptionPane.showMessageDialog(null,
                        "Kimlik fotoğrafları kaydı sırasında"
                        + " bir hata ile karşılaşıldı!",
                        "Hata", JOptionPane.ERROR_MESSAGE);

                LOGGER.log(Level.SEVERE, ex.toString(), ex);
            }
        }
        if (barcodeResult != null) {
            if (SettingFunctions.getSettings().getBarcodeSaveMode().equals(projectEnum.BarcodeSaveFormat.json)) {
                try {
                    FileSaver.saveJson(barcodeResult, "barcodeResult.json");
                } catch (Exception ex) {
                    success = false;
                    JOptionPane.showMessageDialog(null,
                            "Barkod bilgisi kaydı sırasında"
                            + " bir hata ile karşılaşıldı!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.SEVERE, ex.toString(), ex);
                }
            } else {
                try {
                    FileSaver.save(barcodeResult.getText(), "barcodeResult.txt");
                    FileSaver.save(barcodeResult.getBarcodeFormat().toString(), "barcodeFormat.txt");
                } catch (Exception ex) {
                    success = false;
                    JOptionPane.showMessageDialog(null,
                            "Barkod bilgisi kaydı sırasında"
                            + " bir hata ile karşılaşıldı!",
                            "Hata", JOptionPane.ERROR_MESSAGE);
                    LOGGER.log(Level.SEVERE, ex.toString(), ex);
                }
            }
        }
        if (success) {
            if (SettingFunctions.getSettings().getExitOnSave()) {
                System.exit(0);
            }
        }
    }

    public void setSurat(Surat surat) {
        currentSurat = surat;
        frame.jlFace.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(110, 110, 1)));
        frame.jlFaceOnay.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(110, 110, 1)));
        frame.jlKimlikOnay.setIcon(new ImageIcon(surat.getSource().getScaledInstance(196, 110, 1)));//16:9

        frame.jlFace.revalidate();
        frame.jlFace.repaint();
        frame.jlFaceOnay.revalidate();
        frame.jlFaceOnay.repaint();
        frame.jlKimlikOnay.revalidate();
        frame.jlKimlikOnay.revalidate();
        
        checkAutoSave();
    }

    public void setBarcodeResult(Result result) {
        this.barcodeResult = result;
        frame.jlBarcodeText.setText(result.getText());
        frame.jlBarcodeOkunan.setText(result.getText());
        
        checkAutoSave();
    }

    public void resetBarcode(boolean scan) {
        barcodeResult = null;
        frame.jlBarcodeText.setText("...");
        frame.jlBarcodeOkunan.setText("Okunamadı!");
        if (scan) {
            if (barcodeReader != null) {
                barcodeReader.startBarcodeThread(915000L);
            }
        }
    }

    public void resetFace(boolean scan) {
        if (faceDetector == null) {
            return;
        }
        currentSurat = null;

        frame.jlFace.setIcon(null);
        frame.jlFace.revalidate();
        frame.jlFace.repaint();

        frame.jlFaceOnay.setIcon(null);
        frame.jlFaceOnay.revalidate();
        frame.jlFaceOnay.repaint();

        frame.jlKimlikOnay.setIcon(null);
        frame.jlKimlikOnay.revalidate();
        frame.jlKimlikOnay.repaint();

        if (scan) {
            faceDetector.startFaceDetectionThread(915000L);
        }
    }

    public void setLastMotion(long l) {
        if (faceDetector != null) {
            faceDetector.setLastMove(l);
        }
    }

    public void checkAutoSave() {
        if (SettingFunctions.getSettings().getAutoSave()) {
            if ((barcodeResult != null) && (currentSurat != null)) {
                saveAll();
            }
        }
    }
}
