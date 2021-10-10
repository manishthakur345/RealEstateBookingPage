package com.robomech.bookingpage.data;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

;

class GetRawData extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetRawData";
    private DownloadStatus mDownloadStatus;
    OnDownloadCompleteListener mContext;

    public GetRawData(OnDownloadCompleteListener context) {
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            mDownloadStatus = DownloadStatus.PROCESSING;
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while (null != (line = reader.readLine())){
                result.append(line).append("\n");
            }

            mDownloadStatus = DownloadStatus.OK;
            return result.toString();

        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: invalid url" + e.getMessage());
        }catch (IOException e) {
            Log.e(TAG, "doInBackground: IOException" + e.getMessage());
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception" + e.getMessage());
        } finally {
            if(connection != null){
                connection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: error closing reader");
                }
            }
        }
        return null;
    }

    public void executeFromSameThread(String uri) {

        if(mContext != null){
            String result = doInBackground(uri);
            mContext.onDownloadComplete(result, mDownloadStatus);
        }
    }

    @Override
    protected void onPostExecute(String s) {
        mContext.onDownloadComplete(s, mDownloadStatus);
    }



    interface OnDownloadCompleteListener{
        void onDownloadComplete(String data, DownloadStatus status);
    }
}
