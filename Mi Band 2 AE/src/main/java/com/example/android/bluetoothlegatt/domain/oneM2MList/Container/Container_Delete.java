package com.example.android.bluetoothlegatt.domain.oneM2MList.Container;

import android.content.Context;

import com.example.android.bluetoothlegatt.domain.URLInfomation;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Root;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class Container_Delete implements Container_Root {

    private Context context;
    private HttpRequester.NetworkResponseListenerXML XMLResponseListener;
    private HttpRequester.NetworkResponseListenerJSON JSONResponseListener;

    private String operation = "DELETE";
    private HashMap<String, String> containerDeleteHeaderList;
    private String url;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";

    public Container_Delete() {

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
