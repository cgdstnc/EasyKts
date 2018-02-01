/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.System;

import EasyKts.Model.Settings;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

/**
 *
 * @author Administrator
 */
    public class SettingFunctions {

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(SettingFunctions.class.getName());

    private static Settings settings;

    public static Settings getSettings() {

        if (settings == null) {
            File saved = new File("properties");
            if (!saved.exists()) {
                settings = getDefaultSettings();
                System.out.println("settings = getDefaultSettings()");
                try {
                    setSettings(settings);
                } catch (Exception e) {
                    System.out.println("setSettings Hata");
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }

            } else {
                try {
                    settings = new Gson().fromJson(new BufferedReader(new FileReader(saved)), Settings.class);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            }
        }
        return settings;
    }

    private static Settings getDefaultSettings() {
        Settings settings = new Settings();
        settings.setDefaultCamera("not yet set!");
        settings.setKullaniciOnayIste(false);
        settings.setMode(projectEnum.mode.BOTH);
        settings.setResetBarcodeOnMotion(false);
        settings.setResetFaceOnMotion(false);
        settings.setBarcodeSaveMode(projectEnum.BarcodeSaveFormat.json);
        settings.setExitOnSave(true);
        settings.setAutoSave(false);
        settings.setFirstRun(true);
        settings.setUserCanChangeSettings(false);
        settings.setSound(true);
        settings.setOutputPath("KimlikOutput");

        return settings;
    }

    public static void setSettings(Settings s) {
        settings = s;
        try {
            File saved = new File("properties");
            String tmp = new Gson().toJson(settings, settings.getClass());
            tmp = tmp.replaceAll(",", ",\n");
            try {
                FileWriter fileWriter = new FileWriter(saved);
                fileWriter.write(tmp);
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }
}
