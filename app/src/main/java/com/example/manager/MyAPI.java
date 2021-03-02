package com.example.manager;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAPI {

    @POST("/liveData/")
    Call<PostLiveData> post_posts(@Body PostLiveData post);

    @POST("/covidRecord/")
    Call<PostLiveData> post_covid(@Body PostLiveData postCovid);

    //    @PATCH("/surveys/{pk}/")
    //    Call<PostItem> patch_posts(@Path("pk") int pk, @Body PostItem post);

    @DELETE("/liveData/{pk}/")
    Call<PostLiveData> delete_posts(@Path("pk") String pk);

//    @PATCH("/surveys/{pk}/")
//    Call<Post> putData(@Path("pk") String pk , @Body Post post);
//
//    @GET("/surveys/")
//    Call<List<PostItem>> get_posts();
//
//    @GET("/graph_Surveys/")
//    Call<List<PostItem>> get_graph_posts();
//
//    @GET("/surveys/{pk}/")
//    Call<PostItem> get_post_pk(@Path("pk") int pk);
//
//    @GET("/excelDB/{pk}")
//    Call<ExcelDB> get_excelDb(@Path("pk") String pk);
}
