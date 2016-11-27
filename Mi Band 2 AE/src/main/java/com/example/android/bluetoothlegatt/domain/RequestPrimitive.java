package com.example.android.bluetoothlegatt.domain;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * Created by Blossom on 2016-11-08.
 */

public class RequestPrimitive {

    private String Operation; // Mandatory

    // Header Value
    private Header headers[]; // Header set
    private String To; // Mandatory
    private String X_M2M_Origin; // Mandatory
    private String X_M2M_RI; // Mandatory
    private String Content_Type; // Mandatory

    // Body
    private String oneM2MBody;

    private String ACCEPT;

    public RequestPrimitive(String operation, String URL, ArrayList<MainHeaderItem> headerListsItem, String bodyItem) {
        Operation = operation;
        To = URL;
        oneM2MBody = bodyItem;

        headers = new Header[headerListsItem.size()];

        for(int i = 0; i < headerListsItem.size(); i++) {
            MainHeaderItem item = (MainHeaderItem)headerListsItem.get(i);
            headers[i] = new BasicHeader(item.getHeaderName(), item.getHeaderValue());


            if(headerListsItem.get(i).getHeaderName().toUpperCase().equals("ACCEPT")) {
                ACCEPT = headerListsItem.get(i).getHeaderValue();
            }
        }
    }

    public String getTo() { return To; }
    public String getOperation() { return Operation; }
    public String getACCEPT() {
        return ACCEPT;
    }
    public String getContent_Type() {
        return Content_Type;
    }
    public String getOneM2MBody() { return oneM2MBody; }
    public Header[] getHeaderList( ) { return headers; }
}