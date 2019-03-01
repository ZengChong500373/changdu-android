package com.linyi.reader.manager;


import android.text.TextUtils;

import com.api.changdu.proto.BookApi;

import org.linyi.base.constant.ServerConstant;
import org.linyi.base.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class ChapterManager {

    //章节名
    private String chapterName;
    //章节字符数
    private int chapterLen;
    //章节内容
    private String content;
    //书籍实体类
    private BookApi.AckChapter.Data chapterEntity;
    //整章的指针位置
    private int position;
    //章节序号id(0 开始递增)
    private int chapterSort;
    //记录该章每页开始的位置
    private List<Integer> mPageBeginPosList;
    //对章节分页是否成功
    private boolean isInitChapterSuccess;

    public ChapterManager(BookApi.AckChapter.Data chapterEntity){
        mPageBeginPosList = new ArrayList<>();
        setEntity(chapterEntity);
    }

    public void setEntity(BookApi.AckChapter.Data chapterEntity) {
        if (chapterEntity != null) {
            this.chapterEntity = chapterEntity;
            BookApi.ChapterMsg chapterMsg = chapterEntity.getChapterMsg();
            if (chapterMsg != null) {
                chapterName = chapterMsg.getChapterName();
            }
            if (getPayType() == ServerConstant.PayType.FREE) {
                content = chapterEntity.getContent();
                chapterLen = content == null ? 0 : content.length();
            }
            chapterSort = chapterEntity.getChapterSortIndex();
            LogUtil.d("ChapterManager" , "chapterSort = " + chapterSort , " chapterLen = " + chapterLen);
        } else {
            chapterSort = NovelManager.INVALID_CHAPTER_SORT;
        }
    }

    public int getChapterSort() {
        return chapterSort;
    }

    /**
     * 获取position(整本书的指针位置) 处的字符
     */
    public int current(){
        final int position = this.position;
        if(content == null || position < 0 || position >= chapterLen)
            return -1;
        return content.charAt(position);
    }

    /**
     * 返回position 下一个字符
     * @param back 是否改变指针位置 false : 会修改position的值 ; true不会
     */
    public int next(boolean back){
        position++;
        if (position > chapterLen){
            position = chapterLen;
            return -1;
        }
        int result = current();
        if (back) {
            position--;
        }
        return result;
    }

    /**
     * 返回position 上一个字符
     * @param back 是否改变指针位置 false : 会修改position的值 ; true不会
     */
    public int pre(boolean back){
        position--;
        if (position < 0){
            position = 0;
            return -1;
        }
        int result = current();
        if (back) {
            position++;
        }
        return result;
    }

    /**
     * 获取当前位置(position)到段首的字符
     */
    public char[] preLine(){
        if (position <= 0){
            return null;
        }
        StringBuilder line = new StringBuilder();
        while (position >= 0){
            int word = pre(false);
            if (word == -1){
                break;
            }
            if (word == '\n'){
                pre(false);
                break;
            }
            line.append((char) word);
        }
        line.reverse();
        return line.toString().toCharArray();
    }

    /**
     * 获取当前位置(position)到段尾的字符集
     */
    public char[] nextLine(){
        if (position >= chapterLen){
            return null;
        }
        StringBuilder line = new StringBuilder();
        while (position < chapterLen){
            int word = next(false);
            if (word == -1){
                break;
            }
//            char wordChar = (char) word;
            if (word == '\n'){
                next(false);
                break;
            }
            line.append((char) word);
        }
        return line.toString().toCharArray();
    }

    public String getChapterName() {
        return chapterName;
    }

    public int getChapterLen() {
        return chapterLen;
    }

    public String getContent() {
        return content;
    }

    public BookApi.AckChapter.Data getChapterEntity() {
        return chapterEntity;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @param page [0, +)
     * @return 根据页数获取该页第一个字符的指针位置
     */
    public int getPageBeginPos(int page) {
        return mPageBeginPosList != null && mPageBeginPosList.size() > page && page >= 0 ? mPageBeginPosList.get(page) : -1;
    }

    public void setPageBeginPosList(List<Integer> list) {
        if (list != null && !list.isEmpty()) {
            isInitChapterSuccess = getPayType() == ServerConstant.PayType.FREE && !isEmpty();
            LogUtil.d("initChapter, isInitChapterSuccess = " + isInitChapterSuccess);
            this.mPageBeginPosList = list;
        }
    }

    /**
     * @return 页数
     */
    public int getPageCount() {
        return mPageBeginPosList == null ? 0 : mPageBeginPosList.size();
    }

    /**
     * @param position 指针位置
     * @return 根据指针位置获取页数
     */
    public int getPageIndex(int position) {
        if(mPageBeginPosList == null || mPageBeginPosList.isEmpty())
            return -1;
        for (int i = 0; i < mPageBeginPosList.size(); i++) {
            if (mPageBeginPosList.get(i) > position) {
                return i > 0 ? i - 1 : 0;
            }
        }
        return mPageBeginPosList.size() - 1;
    }

    /**
     * 获取本章的支付类型
     */
    public int getPayType() {
        if(chapterEntity == null)
            return ServerConstant.PayType.PAY;
        BookApi.ChapterMsg chapterMsg = chapterEntity.getChapterMsg();
        if(chapterMsg == null)
            return ServerConstant.PayType.PAY;
        return chapterMsg.getPayType();
    }

    public boolean isEmpty() {
        return TextUtils.isEmpty(content);
    }

    public boolean isInitChapterSuccess() {
        return isInitChapterSuccess;
    }

}
