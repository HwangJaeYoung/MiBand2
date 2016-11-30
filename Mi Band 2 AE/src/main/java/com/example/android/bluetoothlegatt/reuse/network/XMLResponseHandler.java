package com.example.android.bluetoothlegatt.reuse.network;

import android.util.Log;

import com.example.android.bluetoothlegatt.etc.PrettyFormatter;
import com.loopj.android.http.SaxAsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

public class XMLResponseHandler extends SaxAsyncHttpResponseHandler<HttpRequester.NetworkResponseListenerXML> {
    private HttpRequester.NetworkResponseListenerXML networkResponseListener;
    private String responseBody;

    public XMLResponseHandler(HttpRequester.NetworkResponseListenerXML aNetworkResponseListener) {
        super(aNetworkResponseListener);
        this.networkResponseListener = aNetworkResponseListener;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, HttpRequester.NetworkResponseListenerXML networkResponseListenerXML) {
        this.networkResponseListener.onSuccess(statusCode, headers, networkResponseListenerXML, responseBody);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, HttpRequester.NetworkResponseListenerXML networkResponseListenerXML) {
        this.networkResponseListener.onFail(statusCode, headers, networkResponseListenerXML, responseBody);
    }


    @Override
    public byte[] getResponseData(HttpEntity httpEntity) throws IOException {

        InputStream inputStream = null;

        try {
            inputStream = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        responseBody = convertStreamToString(inputStream);
        Log.i("testing", PrettyFormatter.getPrettyXML(responseBody));

        return null;
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}