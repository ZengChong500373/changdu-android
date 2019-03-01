package com.linyi.reader.net.api;

import com.api.changdu.proto.Api;
import com.linyi.reader.net.API;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/1 0001 下午 5:15
 */
public interface BookAPI {

    @POST(API.DIRECTORY)
    Observable<Api.Response> directory(@Body RequestBody body);

    @POST(API.CHAPTER)
    Observable<Api.Response> chapter(@Body RequestBody body);

    @POST(API.MANAGER_BOOKSHELF)
    Observable<Api.Response> managerBookshelf(@Body RequestBody body);

    @POST(API.BOOK_DETAIL)
    Observable<Api.Response> bookDetail(@Body RequestBody body);

}
