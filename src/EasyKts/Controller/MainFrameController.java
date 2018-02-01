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
import EasyKts.System.GeneralFunctions;
import EasyKts.System.SettingFunctions;
import EasyKts.System.projectEnum;
import EasyKts.View.MainFrame;
import com.google.zxing.Result;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.logging.Level;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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

    private String beepPath = "resource/beep.wav";

    public MainFrameController() {
        MainFrame view = new MainFrame();
        view.setLocationRelativeTo(null);
        view.setSize((int) screenSize.width / (5 / 2), (int) (screenSize.height / 5) * 4);
//        view.jfSettings.setSize((int) (screenSize.width / 2.75), (int) (screenSize.height / 1.85));
        view.jfSettings.setSize(500, 395);
        view.jfSettings.setLocationRelativeTo(null);

        view.jbSettings.setVisible(SettingFunctions.getSettings().getUserCanChangeSettings());
        Boolean isFirstRun = SettingFunctions.getSettings().getFirstRun();

        if (isFirstRun) {
            view.jtfSaveLocation.setText(new File(SettingFunctions.getSettings().getOutputPath()).getAbsolutePath());
            view.jfSettings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        } else {
            view.jfSettings.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        }

        view.jfSettings.setVisible(isFirstRun);
        view.setVisible(!isFirstRun);

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
            frame.jfSettings.setIconImage(img.getImage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }

        try {
            ImageIcon img = new ImageIcon("resource/run.gif");
            frame.jlGifBarcode.setIcon(img);
            frame.jlGifFace.setIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
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
                frame.jfSaveOnay.setVisible(false);
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

        frame.jbSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.jtfSaveLocation.setText(SettingFunctions.getSettings().getOutputPath());
                frame.jfSettings.setVisible(true);
            }
        });

        frame.jbSaveLocationButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Kayıt Klasoru");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
//                    System.out.println("getCurrentDirectory(): "
//                            + chooser.getCurrentDirectory());
//                    System.out.println("getSelectedFile() : "
//                            + chooser.getSelectedFile());
                    frame.jtfSaveLocation.setText(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    System.out.println("No Selection ");
                }
            }
        });

        frame.jbSettingsSave.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Settings newSettings = SettingFunctions.getSettings();
                newSettings.setKullaniciOnayIste(frame.jcbKullanicidanOnayIste.isSelected());
                newSettings.setResetFaceOnMotion(frame.jcbResetFaceOnMotion.isSelected());
                newSettings.setResetBarcodeOnMotion(frame.jcbResetBarcodeOnMotion.isSelected());
                newSettings.setExitOnSave(frame.jcbExitOnSave.isSelected());
                newSettings.setAutoSave(frame.jcbAutoSave.isSelected());
                newSettings.setUserCanChangeSettings(frame.jcbUserCanChangeSettings.isSelected());
                newSettings.setSound(frame.jcbSound.isSelected());
                projectEnum.BarcodeSaveFormat format = frame.jrbBarcodeFormatJSON.isSelected() ? projectEnum.BarcodeSaveFormat.json : projectEnum.BarcodeSaveFormat.simple;
                newSettings.setBarcodeSaveMode(format);
                newSettings.setOutputPath(frame.jtfSaveLocation.getText());
                projectEnum.mode mode = null;
                mode = frame.jrbModeBoth.isSelected() ? projectEnum.mode.BOTH : mode;
                mode = frame.jrbModeFaceOnly.isSelected() ? projectEnum.mode.FACE : mode;
                mode = frame.jrbModeBarcodeOnly.isSelected() ? projectEnum.mode.BARCODE : mode;

                newSettings.setMode(mode);
                newSettings.setFirstRun(false);

                SettingFunctions.setSettings(newSettings);
                JOptionPane.showMessageDialog(frame, "Ayarlarınız kaydedildi.Etkili olması için lütfen KTS uygulamasını yeniden başlatın.");
                System.exit(0);
            }
        });

        frame.jbInf.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String info = "";
                info += "\n" + System.getProperties().getProperty("java.home");
                info += "\n" + System.getProperties().getProperty("java.vendor");
                info += "\n" + System.getProperties().getProperty("java.vendor.url");
                info += "\n" + System.getProperties().getProperty("java.version");
                info += "\n" + System.getProperties().getProperty("os.arch");
                info += "\n" + System.getProperties().getProperty("os.name");
                info += "\n" + System.getProperties().getProperty("user.dir");
                info += "\n" + System.getProperties().getProperty("java.class.path");
                info += "\n" + System.getProperties().getProperty("user.home");
                info += "\n" + System.getProperties().getProperty("user.name");
                JOptionPane.showMessageDialog(null, info, "Info", 2);
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
            new Thread(new Runnable() {

                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,
                            "Kayıt başarılı.",
                            "Metasoft KTS", JOptionPane.INFORMATION_MESSAGE);
                }
            }).start();
            if (SettingFunctions.getSettings().getExitOnSave()) {
                System.exit(0);
            }
        }
    }

    public void setSurat(Surat surat) {
        currentSurat = surat;
//        frame.jlFace.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(110, 110, 1)));
//        frame.jlFaceOnay.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(110, 110, 1)));
//        frame.jlKimlikOnay.setIcon(new ImageIcon(surat.getSource().getScaledInstance(196, 110, 1)));//16:9
        float faceScale = GeneralFunctions.getFitScreenScale(110, 110, surat.getSurat());
        int scaled_w = (int) (surat.getSurat().getWidth() * faceScale);
        int scaled_h = (int) (surat.getSurat().getHeight() * faceScale);

        float kimlikScale = GeneralFunctions.getFitScreenScale(196, 110, surat.getSource());
        int kimlikScaled_w = (int) (surat.getSource().getWidth() * kimlikScale);
        int kimliScaled_h = (int) (surat.getSource().getHeight() * kimlikScale);

        frame.jlFace.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(scaled_w, scaled_h, 1)));
        frame.jlFaceOnay.setIcon(new ImageIcon(surat.getSurat().getScaledInstance(scaled_w, scaled_h, 1)));
        frame.jlKimlikOnay.setIcon(new ImageIcon(surat.getSource().getScaledInstance(kimlikScaled_w, kimliScaled_h, 1)));//16:9

        frame.jlFace.revalidate();
        frame.jlFace.repaint();
        frame.jlFaceOnay.revalidate();
        frame.jlFaceOnay.repaint();
        frame.jlKimlikOnay.revalidate();
        frame.jlKimlikOnay.revalidate();

        frame.jpFaceMainContainer.setBackground(new Color(102, 102, 0));
        frame.jlGifFace.setVisible(false);
        try {
            GeneralFunctions.playSound(beepPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkAutoSave();
    }

    public void setBarcodeResult(Result result) {
        this.barcodeResult = result;
        frame.jlBarcodeText.setText(result.getText());
        frame.jlBarcodeOkunan.setText(result.getText());

        frame.jpBarcodeContainer.setBackground(new Color(102, 102, 0));

        frame.jlGifBarcode.setVisible(false);
        try {
            GeneralFunctions.playSound(beepPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        checkAutoSave();
    }

    public void resetBarcode(boolean scan) {
        barcodeResult = null;
        frame.jlBarcodeText.setText("...");
        frame.jlBarcodeOkunan.setText("Okunamadı!");

        frame.jpBarcodeContainer.setBackground(new Color(240, 240, 240));
        frame.jlGifBarcode.setVisible(true);

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
        frame.jpFaceMainContainer.setBackground(new Color(240, 240, 240));
        frame.jlGifFace.setVisible(true);

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
