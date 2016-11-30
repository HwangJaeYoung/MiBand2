package com.example.android.bluetoothlegatt.domain;

/**
 * Created by Blossom on 2016-11-27.
 */



public interface URLInfomation {
    // TTA : http://192.168.1.185:10000/~/oneM2M/V1_13_1
    // Mobius-yt : http://203.253.128.151:7579/mobius-yt
    // TITAN : http://192.168.125.142:44664";
    // NTT : "http://192.168.1.222:8090/_/onem2m.lab.ntt.co.jp/MN_CSE_001/mn_cse_name_001";
    // HERIT : "http://192.168.1.222:8090/~/herit-in/herit-cse";
    // KT : "http://192.168.1.114:11111/~/IN_CSE/cb-1";

    String serverURL = "http://203.253.128.151:7579/mobius-yt";
    String AEName = "MiBand2";
    String containerName = "WalkingSteps";
}