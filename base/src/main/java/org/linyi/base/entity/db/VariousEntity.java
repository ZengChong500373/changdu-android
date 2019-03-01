package org.linyi.base.entity.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class VariousEntity {
    //自增主键
    @Id(autoincrement = true)
    private Long id;
    //唯一标识
    @Unique
    private String uniqueType;

    //上次保存时间
    private long lastTime;
    private int page;

    private  String describ;
    private String detailStr;
    public VariousEntity(String describ, String detailStr,Long lastTime) {
        this.describ = describ;
        this.detailStr = detailStr;
        this.lastTime=lastTime;
    }
    public VariousEntity(String uniqueType, int page, String detailStr) {
        this.uniqueType = uniqueType;
        this.page = page;
        this.detailStr = detailStr;
        this.lastTime=System.currentTimeMillis();
    }


    public VariousEntity(String uniqueType, String detailStr) {
        this.uniqueType = uniqueType;
        this.detailStr = detailStr;
        this.lastTime=System.currentTimeMillis();
    }
    @Generated(hash = 987491100)
    public VariousEntity(Long id, String uniqueType, long lastTime, int page,
            String describ, String detailStr) {
        this.id = id;
        this.uniqueType = uniqueType;
        this.lastTime = lastTime;
        this.page = page;
        this.describ = describ;
        this.detailStr = detailStr;
    }
    @Generated(hash = 89551270)
    public VariousEntity() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUniqueType() {
        return this.uniqueType;
    }
    public void setUniqueType(String uniqueType) {
        this.uniqueType = uniqueType;
    }
    public long getLastTime() {
        return this.lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
    public String getDetailStr() {
        return this.detailStr;
    }
    public void setDetailStr(String detailStr) {
        this.detailStr = detailStr;
    }
    public int getPage() {
        return this.page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public String getDescrib() {
        return this.describ;
    }
    public void setDescrib(String describ) {
        this.describ = describ;
    }

   

}
