/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts;

import EasyKts.Controller.MainFrameController;
import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;
import java.util.Locale;
import javax.swing.UIManager;

/**
 *
 * @author Administrator
 */
public class run {

    private static void setLookAndFeel() {
        try {
            Locale.setDefault(Locale.ENGLISH);
            UIManager.setLookAndFeel(SyntheticaSimple2DLookAndFeel.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        setLookAndFeel();
        MainFrameController controller = new MainFrameController();
    }
}
