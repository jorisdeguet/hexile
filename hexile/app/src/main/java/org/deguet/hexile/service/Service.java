package org.deguet.hexile.service;

import android.animation.ArgbEvaluator;

/**
 * Created by joris on 16-02-13.
 */
public class Service {

    /** Returns an interpoloated color, between <code>a</code> and <code>b</code> */
    public static int interpolateColor(int a, int b, double proportion) {
        return (Integer) new ArgbEvaluator().evaluate((float)proportion, a, b);
    }

}
