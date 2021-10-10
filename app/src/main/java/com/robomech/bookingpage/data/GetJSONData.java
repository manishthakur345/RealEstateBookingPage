package com.robomech.bookingpage.data;

import android.os.AsyncTask;
import android.util.Log;

import com.robomech.bookingpage.picks.PickRecommendation;
import com.robomech.bookingpage.slide.SlideShow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetJSONData extends AsyncTask<String, Void, List<PickRecommendation>> implements GetRawData.OnDownloadCompleteListener {

    private static final String TAG = "GetJSONData";
    private List<PickRecommendation> mPhotoList;
    private List<SlideShow> slideList;
    private List<PickRecommendation> latestList;
    private String mBaseUrl, backgroundImagePath;
    private boolean backgroundThread = true;

    private final OnJSONDataReady context;


    public GetJSONData(OnJSONDataReady context, String baseUrl) {
        Log.d(TAG, "GetJSONData: called");
        mBaseUrl = baseUrl;
        this.context = context;
    }

    public void executeOnSameThread() {
        backgroundThread = false;
        Log.d(TAG, "executeOnSameThread: starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute();

        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected List<PickRecommendation> doInBackground(String... strings) {
        Log.d(TAG, "doInBackground");
        backgroundThread = true;
        GetRawData getRawData = new GetRawData(this);
        getRawData.executeFromSameThread(mBaseUrl);
        Log.d(TAG, "doInBackground: ends");
        return mPhotoList;
    }

    public interface OnJSONDataReady {
        void onJSONDataReady(List<PickRecommendation> data, List<SlideShow> slideList, List<PickRecommendation> latestList, String backgroundImagePath, DownloadStatus status);
    }


    @Override
    protected void onPostExecute(List<PickRecommendation> photos) {
        Log.d(TAG, "onPostExecute: starts");

        if (context != null) {
            context.onJSONDataReady(mPhotoList, slideList, latestList, backgroundImagePath, DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: ends");
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {
            mPhotoList = new ArrayList<>();
            slideList = new ArrayList<>();
            latestList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data);
                getSlidesOnDownloadComplete(jsonObject);
                getLatestOnDownloadComplete(jsonObject);
                getBackgroundOnDownloadComplete(jsonObject);
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(1).getJSONArray("items");
                Log.d(TAG, "onDownloadComplete: data is --> " + jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonPhoto = jsonArray.getJSONObject(i);
                    List<String> attributesList = new ArrayList<>();
                    String imagePath = jsonPhoto.getString("thumbnail");
                    Log.d(TAG, "onDownloadComplete: thumbnail " + imagePath);
//                    Integer price = Integer.parseInt(jsonPhoto.getString("price"));
                    String price = jsonPhoto.getString("price");
                    double priceint  = jsonPhoto.getDouble("price");
                    if (priceint >= 10000000) {
                        price = "₹ "+(priceint / 10000000) + " Cr";
                    } else if (priceint >= 100000) {
                        price = "₹ "+(priceint / 100000) + " Lac";
                    }

                    Log.d(TAG, "onDownloadComplete: price  is -- > " + priceint +"length"+Double.toString(priceint).length());
                    String description = jsonPhoto.getString("title");
                    Log.d(TAG, "onDownloadComplete: description  " + description);
                    Boolean isWishlisted = jsonPhoto.getBoolean("in_wishlist");
                    Boolean isVerified = jsonPhoto.getBoolean("is_verified");
                    Log.d(TAG, "onDownloadComplete: jsonObject " + jsonPhoto);


                    //Extracting Attributes
//                        Log.d(TAG, "onDownloadComplete: attributes " + jsonPhoto.getJSONArray("attributes"));
                        JSONArray attributes = jsonPhoto.getJSONArray("attributes");

                        for(int j = 0; j< attributes.length(); j++){
                            JSONObject attr = attributes.getJSONObject(j);
                            Log.d(TAG, "onDownloadComplete: Attributes are " + attr.getString("value") + " " + attr.getString("unit"));
                        }
                    PickRecommendation photoObject = new PickRecommendation(imagePath, price, description, isWishlisted, isVerified);
                    mPhotoList.add(photoObject);
                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: JSON Exception --> " + e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
            Log.d(TAG, "onDownloadComplete: ends");
        }
        if (!backgroundThread && context != null) {
            //Notify MainActivity that parsing is done
//            context.onJSONDataReady(mPhotoList, status);
            context.onJSONDataReady(mPhotoList, slideList, latestList, backgroundImagePath, status);
        }
    }

    private void getBackgroundOnDownloadComplete(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("background_images");
            backgroundImagePath = jsonArray.getJSONObject(0).getString("image");
            Log.d(TAG, "getBackgroundOnDownloadComplete: background is --> " + backgroundImagePath);

        } catch (JSONException e) {
            Log.e(TAG, "getBackgroundOnDownloadComplete: failed" + e.getMessage());
        }

    }

    //Get Slides --> to be called onDownloadComplete <--
    private void getSlidesOnDownloadComplete(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("items");
            Log.d(TAG, "getSlidesOnDownloadComplete: data is --> " + jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPhoto = jsonArray.getJSONObject(i);

                String imagePath = jsonPhoto.getString("image");
                Log.d(TAG, "getSlidesOnDownloadComplete: thumbnail " + imagePath);
                //Testing line below
//                String imagePath = "https://milkiyat.bangalore2.com/media/slides/app_bannera-08.jpg";
                SlideShow photoObject = new SlideShow(imagePath);
                slideList.add(photoObject);
            }

        } catch (JSONException e) {
            Log.e(TAG, "getSlidesOnDownloadComplete: failed" + e.getMessage());
        }

    }

    //Get Latest --> to be called onDownloadComplete <--
    private void getLatestOnDownloadComplete(JSONObject jsonObject) {

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data").getJSONObject(2).getJSONArray("items");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPhoto = jsonArray.getJSONObject(i);
                List<String> attributesList = new ArrayList<>();
                String imagePath = jsonPhoto.getString("thumbnail");
                Log.d(TAG, "getLatestOnDownloadComplete: thumbnail " + imagePath);
//                    Integer price = Integer.parseInt(jsonPhoto.getString("price"));
                String price = jsonPhoto.getString("price");
                double priceint  = jsonPhoto.getDouble("price");
                if (priceint >= 10000000) {
                    price = "₹ "+(priceint / 10000000) + " Cr";
                } else if (priceint >= 100000) {
                    price = "₹ "+(priceint / 100000) + " Lac";
                }

                Log.d(TAG, "getLatestOnDownloadComplete: price  is -- > " + priceint +"length"+Double.toString(priceint).length());
                String description = jsonPhoto.getString("title");
                Log.d(TAG, "getLatestOnDownloadComplete: description  " + description);
                Boolean isWishlisted = jsonPhoto.getBoolean("in_wishlist");
                Boolean isVerified = jsonPhoto.getBoolean("is_verified");
                Log.d(TAG, "getLatestOnDownloadComplete: jsonObject " + jsonPhoto);


                //Extracting Attributes
//                        Log.d(TAG, "onDownloadComplete: attributes " + jsonPhoto.getJSONArray("attributes"));
                JSONArray attributes = jsonPhoto.getJSONArray("attributes");

                for(int j = 0; j< attributes.length(); j++){
                    JSONObject attr = attributes.getJSONObject(j);
                    Log.d(TAG, "getLatestOnDownloadComplete: Attributes are " + attr.getString("value") + " " + attr.getString("unit"));
                }
                PickRecommendation photoObject = new PickRecommendation(imagePath, price, description, isWishlisted, isVerified);
                latestList.add(photoObject);
            }

        } catch (JSONException e) {
            Log.e(TAG, "getLatestOnDownloadComplete: failed" + e.getMessage());
        }

    }
}
