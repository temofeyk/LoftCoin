package com.temofey.loftcoin.data.api;

import com.temofey.loftcoin.data.api.model.RateResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import io.reactivex.Observable;

public interface Api {

    @GET("ticker")
    Observable<RateResponse> ticker(@Query("structure") String structure, @Query("convert") String convert);
}