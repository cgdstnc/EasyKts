/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.Model;

import EasyKts.System.projectEnum;

/**
 *
 * @author Administrator
 */
public class Settings {

    private String defaultCamera;
    private Boolean kullaniciOnayIste;
    private Boolean resetFaceOnMotion;
    private Boolean resetBarcodeOnMotion;
    private String outputFolderName;
    private projectEnum.mode mode;
    private Boolean exitOnSave;
    private projectEnum.BarcodeSaveFormat barcodeSaveMode;
    private Boolean autoSave;
    
    public Boolean getKullaniciOnayIste() {
        return (kullaniciOnayIste != null) ? kullaniciOnayIste : false;
    }

    public void setKullaniciOnayIste(Boolean kullaniciOnayIste) {
        this.kullaniciOnayIste = kullaniciOnayIste;
    }

    public String getDefaultCamera() {
        return (defaultCamera != null) ? defaultCamera : "not yet set!";
    }

    public void setDefaultCamera(String defaultCamera) {
        this.defaultCamera = defaultCamera;
    }

    public Boolean getResetFaceOnMotion() {
        return (resetFaceOnMotion != null) ? resetFaceOnMotion : false;
    }

    public void setResetFaceOnMotion(Boolean resetFaceOnMotion) {
        this.resetFaceOnMotion = resetFaceOnMotion;
    }

    public Boolean getResetBarcodeOnMotion() {
        return (resetBarcodeOnMotion != null) ? resetBarcodeOnMotion : false;
    }

    public void setResetBarcodeOnMotion(Boolean resetBarcodeOnMotion) {
        this.resetBarcodeOnMotion = resetBarcodeOnMotion;
    }

    public String getOutputFolderName() {
        return (outputFolderName != null) ? outputFolderName : "KimlikOutput";
    }

    public void setOutputFolderName(String outputFolderName) {
        this.outputFolderName = outputFolderName;
    }

    public projectEnum.mode getMode() {
        return (mode != null) ? mode : projectEnum.mode.BOTH;
    }

    public void setMode(projectEnum.mode mode) {
        this.mode = mode;
    }

    public Boolean getExitOnSave() {
        return (exitOnSave != null) ? exitOnSave : false;
    }

    public void setExitOnSave(Boolean exitOnSave) {
        this.exitOnSave = exitOnSave;
    }

    public projectEnum.BarcodeSaveFormat getBarcodeSaveMode() {
        return (barcodeSaveMode != null) ? barcodeSaveMode : projectEnum.BarcodeSaveFormat.json;
    }

    public void setBarcodeSaveMode(projectEnum.BarcodeSaveFormat barcodeSaveMode) {
        this.barcodeSaveMode = barcodeSaveMode;
    }

    public Boolean getAutoSave() {
        return autoSave;
    }

    public void setAutoSave(Boolean autoSaveAndExit) {
        this.autoSave = autoSaveAndExit;
    }

    @Override
    public String toString() {
        return "Settings{" + "defaultCamera=" + defaultCamera + ", kullaniciOnayIste=" + kullaniciOnayIste + ", resetFaceOnMotion=" + resetFaceOnMotion + ", resetBarcodeOnMotion=" + resetBarcodeOnMotion + ", outputFolderName=" + outputFolderName + ", mode=" + mode + ", exitOnSave=" + exitOnSave + ", barcodeSaveMode=" + barcodeSaveMode + ", autoSaveAndExit=" + autoSave + '}';
    }
}
