package com.android_1_katzavmall;

import android.view.animation.Interpolator;

public class BounceInterpolator implements Interpolator {

    private double myAmplitude = 0.5;
    private double myFrequancy = 10;

    BounceInterpolator(double amplitude, double frequancy){
        amplitude = myAmplitude;
        frequancy = myFrequancy;
    }
    @Override
    public float getInterpolation(float time) {
        return (float)(-1*Math.pow(Math.E, -time/myAmplitude)*Math.cos(myFrequancy*time)+1);
    }
}
