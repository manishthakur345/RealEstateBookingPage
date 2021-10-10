package com.robomech.bookingpage.slide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robomech.bookingpage.R;
import com.robomech.bookingpage.picks.PickRecommendation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.SlideShowViewHolder> {

    Context context;
    List<SlideShow> mSlideShowList;

    public SlideShowAdapter(Context context, List<SlideShow> slideShowList) {
        this.context = context;
        mSlideShowList = slideShowList;
    }

    @NonNull
    @Override
    public SlideShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slide_item, parent, false);
        return new SlideShowViewHolder(view);
    }

    public void loadNewData(List<SlideShow> newPhotos){
        mSlideShowList = newPhotos;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SlideShowViewHolder holder, int position) {
//        holder.bannerUrl.setImageResource(mSlideShowList.get(position).getBannerUrl());
        Picasso.get()
                .load(mSlideShowList.get(position).getBannerUrl())
                .into(holder.bannerUrl);
    }

    @Override
    public int getItemCount() {
        return mSlideShowList.size();
    }


    public static final class SlideShowViewHolder extends RecyclerView.ViewHolder{
        ImageView bannerUrl;
        public SlideShowViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerUrl = itemView.findViewById(R.id.slideBannerImage);
        }
    }
}
