package com.Mahdy.akhbarak.Adapter;

import static com.Mahdy.akhbarak.MainActivity.MAIN_STACK;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.Mahdy.akhbarak.Article;
import com.Mahdy.akhbarak.Fragment.ArticleDetailsFragment;
import com.Mahdy.akhbarak.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NewsArticleAdapter extends RecyclerView.Adapter<NewsArticleAdapter.ViewHolder> {

    private Context context;
    private List<Article>articleList;

    public NewsArticleAdapter(Context context, List<Article> articleList) {
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.news_article_row,parent,false);

        MaterialCardView materialCard = view.findViewById(R.id.news_article_card_content);
        materialCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = (long) view.getTag();
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragment_container, ArticleDetailsFragment.newInstance(id))
                        .addToBackStack(MAIN_STACK)
                        .commitAllowingStateLoss();
            }
        });


        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Locale current = context.getResources().getConfiguration().locale;
        holder.article_title.setText(articleList.get(position).getTitle());
       // holder.article_body.setText(articleList.get(position).getBody());
//        holder.article_time.setText(articleList.get(position).getUpdatedAt());
          String timeAgo = calculateTimeAgo(articleList.get(position).getUpdatedAt());
          holder.article_time.setText(timeAgo);

        if (!articleList.get(position).getImageUrl().isEmpty()){
            Picasso.get().load(articleList.get(position).getImageUrl()).into(holder.news_img_card_image);

        }
       if (current.getLanguage().equals("ar")){
           holder.article_title.setText(articleList.get(position).getTitleAr());
       }else {
           holder.article_title.setText(articleList.get(position).getTitle());
       }
        System.out.println(current.getLanguage());
        MaterialCardView materialCardView = holder.itemView.findViewById(R.id.news_article_card_content);
        materialCardView.setTag(articleList.get(position).getId());
    }

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


    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView article_title;
        TextView article_body;
        TextView article_time;
        MaterialCardView news_img_card;
        ImageView news_img_card_image;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            article_title = itemView.findViewById(R.id.news_Article_title);
            //article_body = itemView.findViewById(R.id.news_Article_body);
            article_time = itemView.findViewById(R.id.news_Article_time);
            news_img_card = itemView.findViewById(R.id.news_img_card);
            news_img_card_image = itemView.findViewById(R.id.news_img_card_image);
        }
    }
}
