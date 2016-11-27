package com.example.android.bluetoothlegatt.domain.oneM2MList.AE;

import java.util.HashMap;
/**
 * Created by Blossom on 2016-11-03.
 */

public class AE_Update implements AE_Root {

    private String title;
    private String operation = "PUT";
    private String xmlBody;
    private String jsonBody;
    private String url;

    private HashMap<String, String> aeUpdateHeaderList;

    private String KEY_HEADER_ACCEPT = "Accept";
    private String KEY_HEADER_X_M2M_RI = "X-M2M-RI";
    private String KEY_HEADER_X_M2M_ORIGIN = "X-M2M-Origin";


    public AE_Update() {
        this.title = "AE 리소스 변경";

        this.aeUpdateHeaderList = new HashMap<String, String>();

        this.aeUpdateHeaderList.put(KEY_HEADER_ACCEPT, "application/xml");
        this.aeUpdateHeaderList.put(KEY_HEADER_X_M2M_RI, "12345");
        this.aeUpdateHeaderList.put(KEY_HEADER_X_M2M_ORIGIN, "Origin");

        xmlBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<m2m:ae xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" rn=\"seslab_ae5\">\n" +
                "    <api>0.2.481.2.0001.001.000111</api>\n" +
                "</m2m:ae>";

        jsonBody = "{\n" +
                "\t\"m2m:ae\": {\n" +
                "\t\t\"acpi\": [\"ODL-oneM2M-Cse/TestAE/TestACP\"]\n" +
                "\t}\n" +
                "}";

        url = "http://203.253.128.151:7579/mobius-yt/MiBand2";
    }

    @Override
    public String getRequestURL() {
        return url;
    }

    public String getOperation() { return operation; }

    public HashMap<String, String> getHeaderList() {
        return aeUpdateHeaderList;
    }

    public String getXmlBody() {
        return xmlBody;
    }

    public String getJsonBody() {
        return jsonBody;
    }
}
