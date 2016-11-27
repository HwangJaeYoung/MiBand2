package com.example.android.bluetoothlegatt.testing;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by JaeYoung Hwang on 2016-09-30.
 */

public class oneM2MTester {

    private static final int PORT = 62590; // Android server port
    private WebServer server;
    private Context context;
    private int testcaseNumber;

    public oneM2MTester(Context context) {
        this.context = context;
    }

    // Defining the web server for oneM2M
    public class WebServer extends NanoHTTPD {
        public WebServer() {
            super(PORT);
        }

        @Override
        public Response serve(IHTTPSession session) {

            String testcase = "";
            String parsedTestcase = "";

            try {
                HashMap<String, String> files = new HashMap<String, String>();
                session.parseBody(files);

                Iterator<String> keys = files.keySet().iterator();

                while(keys.hasNext()) {
                    String key = keys.next();
                    testcase = files.get(key);
                    Log.i("testing", "Key : " + key + ", " + "Value : " + files.get(key));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            char testcaseArray[] = testcase.toCharArray();

            for(int i = 0; i < testcaseArray.length; i++) {
                if(testcaseArray[i] != '\"')
                    parsedTestcase += testcaseArray[i];
            }

            Log.i("testing", "Parsed Testcase : " + parsedTestcase);

            oneM2MStimulator oneM2MStimulator = new oneM2MStimulator(context);

            switch(parsedTestcase) {
                case oneM2MTestcase.TC_AE_REG_BV_001 :
                    oneM2MStimulator.TC_AE_REG_BV_001();
                  break;

                case oneM2MTestcase.TC_AE_DMR_BV_001 :
                    oneM2MStimulator.TC_AE_REG_BV_001();
                    break;

                case oneM2MTestcase.TC_AE_DMR_BV_003 :
                    oneM2MStimulator.TC_AE_DMR_BV_003();
                    break;
            }
            return new NanoHTTPD.Response("Android Response");
        }
    }

    public WebServer getWebServer() {
        server = new WebServer();
        return server;
    }

    public int getPortNumber() {
        return PORT;
    }

    interface oneM2MOperation {
        void TC_AE_REG_BV_001();
        void TC_AE_DMR_BV_001();
        void TC_AE_DMR_BV_003();
    }
}