package com.example.android.bluetoothlegatt.domain.oneM2MList.Container;

import com.example.android.bluetoothlegatt.domain.URLInfomation;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Root;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class Container_Delete implements Container_Root {

    private String title;
    private String operation = "DELETE";
    private HashMap<String, String> containerDeleteHeaderList;
    private String url;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";

    public Container_Delete() {
        this.title = "AE 리소스 삭제";

        this.containerDeleteHeaderList = new HashMap<String, String>();

        this.containerDeleteHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.containerDeleteHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.containerDeleteHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");

        url = URLInfomation.serverURL + "/" + URLInfomation.AEName + "/" + URLInfomation.containerName;
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() {
        return containerDeleteHeaderList;
    }

    public String getXmlBody() {
        return null;
    }

    public String getJsonBody() {
        return null;
    }
}
