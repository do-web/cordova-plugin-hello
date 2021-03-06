package com.doweb.httpproxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.InetSocketAddress;
import android.util.Log;
import org.apache.cordova.*;
import org.json.JSONArray;

public class Httpproxy extends CordovaPlugin {
    
    
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
        
        if (action.equals("get")) {
            
            String uri = data.getString(0);
            String proxyHost = data.getString(1);
            Integer proxyPort = data.getInt(2);

            try {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                HttpURLConnection con = (HttpURLConnection)new URL(uri).openConnection(proxy);
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                con.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
                String resultData = readStream(con.getInputStream());
                con.disconnect();
                if(resultData == "") {
                    callbackContext.error("Empty response.");
                } else {
                    callbackContext.success(resultData);
                }
            } catch (java.net.SocketTimeoutException e) {
                callbackContext.error("Error: " + e.getMessage());
            } catch (Exception e) {
                callbackContext.error("Error: " + e.getMessage());
            }
            
            return true;
            
        } else {
            return false;
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();
            
            return content.toString();
            
            } catch (IOException e) {
          //   callbackContext.error('Error: ' + e.getMessage());
            } finally {
            if (reader != null) {
                try {
                    reader.close();
                    } catch (IOException e) {
                  //   callbackContext.error('Error: ' + e.getMessage());
                }
            }
        }
        return "";
    }
}