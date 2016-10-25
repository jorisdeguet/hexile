package org.deguet.hexile.model;

import java.util.Arrays;

/**
 * Created by joris on 16-02-07.
 */
public class Tile {

    public Type type = Type.Zero;

    public enum Drawing {Pointy,Roundy}

    public Drawing drawing = Drawing.Roundy;

    public int rotation = 0;

    public String toString(){
        return Arrays.toString(type.positions)+"@"+this.rotation;
    }
}
