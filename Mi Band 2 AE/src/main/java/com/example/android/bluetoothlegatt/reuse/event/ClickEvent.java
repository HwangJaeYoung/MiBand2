package com.example.android.bluetoothlegatt.reuse.event;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Create;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Delete;
import com.example.android.bluetoothlegatt.domain.oneM2MList.AE.AE_Retrieve;
import com.example.android.bluetoothlegatt.domain.oneM2MList.Container.Container_Create;
import com.example.android.bluetoothlegatt.domain.oneM2MList.Container.Container_Delete;
import com.example.android.bluetoothlegatt.domain.oneM2MList.Container.Container_Retrieve;
import com.example.android.bluetoothlegatt.domain.oneM2MList.ContentInstance.ContentInstance_Create;
import com.example.android.bluetoothlegatt.domain.oneM2MList.ContentInstance.ContentInstance_Retrieve;
import com.example.android.bluetoothlegatt.reuse.network.HttpRequester;
import com.example.android.bluetoothlegatt.reuse.network.oneM2M.AE;
import com.example.android.bluetoothlegatt.reuse.network.oneM2M.Container;
import com.example.android.bluetoothlegatt.reuse.network.oneM2M.ContentInstance;

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

    private Button aeDelete;
    private Button cntDelete;
    private Button cinDelete;

    public ClickEvent(Context context,  HttpRequester.NetworkResponseListenerXML XMLResponseListener,  HttpRequester.NetworkResponseListenerJSON JSONResponseListener,
                      Button aeRegistration, Button cntRegistration, Button cinRegistration,
                      Button aeRetrieve ,Button cntRetrieve, Button cinRetrieve,
                      Button aeDelete, Button cntDelete, Button cinDelete) {

        this.context = context;
        this.XMLResponseListener = XMLResponseListener;
        this.JSONResponseListener = JSONResponseListener;

        this.aeRegistration = aeRegistration;
        this.cntRegistration = cntRegistration;
        this.cinRegistration = cinRegistration;

        this.aeRetrieve = aeRetrieve;
        this.cntRetrieve = cntRetrieve;
        this.cinRetrieve = cinRetrieve;

        this.aeDelete = aeDelete;
        this.cntDelete = cntDelete;
        this.cinDelete = cinDelete;
    }

    public void setListener( ) {

        // AE listener
        aeRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AE aeRegistration = new AE(context, XMLResponseListener, JSONResponseListener, new AE_Create());
                aeRegistration.oneM2MResuest();
            }
        });

        aeRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AE aeRetrieve = new AE(context, XMLResponseListener, JSONResponseListener, new AE_Retrieve());
                aeRetrieve.oneM2MResuest();
            }
        });

        aeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AE aeDelete = new AE(context, XMLResponseListener, JSONResponseListener, new AE_Delete());
                aeDelete.oneM2MResuest();
            }
        });

        // Container listener
        cntRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Container containerRegistration = new Container(context, XMLResponseListener, JSONResponseListener, new Container_Create());
                containerRegistration.oneM2MResuest();
            }
        });

        cntRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Container containerRetrieve = new Container(context, XMLResponseListener, JSONResponseListener, new Container_Retrieve());
                containerRetrieve.oneM2MResuest();
            }
        });

        cntDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Container containerDelete = new Container(context, XMLResponseListener, JSONResponseListener, new Container_Delete());
                containerDelete.oneM2MResuest();
            }
        });

        // contentInstance listener
        cinRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInstance contentInstanceRegistration = new ContentInstance(context, XMLResponseListener, JSONResponseListener, new ContentInstance_Create());
                contentInstanceRegistration.oneM2MResuest();
            }
        });

        cinRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentInstance contentInstanceRetrieve = new ContentInstance(context, XMLResponseListener, JSONResponseListener, new ContentInstance_Retrieve());
                contentInstanceRetrieve.oneM2MResuest();
            }
        });
    }
}