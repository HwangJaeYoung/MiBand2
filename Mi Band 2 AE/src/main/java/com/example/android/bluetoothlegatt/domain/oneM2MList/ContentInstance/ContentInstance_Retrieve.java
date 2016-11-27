package com.example.android.bluetoothlegatt.domain.oneM2MList.ContentInstance;

import android.content.Context;

import com.example.android.bluetoothlegatt.domain.URLInfomation;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class ContentInstance_Retrieve implements ContentInstance_Root {

    private Context context;
    private HttpRequester.NetworkResponseListenerXML XMLResponseListener;
    private HttpRequester.NetworkResponseListenerJSON JSONResponseListener;

    private String operation = "GET";
    private HashMap<String, String> contentInstanceRetrieveHeaderList;
    private String url;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";

    public ContentInstance_Retrieve(Context context, HttpRequester.NetworkResponseListenerXML XMLResponseListener, HttpRequester.NetworkResponseListenerJSON JSONResponseListener) {

        this.context = context;
        this.XMLResponseListener = XMLResponseListener;
        this.JSONResponseListener = JSONResponseListener;

        this.contentInstanceRetrieveHeaderList = new HashMap<String, String>();

        this.contentInstanceRetrieveHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.contentInstanceRetrieveHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.contentInstanceRetrieveHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");

        url = URLInfomation.serverURL + "/" + URLInfomation.AEName + "/" + URLInfomation.containerName + "/" + "latest";
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() { return contentInstanceRetrieveHeaderList; }

    public String getXmlBody() {
        return null;
    }

    public String getJsonBody() {
        return null;
    }

    public Context getContext() { return context; }

    public HttpRequester.NetworkResponseListenerXML getXMLResponseListener() { return XMLResponseListener; }

    public HttpRequester.NetworkResponseListenerJSON getJSONResponseListener() { return JSONResponseListener; }
}