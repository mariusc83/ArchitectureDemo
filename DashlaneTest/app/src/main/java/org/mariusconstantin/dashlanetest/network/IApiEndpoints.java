package org.mariusconstantin.dashlanetest.network;

import java.util.concurrent.Callable;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by Marius on 1/1/2016.
 */
public interface IApiEndpoints {

    @GET("u/{id}/listing.txt")
    Call<String> getWebsites(@Path("id") long id);
}
