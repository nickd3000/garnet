package com.physmo.garnet.curve;



public class StandardCurve implements Curve {

    public CurveType curveType;

    public StandardCurve(CurveType curveType) {
        this.curveType = curveType;
    }

    @Override
    public double value(double x) {

        if (x<0) x=0;
        if (x>1) x=1;

        switch (curveType) {
            case LINE_FLAT:
                return 1;
            case LINE_DOWN:
                return lerp(1,0,x);
            case LINE_UP:
                return lerp(0,1,x);
            case EASE_IN_SINE:
                return 1 - Math.cos((x * Math.PI) / 2);
            case EASE_OUT_SINE:
                return Math.sin((x * Math.PI) / 2);
        }
        return 0;
    }

    public static double lerp(double v1, double v2, double pos) {
        double span = v2 - v1;
        return (v1 + span * pos);
    }
}
