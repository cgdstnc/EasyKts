/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.System;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Cagdas
 */
public class GeneralFunctions {

    public static void playSound(final String url) throws Exception{
        if (!SettingFunctions.getSettings().getSound()) return;
       Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(url));
                    clip.open(inputStream);
                    clip.start();
       Thread.sleep(100);

    }

    public static void main(String[] args) throws Exception {
        playSound("C:\\Users\\Cagdas\\Documents\\NetBeansProjects\\EasyKts\\dist\\resource\\beep.wav");
    }
}
