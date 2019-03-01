package org.linyi.base.http;






import org.linyi.base.VivaConstant;
import org.linyi.base.VivaSdk;
import org.linyi.base.utils.UIUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/17.
 */

public class NetWork {
    private static OkHttpClient mOkHttpClient;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    private static VivaMethods videoMethods;
    public static void initOkhttp() {
        if (mOkHttpClient == null) {
            synchronized (NetWork.class) {
                if (mOkHttpClient == null) {
                    File mFile = new File(UIUtils.getContext().getCacheDir(), "cache");
                    Cache mCache = new Cache(mFile, 1024 * 1024 * 200);
                   NetworkInterceptor mNetworkInterceptor = new NetworkInterceptor();
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(mCache)
                            .addInterceptor(mNetworkInterceptor)
                            .addInterceptor(new LogInterceptor())
                            .addNetworkInterceptor(mNetworkInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }


    public static <T> T  Builder(Class<T> service)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .client(mOkHttpClient)
                .baseUrl(VivaConstant.baseUrl)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        return retrofit.create(service);
    }
}
