package org.deguet.hexile.service;

import android.graphics.Color;

/**
 * Created by joris on 16-02-12.
 */
public class Consts {

    //public static final int strokeColor(){return Color.rgb(213, 99, 82);}
    //public static int hexBorderColor() {return Color.rgb(149, 241, 191);}
    //public static int hexFillColor() {return Color.rgb(169, 251, 211);}

    public static final int strokeColor(){return Color.rgb(62, 92, 152);}
    public static int hexBorderColor() {return Color.rgb(192, 222, 245);}
    public static int hexFillColor(){return Color.rgb(202, 232, 255);}

    public static String prefName(){return "Hexile-prefs";}
    public static String hasPlayed() {return "hexile-has-played";}
    public static String hasPaid() {return "hexile-has-paid";}

    public static String extraNumberOfCols() { return "number-f-cols";}
    public static String extraHexile() {return "serialized-hexile";}


    public static String tutoMode() { return "tuto-mode";}
}
