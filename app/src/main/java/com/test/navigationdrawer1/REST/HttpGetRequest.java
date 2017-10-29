package com.test.navigationdrawer1.REST;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by osvaldo on 10/25/17.
 */

public class HttpGetRequest extends AsyncTask<Object, Void ,String> {
    String server_response;
    IHttpResponseMethods responseMethods;
    Activity parent;

    public  HttpGetRequest(Activity activity) {
        parent = activity;
    }

    @Override
    protected String doInBackground(Object... args) {

        URL url;
        HttpURLConnection urlConnection = null;

        try {
            if (args[0] == null) {
                throw new Exception("Es necesario especificar una URL destino");
            }

            if (args[0] == "") {
                throw new Exception("Es necesario especificar una URL destino");
            }

            url = new URL((String)args[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            responseMethods = (IHttpResponseMethods)args[1];

            final int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                server_response = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", server_response);

                if (responseMethods != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseMethods.response(server_response);
                        }
                    });
                }
                return server_response;
            }
            else {
                if (responseMethods != null) {
                    parent.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            responseMethods.error(String.valueOf(responseCode));
                        }
                    });
                }
                return String.valueOf(responseCode);
            }
        } catch (final Exception e) {
            e.printStackTrace();
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

            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("Response", "" + s);


    }

    // Converting InputStream to String

    String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
