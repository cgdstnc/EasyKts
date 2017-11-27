/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts;

import EasyKts.Controller.MainFrameController;
import EasyKts.System.FileLogOutputStream;
import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static void startLogger() {
        InputStream consoleInput = System.in;
        OutputStream consoleOutput = System.out;
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        Date date = new Date();
        FileLogOutputStream fileLogger = new FileLogOutputStream(consoleInput, consoleOutput, new File("log/" + dateFormat.format(date) + ".log"));
        System.setOut(new PrintStream(fileLogger));
        System.setErr(new PrintStream(fileLogger));
    }

    public static void main(String[] args) {
        startLogger();
        setLookAndFeel();
        new MainFrameController();
    }
}
