package com.robomech.bookingpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.robomech.bookingpage.data.DownloadStatus;
import com.robomech.bookingpage.data.GetJSONData;
import com.robomech.bookingpage.latest.LatestAdapter;
import com.robomech.bookingpage.picks.PickAdapter;
import com.robomech.bookingpage.picks.PickRecommendation;
import com.robomech.bookingpage.slide.SlideShow;
import com.robomech.bookingpage.slide.SlideShowAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetJSONData.OnJSONDataReady, View.OnClickListener{

    private static final String TAG = "MainActivity";
    RecyclerView sliderBanner, sliderPick, sliderLatest;
    TextView searchButton;
    Button postProperty, findProperty;
    ImageView home, explore, notification, save;
    private SlideShowAdapter slideAdapter;
    private PickAdapter pickAdapter;
    private LatestAdapter latestAdapter;
    final String url = "https://milkiyat.bangalore2.com/api/home/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<SlideShow> listBanners = new ArrayList<>();

        setBannerRecycler(listBanners);

        List<PickRecommendation> listPicks = new ArrayList<>();

        setPickRecycler(listPicks);

        List<PickRecommendation> listLatest = new ArrayList<>();

        setLatestRecycler(listLatest);

        //1. a --> Binding Bottom Nav Buttons
        home = findViewById(R.id.imageHome);
        save = findViewById(R.id.imageSave);
        notification = findViewById(R.id.imageNotification);
        explore = findViewById(R.id.imageExplore);

        //1. b --> Onclick Listeners for every buttons
        home.setOnClickListener(this);
        save.setOnClickListener(this);
        notification.setOnClickListener(this);
        explore.setOnClickListener(this);

        //Implement Search Button
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        //Find and Post property buttons
        postProperty = findViewById(R.id.postProperty);
        findProperty = findViewById(R.id.findProperty);
        postProperty.setOnClickListener(this);
        findProperty.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Starts");
        super.onResume();
        GetJSONData getJSONData = new GetJSONData(this, url);
        getJSONData.execute();
        Log.d(TAG, "onResume: Ends");
    }

    private void setPickRecycler(List<PickRecommendation> listPicks){
        sliderPick = findViewById(R.id.buy_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        sliderPick.setLayoutManager(layoutManager);
        pickAdapter = new PickAdapter(this, listPicks);
        sliderPick.setAdapter(pickAdapter);
    }

    private void setLatestRecycler(List<PickRecommendation> listLatest){
        sliderLatest = findViewById(R.id.latest_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        sliderLatest.setLayoutManager(layoutManager);
        latestAdapter = new LatestAdapter(this, listLatest);
        sliderLatest.setAdapter(latestAdapter);
    }

    private void setBannerRecycler(List<SlideShow> listBanners){
        sliderBanner = findViewById(R.id.banner_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        sliderBanner.setLayoutManager(layoutManager);
        slideAdapter = new SlideShowAdapter(this, listBanners);
        sliderBanner.setAdapter(slideAdapter);
    }

    @Override
    public void onJSONDataReady(List<PickRecommendation> data, List<SlideShow> slideList, List<PickRecommendation> latestList, String backgroundImagePath, DownloadStatus status) {
        Log.d(TAG, "OnJSONDataReady: data is --> " + data);

        if(status == DownloadStatus.OK) {
            pickAdapter.loadNewData(data);
            slideAdapter.loadNewData(slideList);
            latestAdapter.loadNewData(latestList);

            Log.d(TAG, "onJSONDataReady: Latest list " + latestList);
        }else{
            Log.e(TAG, "onJSONDataReady: failed with status " + status);
        }

        Log.d(TAG, "onJSONDataReady: ends");
    }

    //Listener for every button on page
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageHome:
                Toast.makeText(this, "Go to Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageSave:
                Toast.makeText(this, "Wishlisted Items", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageExplore:
                Toast.makeText(this, "Explore", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageNotification:
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.searchButton:
                EditText searchQuery = findViewById(R.id.query);
                Toast.makeText(this, "Search : "+searchQuery.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.findProperty:
                Toast.makeText(this, "Find Property", Toast.LENGTH_SHORT).show();
                break;
            case R.id.postProperty:
                Toast.makeText(this, "Post Property", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}