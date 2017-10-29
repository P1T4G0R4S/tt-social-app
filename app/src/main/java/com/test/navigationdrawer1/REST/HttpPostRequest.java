package com.test.navigationdrawer1.REST;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by osvaldo on 10/25/17.
 */

public class HttpPostRequest extends AsyncTask<Object, Void, String> {

    IHttpResponseMethods responseMethods;
    Activity parent;

    public HttpPostRequest(Activity activity) {
        parent = activity;
    }

    protected void onPreExecute(){}

    protected String doInBackground(Object... args) {

        try {
            if (args[0] == null) {
                throw new Exception("Es necesario especificar una URL destino");
            }

            if (args[0] == "") {
                throw new Exception("Es necesario especificar una URL destino");
            }

            /*if (args[1] == null) {
                throw new Exception("Es necesario especificar una interfaz de tipo IHttpResponseMethods");
            }*/

            //URL url = new URL("https://httpbin.org/post"); // here is your URL path
            URL url = new URL((String)args[0]); // here is your URL path

            responseMethods = (IHttpResponseMethods) args[1];

            //JSONObject postDataParams = new JSONObject();
            //postDataParams.put("name", "abc");
            //postDataParams.put("email", "abc@gmail.com");
            JSONObject postDataParams = args[2] != null ? (JSONObject) args[2] : new JSONObject();
            Log.e("params",postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            final int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getInputStream()));

                final StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                if (responseMethods != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseMethods.response(sb.toString());
                        }
                    });
                }
                return sb.toString();

            }
            else {
                BufferedReader in=new BufferedReader(new
                        InputStreamReader(
                        conn.getErrorStream()));

                final StringBuffer sb = new StringBuffer("");
                String line="";

                while((line = in.readLine()) != null) {
                    sb.append(line);
                }

                in.close();

                if (responseMethods != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseMethods.error(String.valueOf(responseCode));
                        }
                    });
                }
                return new String("false : "+responseCode + " " + sb.toString());
            }
        }
        catch(final Exception e){
            Log.e("Error", e.getMessage());
            if (responseMethods != null) {
                if (responseMethods != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseMethods.error(e.getMessage());
                        }
                    });
                }
            }
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("POST_RESPONSE", result);

        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }

    String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
