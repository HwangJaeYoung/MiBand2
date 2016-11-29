package com.example.android.bluetoothlegatt.etc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Blossom on 2016-11-15.
 */

public class PrettyFormatter {

    // Pretty-JSON formatter
    public static String getPrettyJSON(JSONObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonObject.toString());
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }

    // Pretty-XML formatter
    public static String getPrettyXML(String input) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(parseXml(input));
            transformer.transform(source, result);
            return result.getWriter().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return input;
        }
    }

    private static Document parseXml(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}