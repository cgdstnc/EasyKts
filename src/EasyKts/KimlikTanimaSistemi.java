///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package EasyKts;
//
//import EasyKts.View.EditablePanel;
//import EasyKts.System.FileLogOutputStream;
//import EasyKts.Model.Kisi_old;
//import EasyKts.Model.Settings;
//import EasyKts.Common.BarcodeReader;
//import EasyKts.System.SettingFunctions;
//import com.github.sarxos.webcam.Webcam;
//import com.github.sarxos.webcam.WebcamMotionDetector;
//import com.github.sarxos.webcam.WebcamMotionEvent;
//import com.github.sarxos.webcam.WebcamMotionListener;
//import com.github.sarxos.webcam.WebcamPanel;
//import com.google.zxing.Result;
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.GridLayout;
//import java.awt.Stroke;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintStream;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import javax.imageio.ImageIO;
//import javax.swing.BoxLayout;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.ImageIcon;
//import javax.swing.JOptionPane;
//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract1;
//import net.sourceforge.tess4j.TesseractException;
//import org.openimaj.image.ImageUtilities;
//import org.openimaj.image.processing.face.detection.DetectedFace;
//import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
//import org.openimaj.math.geometry.shape.Rectangle;
//
///**
// *
// * @author Administrator
// */
//public class KimlikTanimaSistemi extends javax.swing.JFrame implements WebcamMotionListener {
//
//    private static final HaarCascadeDetector faceDetector = new HaarCascadeDetector();
//    private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, new float[]{1.0f}, 0.0f);
//
//    private Webcam webcam = null;
//    private WebcamPanel webcamPanel = null;
//    private WebcamPanel.Painter painter = null;
//    private WebcamMotionDetector motionDetector;
//    private boolean init = true;
//    private List<DetectedFace> biggestFace = null;
//    private Integer zoom = 0;
//    private ITesseract iTesseract;
//
//    private Kisi_old kisi;
//    private EditablePanel editablePanel = null;
//
//    public KimlikTanimaSistemi() {
//
//        if (Webcam.getWebcams().size() < 1) {
//            JOptionPane.showMessageDialog(this, "Hiçbir kamera bulunamadı!", "Hata", JOptionPane.ERROR_MESSAGE);
//            System.exit(1);
//            return;
//        }
//
//        initComponents();
//        initComp();
//
//        GridLayout gl = new GridLayout(0, 1);
//        jpImage.setLayout(gl);
//
//        setLocationRelativeTo(null);
//        setTitle("Kimlik Tanıma Sistemi");
//        this.jfManuel.setTitle("Kimlik Tanıma Sistemi");
//
//        try {
//            ImageIcon img = new ImageIcon("resource/logo.png");
//            setIconImage(img.getImage());
//            this.jfManuel.setIconImage(img.getImage());
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String defaultCameraName = SettingFunctions.getSettings().getDefaultCamera();
//                int defaultIndex = jcbCams.getItemCount() - 1;
//                for (int i = 0; i < jcbCams.getItemCount(); i++) {
//                    String name = jcbCams.getItemAt(i);
//                    if (name.equalsIgnoreCase(defaultCameraName)) {
//                        defaultIndex = i;
//                        break;
//                    }
//                }
//                changeCam(defaultIndex);
//                reset();
//            }
//        }).start();
//
//        try {
////             initTess4J();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
//    private void initComponents() {
//
//        jfManuel = new javax.swing.JFrame();
//        jpImage = new javax.swing.JPanel();
//        jpManuelKaydet = new javax.swing.JPanel();
//        jbManuelKaydet = new javax.swing.JButton();
//        jpSelectCam = new javax.swing.JPanel();
//        jcbCams = new javax.swing.JComboBox<>();
//        jpMain = new javax.swing.JPanel();
//        jpCamera = new javax.swing.JPanel();
//        jPanel5 = new javax.swing.JPanel();
//        jpFaceMainContainer = new javax.swing.JPanel();
//        jpFace = new javax.swing.JPanel();
//        jlFace = new javax.swing.JLabel();
//        jsZoom = new javax.swing.JSlider();
//        jpTC = new javax.swing.JPanel();
//        jtfTCNo = new javax.swing.JLabel();
//        jPanel7 = new javax.swing.JPanel();
//        jbSave = new javax.swing.JButton();
//        jbReset = new javax.swing.JButton();
//        jbManuel = new javax.swing.JButton();
//
//        jpImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        javax.swing.GroupLayout jpImageLayout = new javax.swing.GroupLayout(jpImage);
//        jpImage.setLayout(jpImageLayout);
//        jpImageLayout.setHorizontalGroup(
//            jpImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 346, Short.MAX_VALUE)
//        );
//        jpImageLayout.setVerticalGroup(
//            jpImageLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 268, Short.MAX_VALUE)
//        );
//
//        jpManuelKaydet.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        jbManuelKaydet.setText("Kaydet");
//        jbManuelKaydet.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jbManuelKaydetActionPerformed(evt);
//            }
//        });
//
//        javax.swing.GroupLayout jpManuelKaydetLayout = new javax.swing.GroupLayout(jpManuelKaydet);
//        jpManuelKaydet.setLayout(jpManuelKaydetLayout);
//        jpManuelKaydetLayout.setHorizontalGroup(
//            jpManuelKaydetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jbManuelKaydet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        jpManuelKaydetLayout.setVerticalGroup(
//            jpManuelKaydetLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpManuelKaydetLayout.createSequentialGroup()
//                .addGap(0, 0, Short.MAX_VALUE)
//                .addComponent(jbManuelKaydet))
//        );
//
//        javax.swing.GroupLayout jfManuelLayout = new javax.swing.GroupLayout(jfManuel.getContentPane());
//        jfManuel.getContentPane().setLayout(jfManuelLayout);
//        jfManuelLayout.setHorizontalGroup(
//            jfManuelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jpImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(jpManuelKaydet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        jfManuelLayout.setVerticalGroup(
//            jfManuelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jfManuelLayout.createSequentialGroup()
//                .addComponent(jpImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addGap(1, 1, 1)
//                .addComponent(jpManuelKaydet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//        );
//
//        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//
//        jpSelectCam.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        jcbCams.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
//        jcbCams.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jcbCamsActionPerformed(evt);
//            }
//        });
//
//        javax.swing.GroupLayout jpSelectCamLayout = new javax.swing.GroupLayout(jpSelectCam);
//        jpSelectCam.setLayout(jpSelectCamLayout);
//        jpSelectCamLayout.setHorizontalGroup(
//            jpSelectCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpSelectCamLayout.createSequentialGroup()
//                .addComponent(jcbCams, 0, 296, Short.MAX_VALUE)
//                .addGap(0, 0, 0))
//        );
//        jpSelectCamLayout.setVerticalGroup(
//            jpSelectCamLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpSelectCamLayout.createSequentialGroup()
//                .addComponent(jcbCams, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addGap(2, 2, 2))
//        );
//
//        jpMain.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        jpCamera.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        javax.swing.GroupLayout jpCameraLayout = new javax.swing.GroupLayout(jpCamera);
//        jpCamera.setLayout(jpCameraLayout);
//        jpCameraLayout.setHorizontalGroup(
//            jpCameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 148, Short.MAX_VALUE)
//        );
//        jpCameraLayout.setVerticalGroup(
//            jpCameraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGap(0, 0, Short.MAX_VALUE)
//        );
//
//        jpFaceMainContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        jpFace.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
//        jpFace.setMaximumSize(new java.awt.Dimension(110, 110));
//        jpFace.setMinimumSize(new java.awt.Dimension(110, 110));
//
//        jlFace.setMaximumSize(new java.awt.Dimension(110, 110));
//        jlFace.setMinimumSize(new java.awt.Dimension(110, 110));
//        jlFace.setPreferredSize(new java.awt.Dimension(110, 110));
//
//        javax.swing.GroupLayout jpFaceLayout = new javax.swing.GroupLayout(jpFace);
//        jpFace.setLayout(jpFaceLayout);
//        jpFaceLayout.setHorizontalGroup(
//            jpFaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jlFace, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
//        );
//        jpFaceLayout.setVerticalGroup(
//            jpFaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jlFace, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
//        );
//
//        jsZoom.setMaximum(25);
//        jsZoom.setToolTipText("");
//        jsZoom.setValue(0);
//        jsZoom.addChangeListener(new javax.swing.event.ChangeListener() {
//            public void stateChanged(javax.swing.event.ChangeEvent evt) {
//                jsZoomStateChanged(evt);
//            }
//        });
//
//        javax.swing.GroupLayout jpFaceMainContainerLayout = new javax.swing.GroupLayout(jpFaceMainContainer);
//        jpFaceMainContainer.setLayout(jpFaceMainContainerLayout);
//        jpFaceMainContainerLayout.setHorizontalGroup(
//            jpFaceMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jsZoom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
//            .addGroup(jpFaceMainContainerLayout.createSequentialGroup()
//                .addContainerGap()
//                .addComponent(jpFace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//        jpFaceMainContainerLayout.setVerticalGroup(
//            jpFaceMainContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpFaceMainContainerLayout.createSequentialGroup()
//                .addComponent(jpFace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addComponent(jsZoom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//        );
//
//        jpTC.setBorder(javax.swing.BorderFactory.createEtchedBorder());
//
//        jtfTCNo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
//        jtfTCNo.setForeground(new java.awt.Color(102, 102, 102));
//        jtfTCNo.setText("tc no aranıyor...");
//
//        javax.swing.GroupLayout jpTCLayout = new javax.swing.GroupLayout(jpTC);
//        jpTC.setLayout(jpTCLayout);
//        jpTCLayout.setHorizontalGroup(
//            jpTCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpTCLayout.createSequentialGroup()
//                .addContainerGap()
//                .addComponent(jtfTCNo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//        jpTCLayout.setVerticalGroup(
//            jpTCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpTCLayout.createSequentialGroup()
//                .addContainerGap()
//                .addComponent(jtfTCNo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addContainerGap())
//        );
//
//        jbSave.setText("Kaydet");
//        jbSave.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jbSaveActionPerformed(evt);
//            }
//        });
//
//        jbReset.setText("Sıfırla");
//        jbReset.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jbResetActionPerformed(evt);
//            }
//        });
//
//        jbManuel.setText("Manuel");
//        jbManuel.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jbManuelActionPerformed(evt);
//            }
//        });
//
//        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
//        jPanel7.setLayout(jPanel7Layout);
//        jPanel7Layout.setHorizontalGroup(
//            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jbSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(jbReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(jbManuel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        jPanel7Layout.setVerticalGroup(
//            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel7Layout.createSequentialGroup()
//                .addComponent(jbSave, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jbReset)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jbManuel)
//                .addContainerGap())
//        );
//
//        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
//        jPanel5.setLayout(jPanel5Layout);
//        jPanel5Layout.setHorizontalGroup(
//            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel5Layout.createSequentialGroup()
//                .addGap(0, 0, 0)
//                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
//                    .addComponent(jpTC, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
//                    .addComponent(jpFaceMainContainer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                .addGap(0, 0, 0))
//        );
//        jPanel5Layout.setVerticalGroup(
//            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel5Layout.createSequentialGroup()
//                .addGap(0, 0, 0)
//                .addComponent(jpFaceMainContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(jpTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addGap(0, 0, 0))
//        );
//
//        javax.swing.GroupLayout jpMainLayout = new javax.swing.GroupLayout(jpMain);
//        jpMain.setLayout(jpMainLayout);
//        jpMainLayout.setHorizontalGroup(
//            jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jpMainLayout.createSequentialGroup()
//                .addComponent(jpCamera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
//        );
//        jpMainLayout.setVerticalGroup(
//            jpMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jpCamera, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addGroup(jpMainLayout.createSequentialGroup()
//                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addContainerGap(16, Short.MAX_VALUE))
//        );
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
//        getContentPane().setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jpSelectCam, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(jpMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                .addComponent(jpSelectCam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jpMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addGap(0, 0, 0))
//        );
//
//        pack();
//    }// </editor-fold>//GEN-END:initComponents
//
//    private void jcbCamsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCamsActionPerformed
//        if (init) {
//            init = false;
//            return;
//        }
//        changeCam(jcbCams.getSelectedIndex());
//        Settings s = new Settings();
//        s.setDefaultCamera(jcbCams.getSelectedItem().toString());
//        SettingFunctions.setSettings(s);
//    }//GEN-LAST:event_jcbCamsActionPerformed
//
//    private void jsZoomStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jsZoomStateChanged
//        zoom = jsZoom.getValue();
//    }//GEN-LAST:event_jsZoomStateChanged
//
//    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
//        try {
//            saveKimlik();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }//GEN-LAST:event_jbSaveActionPerformed
//
//    private void jbResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbResetActionPerformed
//        reset();
//    }//GEN-LAST:event_jbResetActionPerformed
//
//    private void jbManuelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbManuelActionPerformed
//        try {
//            BufferedImage anlik = getImage();
//            editablePanel = new EditablePanel(anlik, this);
//
//            jpImage.removeAll();
//            jpImage.add(editablePanel);
//            jpImage.repaint();
//            jfManuel.setSize(anlik.getWidth(), anlik.getHeight() + jpManuelKaydet.getHeight());
//
//            jfManuel.setLocation(getLocation());
//            jfManuel.setVisible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }//GEN-LAST:event_jbManuelActionPerformed
//
//    private void jbManuelKaydetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbManuelKaydetActionPerformed
//        BufferedImage tumResim = editablePanel.getCameraImage();
//        BufferedImage seciliBolge = editablePanel.getSelectedArea();
//
//        try {
//            saveManuel(tumResim, seciliBolge);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }//GEN-LAST:event_jbManuelKaydetActionPerformed
//
//    public static void main(String args[]) {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(KimlikTanimaSistemi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(KimlikTanimaSistemi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(KimlikTanimaSistemi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(KimlikTanimaSistemi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//
//        //LOGGER
//        try {
//            InputStream consoleInput = System.in;
//            OutputStream consoleOutput = System.out;
//
//            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
//            Date date = new Date();
//            FileLogOutputStream fileLogger = new FileLogOutputStream(consoleInput, consoleOutput, new File("log/" + dateFormat.format(date) + ".log"));
//            System.setOut(new PrintStream(fileLogger));
//            System.setErr(new PrintStream(fileLogger));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new KimlikTanimaSistemi().setVisible(true);
//            }
//        });
//    }
//
//    // Variables declaration - do not modify//GEN-BEGIN:variables
//    public javax.swing.JPanel jPanel5;
//    public javax.swing.JPanel jPanel7;
//    public javax.swing.JButton jbManuel;
//    public javax.swing.JButton jbManuelKaydet;
//    public javax.swing.JButton jbReset;
//    public javax.swing.JButton jbSave;
//    public javax.swing.JComboBox<String> jcbCams;
//    public javax.swing.JFrame jfManuel;
//    public javax.swing.JLabel jlFace;
//    public javax.swing.JPanel jpCamera;
//    public javax.swing.JPanel jpFace;
//    public javax.swing.JPanel jpFaceMainContainer;
//    public javax.swing.JPanel jpImage;
//    public javax.swing.JPanel jpMain;
//    public javax.swing.JPanel jpManuelKaydet;
//    public javax.swing.JPanel jpSelectCam;
//    public javax.swing.JPanel jpTC;
//    public javax.swing.JSlider jsZoom;
//    public javax.swing.JLabel jtfTCNo;
//    // End of variables declaration//GEN-END:variables
//
//    private Thread faceDetectionThread;
//    private Long faceThreadValid;
//
//    private Thread tcDetectionThread;
//    private long tcThreadValid;
//    private Thread barcodeThread;
//    private long barcodeThreadValid;
//
//    private void initComp() {
//        String[] names = new String[Webcam.getWebcams().size()];
//        for (int i = 0; i < Webcam.getWebcams().size(); i++) {
//            names[i] = Webcam.getWebcams().get(i).getName();
//        }
//
//        DefaultComboBoxModel model = new DefaultComboBoxModel(names);
//        jcbCams.setModel(model);
//    }
//
//    private Dimension getMaxSize(Webcam webcam) {
//        Dimension[] dims = webcam.getViewSizes();
//        Dimension max = dims[0];
//        for (int i = 0; i < dims.length; i++) {
//            Dimension dim = dims[i];
//            if (dim.height > max.height) {
//                max = dim;
//            }
//        }
//
//        return max;
//    }
//
//    private void changeCam(int index) {
//        try {
//            jcbCams.setSelectedIndex(index);
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//        if (webcam != null && webcam.isOpen()) {
//            webcam.close();
//        }
//        webcam = Webcam.getWebcams().get(index);//getDefault();
//
//        Dimension dim = getMaxSize(webcam);
//        //this.setSize((int) dim.getWidth() + jpFaceMainContainer.getWidth(), (int) (dim.getHeight() + jpSelectCam.getHeight() /*+ jpMain.getHeight()*/));
//        webcam.setViewSize(dim);
//
//        setSize(450, 450);
//
//        webcamPanel = new WebcamPanel(webcam, false);
//        webcamPanel.setFPSDisplayed(false);
//        jpCamera.removeAll();
//        BoxLayout boxLayout0 = new BoxLayout(jpCamera, BoxLayout.PAGE_AXIS);
//        jpCamera.setLayout(boxLayout0);
//        jpCamera.add(webcamPanel);
//
//        if (webcam.isOpen()) {
//            webcam.close();
//        }
//
//        int i = 0;
//        do {
//            if (webcam.getLock().isLocked()) {
//                //System.out.println("Waiting for lock to be released " + i);
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e1) {
//                    return;
//                }
//            } else {
//                break;
//            }
//        } while (i++ < 3);
//        try {
//            webcam.open();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Kameranın başka bir uygulama tarafından kullanilmadiğindan emin olunuz!", "Hata", JOptionPane.ERROR_MESSAGE);
//            JOptionPane.showMessageDialog(this, e.getLocalizedMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
//            System.err.println(e.getMessage());
//
//            e.printStackTrace();
//            System.exit(1);
//
//        }
//        painter = webcamPanel.getDefaultPainter();
//
//        webcamPanel.start();
//        jpCamera.revalidate();
//        jpCamera.repaint();
//        motionDetector = new WebcamMotionDetector(webcam);
//        motionDetector.setInterval(500);
//        motionDetector.setPixelThreshold(90);
//        motionDetector.addMotionListener(this);
//        motionDetector.start();
//        reset();
//    }
//
//    private void reset() {
//        kisi = new Kisi_old();
//
//        jlFace.setIcon(null);
//        jlFace.revalidate();
//        jtfTCNo.setText("tc no aranıyor...");
//        jpCamera.revalidate();
//        jpCamera.repaint();
//        jpTC.setVisible(false);
//        faceThreadValid = System.currentTimeMillis();
//        startFaceDetectionThread(15000L);
//        tcThreadValid = faceThreadValid;
//        //startTcDetectionThread(15000L);
//        barcodeThreadValid = System.currentTimeMillis();
//        startBarcodeThread(15000L);
//    }
//
//    private void deleteAllFilesInOutputFolder() {
//        try {
//            File tc = new File("KimlikOutput\\tc.txt");
//            tc.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            File surat = new File("KimlikOutput\\surat.png");
//            surat.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            File kimlik = new File("KimlikOutput\\kimlik.png");
//            kimlik.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void saveKimlik() throws IOException {
//        deleteAllFilesInOutputFolder();
//        File folder = new File("KimlikOutput");
//        folder.mkdirs();
//        if (kisi.getTc() != null) {
//            File f = new File(folder.getAbsolutePath() + "\\tc.txt");
//            FileWriter fileWriter = new FileWriter(f);
//            fileWriter.append(kisi.getTc().toString());
//            fileWriter.flush();
//            fileWriter.close();
//        }
//        File img = new File(folder.getAbsolutePath() + "\\surat.jpg");
//        ImageIO.write(kisi.getSurat(), "JPG", img);
//
//        BufferedImage kmlk = cloneBufferedImage(kisi.getKimlik());
//        Graphics g = kmlk.getGraphics();
//        g.setColor(new Color(10, 10, 10, 90));
//        g.setFont(new Font(g.getFont().getName(), Font.ITALIC, 28));
//        for (int i = 0; i < kmlk.getHeight(); i += 32) {
//            g.drawString("Kimlik Tanima Sistemi Ile Elde Edilmistir Kimlik Tanima Sistemi Ile Elde Edilmistir  Kimlik Tanima Sistemi Ile Elde Edilmistir", 10, i);
//        }
//        img = new File(folder.getAbsolutePath() + "\\kimlik.jpg");
//        ImageIO.write(kmlk, "JPG", img);
//
//        System.exit(0);
//    }
//
//    private void saveManuel(BufferedImage tumResim, BufferedImage surat) throws IOException     {
//        deleteAllFilesInOutputFolder();
//        File folder = new File("KimlikOutput");
//        folder.mkdirs();
//        File img = new File(folder.getAbsolutePath() + "\\surat.jpg");
//        ImageIO.write(surat, "JPG", img);
//
//        BufferedImage kmlk = cloneBufferedImage(tumResim);
//        Graphics g = kmlk.getGraphics();
//        g.setColor(new Color(10, 10, 10, 90));
//        g.setFont(new Font(g.getFont().getName(), Font.ITALIC, 28));
//        for (int i = 0; i < kmlk.getHeight(); i += 32) {
//            g.drawString("Kimlik Tanima Sistemi Ile Elde Edilmistir Kimlik Tanima Sistemi Ile Elde Edilmistir  Kimlik Tanima Sistemi Ile Elde Edilmistir", 10, i);
//        }
//        img = new File(folder.getAbsolutePath() + "\\kimlik.jpg");
//        ImageIO.write(kmlk, "JPG", img);
//
//        System.exit(0);
//    }
//
//    private BufferedImage cloneBufferedImage(BufferedImage img) {
//        BufferedImage clone = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
//        Graphics g = clone.getGraphics();
//        g.drawImage(img, 0, 0, null);
//
//        return clone;
//    }
//
//    private synchronized BufferedImage getImage() {
//        return webcam.getImage();
//    }
//
//    //DLL HATALARI YUZUNDEN KAPALI
////    //region##########TESS4J##########
////    private void initTess4J() {
////        File tessDataFolder = new File("resource/tessdata");
////        if (!tessDataFolder.exists()) {
////            return;
////        }
////        try {
////            iTesseract = new Tesseract1();
////            iTesseract.setDatapath(tessDataFolder.getAbsolutePath());
////            iTesseract.setLanguage("eng");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
////
////    private Long getTC(BufferedImage bufferedImage) throws TesseractException {
////        try {
////            String str = iTesseract.doOCR(rgbToGrayscaleBW(bufferedImage));
////            for (int i = 0; i < str.length(); i++) {
////                try {
////                    String tmp = str.substring(i, i + 11);
////                    Long tc = Long.valueOf(tmp);
////                    if (isValidTckn(tc)) {
////                        return tc;
////                    }
////                } catch (Exception known) {
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
////
////    private boolean isValidTckn(Long tckn) {
////        try {
////            String tmp = tckn.toString();
////
////            if (tmp.length() == 11) {
////                int totalOdd = 0;
////
////                int totalEven = 0;
////
////                for (int i = 0; i < 9; i++) {
////                    int val = Integer.valueOf(tmp.substring(i, i + 1));
////
////                    if (i % 2 == 0) {
////                        totalOdd += val;
////                    } else {
////                        totalEven += val;
////                    }
////                }
////
////                int total = totalOdd + totalEven + Integer.valueOf(tmp.substring(9, 10));
////
////                int lastDigit = total % 10;
////
////                if (tmp.substring(10).equals(String.valueOf(lastDigit))) {
////                    int check = (totalOdd * 7 - totalEven) % 10;
////
////                    if (tmp.substring(9, 10).equals(String.valueOf(check))) {
////                        return true;
////                    }
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return false;
////    }
////
////    private void startTcDetectionThread(long timeOut) {
////        if (iTesseract == null) {
////            return;
////        }
////        if (tcDetectionThread == null || tcDetectionThread.getState() == Thread.State.TERMINATED) {
////            tcDetectionThread = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    long valid = tcThreadValid;
////                    long start = System.currentTimeMillis();
////                    while (valid == tcThreadValid) {
////                        if ((System.currentTimeMillis() - start) > timeOut) {
////                            jtfTCNo.setText("TC okunamadı.");
////                            return;
////                        }
////                        try {
////                            if (jtfTCNo.getText().length() != 11) {
////                                Long tc = getTC(getImage());
////                                if (tc != null && (valid == tcThreadValid)) {
////                                    System.out.println(tc);
////                                    jtfTCNo.setText(tc.toString());
////                                    jpTC.setVisible(true);
////                                     System.out.println("TC+valid" + valid);
////                                     System.out.println("TC+kisi.getKey()" + kisi.getKey());
////                                    //if (valid == kisi.getKey()) {
////                                        kisi.setTc(tc);
////                                    //}
////                                    return;
////                                }
////                            }
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                            jtfTCNo.setText("TC okunamadı.");
////                            return;
////                        }
////                    }
////                }
////            });
////        }
////        if (!tcDetectionThread.isAlive()) {
////            System.out.println("tcDetectionThread:" + tcDetectionThread.getState().toString());
////            tcDetectionThread.start();
////        }
////    }
////
////    private BufferedImage rgbToGrayscaleBW(BufferedImage img) {
////        Long start = System.currentTimeMillis();
////        BufferedImage gray = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
////        Graphics2D g = gray.createGraphics();
////        g.drawImage(img, 0, 0, null);
////        //System.out.println("rgbToGrayscaleBW Took " + (System.currentTimeMillis() - start) + " ms.");
////        return gray;
////    }
//    //endregion##########TESS4J##########
//    //region##########FACE##########
//    private void startFaceDetectionThread(long timeOut) {
//        if (faceDetectionThread == null || faceDetectionThread.getState() == Thread.State.TERMINATED) {
//            faceDetectionThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    long valid = faceThreadValid;
//                    long start = System.currentTimeMillis();
//                    int i = 0;
//                    jsZoom.setVisible(false);
//                    while (valid == faceThreadValid) {
//                        if ((System.currentTimeMillis() - start) > timeOut) {
//                            return;
//                        }
//                        try {
//                            BufferedImage newFrame = getImage();
//                            biggestFace = faceDetector.detectFaces(ImageUtilities.createFImage(newFrame));
//                            if (biggestFace.size() != 0) {
//                                DetectedFace face = biggestFace.get(0);
//                                for (DetectedFace itr : biggestFace) {
//                                    if (itr.getBounds().getHeight() * getWidth() > face.getBounds().getHeight() * getWidth()) {
//                                        face = itr;
//                                    }
//                                }
//                                //DetectedFace face = faces.get(0);
//                                Rectangle bounds = face.getBounds();
//
//                                int dx = (int) (0.1 * bounds.width);
//                                int dy = (int) (0.2 * bounds.height);
//                                int x = (int) bounds.x - dx;
//                                int y = (int) bounds.y - dy;
//                                int w = (int) bounds.width + 2 * dx;
//                                int h = (int) bounds.height + dy;
//                                BufferedImage buf = null;
//                                synchronized (zoom) {
//                                    try {
//                                        // BufferedImage buf = newFrame.getSubimage(x - zoom, y - zoom, w + (zoom * 2), h + (zoom * 2));
//                                        buf = newFrame.getSubimage(x - 25, y - 25, w + (25 * 2), h + (25 * 2));
//                                        jlFace.setIcon(new ImageIcon(buf.getScaledInstance(110, 110, 1)));
//                                    } catch (Exception e) {
//                                        if (String.valueOf(e.getClass()) == "java.awt.image.RasterFormatException") {
//                                            buf = newFrame.getSubimage(x, y, w, h);
//                                            jlFace.setIcon(new ImageIcon(buf.getScaledInstance(110, 110, 1)));
//                                        }
//                                    }
//                                }
//                                if (kisi != null) {
//                                    kisi.setSurat(buf);
//                                    kisi.setKimlik(newFrame);
//                                    kisi.setSuratX(x);
//                                    kisi.setSuratY(y);
//                                    kisi.setKey(valid);
//                                    // System.out.println("Face+valid" + valid);
//                                }
//
//                                jlFace.revalidate();
//                                jlFace.repaint();
//                                i++;
//                                // jsZoom.setVisible(true);
//                                if (i > 20) {
//                                    break;//ilk koyarken yuzu bulunca hemen cikmasin bulaniklik azalsin
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return;
//                        }
//                    }
//                }
//            });
//        }
//        if (!faceDetectionThread.isAlive()) {
//            // System.out.println("faceDetectionThread:" + faceDetectionThread.getState().toString());
//            faceDetectionThread.start();
//        }
//    }
//
//    //endregion##########FACE/END##########
//    //region##########BARKOD##########
//    private void startBarcodeThread(long timeOut) {
//        if (barcodeThread == null || barcodeThread.getState() == Thread.State.TERMINATED) {
//            barcodeThread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    long valid = barcodeThreadValid;
//                    long start = System.currentTimeMillis();
//                    int i = 0;
//
//                    while (valid == barcodeThreadValid) {
//                        if ((System.currentTimeMillis() - start) > timeOut) {
//                            return;
//                        }
//                        try {
//                            BufferedImage newFrame = getImage();
//                            Result r = BarcodeReader.readBarcode(newFrame);
//                            if (r != null) {
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            return;
//                        }
//                    }
//                }
//            });
//        }
//        if (!barcodeThread.isAlive()) {
//            // System.out.println("barcodeThread:" + barcodeThread.getState().toString());
//            barcodeThread.start();
//        }
//    }
//    //endregion##########BARKOD##########
//
//    @Override
//    public void motionDetected(WebcamMotionEvent wme) {
//        // System.out.println("Motion " + wme.getArea());
//        reset();
//    }
//
//}
//