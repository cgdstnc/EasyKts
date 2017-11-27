/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.View;

//import EasyKts.KimlikTanimaSistemi;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */
public class EditablePanel extends javax.swing.JPanel {

    private BufferedImage cameraImage;
    private float scale = 1;

    private int rect_x0 = 0;
    private int rect_y0 = 0;
    private int rect_x1 = 0;
    private int rect_y1 = 0;

    public EditablePanel(BufferedImage cameraImage) {
        initComponents();
        
        this.cameraImage = cameraImage;
        MouseEvents mouseEvents = new MouseEvents();
        addMouseListener(mouseEvents);
        addMouseMotionListener(mouseEvents);
        setSize(((int) (cameraImage.getWidth() * scale)) + 5, ((int) (cameraImage.getHeight() * scale)) + 5);
        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(cameraImage, 0, 0, (int) (cameraImage.getWidth() * scale), (int) (cameraImage.getHeight() * scale), null);
        g.setColor(Color.red);

        int solKoseX = Math.min(rect_x0, rect_x1);
        int solKoseY = Math.min(rect_y0, rect_y1);
        int sagKoseX = Math.max(rect_x0, rect_x1);
        int sagKoseY = Math.max(rect_y0, rect_y1);
        Graphics2D g2 = (Graphics2D) g;
        float thickness = 3.5f;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(thickness));
        g2.drawRect(solKoseX, solKoseY, Math.abs((solKoseX - sagKoseX)), Math.abs((solKoseY - sagKoseY)));
//        g.drawRect(solKoseX, solKoseY, Math.abs((solKoseX - sagKoseX)), Math.abs((solKoseY - sagKoseY)));
//        g2.drawRect(x, y, width, height);
        g2.setStroke(oldStroke);
    }

    public BufferedImage getCameraImage() {
        return cameraImage;
    }

    public void setCameraImage(BufferedImage cameraImage) {
        this.cameraImage = cameraImage;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFrame1 = new javax.swing.JFrame();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 489, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 372, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        rect_x0 = 0;
        rect_y0 = 0;
        rect_x1 = 0;
        rect_y1 = 0;
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFrame jFrame1;
    // End of variables declaration//GEN-END:variables

    public BufferedImage getSelectedArea() {
        return cameraImage.getSubimage((int) (rect_x0), (int) (rect_y0),
                (int) (Math.abs((rect_x0 - rect_x1))),
                (int) (Math.abs((rect_y0 - rect_y1))));
    }

    private float getFitScreenScale(Image original, int hd_H, int hd_W) {
//        try {
//            int h = Math.min(original.getHeight(null), original.getWidth(null));
//            int w = Math.max(original.getHeight(null), original.getWidth(null));
//
////            int hd_H = 720;
////            int hd_W = 1280;
//            while ((h >= hd_H) || (w >= hd_W)) {
//                if ((h >= hd_H) && (w >= hd_W)) {
//                    h = (int) (h / 1.025);
//                    w = (int) (w / 1.025);
//                } else { //if ((h>=110) && (w<110))
//                    h = (int) (h / 1.0125);
//                    w = (int) (w / 1.0125);
//                }
//            }
//            float orj = original.getHeight(null);
//            float ret = (h / orj);
//            return ret;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return 1;
    }

    public int getFaceX() {
        return Math.min(rect_x0, rect_x1);
    }

    public int getFaceY() {
        return Math.min(rect_y0, rect_y1);
    }
    
    
    class MouseEvents implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            rect_x0 = e.getX();
            rect_y0 = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            repaint();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            rect_x1 = e.getX();
            rect_y1 = e.getY();
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

}
