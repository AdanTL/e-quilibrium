package com.app.sirdreadlocks.e_quilibrium;

/**
 * Created by Adán on 17/11/2016.
 */

public class Test {
    private long date;
    private String type;
    private float OSI_AS;
    private float OSI_SD;
    private float API_AS;
    private float API_SD;
    private float MLI_AS;
    private float MLI_SD;
    private float TZ_A ;
    private float TZ_B ;
    private float TZ_C ;
    private float TZ_D ;
    private float TQ_I ;
    private float TQ_II ;
    private float TQ_III;
    private float TQ_IV ;

    public Test (){
        // Default constructor required for calls to DataSnapshot.getValue(Test.class)
    }

    public Test(long date, String type, float OSI_AS, float OSI_SD, float API_AS, float API_SD, float MLI_AS, float MLI_SD, float TZ_A, float TZ_B, float TZ_C, float TZ_D, float TQ_I, float TQ_II, float TQ_III, float TQ_IV) {
        this.date = date;
        this.type = type;
        this.OSI_AS = OSI_AS;
        this.OSI_SD = OSI_SD;
        this.API_AS = API_AS;
        this.API_SD = API_SD;
        this.MLI_AS = MLI_AS;
        this.MLI_SD = MLI_SD;
        this.TZ_A = TZ_A;
        this.TZ_B = TZ_B;
        this.TZ_C = TZ_C;
        this.TZ_D = TZ_D;
        this.TQ_I = TQ_I;
        this.TQ_II = TQ_II;
        this.TQ_III = TQ_III;
        this.TQ_IV = TQ_IV;
    }

    public long getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public float getOSI_AS() {
        return OSI_AS;
    }

    public float getOSI_SD() {
        return OSI_SD;
    }

    public float getAPI_AS() {
        return API_AS;
    }

    public float getAPI_SD() {
        return API_SD;
    }

    public float getMLI_AS() {
        return MLI_AS;
    }

    public float getMLI_SD() {
        return MLI_SD;
    }

    public float getTZ_A() {
        return TZ_A;
    }

    public float getTZ_B() {
        return TZ_B;
    }

    public float getTZ_C() {
        return TZ_C;
    }

    public float getTZ_D() {
        return TZ_D;
    }

    public float getTQ_I() {
        return TQ_I;
    }

    public float getTQ_II() {
        return TQ_II;
    }

    public float getTQ_III() {
        return TQ_III;
    }

    public float getTQ_IV() {
        return TQ_IV;
    }
}
