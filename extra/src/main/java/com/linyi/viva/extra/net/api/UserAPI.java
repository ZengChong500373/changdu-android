package com.linyi.viva.extra.net.api;

import com.api.changdu.proto.Api;
import com.linyi.viva.extra.net.API;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/1/24 0024 上午 11:27
 */
public interface UserAPI {

    @POST(API.LOGIN)
    Observable<Api.Response> login(@Body RequestBody body);

    @POST(API.SEND_CODE)
    Observable<Api.Response> sendCode(@Body RequestBody body);

    @POST(API.FAVORITE)
    Observable<Api.Response> favorite(@Body RequestBody body);

    @POST(API.USER_INFO)
    Observable<Api.Response> userInfo(@Body RequestBody body);

    @POST(API.EXP_INFO)
    Observable<Api.Response> expInfo(@Body RequestBody body);

    @POST(API.DEVICE_LOGIN)
    Observable<Api.Response> deviceLogin(@Body RequestBody body);

}
