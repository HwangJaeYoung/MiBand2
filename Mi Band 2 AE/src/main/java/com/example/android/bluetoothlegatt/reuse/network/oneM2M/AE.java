package com.example.android.bluetoothlegatt.reuse.network.oneM2M;

import android.util.Log;

import com.example.android.bluetoothlegatt.domain.MainHeaderItem;
import com.example.android.bluetoothlegatt.domain.RequestPrimitive;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Root;
import com.example.android.bluetoothlegatt.reuse.network.oneM2MRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Blossom on 2016-11-27.
 */

public class AE {
    private AE_Root aeRoot;

    public AE( AE_Root aeRoot) {
        this.aeRoot = aeRoot;
    }

    public void oneM2MResuest( ) {

        HashMap<String, String> headerItemList = aeRoot.getHeaderList();
        ArrayList<MainHeaderItem> headerLists = new ArrayList<MainHeaderItem>();

        Iterator<String> iterator = headerItemList.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            headerLists.add(new MainHeaderItem(key, headerItemList.get(key)));
        }

        oneM2MRequest oneM2MRequestor = new oneM2MRequest();

        RequestPrimitive requestPrimitive = new RequestPrimitive(aeRoot.getOperation(), aeRoot.getRequestURL(), headerLists, aeRoot.getXmlBody());

        String accept = requestPrimitive.getACCEPT();
        String operation = requestPrimitive.getOperation();

        if(accept.equals("application/xml")) {
            oneM2MRequestor.XML(aeRoot.getContext(), aeRoot.getXMLResponseListener(), operation, requestPrimitive);
        } else if(accept.equals("application/json")) {
            try {
                oneM2MRequestor.JSON(aeRoot.getContext(), aeRoot.getJSONResponseListener(), operation, requestPrimitive);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}