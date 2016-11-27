package com.example.android.bluetoothlegatt.domain.oneM2MList.Container;

import com.example.android.bluetoothlegatt.domain.URLInfomation;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Root;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-03.
 */

public class Container_Create implements Container_Root {

    private String title;
    private String operation = "POST";
    private String xmlBody;
    private String jsonBody;
    private String url;

    private HashMap<String, String> containerCreateHeaderList;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";
    private String KEY_HEADER_CONTENT_TYPE = "Content-Type";

    public Container_Create() {
        this.containerCreateHeaderList = new HashMap<String, String>();

        this.containerCreateHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.containerCreateHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.containerCreateHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");
        this.containerCreateHeaderList.put(KEY_HEADER_CONTENT_TYPE, "application/vnd.onem2m-res+xml; ty=3");


        xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<m2m:cnt xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" rn = \"" + URLInfomation.containerName + "\">\n" +
                "</m2m:cnt>";

        jsonBody = "{\n" +
                "    \"m2m:ae\": {\n" +
                "        \"rn\": \"andtesting33\",\n" +
                "        \"api\": \"0.2.481.2.0001.001.000111\"\n" +
                "    }\n" +
                "}";

        url = URLInfomation.serverURL + "/" + URLInfomation.AEName;
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() {
        return containerCreateHeaderList;
    }

    public String getXmlBody() {
        return xmlBody;
    }

    public String getJsonBody() {
        return jsonBody;
    }
}
