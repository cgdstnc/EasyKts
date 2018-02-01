/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.System;

import java.awt.Image;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Cagdas
 */
public class GeneralFunctions {

    public static void playSound(final String url) throws Exception {
        if (!SettingFunctions.getSettings().getSound()) {
            return;
        }
        Clip clip = AudioSystem.getClip();
        AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
        clip.open(inputStream);
        clip.start();
        Thread.sleep(100);

    }

    public static float getFitScreenScale(int h, int w, Image original) {
        try {
            int _h = original.getHeight(null);
            int _w = original.getWidth(null);

            while ((_h >= h - 2) || (_w >= w - 2)) {
                if ((_h >= h - 2) && (_w >= w - 2)) {
                    _h = (int) (_h / 1.025);
                    _w = (int) (_w / 1.025);
                } else {
                    _h = (int) (_h / 1.0125);
                    _w = (int) (_w / 1.0125);
                }
            }
            float orj = original.getHeight(null);
            float ret = (h / orj);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void main(String[] args) throws Exception {
        playSound("C:\\Users\\Cagdas\\Documents\\NetBeansProjects\\EasyKts\\dist\\resource\\beep.wav");
    }
}
