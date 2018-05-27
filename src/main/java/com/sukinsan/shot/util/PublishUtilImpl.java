package com.sukinsan.shot.util;

import com.sukinsan.shot.retrofit.Api;
import com.sukinsan.shot.retrofit.reponse.PublishResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;

public class PublishUtilImpl implements PubishUtil {

    private Api api;

    public PublishUtilImpl(Api api) {
        this.api = api;
    }

    @Override
    public void publish(File file, OnPubish onPubish) {
        api.publish(file).enqueue(new Callback<PublishResponse>() {
            @Override
            public void onResponse(Call<PublishResponse> call, Response<PublishResponse> response) {
                if (response.isSuccessful()) {
                    onPubish.success(response.body().url);
                } else {
                    try {
                        onPubish.fail(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                        onPubish.fail("something went wrong");
                    }
                }
            }

            @Override
            public void onFailure(Call<PublishResponse> call, Throwable t) {
                onPubish.fail(t.getMessage());
            }
        });
    }

}