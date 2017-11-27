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

/**
 *
 * @author Administrator
 */
public class SettingFunctions {
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
                    e.printStackTrace();
                }

            } else {
                try {
                    settings = new Gson().fromJson(new BufferedReader(new FileReader(saved)), Settings.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return settings;
    }

    private static Settings getDefaultSettings() {
        Settings settings = new Settings();
        settings.setDefaultCamera("not yet set!");

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
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
