/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.System;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class FileLogOutputStream extends OutputStream {

    private File log;
    private FileWriter fileWriter;
    private InputStream consoleInput;
    private OutputStream consoleOutput;

    public FileLogOutputStream(InputStream consoleInput, OutputStream consoleOutput, File log) {
        try {
            this.consoleInput = consoleInput;
            this.consoleOutput = consoleOutput;

            new File("log").mkdirs();
            this.log = log;
            fileWriter = new FileWriter(log, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void write(int b) throws IOException {
        try {
            consoleOutput.write((char) b);
            consoleOutput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fileWriter.append(String.valueOf((char) b));
        fileWriter.flush();
    }

}
