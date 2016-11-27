package com.example.android.bluetoothlegatt.domain.oneM2MList.AE;

import com.example.android.bluetoothlegatt.domain.URLInfomation;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class AE_Retrieve implements AE_Root {

    private String title;
    private String operation = "GET";
    private HashMap<String, String> aeRetrieveHeaderList;
    private String url;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";

    public AE_Retrieve() {
        this.aeRetrieveHeaderList = new HashMap<String, String>();

        this.aeRetrieveHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.aeRetrieveHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.aeRetrieveHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");

        url = URLInfomation.serverURL + "/" + URLInfomation.AEName;
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() { return aeRetrieveHeaderList; }

    public String getXmlBody() {
        return null;
    }

    public String getJsonBody() {
        return null;
    }
}