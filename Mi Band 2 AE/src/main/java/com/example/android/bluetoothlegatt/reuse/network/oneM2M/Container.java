package com.example.android.bluetoothlegatt.reuse.network.oneM2M;

import android.content.Context;
import android.util.Log;

import com.example.android.bluetoothlegatt.domain.MainHeaderItem;
import com.example.android.bluetoothlegatt.domain.RequestPrimitive;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Root;
import com.example.android.bluetoothlegatt.domain.oneM2MList.Container.Container_Root;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;
import com.example.android.bluetoothlegatt.reuse.network.oneM2MRequest;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Blossom on 2016-11-27.
 */

public class Container {

    private Context context;
    private Container_Root containerRoot;
    private HttpRequester.NetworkResponseListenerXML XMLResponseListener;
    private HttpRequester.NetworkResponseListenerJSON JSONResponseListener;

    public Container(Context context, HttpRequester.NetworkResponseListenerXML XMLResponseListener, HttpRequester.NetworkResponseListenerJSON JSONResponseListener, Container_Root containerRoot) {
        this.context = context;
        this.containerRoot = containerRoot;
        this.XMLResponseListener = XMLResponseListener;
        this.JSONResponseListener = JSONResponseListener;
    }

    public void oneM2MResuest( ) {

        HashMap<String, String> headerItemList = containerRoot.getHeaderList();
        ArrayList<MainHeaderItem> headerLists = new ArrayList<MainHeaderItem>();

        Iterator<String> iterator = headerItemList.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Log.i("testing", key);
            headerLists.add(new MainHeaderItem(key, headerItemList.get(key)));
        }

        oneM2MRequest oneM2MRequestor = new oneM2MRequest();

        RequestPrimitive requestPrimitive = new RequestPrimitive(containerRoot.getOperation(), containerRoot.getRequestURL(), headerLists, containerRoot.getXmlBody());

        String accept = requestPrimitive.getACCEPT();
        String operation = requestPrimitive.getOperation();

        if(accept.equals("application/xml")) {
            oneM2MRequestor.XML(context, XMLResponseListener, operation, requestPrimitive);
        } else if(accept.equals("application/json")) {
            try {
                oneM2MRequestor.JSON(context, JSONResponseListener, operation, requestPrimitive);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
