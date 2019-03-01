package org.linyi.base.mvp.module;


import android.text.TextUtils;

import com.api.changdu.proto.BookApi;
import com.google.protobuf.Message;

import org.linyi.base.BaseApp;

import org.linyi.base.constant.SystemConstant;
import org.linyi.base.entity.db.VariousEntity;
import org.linyi.base.utils.help.ModuleHelp;
import org.linyi.viva.db.VariousEntityDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class VariousModule {
    public static VariousEntity getEntity(String type) {
        return getDao().queryBuilder().where(VariousEntityDao.Properties.UniqueType.eq(type)).unique();
    }
    public static VariousEntity getEntity(String type,int page) {
        return getDao().queryBuilder().where(VariousEntityDao.Properties.UniqueType.eq(type),VariousEntityDao.Properties.Page.eq(page)).unique();
    }
    public static List<VariousEntity> getSearchHistoryList(){
        return getDao().queryBuilder().where(VariousEntityDao.Properties.Describ.eq("seacrh_history")).orderDesc(VariousEntityDao.Properties.LastTime).list();
    }
    public static VariousEntityDao getDao(){
        return BaseApp.getDaoSession().getVariousEntityDao();
    }
    public static void save(String type, Message message){
        if (message==null){
            return;
        }
        VariousEntity entity=new VariousEntity(type, ModuleHelp.parseString(message));
        getDao().insertOrReplace(entity);
    }
    public static void saveSearchTag(String describ, String detailStr,Long lastTime){
        if (TextUtils.isEmpty(describ)||TextUtils.isEmpty(detailStr)){
            return;
        }
        VariousEntity entity=new VariousEntity(describ, detailStr,lastTime);
        List<VariousEntity> list=   getDao().queryBuilder().where(VariousEntityDao.Properties.Describ.eq("seacrh_history"),VariousEntityDao.Properties.DetailStr.eq(detailStr)).orderDesc(VariousEntityDao.Properties.LastTime).list();
        if (list==null||list.size()==0){
            getDao().insertOrReplace(entity);
        }
    }
    public static void cleanSearchTag(){
        List<VariousEntity>  variousEntityList= VariousModule.getSearchHistoryList();
        getDao().deleteInTx(variousEntityList);
    }
    public static void save(String type,int page, Message message){
        if (message==null){
            return;
        }
        VariousEntity entity=new VariousEntity(type, page,ModuleHelp.parseString(message));
        getDao().insertOrReplace(entity);
    }
    public static Observable<BookApi.AckDomainRecommend> getMainStackCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckDomainRecommend>() {
                    @Override
                    public BookApi.AckDomainRecommend apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckDomainRecommend ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckDomainRecommend.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckBookDetail> getBookDetailCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckBookDetail>() {
                    @Override
                    public BookApi.AckBookDetail apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckBookDetail ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckBookDetail.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckBookShelf> getBookDetailStatusCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckBookShelf>() {
                    @Override
                    public BookApi.AckBookShelf apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckBookShelf ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckBookShelf.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckCategoryList> getClassifyCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckCategoryList>() {
                    @Override
                    public BookApi.AckCategoryList apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckCategoryList ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckCategoryList.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckRankOption> getClassifyTitleCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckRankOption>() {
                    @Override
                    public BookApi.AckRankOption apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckRankOption ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckRankOption.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckBookList> getClassifyListCache(String str,final int page){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckBookList>() {
                    @Override
                    public BookApi.AckBookList apply(String s) throws Exception {
                        VariousEntity data=getEntity(s,page);
                        BookApi.AckBookList ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckBookList.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckIndexRankingList> getRankInitCache(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckIndexRankingList>() {
                    @Override
                    public BookApi.AckIndexRankingList apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckIndexRankingList ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckIndexRankingList.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BookApi.AckIndexRankingDetail> getRankInitDetail(String str,final int page){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckIndexRankingDetail>() {
                    @Override
                    public BookApi.AckIndexRankingDetail apply(String s) throws Exception {
                        VariousEntity data=getEntity(s,page);
                        BookApi.AckIndexRankingDetail ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckIndexRankingDetail.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<BookApi.AckHotKeyWord> getSearchInit(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckHotKeyWord>() {
                    @Override
                    public BookApi.AckHotKeyWord apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckHotKeyWord ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckHotKeyWord.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckSearch> getSearchList(String str,final int page){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckSearch>() {
                    @Override
                    public BookApi.AckSearch apply(String s) throws Exception {
                        VariousEntity data=getEntity(s,page);
                        BookApi.AckSearch ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckSearch.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<BookApi.AckBookThemeIntro> getThemeIntro(String str,final int page){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckBookThemeIntro>() {
                    @Override
                    public BookApi.AckBookThemeIntro apply(String s) throws Exception {
                        VariousEntity data=getEntity(s,page);
                        BookApi.AckBookThemeIntro ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckBookThemeIntro.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
    public static Observable<BookApi.AckBookThemeDetail> getThemeDetail(String str){
        return Observable.just(str)
                .observeOn(Schedulers.newThread())
                .map(new Function<String, BookApi.AckBookThemeDetail>() {
                    @Override
                    public BookApi.AckBookThemeDetail apply(String s) throws Exception {
                        VariousEntity data=getEntity(s);
                        BookApi.AckBookThemeDetail ack=null;
                        if (data != null && !TextUtils.isEmpty(data.getDetailStr())){
                            ack= BookApi.AckBookThemeDetail.parseFrom(data.getDetailStr().getBytes(SystemConstant.CHARSET_NAME));
                        }
                        return ack;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}


