package com.lcz.bm.util;

import com.lcz.bm.api.BMApiService;
import com.lcz.bm.entity.BaseUserEntity;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * desc: TODO
 * <p>
 * create by Arrow on 2020-11-05
 */
public class Test {
    public Test() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("MyServer.URL")
                .build();

        BMApiService bmApiService = retrofit.create(BMApiService.class);
        Call<BaseUserEntity> login = bmApiService.login(new HashMap<>());
        login.enqueue(new Callback<BaseUserEntity>() {
            @Override
            public void onResponse(Call<BaseUserEntity> call, Response<BaseUserEntity> response) {
                BaseUserEntity body = response.body();
            }

            @Override
            public void onFailure(Call<BaseUserEntity> call, Throwable t) {

            }
        });

        switch (10) {
            case 1:
                break;
            case 10:
                break;
            default:
                break;
        }
    }

}
