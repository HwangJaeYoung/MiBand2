package com.example.android.bluetoothlegatt.domain.oneM2MList.Container;

import java.util.HashMap;

/**
 * Created by Blossom on 2016-11-27.
 */

public interface Container_Root {
    String getRequestURL();
    String getOperation();
    HashMap<String, String> getHeaderList();
    String getXmlBody();
    String getJsonBody();
}
