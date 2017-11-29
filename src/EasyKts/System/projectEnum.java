/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EasyKts.System;

/**
 *
 * @author Cagdas
 */
public class projectEnum {

    public static enum mode{
        FACE(),
        BARCODE(),
        BOTH();
    }
    
    public static enum BarcodeSaveFormat{
        simple(),
        json();
    }
}
