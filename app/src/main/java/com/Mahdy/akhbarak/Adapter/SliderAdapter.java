package com.Mahdy.akhbarak.Adapter;

import static com.Mahdy.akhbarak.MainActivity.MAIN_STACK;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.Mahdy.akhbarak.Article;
import com.Mahdy.akhbarak.Fragment.ArticleDetailsFragment;
import com.Mahdy.akhbarak.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<Article>sliderItems;
    private  ViewPager2 viewPager2;
    private Context context;

    public SliderAdapter(Context context,List<Article> sliderItems) {
        System.out.println("Adapter constructor");
        this.sliderItems = sliderItems;
        this.context=context;

    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println(" Creating row");
        View view = (LayoutInflater.from(context).inflate(R.layout.slider_item_container,parent,false));

        ConstraintLayout constraintLayout = view.findViewById(R.id.sliderImage_parent);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) view.getTag();
                ((AppCompatActivity) context) .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, ArticleDetailsFragment.newInstance(id))
                        .addToBackStack(MAIN_STACK)
                        .commitAllowingStateLoss();
            }
        });

        Collections.reverse(sliderItems);
        SliderViewHolder sliderViewHolder = new SliderViewHolder(view);
        return sliderViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        System.out.println("Binding row");
        holder.setImageView(sliderItems.get(position));
        if (sliderItems.get(position).getEnglish()) {
            holder.slider_img_title.setText(sliderItems.get(position).getTitle());
        } else {
            holder.slider_img_title.setText(sliderItems.get(position).getTitleAr());
        }
       // holder.slider_img_time.setText(sliderItems.get(position).getUpdatedAt());

        String timeAgo = calculateTimeAgo(sliderItems.get(position).getUpdatedAt());
        holder.slider_img_time.setText(timeAgo);


        ConstraintLayout constraintLayout= holder.itemView.findViewById(R.id.sliderImage_parent);
        constraintLayout.setTag(sliderItems.get(position).getId());
        Collections.reverse(sliderItems);
//       if (position == sliderItems.size() - 2 ){
//           viewPager2.post(runnable);
//       }
    }

    @Override
    public int getItemCount() {

            return 10;
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
         RoundedImageView imageView;
         TextView slider_img_title;
         TextView slider_img_time;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            System.out.println("Creating view holder");
            imageView = itemView.findViewById(R.id.sliderImage);
            slider_img_title = itemView.findViewById(R.id.slider_img_title);
            slider_img_time = itemView.findViewById(R.id.slider_img_time);
        }
        void setImageView(Article sliderItem){

            if (!sliderItem.getImageUrl().isEmpty())
            {
                Picasso.get().load(sliderItem.getImageUrl()).into(imageView);
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();

        }
    };

    private String calculateTimeAgo(String updatedAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            long time = simpleDateFormat.parse(updatedAt).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
