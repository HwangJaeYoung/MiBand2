package com.example.android.bluetoothlegatt.domain.oneM2MList.AE;

import android.content.Context;

import com.example.android.bluetoothlegatt.domain.URLInfomation;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class AE_Create implements AE_Root {

    private Context context;
    private HttpRequester.NetworkResponseListenerXML XMLResponseListener;
    private HttpRequester.NetworkResponseListenerJSON JSONResponseListener;

    private String title;
    private String operation = "POST";
    private String xmlBody;
    private String jsonBody;
    private String url;

    private HashMap<String, String> aeCreateHeaderList;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";
    private String KEY_HEADER_CONTENT_TYPE = "Content-Type";

    public AE_Create(Context context, HttpRequester.NetworkResponseListenerXML XMLResponseListener, HttpRequester.NetworkResponseListenerJSON JSONResponseListener) {

        this.context = context;
        this.XMLResponseListener = XMLResponseListener;
        this.JSONResponseListener = JSONResponseListener;

        this.aeCreateHeaderList = new HashMap<String, String>();

        this.aeCreateHeaderList.put(KEY_HEADER_ACCEPT, "application/json");
        this.aeCreateHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.aeCreateHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "C");
        this.aeCreateHeaderList.put(KEY_HEADER_CONTENT_TYPE, "application/vnd.onem2m-res+xml; ty=2");

        xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<m2m:ae xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" rn = \"" + URLInfomation.AEName + "\">\n" +
                "    <api>0.2.481.2.0001.001.000111</api>\n" +
                "    <rr>false</rr>" +
                "</m2m:ae>";

        jsonBody = "{\n" +
                "    \"m2m:ae\": {\n" +
                "        \"rn\": \"" + URLInfomation.AEName + "\"\n" +
                "        \"api\": \"0.2.481.2.0001.001.000111\"\n" +
                "    }\n" +
                "}";

        url = URLInfomation.serverURL;
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() {
        return aeCreateHeaderList;
    }

    public String getXmlBody() {
        return xmlBody;
    }

    public String getJsonBody() {
        return jsonBody;
    }

    public Context getContext() { return context; }

    public HttpRequester.NetworkResponseListenerXML getXMLResponseListener() { return XMLResponseListener; }

    public HttpRequester.NetworkResponseListenerJSON getJSONResponseListener() { return JSONResponseListener; }
}