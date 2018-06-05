package com.sukinsan.shot.retrofit;

import com.sukinsan.shot.entity.ShotEntity;
import com.sukinsan.shot.retrofit.reponse.RedmineUserResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface KoShotServerService {

    @GET("/ping")
    Call<Void> ping();

    @Multipart
    @POST("/api/shot")
    Call<ShotEntity> publish(@Header("Authorization") String auth, @Part MultipartBody.Part filePart);

    @GET("https://redmine.ekreative.com/users/current.json")
    Call<RedmineUserResponse> login(@Header("Authorization") String auth);

    @GET("https://redmine.ekreative.com/users/current.json")
    Call<RedmineUserResponse> loginCheck(@Header("X-Redmine-API-Key") String apiKey);

}
