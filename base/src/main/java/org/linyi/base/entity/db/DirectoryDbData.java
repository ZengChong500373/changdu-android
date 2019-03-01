package org.linyi.base.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @fun: 目录表
 * @developer: rwz
 * @date: 2019/2/18 0018 下午 7:40
 */
@Entity
public class DirectoryDbData {
    //自增主键
    @Id(autoincrement = true)
    private Long id;
    private String bookID;
    private String directoryData;

    public DirectoryDbData(String bookID, String directoryData) {
        this.bookID = bookID;
        this.directoryData = directoryData;
    }

    @Generated(hash = 25404625)
    public DirectoryDbData(Long id, String bookID, String directoryData) {
        this.id = id;
        this.bookID = bookID;
        this.directoryData = directoryData;
    }

    @Generated(hash = 2054707206)
    public DirectoryDbData() {
    }

    public String getBookID() {
        return bookID;
    }

    public String getDirectoryData() {
        return directoryData;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setDirectoryData(String directoryData) {
        this.directoryData = directoryData;
    }

}
