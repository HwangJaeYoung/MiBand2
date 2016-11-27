package com.example.android.bluetoothlegatt.domain.oneM2MList.AE;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class AE_Delete implements AE_Root {

    private String title;
    private String operation = "DELETE";
    private HashMap<String, String> aeRetrieveHeaderList;
    private String url;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";

    public AE_Delete() {
        this.title = "AE 리소스 삭제";

        this.aeRetrieveHeaderList = new HashMap<String, String>();

        this.aeRetrieveHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.aeRetrieveHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.aeRetrieveHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");

        url = "http://203.253.128.151:7579/mobius-yt/MiBand2";
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() {
        return aeRetrieveHeaderList;
    }

    public String getXmlBody() {
        return null;
    }

    public String getJsonBody() {
        return null;
    }
}
