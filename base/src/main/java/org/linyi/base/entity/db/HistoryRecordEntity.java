package org.linyi.base.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class HistoryRecordEntity {



    @Generated(hash = 560277705)
    public HistoryRecordEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthorName() {
        return this.authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getReadChapterTitle() {
        return this.readChapterTitle;
    }
    public void setReadChapterTitle(String readChapterTitle) {
        this.readChapterTitle = readChapterTitle;
    }
    public Long getLastReadTime() {
        return this.lastReadTime;
    }
    public void setLastReadTime(Long lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public boolean getIsSelected() {
        return this.selected;
    }

    public void setIsSelected(boolean isSelected) {
        this.selected = isSelected;
    }
    public boolean getSelected() {
        return this.selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //自增主键
    @Id(autoincrement = true)
    private Long id;
    //book id
    @Unique
    private String bookId;
    private  String title;
    private String authorName;
    private String readChapterTitle;

    public HistoryRecordEntity(String bookId, String title, String authorName, String readChapterTitle, String coverImage) {
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.readChapterTitle = readChapterTitle;
        this.coverImage = coverImage;
        this.lastReadTime=System.currentTimeMillis();
    }

    public HistoryRecordEntity(String bookId, String readChapterTitle) {
        this.bookId = bookId;
        this.readChapterTitle = readChapterTitle;
        this.lastReadTime=System.currentTimeMillis();
    }
    @Generated(hash = 463660276)
    public HistoryRecordEntity(Long id, String bookId, String title, String authorName, String readChapterTitle,
            Long lastReadTime, String coverImage, boolean selected) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.readChapterTitle = readChapterTitle;
        this.lastReadTime = lastReadTime;
        this.coverImage = coverImage;
        this.selected = selected;
    }

    private Long lastReadTime;
    private String coverImage;
    private boolean selected;
}
