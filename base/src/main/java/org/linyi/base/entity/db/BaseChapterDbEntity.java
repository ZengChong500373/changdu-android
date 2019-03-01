package org.linyi.base.entity.db;

import com.api.changdu.proto.BookApi;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.linyi.base.utils.LogUtil;
import org.linyi.base.utils.help.ModuleHelp;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @fun: 本地持久化的章节需要继承该类
 * @developer: rwz
 * @date: 2019/1/23 0023 下午 4:00
 */
@Entity
public class BaseChapterDbEntity {

    //自增主键
    @Id(autoincrement = true)
    private Long id;
    //唯一标识(bookID+sourceID+chapterID, 详NovelUtils)
    @Unique
    private String uniqueID;
    //上次保存时间
    private long lastTime;
    //本地存储目录
    private String filePath;
    //阅读进度
    private long position;
    //总字符数
    private long total;
    //章节详情(避免内存暴涨，内容未保存在数据库)
    private String detailStr;

    public BaseChapterDbEntity(String uniqueID, BookApi.AckChapter.Data data) {
        if (data != null) {
            this.uniqueID = uniqueID;
            //将content置空
            BookApi.AckChapter.Data build = data.toBuilder().setContent("").build();
            this.detailStr = ModuleHelp.parseString(build);
        }
    }

    @Generated(hash = 703595237)
    public BaseChapterDbEntity(Long id, String uniqueID, long lastTime, String filePath, long position,
            long total, String detailStr) {
        this.id = id;
        this.uniqueID = uniqueID;
        this.lastTime = lastTime;
        this.filePath = filePath;
        this.position = position;
        this.total = total;
        this.detailStr = detailStr;
    }

    @Generated(hash = 514057375)
    public BaseChapterDbEntity() {
    }

    public long getLastTime() {
        return lastTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getPosition() {
        return position;
    }

    public long getTotal() {
        return total;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getDetailStr() {
        return detailStr;
    }

    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "BaseChapterDbEntity{" +
                "id=" + id +
                ", uniqueID='" + uniqueID + '\'' +
                ", lastTime=" + lastTime +
                ", filePath='" + filePath + '\'' +
                ", position=" + position +
                ", total=" + total +
                ", detailStr='" + detailStr + '\'' +
                '}';
    }
}
