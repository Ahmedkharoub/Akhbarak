package com.Mahdy.akhbarak.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Mahdy.akhbarak.Article;
import com.Mahdy.akhbarak.ArticleAPI;
import com.Mahdy.akhbarak.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleDetailsFragment extends Fragment {

    private static final String ID_PARAM = "id_param";

    private long articleId;
    private ConstraintLayout articleDetails_view ;
    private ImageView articleDetails_img;
    private TextView articleDetails_title;
    private WebView articleDetails_body;
    private TextView articleDetails_time;
    ProgressBar progressBarDetails;
    CoordinatorLayout coordinatorLayout;
    WebSettings webSettings;
    Toolbar toolbar2;

    public static ArticleDetailsFragment newInstance(long articleId) {
        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, articleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleId = getArguments().getLong(ID_PARAM);
            System.out.printf("Article ID: %d%n", articleId);
            System.out.println(articleId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articledetails, container, false);
        progressBarDetails = view.findViewById(R.id.progressBar_details);
        coordinatorLayout = view.findViewById(R.id.articledetails_parent);
        articleDetails_img = view.findViewById(R.id.articledetails_img);
        articleDetails_title = view.findViewById(R.id.articledetails_title);
        articleDetails_body = view.findViewById(R.id.articledetails_body);

        articleDetails_body.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        toolbar2 = view.findViewById(R.id.details_id);
        toolbar2.getOverflowIcon().setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);


        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar2);

        webSettings = articleDetails_body.getSettings();
        webSettings.setJavaScriptEnabled(true);

        articleDetails_time = view.findViewById(R.id.articledetails_time);
        Toolbar toolbar = view.findViewById(R.id.details_id);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AppCompatActivity)getActivity()).onBackPressed();
            }
        });
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);
        setHasOptionsMenu(true);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.mohkamfer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        progressBarDetails.setVisibility(View.VISIBLE);
        if (progressBarDetails.isEnabled()){
            coordinatorLayout.setVisibility(View.GONE);
        }
        ArticleAPI articleAPI = retrofit.create(ArticleAPI.class);
        Call<Article>call=articleAPI.getDetails(articleId);

        call.enqueue(new Callback<Article>() {

            @Override
            public void onResponse(Call<Article> call, Response<Article> response) {
                progressBarDetails.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                Article article = response.body();

                if (article.getEnglish()) {
                    articleDetails_title.setText(article.getTitle());
                    articleDetails_body.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + article.getBody(), "text/html", "UTF-8", null);
                } else {
                    articleDetails_title.setText(article.getTitleAr());
                    articleDetails_body.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}</style>" + article.getBodyAr(), "text/html", "UTF-8", null);
                }
                String timeAgo = calculateTimeAgo(article.getUpdatedAt());
                articleDetails_time.setText(timeAgo);
//                    articleDetails_time.setText(article.getUpdatedAt());
                if (!article.getImageUrl().isEmpty()) {
                    Picasso.get().load(article.getImageUrl()).resize(600,200).centerCrop().into(articleDetails_img);
                }
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
            public void onFailure(Call<Article> call, Throwable t) {
                progressBarDetails.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
            }
        });
        return view;

    }

}