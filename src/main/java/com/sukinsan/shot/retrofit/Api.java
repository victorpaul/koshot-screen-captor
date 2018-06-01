package com.sukinsan.shot.retrofit;

import com.sukinsan.shot.retrofit.reponse.PublishResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class Api {

    private KoShotServerService service;

    public Api(String host) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(KoShotServerService.class);
    }

    public Call<PublishResponse> publish(File file) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse("image/*"), file));
        return service.publish(filePart);
    }

    public Call<Void> ping() {
        return service.ping();
    }
}
