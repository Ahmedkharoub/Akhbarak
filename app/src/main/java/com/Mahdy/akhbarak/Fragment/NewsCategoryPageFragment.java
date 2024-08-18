package com.Mahdy.akhbarak.Fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.Mahdy.akhbarak.Article;
import com.Mahdy.akhbarak.Adapter.NewsArticleAdapter;
import com.Mahdy.akhbarak.ArticleAPI;
import com.Mahdy.akhbarak.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NewsCategoryPageFragment extends Fragment {

    private static final String CATEGORY_ID = "CATEGORY_ID";

    private List<Article> articleList;
    private Long categoryId;

    private TextView latestNewsLabel;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorMessage;
    Toolbar toolbar;

    public NewsCategoryPageFragment() {
    }

    public static NewsCategoryPageFragment newInstance(Long categoryId) {
        NewsCategoryPageFragment fragment = new NewsCategoryPageFragment();
        Bundle args = new Bundle();
        args.putLong(CATEGORY_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getLong(CATEGORY_ID);
            System.out.println("Page for category id " + categoryId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_category_page, container, false);

        latestNewsLabel = view.findViewById(R.id.latest_new);
        recyclerView = view.findViewById(R.id.recycler_view);


        progressBar = view.findViewById(R.id.news_category_page_progressbar);
        errorMessage = view.findViewById(R.id.errorMessage_id);
        articleList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.mohkamfer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArticleAPI articleAPI = retrofit.create(ArticleAPI.class);
        Locale current = NewsCategoryPageFragment.this.getResources().getConfiguration().locale;
        Call<ArticlesResponse> call = articleAPI.getCategoryArticles(categoryId, !current.getLanguage().equals("ar"));

        setLoading(true);
        call.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (!response.isSuccessful()) {
//                   Toast.makeText(NewsCategoryPageFragment.this.getActivity(), "code " + response.code(), Toast.LENGTH_SHORT).show();
                    latestNewsLabel.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    errorMessage.setVisibility(View.VISIBLE);

                    return;
                }
                List<Article> articles = (List<Article>) response.body().getArticles();
                for (Article article : articles) {
                    articleList.add(article);


                }

                if (articleList.size() > 0) {
                    setLoading(false);
                    putDataIntoRecyclerView(articleList);
                    System.out.println("Setting ViewPager adapter");
                } else {
                    errorMessage.setText("No Articles Found ");
                    errorMessage.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                Log.e("NewsCategoryPageFragment", "/articles onFailure: ", t);
                setLoading(false);
            }

        });


        return view;
    }

    private void setLoading(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            latestNewsLabel.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            latestNewsLabel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.INVISIBLE);

        }

    }

    private void putDataIntoRecyclerView(List<Article> articleList) {
        NewsArticleAdapter newsArticleAdapter = new NewsArticleAdapter(getActivity(), articleList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(newsArticleAdapter);
    }
}