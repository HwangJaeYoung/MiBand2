package com.example.android.bluetoothlegatt.reuse.event;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Create;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Retrieve;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;
import com.example.android.bluetoothlegatt.reuse.network.oneM2M.AE;

/**
 * Created by Blossom on 2016-11-27.
 */

public class ClickEvent {

    // Default setting
    private Context context;
    private HttpRequester.NetworkResponseListenerXML XMLResponseListener;
    private  HttpRequester.NetworkResponseListenerJSON JSONResponseListener;


    // Manual CRUDN Button lists
    private Button aeRegistration;
    private Button cntRegistration;
    private Button cinRegistration;

    private Button aeRetrieve;
    private Button cntRetrieve;
    private Button cinRetrieve;

    public ClickEvent(Context context,  HttpRequester.NetworkResponseListenerXML XMLResponseListener,  HttpRequester.NetworkResponseListenerJSON JSONResponseListener,
                      Button aeRegistration,Button cntRegistration,Button cinRegistration, Button aeRetrieve ,Button cntRetrieve, Button cinRetrieve) {

        this.context = context;
        this.XMLResponseListener = XMLResponseListener;
        this.JSONResponseListener = JSONResponseListener;

        this.aeRegistration = aeRegistration;
        this.cntRegistration = cntRegistration;
        this.cinRegistration = cinRegistration;

        this.aeRetrieve = aeRetrieve;
        this.cntRetrieve = cntRetrieve;
        this.cinRetrieve = cinRetrieve;
    }

    public void setListener( ) {
        aeRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AE aeRegistration = new AE(context, XMLResponseListener, JSONResponseListener, new AE_Create());
                aeRegistration.oneM2MResuest();
            }
        });

        cntRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cinRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        aeRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AE aeRetrieve = new AE(context, XMLResponseListener, JSONResponseListener, new AE_Retrieve());
                aeRetrieve.oneM2MResuest();
            }
        });

        cntRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cinRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}
