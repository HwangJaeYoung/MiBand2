package com.example.android.bluetoothlegatt.domain;

/**
 * Created by Blossom on 2016-11-27.
 */



public interface URLInfomation {

    // TTA : http://192.168.1.185:10000/~/oneM2M/V1_13_1
    // Mobius-yt : http://203.253.128.151:7579/mobius-yt

    String serverURL = "http://192.168.1.185:10000/~/oneM2M/V1_13_1"; //http://192.168.125.142:44674";
    String AEName = "MiBand2";
    String containerName = "WalkingSteps";
}