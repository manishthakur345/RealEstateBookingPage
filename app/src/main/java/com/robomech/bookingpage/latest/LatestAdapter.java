package com.robomech.bookingpage.latest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robomech.bookingpage.R;
import com.robomech.bookingpage.picks.PickRecommendation;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LatestAdapter extends RecyclerView.Adapter<LatestAdapter.PickItemViewHolder> {

    Context mContext;
    List<PickRecommendation> pickList;

    public LatestAdapter(Context context, List<PickRecommendation> pickList) {
        mContext = context;
        this.pickList = pickList;
    }

    public void loadNewData(List<PickRecommendation> newPhotos){
        pickList = newPhotos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PickItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pick_item, parent, false);
        return new PickItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickItemViewHolder holder, int position) {
//        holder.pickImage.setImageResource(pickList.get(position).getImagePath());
        PickRecommendation pickItem = pickList.get(position);

        Picasso.get()
                .load(pickItem.getImagePath())
                .into(holder.pickImage);
        holder.pickPrice.setText(pickItem.getPrice());
        holder.title.setText(pickItem.getDescription());

//        holder.pickPrice.setText("1000");
    }


    @Override
    public int getItemCount() {
        return pickList.size();
    }

    public static final class PickItemViewHolder extends RecyclerView.ViewHolder{
        ImageView pickImage;
        TextView pickPrice;
        TextView title;
        public PickItemViewHolder(@NonNull View itemView) {
            super(itemView);
            pickImage = itemView.findViewById(R.id.pickImage);
            pickPrice = itemView.findViewById(R.id.pickPrice);
            title = itemView.findViewById(R.id.addressTextView);
        }
    }
}
