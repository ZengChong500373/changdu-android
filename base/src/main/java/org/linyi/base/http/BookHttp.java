package org.linyi.base.http;


import com.api.changdu.proto.Api;



import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BookHttp {
    @POST("v1/book/domainRecommend")
    Observable<Api.Response> stackRecommend(@Body RequestBody body);

    @POST("v1/book/categoryList")
    Observable<Api.Response> categoryList(@Body RequestBody body);

    @POST("v1/book/list")
    Observable<Api.Response> bookList(@Body RequestBody body);

    @POST("v1/book/rankOption")
    Observable<Api.Response> rankOption(@Body RequestBody body);

    @POST(" v1/book/detail")
    Observable<Api.Response> bookDetail(@Body RequestBody body);

    @POST("v1/book/indexRankingList")
    Observable<Api.Response> rankList(@Body RequestBody body);

    @POST("v1/book/indexRankingDetail")
    Observable<Api.Response> rankingDetail(@Body RequestBody body);

    @POST("v1/book/addOrRemoveBookShelf")
    Observable<Api.Response> addBookShelf(@Body RequestBody body);

    @POST("v1/book/myBookShelf")
    Observable<Api.Response> myBookShelf(@Body RequestBody body);

    @POST("v1/book/topBookShelf")
    Observable<Api.Response> topBookShelf(@Body RequestBody body);

    @POST("v1/book/chapterIndex")
    Observable<Api.Response> chapterIndex(@Body RequestBody body);

    @POST("v1/book/hotKeyWord")
    Observable<Api.Response> searchInit(@Body RequestBody body);

    @POST("v1/book/search")
    Observable<Api.Response> searchList(@Body RequestBody body);

    @POST("v1/book/themeIntro")
    Observable<Api.Response> themeIntro(@Body RequestBody body);

    @POST("v1/book/themeDetail")
    Observable<Api.Response> themeDetail(@Body RequestBody body);

}
