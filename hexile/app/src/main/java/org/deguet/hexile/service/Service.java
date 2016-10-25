package org.deguet.hexile.service;

import android.animation.ArgbEvaluator;

//import com.google.common.eventbus.EventBus;

//import org.apache.commons.io.IOUtils;
import org.deguet.hexile.model.Hexile;
import org.deguet.hexile.model.Tile;
import org.deguet.hexile.model.Type;

/**
 * Created by joris on 16-02-13.
 */
public class Service {

    public static Hexile tutoHexile(){
        Hexile result = new Hexile(5,5);
        result.get(2,2).type = Type.Four3;
        result.get(2,2).rotation = 3;
        result.get(2,2).drawing = Tile.Drawing.Pointy;
        {
            result.get(1,1).type = Type.Two2;
            result.get(1,1).rotation = 5;
            result.get(1,1).drawing = Tile.Drawing.Pointy;
            result.get(1,2).type = Type.Two2;
            result.get(1,2).rotation = 0;
            result.get(1,2).drawing = Tile.Drawing.Pointy;

            result.get(3,1).type = Type.Two2;
            result.get(3,1).rotation = 3;
            result.get(3,1).drawing = Tile.Drawing.Pointy;
            result.get(3,2).type = Type.Two2;
            result.get(3,2).rotation = 1;
            result.get(3,2).drawing = Tile.Drawing.Pointy;
        }

        {
            result.get(1,0).type = Type.One;
            result.get(1,0).rotation = 2;
            result.get(1,0).drawing = Tile.Drawing.Pointy;
            result.get(1,3).type = Type.One;
            result.get(1,3).rotation = 5;
            result.get(1,3).drawing = Tile.Drawing.Pointy;

            result.get(3,0).type = Type.One;
            result.get(3,0).rotation = 2;
            result.get(3,0).drawing = Tile.Drawing.Pointy;
            result.get(3,3).type = Type.One;
            result.get(3,3).rotation = 5;
            result.get(3,3).drawing = Tile.Drawing.Pointy;
        }

        return result;
    }

    /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
    public static int interpolateColor(int a, int b, double proportion) {
        return (Integer) new ArgbEvaluator().evaluate((float)proportion, a, b);
    }
}
