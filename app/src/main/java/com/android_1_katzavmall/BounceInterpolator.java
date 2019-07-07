package com.android_1_katzavmall;

import android.view.animation.Interpolator;

public class BounceInterpolator implements Interpolator {

    double myAmplitude;
    double myFrequancy;

    BounceInterpolator(double amplitude, double frequancy){
        myAmplitude = amplitude;
        myFrequancy = frequancy;
    }
    @Override
    public float getInterpolation(float time) {
        return (float)(-1*Math.pow(Math.E, -time/myAmplitude)*Math.cos(myFrequancy*time)+1);
    }
}
