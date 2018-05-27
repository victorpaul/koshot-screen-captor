package com.sukinsan.shot.retrofit;

import com.sukinsan.shot.retrofit.reponse.PublishResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface KoShotServerService {

    @Multipart
    @POST("/api/publish")
    Call<PublishResponse> publish(@Part MultipartBody.Part filePart);
}
