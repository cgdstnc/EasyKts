/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts;

import EasyKts.Common.Camera;
import EasyKts.Controller.MainFrameController;
import EasyKts.Model.Settings;
import EasyKts.System.FileLogOutputStream;
import EasyKts.System.SettingFunctions;
import EasyKts.System.projectEnum;
import de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author Administrator
 */
public class run {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(run.class.getName());

    private static String getArgOption(String option, String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.toLowerCase().contains(option.toLowerCase())) {
                try {
                    return arg.split("=")[1];
                } catch (Exception e) {
                    return arg;
                }
            }
        }
        return null;
    }

    private static void setStartOptions(String[] args) {
        if (args == null || args.length < 1) {
            return;
        }
        Settings s = SettingFunctions.getSettings();
        if (getArgOption("-help", args) != null) {
            JOptionPane.showMessageDialog(null, ""
                    + "Kullanilabilecek başlangıc argumanları:\n"
                    + " -kullaniciOnayIste=true|false\n"
                    + " -defaultCamera=\"kamera adı\" \n"
                    + " -listCams \n"
                    + " -resetFaceOnMotion=true|false \n"
                    + " -resetBarcodeOnMotion=true|false \n"
                    + " -mode=\"face\"|\"barcode\"|\"both\" \n"
                    + " -setOutputPath=\"KimlikOutput\" \n"
                    + " -barcodeSaveMode=\"simple\"|\"json\"  \n"
                    + " -exitOnSave=true|false  \n"
                    + " -autoSave=true|false  \n",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        if (getArgOption("-listCams", args) != null) {
            try {
                JOptionPane.showMessageDialog(null, "Tum kameralar:\n"
                        + Arrays.toString(new Camera(null).getWebcamNames())
                        .replaceAll("\\[", "")
                        .replaceAll("\\]", "")
                        .replaceAll(", ", "\n"),
                        "Kamera Listesi", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-defaultCamera", args) != null) {
            try {
                s.setDefaultCamera(getArgOption("-defaultCamera", args));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-kullaniciOnayIste", args) != null) {
            try {
                s.setKullaniciOnayIste(Boolean.valueOf(getArgOption("-kullaniciOnayIste", args)));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
                System.out.println("Ornek parametre Kullanim: -kullaniciOnayIste=true");
            }
        }

        if (getArgOption("-resetFaceOnMotion", args) != null) {
            try {
                s.setResetFaceOnMotion(Boolean.valueOf(getArgOption("-resetFaceOnMotion", args)));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-resetBarcodeOnMotion", args) != null) {
            try {
                s.setResetBarcodeOnMotion(Boolean.valueOf(getArgOption("-resetBarcodeOnMotion", args)));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-mode", args) != null) {
            try {
                projectEnum.mode mode;
                String modeStr = getArgOption("-mode", args).toLowerCase();
                switch (modeStr) {
                    case "face":
                        mode = projectEnum.mode.FACE;
                        break;
                    case "barcode":
                        mode = projectEnum.mode.BARCODE;
                        break;
                    case "both":
                        mode = projectEnum.mode.BOTH;
                        break;
                    default:
                        mode = projectEnum.mode.BOTH;
                        break;
                }

                s.setMode(mode);
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-setOutputPath", args) != null) {
            try {
                s.setOutputPath(getArgOption("-setOutputPath", args));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-barcodeSaveMode", args) != null) {
            try {
                projectEnum.BarcodeSaveFormat barcodeSaveFormat;
                String modeStr = getArgOption("-barcodeSaveMode", args).toLowerCase();
                switch (modeStr) {
                    case "json":
                        barcodeSaveFormat = projectEnum.BarcodeSaveFormat.json;
                        break;
                    case "simple":
                        barcodeSaveFormat = projectEnum.BarcodeSaveFormat.simple;
                        break;
                    default:
                        barcodeSaveFormat = projectEnum.BarcodeSaveFormat.json;
                        break;
                }

                s.setBarcodeSaveMode(barcodeSaveFormat);
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-exitOnSave", args) != null) {
            try {
                s.setExitOnSave(Boolean.valueOf(getArgOption("-exitOnSave", args)));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }

        if (getArgOption("-autoSave", args) != null) {
            try {
                s.setAutoSave(Boolean.valueOf(getArgOption("-autoSave", args)));
                SettingFunctions.setSettings(s);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

    private static void setLookAndFeel() {
        try {
            Locale.setDefault(Locale.ENGLISH);
            UIManager.setLookAndFeel(SyntheticaSimple2DLookAndFeel.class.getName());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.toString(), ex);
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

    private static void logJavaInf() {
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
        LOGGER.log(Level.INFO, "java inf:" + info);
    }

    public static void main(String[] args) {
//        //debug
//        args = new String[4];
//        args[0] = "-outputFolderName=outputFolderName";
//        args[1] = "-mode=barcode";
//        args[2] = "-resetFaceOnMotion=true";
//        args[3] = "-kullaniciOnayIste=false";

        LOGGER.log(Level.INFO, Arrays.toString(args));
        setStartOptions(args);
        startLogger();
        setLookAndFeel();
        new MainFrameController();
        logJavaInf();
    }
}
