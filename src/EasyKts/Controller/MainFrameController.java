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
import EasyKts.View.EditablePanel;
import EasyKts.View.MainFrame;
import com.google.zxing.Result;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private EditablePanel editablePanel;

    private Camera camera;
    private FaceDetector faceDetector;
    private BarcodeReader barcodeReader;

    private boolean initState = true;
    private boolean lockFoundFace = false;
    private boolean lockFoundBarcode = false;

    private Surat currentSurat;
    private Result barcodeResult;

    public MainFrameController() {
        MainFrame view = new MainFrame();
        view.setLocationRelativeTo(null);
        view.setSize((int) screenSize.width / 3, (int) (screenSize.height / 5) * 4);
        view.setVisible(true);

        this.frame = view;

        camera = new Camera(this);
        faceDetector = new FaceDetector(camera, this);
        barcodeReader = new BarcodeReader(camera, this);

        DefaultComboBoxModel model = new DefaultComboBoxModel(camera.getWebcamNames());
        frame.jcbCams.setModel(model);

        GridLayout gl = new GridLayout(0, 1);
        frame.jpImage.setLayout(gl);

        frame.setLocationRelativeTo(null);
        frame.setTitle("Kimlik Tanıma Sistemi");
        frame.jfManuel.setTitle("Kimlik Tanıma Sistemi");
        frame.jfSaveOnay.setTitle("Kimlik Tanıma Sistemi");

        try {
            ImageIcon img = new ImageIcon("resource/logo.png");
            frame.setIconImage(img.getImage());
            frame.jfManuel.setIconImage(img.getImage());
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
                reset();
            }
        }).start();

        addActionListeners();
    }

    private void addActionListeners() {
        //Camera ComboBox
        frame.jcbCams.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (initState) {
//                    initState = false;
//                    return;
//                }
                camera.changeCam(frame.jcbCams.getSelectedIndex());
                Settings s = new Settings();
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

        //Kimlik bölümündeki save button
        frame.jbKimlikSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jbKimlikSave.setEnabled(false);
                faceDetector.stop();
                lockFoundFace = true;
            }
        });

        //Kimlik bölümündeki reset
        frame.jbKimlikReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jbKimlikSave.setEnabled(true);
                lockFoundFace = false;
                reset();
            }
        });

        //Kimlik bölümündeki manuel
        frame.jbKimlikManuel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lockFoundFace = true;
                faceDetector.stop();
                reset();

                BufferedImage anlik = camera.getImage();
                editablePanel = new EditablePanel(anlik);
                frame.jpImage.removeAll();
                frame.jpImage.add(editablePanel);
                frame.jpImage.repaint();
                frame.jfManuel.setSize(anlik.getWidth(), anlik.getHeight() + frame.jpManuelKaydet.getHeight());
                frame.jfManuel.setVisible(true);
            }
        });

        //Manuel yüz seçme frame indeki kaydet Buttonu
        frame.jbManuelKaydet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                faceDetector.stop();
                lockFoundFace = true;
                reset();
                setSurat(new Surat(editablePanel.getCameraImage(), editablePanel.getSelectedArea(), editablePanel.getFaceX(), editablePanel.getFaceY()));
                lockFoundFace = true;
                frame.jbKimlikSave.setEnabled(false);
            }
        });

        //Barcode yeniden ara
        frame.jbBarcodeReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lockFoundBarcode = false;
                reset();
            }
        });

        //Reset All
        frame.jbResetAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jbKimlikSave.setEnabled(true);
                lockFoundBarcode = false;
                lockFoundFace = false;
                reset();
            }
        });

        //Save All
        frame.jbSaveAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                faceDetector.stop();
                lockFoundFace = true;
                barcodeReader.stop();
                lockFoundBarcode=true;
                
                frame.jfSaveOnay.setSize((int) screenSize.width / 2, 165);
                frame.jfSaveOnay.setLocationRelativeTo(null);
                frame.jfSaveOnay.setVisible(true);
                frame.jcbKaydetOnay.setSelected(false);
                frame.jbSaveFinal.setEnabled(false);
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
                faceDetector.stop();
                barcodeReader.stop();
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
                    try {
                        FileSaver.save(barcodeResult.getText(), "barcodeResult.txt");
                        FileSaver.save(barcodeResult.getBarcodeFormat().toString(), "barcodeFormat.txt");
                    } catch (Exception ex) {
                        success = false;
                        LOGGER.log(Level.SEVERE, ex.toString(), ex);
                    }
                }
                if (success) {
                    System.exit(0);
                }
            }
        });

        //ResetAll
        frame.jbStartOver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jbKimlikSave.setEnabled(true);
                lockFoundBarcode = false;
                lockFoundFace = false;
                frame.jfSaveOnay.setVisible(false);
                reset();
            }
        });
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
    }

    public void setBarcodeResult(Result result) {
        lockFoundBarcode = true;
        this.barcodeResult = result;
        frame.jlBarcodeText.setText(result.getText());
        frame.jlBarcodeOkunan.setText(result.getText());
    }

    public void reset() {
        frame.jpCamera.revalidate();
        frame.jpCamera.repaint();
        if (!lockFoundFace) {
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

            faceDetector.startFaceDetectionThread(15000L);
        }
        if (!lockFoundBarcode) {
            barcodeResult = null;
            frame.jlBarcodeText.setText("...");
            frame.jlBarcodeOkunan.setText("Okunamadı!");
            barcodeReader.startBarcodeThread(15000L);
        }
    }
}
