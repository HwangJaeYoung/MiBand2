package com.example.android.bluetoothlegatt.reuse.network;

import android.content.Context;
import android.os.Looper;

import com.example.android.bluetoothlegatt.domain.RequestPrimitive;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpRequester {
    //private static final String BASE_URL = "http://192.168.35.135:62590";
    private static final String BASE_URL = "http://203.253.128.151:7579";

    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    private static AsyncHttpClient client = new AsyncHttpClient();

    // JSON Function
    public static void postJSON(Context context, RequestParams params, JsonHttpResponseHandler responseHandler, RequestPrimitive requestPrimitive) {
        requestJSON(context, params, responseHandler, requestPrimitive, true);
    }

    public static void getJSON(Context context, RequestParams params, JsonHttpResponseHandler responseHandler, RequestPrimitive requestPrimitive) {
        requestJSON(context, params, responseHandler, requestPrimitive, false);
    }

    public static void requestJSON(Context context, RequestParams params, JsonHttpResponseHandler responseHandler, RequestPrimitive requestPrimitive, boolean anIsPost) {

        getClient().setTimeout(3000);

        if (anIsPost) {
            StringEntity oneM2MBody = null;

            try {
                oneM2MBody = new StringEntity(requestPrimitive.getOneM2MBody());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            getClient().post(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList(), oneM2MBody, requestPrimitive.getContent_Type(), responseHandler);
        } else
            getClient().get(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList() , params, responseHandler);
    }

    public static void requestXML(Context context, RequestParams params, XMLResponseHandler responseHandler, RequestPrimitive requestPrimitive, int operation) {

        getClient().setTimeout(3000);

        if (operation == oneM2MRequest.OPERATION_POST) {
            StringEntity oneM2MBody = null;

            try {
                oneM2MBody = new StringEntity(requestPrimitive.getOneM2MBody());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            getClient().post(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList(), oneM2MBody, requestPrimitive.getContent_Type(), responseHandler);
        } else if (operation == oneM2MRequest.OPERATION_GET) {
            getClient().get(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList(), params, responseHandler);
        } else if(operation == oneM2MRequest.OPERATION_DELETE) {
            getClient().delete(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList(), responseHandler);
        } else if(operation == oneM2MRequest.OPERATION_PUT) {

            StringEntity oneM2MBody = null;

            try {
                oneM2MBody = new StringEntity(requestPrimitive.getOneM2MBody());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            getClient().put(context, requestPrimitive.getTo(), requestPrimitive.getHeaderList(), oneM2MBody, requestPrimitive.getContent_Type(), responseHandler);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    private static AsyncHttpClient getClient() {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return client;
    }

    // 처리를 위해 공통적인 규약을 준것이다.
    public interface NetworkResponseListenerJSON {
        void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject);
        void onFail(int statusCode, Header[] headers, JSONObject jsonObject, String responseString);
    }

    public static abstract class NetworkResponseListenerXML extends DefaultHandler {

        private String startElement;
        private Map<String, String > xmlResponse = new HashMap<String, String>();

        public abstract void onSuccess(int statusCode, Header[] headers, NetworkResponseListenerXML networkResponseListenerXML, String responseBody);
        public abstract void onFail(int statusCode, Header[] headers, NetworkResponseListenerXML networkResponseListenerXML, String responseBody);

        public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) {
            startElement = rawName;
        }

        public void characters(char[] data, int off, int length) {
            if (length > 0 && data[0] != '\n') {
                xmlResponse.put(startElement, new String(data, off, length));
            }
        }

        public Map<String, String > getXmlResponse() {
            return xmlResponse;
        }
    }
}