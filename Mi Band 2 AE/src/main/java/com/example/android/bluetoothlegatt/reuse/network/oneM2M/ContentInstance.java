package com.example.android.bluetoothlegatt.reuse.network.oneM2M;

import android.util.Log;

import com.example.android.bluetoothlegatt.domain.MainHeaderItem;
import com.example.android.bluetoothlegatt.domain.RequestPrimitive;
import com.example.android.bluetoothlegatt.domain.oneM2MList.ContentInstance.ContentInstance_Root;
import com.example.android.bluetoothlegatt.reuse.network.oneM2MRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Blossom on 2016-11-27.
 */

public class ContentInstance {
    private ContentInstance_Root contentInstanceRoot;

    public ContentInstance(ContentInstance_Root contentInstanceRoot) {
        this.contentInstanceRoot = contentInstanceRoot;
    }

    public void oneM2MResuest( ) {

        HashMap<String, String> headerItemList = contentInstanceRoot.getHeaderList();
        ArrayList<MainHeaderItem> headerLists = new ArrayList<MainHeaderItem>();

        Iterator<String> iterator = headerItemList.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Log.i("testing", key);
            headerLists.add(new MainHeaderItem(key, headerItemList.get(key)));
        }

        oneM2MRequest oneM2MRequestor = new oneM2MRequest();

        RequestPrimitive requestPrimitive = new RequestPrimitive(contentInstanceRoot.getOperation(), contentInstanceRoot.getRequestURL(), headerLists, contentInstanceRoot.getXmlBody());

        String accept = requestPrimitive.getACCEPT();
        String operation = requestPrimitive.getOperation();

        if(accept.equals("application/xml")) {
            oneM2MRequestor.XML(contentInstanceRoot.getContext(), contentInstanceRoot.getXMLResponseListener(), operation, requestPrimitive);
        } else if(accept.equals("application/json")) {
            try {
                oneM2MRequestor.JSON(contentInstanceRoot.getContext(), contentInstanceRoot.getJSONResponseListener(), operation, requestPrimitive);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}