package com.example.android.bluetoothlegatt.domain;

/**
 * Created by Blossom on 2016-10-27.
 */

public class MainHeaderItem {

    private String headerName, headerValue;

    public MainHeaderItem(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }
}