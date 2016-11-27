package com.example.android.bluetoothlegatt.reuse.network;

import android.content.Context;

import com.example.android.bluetoothlegatt.domain.RequestPrimitive;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;



public class oneM2MRequest {

	// Operation number
	public static final int OPERATION_GET = 1;
	public static final int OPERATION_POST = 2;
	public static final int OPERATION_PUT = 3;
	public static final int OPERATION_DELETE = 4;

	public oneM2MRequest( ) { }

	public void JSON(Context context, final HttpRequester.NetworkResponseListenerJSON aNetworkListener, String method, RequestPrimitive requestPrimitive) throws JSONException {
		RequestParams requestParams = new RequestParams( );

		if(method.equals("POST") || method.equals("PUT"))
			HttpRequester.postJSON(context, requestParams, new JsonResponseHandler(aNetworkListener), requestPrimitive);
		else if(method.equals("GET") || method.equals("DELETE"))
			HttpRequester.getJSON(context, requestParams, new JsonResponseHandler(aNetworkListener), requestPrimitive);
	}

	public void XML(Context context, final HttpRequester.NetworkResponseListenerXML aNetworkListener, String method, RequestPrimitive requestPrimitive) {
		RequestParams requestParams = new RequestParams( );

		if(method.equals("POST"))
			HttpRequester.requestXML(context, requestParams, new XMLResponseHandler(aNetworkListener), requestPrimitive, OPERATION_POST);
		else if(method.equals("PUT"))
			HttpRequester.requestXML(context, requestParams, new XMLResponseHandler(aNetworkListener), requestPrimitive, OPERATION_PUT);
		else if(method.equals("GET"))
			HttpRequester.requestXML(context, requestParams, new XMLResponseHandler(aNetworkListener), requestPrimitive, OPERATION_GET);
		else if(method.equals("DELETE"))
			HttpRequester.requestXML(context, requestParams, new XMLResponseHandler(aNetworkListener), requestPrimitive, OPERATION_DELETE);
	}
}