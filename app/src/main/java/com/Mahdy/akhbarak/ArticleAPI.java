package com.Mahdy.akhbarak;

import com.Mahdy.akhbarak.Fragment.ArticlesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleAPI {
   @GET("articles")
   Call<ArticlesResponse>getArticle(@Query("isEnglish") Boolean isEnglish);

   @GET("articles/{articleId}")
   Call<Article>getDetails(@Path("articleId") Long id);

   @GET("articles")
   Call<ArticlesResponse>getCategoryArticles(@Query("categoryId") Long id,
                                          @Query("isEnglish") Boolean isEnglish);

   @GET("categories")
   Call<List<Category>>getCategories();


}
