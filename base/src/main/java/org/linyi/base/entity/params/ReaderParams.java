package org.linyi.base.entity.params;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/18 0018 下午 4:11
 */
public class ReaderParams implements Parcelable {
    
    //书籍ID
    private String bookID;
    //书籍名
    private String bookName;
    //章节位置索引[-1, +) (-1 ： 取当前章节、不跳转到指定章节)
    private int chapterSort = -1;
    //位置[0, +)
    private int position;

    public ReaderParams(String bookID, String bookName) {
        this.bookID = bookID;
        this.bookName = bookName;
    }

    public ReaderParams(String bookID, String bookName, int chapterSort, int position) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.chapterSort = chapterSort;
        this.position = position;
    }

    public String getBookID() {
        return bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public int getChapterSort() {
        return chapterSort;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookID);
        dest.writeString(this.bookName);
        dest.writeInt(this.chapterSort);
        dest.writeInt(this.position);
    }

    protected ReaderParams(Parcel in) {
        this.bookID = in.readString();
        this.bookName = in.readString();
        this.chapterSort = in.readInt();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<ReaderParams> CREATOR = new Parcelable.Creator<ReaderParams>() {
        @Override
        public ReaderParams createFromParcel(Parcel source) {
            return new ReaderParams(source);
        }

        @Override
        public ReaderParams[] newArray(int size) {
            return new ReaderParams[size];
        }
    };
}
