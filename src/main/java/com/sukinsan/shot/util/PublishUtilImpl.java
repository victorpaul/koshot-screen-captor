package com.sukinsan.shot.util;

import com.sukinsan.shot.entity.ShotEntity;
import com.sukinsan.shot.retrofit.Api;
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
    public void publish(String auth, File file, OnPubish onPubish) {
        api.publish(auth, file).enqueue(new Callback<ShotEntity>() {
            @Override
            public void onResponse(Call<ShotEntity> call, Response<ShotEntity> response) {
                if (response.isSuccessful()) {
                    onPubish.success(response.body().getPublicUrl());
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
            public void onFailure(Call<ShotEntity> call, Throwable t) {
                onPubish.fail(t.getMessage());
            }
        });
    }

}