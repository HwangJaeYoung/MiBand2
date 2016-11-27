package com.example.android.bluetoothlegatt.testing;

import android.content.Context;
import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by JaeYoung Hwang on 2016-09-30.
 */

public class oneM2MTester {

    private static final int PORT = 8080; // Android server port
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

            Log.d("session", session.toString());
            Map<String, String> map = session.getHeaders();
            Iterator<String> keys = map.keySet().iterator();

			/* while( keys.hasNext() ){
				String key = keys.next();
				Log.i("Testing", "Key : " + key + ", " + "Value : " + map.get(key));
			} */

            testcaseNumber = Integer.parseInt(map.get("testcase"));

            oneM2MStimulator oneM2MStimulator = new oneM2MStimulator( );

            switch(testcaseNumber) {
                case oneM2MTestcase.TESTCASE_AE_INITIAL_REGISTRATION :
                case oneM2MTestcase.TESTCASE_AE_RE_REGISTRATION :
                    oneM2MStimulator.Create();
                    break;

                case 2 :
                    break;

                case 3 :
                    break;
            }
            return new NanoHTTPD.Response("Android Response");
        }
    }

    public WebServer getWebServer() {
        server = new WebServer();
        return server;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String ipAddr = inetAddress.getHostAddress();
                        return ipAddr;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("Server", ex.toString());
        }
        return null;
    }

    public int getPortNumber() {
        return PORT;
    }

    interface oneM2MOperation {
        void Create();
        void Retrieve();
        void Update();
        void Delete();
        void Notify();
    }
}