package com.Mahdy.akhbarak.Fragment;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Mahdy.akhbarak.Article;
import com.Mahdy.akhbarak.Category;
import com.Mahdy.akhbarak.Adapter.SliderAdapter;
import com.Mahdy.akhbarak.Adapter.NewsCategoryPagerAdapter;
import com.Mahdy.akhbarak.ArticleAPI;
import com.Mahdy.akhbarak.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment {
    private RecyclerView recyclerView;
    private ViewPager2 imageViewPager;
    WormDotsIndicator wormDotsIndicator;
    ProgressBar progressBar;
    CoordinatorLayout constraintLayout;
    ViewPager categoriesViewPager;
    TabLayout categoriesTabLayout;
    NewsCategoryPagerAdapter categoriesAdapter;
    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    Retrofit retrofit;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;


    private Handler sliderHandler = new Handler();
    private String language;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("MainFragment onCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

//        recyclerView = view.findViewById(R.id.recycler_view);
        imageViewPager = view.findViewById(R.id.viewpagerImageSlider);
        wormDotsIndicator = view.findViewById(R.id.worm_dots_indicator);
        progressBar = view.findViewById(R.id.progressBar);
        constraintLayout = view.findViewById(R.id.main_fragment_coordinator_layout);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setColorFilter(Color.parseColor("#FF000000"), PorterDuff.Mode.SRC_ATOP);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        appBarLayout = view.findViewById(R.id.main_fragment_app_bar);
        categoriesTabLayout = view.findViewById(R.id.tabs);
        categoriesViewPager =view.findViewById(R.id.main_view_pager);
        collapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        categoriesAdapter = new NewsCategoryPagerAdapter(getChildFragmentManager(),getContext());
        categoriesViewPager.setAdapter(categoriesAdapter);
        categoriesTabLayout.setupWithViewPager(categoriesViewPager);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        imageViewPager.setClipToPadding(false);
        imageViewPager.setClipChildren(false);
        imageViewPager.setOffscreenPageLimit(1);
        imageViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position) ;
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        imageViewPager.setPageTransformer(compositePageTransformer);
        imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });

                retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.mohkamfer.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.white, R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

              refresh();

              swipeRefreshLayout.setRefreshing(false);
            }
        });
        Locale current = MainFragment.this.getResources().getConfiguration().locale;
        language = current.getLanguage();
        refresh();
        return view;
    }

    private void refresh(){
        getArticleList();
        getCategoriesAdapter();

    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int currentItem = imageViewPager.getCurrentItem();
            int childCount = imageViewPager.getChildCount();

            if (currentItem==childCount){
                imageViewPager.setCurrentItem(0);
            }else {
                imageViewPager.setCurrentItem(currentItem + 1);
            }

        }
    };
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        //The Refresh must be only active when the offset is zero :
        swipeRefreshLayout.setEnabled(i == 0);
    }

    public void getArticleList() {
        progressBar.setVisibility(View.VISIBLE);
        if (progressBar.isEnabled()) {
            constraintLayout.setVisibility(View.GONE);
        }
        ArticleAPI articleAPI = retrofit.create(ArticleAPI.class);

        Locale current = MainFragment.this.getResources().getConfiguration().locale;
        Call<ArticlesResponse>call = articleAPI.getArticle(!current.getLanguage().equals("ar"));
        call.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainFragment.this.getActivity(), "code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Article> articles = response.body().getArticles();

                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
//                      putDataIntoRecyclerView(articleList);
                System.out.println("Setting ViewPager adapter");
                imageViewPager.setAdapter(new SliderAdapter(MainFragment.this.getActivity(), articles));
                wormDotsIndicator.setViewPager2(imageViewPager);
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
            }

        });

    }

    public void getCategoriesAdapter(){

        ArticleAPI aPi = retrofit.create(ArticleAPI.class);
        Call<List<Category>> categories = aPi.getCategories();
        categories.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainFragment.this.getActivity(), "code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Category> categories = response.body();
                for (int i = 0 ; i< categories.size(); i++ )
                    System.out.println("==============="+categories.get(i).getName() + " " + categories.get(i).getId());

                List<Category> filteredCategories = new ArrayList<>();

                for (Category category : categories) {
                    if (language.equals("en") && !(category.getId().equals(1L) || category.getId().equals(11L) || category.getId().equals(31L))) {
                        filteredCategories.add(category);
                    } else if (language.equals("ar") && !(category.getId().equals(31L))) {
                        filteredCategories.add(category);
                    }
                }
                categoriesAdapter.setData(filteredCategories);
            }


            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("MainFragment onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("MainFragment onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
//        appBarLayout.removeOnOffsetChangedListener();
        System.out.println("MainFragment onPause");

    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,3000);
//        appBarLayout.addOnOffsetChangedListener();

    }

    private void putDataIntoRecyclerView(List<Article> articleList) {
//        Adapter adapter = new Adapter(getActivity(), articleList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.setting_menu_xml, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.setting_menu) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
