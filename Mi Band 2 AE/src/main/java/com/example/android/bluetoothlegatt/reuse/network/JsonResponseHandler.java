package com.example.android.bluetoothlegatt.reuse.network;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class JsonResponseHandler extends JsonHttpResponseHandler {
    private HttpRequester.NetworkResponseListenerJSON networkResponseListener;

    public JsonResponseHandler(HttpRequester.NetworkResponseListenerJSON aNetworkResponseListener) {
        this.networkResponseListener = aNetworkResponseListener;
    }

    // 여기가 콜백 메소드 부분이다.
    // Fired when a request returns successfully
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        this.networkResponseListener.onSuccess(statusCode, headers, response);
    }

    // Returns when request failed
    @Override
    public void onFailure(int statusCode, Header[] headers,  Throwable throwable, JSONObject response) {
        this.networkResponseListener.onFail(statusCode, headers, response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        this.networkResponseListener.onFail(statusCode, headers, responseString);
    }
}